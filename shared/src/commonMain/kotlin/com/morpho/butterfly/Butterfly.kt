package com.morpho.butterfly

import app.bsky.actor.PreferencesUnion
import app.bsky.feed.Like
import app.bsky.feed.Repost
import com.atproto.repo.CreateRecordRequest
import com.atproto.repo.DeleteRecordRequest
import com.atproto.server.CreateSessionRequest
import com.atproto.server.RefreshSessionResponse
import com.morpho.butterfly.auth.*
import com.morpho.butterfly.model.RecordType
import com.morpho.butterfly.model.RecordUnion
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.storage.RkeyCacheEntry
import com.morpho.butterfly.xrpc.JWTAuthPlugin
import com.morpho.butterfly.xrpc.XrpcBlueskyApi
import com.morpho.butterfly.xrpc.toAtpResult
import com.morpho.butterfly.xrpc.withXrpcConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.http.takeFrom
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import kotlin.collections.set

private const val TAG = "butterfly"

expect fun getPlatformCache(): CacheStorage

class Butterfly: KoinComponent {

    // TODO: implement this cache in a better way
    private val rkeyCache: MutableMap<AtUri, RkeyCacheEntry> = mutableMapOf()
    private val authCache = arrayListOf<BearerTokens>()


    val userService: UserRepository by inject()
    val session: SessionRepository by inject()


    private val sessionTokens = MutableStateFlow(session.auth?.toTokens())

    var atpUser: AtpUser? = null

    var id: AtIdentifier? = null
        private set

    companion object {
        val log = logging()
    }
    init {
        runBlocking {
            val auth = session.auth
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
            id = if(atpUser?.auth != null) {
                atpUser!!.auth?.let { authCache.add(it.toTokens()) }
                atpUser?.auth?.did
            } else atpUser?.id
            log.v { "User:\n${atpUser}" }
            log.d { "User ID: $id" }

        }
    }


    var atpClient = HttpClient(CIO) {
        engine {
            //pipelining = true
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
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

        install(Auth) {
            bearer {
                loadTokens {
                    if (sessionTokens.value != null) {
                        val auth = atpUser?.id?.let { userService.getAuth(it) }
                        if (auth != null) {
                            sessionTokens.value = auth.toTokens()
                        } else {
                            sessionTokens.value = session.auth?.toTokens()
                        }
                        atpUser?.id?.let { session.auth?.let { it1 -> userService.setAuth(it, it1) } }
                        log.v { "Loaded tokens:\n${authCache.last()}" }
                        sessionTokens.value
                    } else if(authCache.isNotEmpty()) {
                        log.v { "Loaded tokens:\n${authCache.last()}" }
                        sessionTokens.value
                    } else {
                        log.w { "Loading blank bearer auth" }
                        BearerTokens("","")
                    }
                }

                refreshTokens {
                    val refresh = session.auth?.refreshJwt
                    val refreshResponse = client.post("/xrpc/com.atproto.server.refreshSession") {
                        if (refresh != null) {
                            bearerAuth(refresh)
                        }
                        markAsRefreshTokenRequest()
                    }.toAtpResult<AuthInfo>().getOrNull()
                    if (refreshResponse != null) {
                        session.auth = refreshResponse
                        sessionTokens.value = refreshResponse.toTokens()
                        atpUser?.id?.let { userService.setAuth(it, refreshResponse) }
                        log.d { "Refreshed tokens:\n${refreshResponse}" }
                        refreshResponse.toTokens()
                    } else {
                        BearerTokens("","")
                    }
                }
                sendWithoutRequest  { request ->
                    // figure out how to programmatically detect xrpc api calls that don't need authentication
                    val host = if(atpUser != null) {
                        atpUser!!.server.host
                    } else {
                        Server.BlueskySocial.host
                    }
                    request.url.toString().contains(host) || request.url.toString().contains(Server.BlueskySocial.host)
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = Long.MAX_VALUE
        }

        expectSuccess = false
    }

    var api: BlueskyApi = XrpcBlueskyApi(atpClient)

    private fun AuthInfo.toTokens() = BearerTokens(accessJwt, refreshJwt)

    private fun AuthInfo.withTokens(tokens: BearerTokens) = copy(
        accessJwt = tokens.accessToken,
        refreshJwt = tokens.refreshToken,
    )

    // Pulled this out of where I stuck it in the API so it doesn't get overwritten
    // TODO: Figure out root cause of why that first normal refresh fucks up, wtf did Christian do?
    suspend fun refreshSession(auth: AuthInfo): Result<RefreshSessionResponse> {
        return atpClient.withXrpcConfiguration().post("/xrpc/com.atproto.server.refreshSession") {
            bearerAuth(auth.refreshJwt)
        }.toAtpResult()
    }

    suspend fun getUserPreferences() : Result<List<PreferencesUnion>> {
        return api.getPreferences().map { it.preferences }.onSuccess {
            log.v { it }
        }
    }


    fun isLoggedIn(): Boolean {
        log.d { "User:\n${atpUser}" }
        //log.d { "Cache:\n${authCache.lastOrNull()}"}
        log.d { "Session:\n${session.auth}" }
        return (atpUser != null || session.auth != null)
    }


    suspend fun makeLoginRequest(credentials: Credentials, server: Server = Server.BlueskySocial): Result<AuthInfo> {
        return withContext(Dispatchers.IO) {
            atpUser = AtpUser(credentials, server)
            resetEngine()
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
                userService.addUser(credentials, newServer)
                userService.setAuth(credentials.username, it)
                atpUser = AtpUser(credentials, newServer, it)
                resetEngine()
            }
        }
    }

    private fun resetEngine() {
        log.d { "Resetting HTTP engine" }
        atpClient.close()
        atpClient = HttpClient(CIO) {
            engine {
                //pipelining = true
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
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

            install(Auth) {
                bearer {
                    loadTokens {
                        if (sessionTokens.value != null) {
                            val auth = atpUser?.id?.let { userService.getAuth(it) }
                            if (auth != null) {
                                sessionTokens.value = auth.toTokens()
                            } else {
                                sessionTokens.value = session.auth?.toTokens()
                            }
                            atpUser?.id?.let { session.auth?.let { it1 -> userService.setAuth(it, it1) } }
                            log.v { "Loaded tokens:\n${authCache.last()}" }
                            sessionTokens.value
                        } else if(authCache.isNotEmpty()) {
                            log.v { "Loaded tokens:\n${authCache.last()}" }
                            sessionTokens.value
                        } else {
                            log.w { "Loading blank bearer auth" }
                            BearerTokens("","")
                        }
                    }

                    refreshTokens {
                        val refresh = session.auth?.refreshJwt
                        val refreshResponse = client.post("/xrpc/com.atproto.server.refreshSession") {
                            if (refresh != null) {
                                bearerAuth(refresh)
                            }
                            markAsRefreshTokenRequest()
                        }.toAtpResult<AuthInfo>().getOrNull()
                        if (refreshResponse != null) {
                            session.auth = refreshResponse
                            sessionTokens.value = refreshResponse.toTokens()
                            atpUser?.id?.let { userService.setAuth(it, refreshResponse) }
                            log.d { "Refreshed tokens:\n${refreshResponse}" }
                            refreshResponse.toTokens()
                        } else {
                            BearerTokens("","")
                        }
                    }
                    sendWithoutRequest  { request ->
                        // figure out how to programmatically detect xrpc api calls that don't need authentication
                        val host = if(atpUser != null) {
                            atpUser!!.server.host
                        } else {
                            Server.BlueskySocial.host
                        }
                        request.url.toString().contains(host) || request.url.toString().contains(Server.BlueskySocial.host)
                    }
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = Long.MAX_VALUE
            }

            expectSuccess = false
        }
        api = XrpcBlueskyApi(atpClient)
    }

    fun createRecord(
        record: RecordUnion
    ) = CoroutineScope(Dispatchers.IO).launch {
        val did = session.auth?.did
        val timestamp : Timestamp = Clock.System.now()
        val uri: AtUri
        if (did != null) {
            val request = when(record) {
                is RecordUnion.Like -> {
                    uri = record.subject.uri
                    val like = Like(record.subject, timestamp)
                    CreateRecordRequest(
                        repo = did,
                        collection = record.type.collection,
                        record = json.encodeToJsonElement(value = like)
                    )
                }
                is RecordUnion.MakePost -> {
                    uri = AtUri(did,record.type.collection,"$timestamp")
                    CreateRecordRequest(
                        repo = did,
                        collection = record.type.collection,
                        record = json.encodeToJsonElement(value = record.post)
                    )
                }
                is RecordUnion.Repost -> {
                    uri = record.subject.uri
                    val repost = Repost(record.subject, timestamp)
                    CreateRecordRequest(
                        repo = did,
                        collection = record.type.collection,
                        record = json.encodeToJsonElement(value = repost)
                    )
                }
            }
            log.d {"Record request: $request"}
            val rkey = getRkey(api.createRecord(request).getOrNull()?.uri)
            log.d {"Rkey for $record: $rkey"}
            when(record) {
                is RecordUnion.Like -> {
                    if (rkeyCache.containsKey(uri)) {
                        rkeyCache[uri]?.likeKey = rkey
                    } else {
                        rkeyCache[uri] = RkeyCacheEntry(likeKey = rkey)
                    }
                }
                is RecordUnion.MakePost -> {
                    if (rkeyCache.containsKey(uri)) {
                        rkeyCache[uri]?.postKey = rkey
                    } else {
                        rkeyCache[uri] = RkeyCacheEntry(postKey = rkey)
                    }
                }
                is RecordUnion.Repost -> if (rkeyCache.containsKey(uri)) {
                    rkeyCache[uri]?.repostKey = rkey
                } else {
                    rkeyCache[uri] = RkeyCacheEntry(repostKey = rkey)
                }
            }
        }
    }
    fun deleteRecord(type: RecordType, uri: AtUri?) {
        if (uri != null) {
            // If this is the right kind of uri for the record, we can use the last bit as the rkey
            val rkey = if(uri.atUri.contains(type.collection.nsid)) {
                getRkey(uri)
            } else {
                // Otherwise, we check our cache for it
                when(type) {
                    RecordType.Post -> rkeyCache[uri]?.postKey
                    RecordType.Like -> rkeyCache[uri]?.likeKey
                    RecordType.Repost -> rkeyCache[uri]?.repostKey
                }
            }
            if (rkey != null) {
                deleteRecord(type, rkey)
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