package com.morpho.butterfly

import app.bsky.feed.Like
import app.bsky.feed.Repost
import app.bsky.graph.Block
import app.bsky.graph.Follow
import app.bsky.graph.Listblock
import com.atproto.repo.CreateRecordRequest
import com.atproto.repo.DeleteRecordRequest
import com.atproto.server.CreateSessionRequest
import com.morpho.butterfly.auth.*
import com.morpho.butterfly.model.RecordType
import com.morpho.butterfly.model.RecordUnion
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.xrpc.JWTAuthPlugin
import com.morpho.butterfly.xrpc.XrpcBlueskyApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.takeFrom
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import kotlin.time.Duration

private const val TAG = "butterfly"

expect fun getPlatformCache(): CacheStorage

class Butterfly: KoinComponent {


    private val authCache = arrayListOf<BearerTokens>()


    val userService: UserRepository by inject()
    val session: SessionRepository by inject()


    private val sessionTokens = MutableStateFlow(session.auth?.toTokens())

    var atpUser: AtpUser? = null

    companion object {
        val log = logging()
        val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
    private var refreshService: Job? = null

    init {
        serviceScope.launch {
            val auth = session.auth
            val decoded = auth?.let { decodeJwt(it.accessJwt) }
            if (decoded?.expiresAt != null && decoded.expiresAt < Clock.System.now()) {
                val refreshDecoded = decodeJwt(auth.refreshJwt)
                if (refreshDecoded?.expiresAt != null && refreshDecoded.expiresAt < Clock.System.now()) {
                    log.d { "Refresh token expired at ${refreshDecoded.expiresAt}" }
                    log.d { "Kicking to login screen" }
                    return@launch   // If the refresh token is expired, we can't refresh
                } else {
                    log.d { "Access token expired at ${decoded.expiresAt}" }
                    log.d { "Refreshing..." }
                    refreshSession()
                }
            }
            log.d { "Startup auth:\n$auth" }
            atpUser = if (auth != null) {
                // If we have an auth token, we can use that to get the user
                var maybeUser = userService.findUser(auth.did)
                log.v { "Maybe user:\n$maybeUser"}
                if (maybeUser == null) {
                    maybeUser = userService.findUser(auth.handle)
                    log.v { "Maybe user:\n$maybeUser"}
                }
                if(maybeUser == null) {
                    // If we don't have the user, we can create it (make some assumptions if we don't have the server info)
                    val u = AtpUser(auth.did, Server.BlueskySocial, auth)
                    userService.addUser(u)
                    u
                } else maybeUser
            } else if(userService.firstUser() != null){
                userService.firstUser()
            } else atpUser
            log.v { "User:\n${atpUser}" }
            log.d { "User ID: ${atpUser?.id}" }

        }
        refreshService = sessionRefresh()
    }


    private var atpClient = HttpClient(CIO) {
        engine {
            pipelining = false
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO //LogLevel.ALL
        }

        install(JWTAuthPlugin) {
            authTokens = sessionTokens
        }

        install(HttpCache) {
            //publicStorage(getPlatformCache())
        }

        defaultRequest {
            val hostUrl = if(atpUser != null) {
                runCatching {
                    log.v { "Custom Host URL: ${atpUser!!.server.host}"}
                    url.takeFrom(atpUser!!.server.host)
                }.mapCatching {
                    it
                }.getOrThrow()

            } else {
                url.takeFrom(Server.BlueskySocial.host)
            }
            log.v { "Host URL: $hostUrl"}
            url.protocol = hostUrl.protocol
            url.host = hostUrl.host
            url.port = hostUrl.port
        }

        install(HttpTimeout) {
            requestTimeoutMillis = Long.MAX_VALUE // TODO: make this configurable
        }

        expectSuccess = false
    }

    var api: BlueskyApi = XrpcBlueskyApi(atpClient)

    private fun AuthInfo.toTokens() = BearerTokens(accessJwt, refreshJwt)

    private fun AuthInfo.withTokens(tokens: BearerTokens) = copy(
        accessJwt = tokens.accessToken,
        refreshJwt = tokens.refreshToken,
    )


    private fun sessionRefresh() = serviceScope.launch {
        while(true) {
            delay(Duration.parse("1m"))
            refreshSession()
            delay(Duration.parse("20m"))
        }
    }
    fun refreshSession() = serviceScope.launch {
        session.auth?.let { api.refreshSession() }?.onFailure {
            log.e { "Failed to refresh session: $it" }
            refreshFailed = true
        }?.onSuccess { refreshResponse ->
            val auth = if(session.auth != null) {
                session.auth?.copy(
                    accessJwt = refreshResponse.accessJwt,
                    refreshJwt = refreshResponse.refreshJwt,
                    handle = refreshResponse.handle
                )
            } else AuthInfo(
                refreshResponse.accessJwt,
                refreshResponse.refreshJwt,
                refreshResponse.handle,
                refreshResponse.did
            )
            session.auth = auth
            sessionTokens.update { auth?.toTokens() }
            atpUser?.id?.let {
                if (auth != null) {
                    userService.setAuth(it, auth)
                }
            }
            log.d { "Refreshed tokens:\n${auth}" }
        }
    }

    private var refreshFailed = false

    fun isLoggedIn(): Boolean {
        log.d { "User:\n${atpUser}" }
        log.d { "Session:\n${session.auth}" }
        return ((atpUser != null || session.auth != null) && !refreshFailed)
    }

    suspend fun switchUser(id: AtIdentifier) {
        sessionTokens.value?.let { tokens ->
            session.auth?.withTokens(tokens)?.let { userService.setAuth(id, it) } }
        atpUser = userService.findUser(id)
        session.auth = atpUser?.auth
        api.getSession().onSuccess {
            log.d { "New session:\n$it" }
            val newServer = if (it.didDoc != null) {
                val service =
                    it.didDoc.jsonObject["service"]?.jsonArray?.get(0)?.jsonObject?.get("serviceEndpoint")?.jsonPrimitive?.content
                if (service != null) {
                    Server.CustomServer(service)
                } else atpUser?.server ?: Server.BlueskySocial
            } else atpUser?.server ?: Server.BlueskySocial
            atpUser = atpUser?.copy(server = newServer)
        }.onFailure {
            log.e { "Failed to get session: $it" }
            refreshFailed = true
        }
    }


    suspend fun makeLoginRequest(credentials: Credentials, server: Server = Server.BlueskySocial): Result<AuthInfo> {
        return withContext(Dispatchers.IO) {
            atpUser = AtpUser(credentials, server)
            api.createSession(CreateSessionRequest(credentials.username.handle, credentials.password)).map { response ->
                AuthInfo(
                    accessJwt = response.accessJwt,
                    refreshJwt = response.refreshJwt,
                    handle = response.handle,
                    did = response.did,
                    didDoc = response.didDoc
                )
            }
            .onSuccess {
                // If the didDoc has a PDS endpoint listed, we can use that instead of the overall server
                val newServer = if (it.didDoc != null) {
                    val service =
                        it.didDoc.jsonObject["service"]?.jsonArray?.get(0)?.jsonObject?.get("serviceEndpoint")?.jsonPrimitive?.content
                    if (service != null) {
                        Server.CustomServer(service)
                    } else server
                } else server
                session.auth = it
                authCache.add(it.toTokens())
                sessionTokens.value = it.toTokens()
                userService.addUser(credentials, newServer)
                userService.setAuth(credentials.username, it)
                atpUser = AtpUser(credentials, newServer, it)
            }
        }
    }

    fun createRecord(
        record: RecordUnion
    ) = CoroutineScope(Dispatchers.IO).launch {
        val did = session.auth?.did
        val timestamp : Timestamp = Clock.System.now()
        if (did != null) {
            val request = when(record) {
                is RecordUnion.Like -> {
                    val like = Like(record.subject, timestamp)
                    CreateRecordRequest(
                        repo = did,
                        //rkey = rkey,
                        collection = record.type.collection,
                        record = json.encodeToJsonElement(value = like)
                    )
                }
                is RecordUnion.MakePost -> {
                    CreateRecordRequest(
                        repo = did,
                        //rkey = rkey,
                        collection = record.type.collection,
                        record = json.encodeToJsonElement(value = record.post)
                    )
                }
                is RecordUnion.Repost -> {
                    val repost = Repost(record.subject, timestamp)
                    CreateRecordRequest(
                        repo = did,
                        //rkey = rkey,
                        collection = record.type.collection,
                        record = json.encodeToJsonElement(value = repost)
                    )
                }

                is RecordUnion.Block -> {
                    val block = Block(record.subject, timestamp)
                    CreateRecordRequest(
                        repo = did,
                        //rkey = rkey,
                        collection = record.type.collection,
                        record = json.encodeToJsonElement(value = block)
                    )
                }
                is RecordUnion.Follow -> {
                    val follow = Follow(record.subject, timestamp)
                    CreateRecordRequest(
                        repo = did,
                        //rkey = rkey,
                        collection = record.type.collection,
                        record = json.encodeToJsonElement(value = follow)
                    )
                }

                is RecordUnion.ListBlock -> {
                    val listBlock = Listblock(record.subject, timestamp)
                    CreateRecordRequest(
                        repo = did,
                        //rkey = rkey,
                        collection = record.type.collection,
                        record = json.encodeToJsonElement(value = listBlock)
                    )
                }
            }
            log.d {"Record request: $request"}
            val resp = api.createRecord(request).onFailure { log.e { "Failed to create record: $it" } }
            val uri = resp.getOrNull()?.uri ?: return@launch
            val rkey = getRkey(uri)
            log.d { "Rkey for $record: $rkey" }

        }
    }
    fun deleteRecord(type: RecordType, uri: AtUri?, rkey: String? = null) {
        if (uri != null) {
            // If this is the right kind of uri for the record, we can use the last bit as the rkey
            val searchRkey = if(uri.atUri.contains(type.collection.nsid) && rkey == null) {
                getRkey(uri)
            } else rkey
            if (searchRkey != null) {
                deleteRecord(type, searchRkey)
            }
        }
    }

    private fun deleteRecord(type: RecordType, rkey: String) = CoroutineScope(Dispatchers.IO).launch {
        val did = session.auth?.did
        if (did != null) {
            log.v { "Deleting record $rkey of type $type" }
            api.deleteRecord(DeleteRecordRequest(did, type.collection, rkey))
        }
    }

}