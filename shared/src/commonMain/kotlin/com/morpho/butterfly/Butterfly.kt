package com.morpho.butterfly

import app.bsky.actor.PreferencesUnion
import app.bsky.feed.Like
import app.bsky.feed.Repost
import com.atproto.repo.CreateRecordRequest
import com.atproto.repo.DeleteRecordRequest
import com.atproto.server.CreateSessionRequest
import com.atproto.server.RefreshSessionResponse
import com.morpho.butterfly.auth.AuthInfo
import com.morpho.butterfly.auth.Credentials
import com.morpho.butterfly.auth.Server
import com.morpho.butterfly.auth.SessionRepository
import com.morpho.butterfly.auth.User
import com.morpho.butterfly.auth.UserRepository
import com.morpho.butterfly.model.RecordType
import com.morpho.butterfly.model.RecordUnion
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.storage.RkeyCacheEntry
import com.morpho.butterfly.xrpc.XrpcBlueskyApi
import com.morpho.butterfly.xrpc.toAtpResult
import com.morpho.butterfly.xrpc.withXrpcConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.json.encodeToJsonElement
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.collections.set

private const val TAG = "butterfly"


class Butterfly(
    val id: AtIdentifier? = null
): KoinComponent {

    // TODO: implement this cache in a better way
    private val rkeyCache: MutableMap<AtUri, RkeyCacheEntry> = mutableMapOf()
    private val authCache = arrayListOf<BearerTokens>()

    val userService: UserRepository by inject()
    val session: SessionRepository by inject()

    var user: User? = null

    init {
        runBlocking {
            if (id != null) {
                val u = userService.findUser(id)
                if (u != null) {
                    user = u
                    if (u.auth != null) {
                        authCache.add(u.auth!!.toTokens())
                    }
                } else {
                    user = User(id, Server.BlueskySocial)
                    userService.addUser(user!!)
                }
            }
        }
    }


    var atpClient = HttpClient(CIO) {
        engine {
            pipelining = true
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(HttpCache) {
            //val cache = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.toFile()
            //publicStorage(FileStorage(cache))
        }

        defaultRequest {
            val hostUrl = if(user != null) Url(user!!.server.host) else Url(Server.BlueskySocial.host)
            url.protocol = hostUrl.protocol
            url.host = hostUrl.host
            url.port = hostUrl.port
        }

        install(Auth) {
            authConfig
        }

        install(HttpTimeout) {
            requestTimeoutMillis = Long.MAX_VALUE
        }

        expectSuccess = false
    }


    private val authConfig =
        BearerAuthProvider(
            loadTokens = {
                if (session.auth != null) {
                    if(id != null){
                        val auth = userService.getAuth(id)
                        if (auth != null) {
                            authCache.add(auth.toTokens())
                        } else {
                            authCache.add(session.auth!!.toTokens())
                        }
                    } else {
                        authCache.add(session.auth!!.toTokens())
                    }
                    if (id != null) userService.setAuth(id, session.auth!!)
                    authCache.last()
                } else {
                    BearerTokens("","")
                }
            },

            refreshTokens = {
                val refresh = session.auth?.refreshJwt
                val refreshResponse = client.post("/xrpc/com.atproto.server.refreshSession") {
                    if (refresh != null) {
                        bearerAuth(refresh)
                    }
                    markAsRefreshTokenRequest()
                }.toAtpResult<AuthInfo>().getOrNull()
                if (refreshResponse != null) {
                    session.auth = refreshResponse
                    authCache.add(refreshResponse.toTokens())
                    refreshResponse.toTokens()
                } else {
                    BearerTokens("","")
                }
            },
            sendWithoutRequestCallback = {request ->
                // figure out how to programmatically detect xrpc api calls that don't need authentication
                user != null && (user?.server?.host?.let { request.url.toString().contains(it) } == true)
            },
            realm = "BlueskySocial"
        )


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
            this.bearerAuth(auth.refreshJwt)
        }.toAtpResult()
    }

    suspend fun getUserPreferences() : Result<List<PreferencesUnion>> {
        return api.getPreferences().map { it.preferences }
    }


    suspend fun makeLoginRequest(credentials: Credentials, server: Server = Server.BlueskySocial): Result<AuthInfo> {
        return withContext(Dispatchers.IO) {
            user = User(credentials, server)
            resetEngine()
            api.createSession(CreateSessionRequest(credentials.username.handle, credentials.password)).map { response ->
                AuthInfo(
                    accessJwt = response.accessJwt,
                    refreshJwt = response.refreshJwt,
                    handle = response.handle,
                    did = response.did,
                )
            }
            .onSuccess {
                session.auth = it
                userService.addUser(credentials, server)
                userService.setAuth(credentials.username, it)
                resetEngine()
            }.onFailure {
            }
        }
    }

    private fun resetEngine() {
        atpClient.close()
        atpClient = HttpClient(CIO) {
            engine {
                pipelining = true
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(HttpCache) {
                //val cache = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.toFile()
                //publicStorage(FileStorage(cache))
            }

            defaultRequest {
                val hostUrl = if(user != null) Url(user!!.server.host) else Url(Server.BlueskySocial.host)
                url.protocol = hostUrl.protocol
                url.host = hostUrl.host
                url.port = hostUrl.port
            }

            install(Auth) {
                authConfig
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
                    uri = AtUri("$did/${record.type.collection}/$timestamp")
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
            val rkey = getRkey(api.createRecord(request).getOrNull()?.uri)
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
            api.deleteRecord(DeleteRecordRequest(did, type.collection, rkey))
        }
    }

}