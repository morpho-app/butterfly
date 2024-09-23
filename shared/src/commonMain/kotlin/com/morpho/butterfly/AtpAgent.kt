package com.morpho.butterfly

import com.atproto.server.CreateSessionRequest
import com.morpho.butterfly.auth.AuthInfo
import com.morpho.butterfly.auth.Credentials
import com.morpho.butterfly.auth.Server
import com.morpho.butterfly.auth.SessionRepository
import com.morpho.butterfly.auth.TokenStatus
import com.morpho.butterfly.auth.UserRepository
import com.morpho.butterfly.auth.decodeJwt
import com.morpho.butterfly.xrpc.JWTAuthPlugin
import com.morpho.butterfly.xrpc.XrpcBlueskyApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.takeFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import kotlin.time.Duration

open class AtpAgent: KoinComponent {
    protected val userData: UserRepository by inject()
    protected val session: SessionRepository by inject()

    val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    companion object {
        val log = logging("AtpAgent")
    }

    protected var refreshService: Job? = null

    var id: Did? = null
        get() = session.auth?.did
        private set

    val server: Server
        get() = userData.getUser(id)?.server ?: Server.BlueskySocial

    protected val auth: AuthInfo?
        get() = session.auth ?: userData.getUser(id)?.auth ?: userData.firstUser()?.auth

    protected suspend fun setAuth(auth: AuthInfo?) {
        session.auth = auth
        if (auth != null) {
            id = auth.did
            userData.setAuth(id!!, auth)
            sessionTokens.update { auth.toTokens() }
        } else if (id != null) {
            userData.setAuth(id!!, null)
            sessionTokens.update { null }
        } else {
            session.auth = null
            sessionTokens.update { null }
        }
    }

    val isLoggedIn: Boolean
        get() = auth != null

    protected val sessionTokens = MutableStateFlow(
        if (checkTokens(auth) != TokenStatus.NoAuth) auth?.toTokens()
        else if (userData.getUser(id) != null
            && checkTokens(userData.getUser(id)?.auth) != TokenStatus.NoAuth
        ) userData.getUser(id)?.auth?.toTokens()
        else if (userData.firstUser() != null
            && checkTokens(userData.firstUser()?.auth) != TokenStatus.NoAuth
        ) userData.firstUser()?.auth?.toTokens()
        else null
    )

    protected fun AuthInfo.toTokens() = BearerTokens(accessJwt, refreshJwt)

    protected fun AuthInfo.withTokens(tokens: BearerTokens) = copy(
        accessJwt = tokens.accessToken,
        refreshJwt = tokens.refreshToken,
    )

    protected var atpClient = HttpClient(CIO) {
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
            val hostUrl = url.takeFrom(server.host)
            url.protocol = hostUrl.protocol
            url.host = hostUrl.host
            url.port = hostUrl.port
        }

        install(HttpTimeout) {
            requestTimeoutMillis = Long.MAX_VALUE // TODO: make this configurable
        }

        expectSuccess = false
    }


    protected fun checkTokens(auth: AuthInfo?): TokenStatus {
        if (auth == null) return TokenStatus.NoAuth
        val decoded = decodeJwt(auth.accessJwt)
        log.v { "Decoded auth: $decoded" }
        log.d { "Time: ${Clock.System.now()}" }
        log.d { "Expiry: ${decoded?.expiresAt}" }
        if (decoded?.expiresAt != null && decoded.expiresAt < Clock.System.now()) {
            val refreshDecoded = decodeJwt(auth.refreshJwt)
            log.v { "Refresh decoded: $refreshDecoded" }
            log.d { "Refresh expiry: ${refreshDecoded?.expiresAt}" }
            if (refreshDecoded?.expiresAt != null && refreshDecoded.expiresAt < Clock.System.now()) {
                log.d { "Refresh token expired at ${refreshDecoded.expiresAt}" }
                log.d { "Kicking to login screen" }
                return TokenStatus.RefreshFailed
            }
            log.d { "Access token expired at ${decoded.expiresAt}" }
            return TokenStatus.AccessExpired
        }
        return TokenStatus.Valid
    }

    var api: BlueskyApi = XrpcBlueskyApi(atpClient, butterflySerializersModule)


    protected fun refreshSession() = serviceScope.launch {
        if (auth == null) return@launch
        api.refreshSession().onFailure {
            log.e { "Failed to refresh session: $it" }
            setAuth(null)
        }.onSuccess { response ->
            val newAuth = if (response.did != auth?.did) {
                return@launch
            } else session.auth?.copy(
                accessJwt = response.accessJwt,
                refreshJwt = response.refreshJwt,
                handle = response.handle,
                did = response.did,
                didDoc = response.didDoc
            )
            setAuth(newAuth)
        }
    }

    protected fun sessionRefresh() = serviceScope.launch {
        while (true) {
            delay(Duration.parse("20m"))
            refreshSession()
            delay(Duration.parse("60m"))
        }
    }

    init {
        serviceScope.launch {

            when (checkTokens(auth)) {
                TokenStatus.Valid -> resumeSession()
                TokenStatus.AccessExpired -> {
                    log.d { "Refreshing..." }
                    refreshSession().invokeOnCompletion { serviceScope.launch { resumeSession() } }
                }

                else -> setAuth(null)
            }
        }
    }

    protected fun extractServer(didDoc: JsonElement?, defaultServer: Server = Server.BlueskySocial): Server {
        return if (didDoc != null) {
            val service =
                didDoc.jsonObject["service"]?.jsonArray?.get(0)?.jsonObject?.get("serviceEndpoint")?.jsonPrimitive?.content
            if (service != null) {
                Server.CustomServer(service)
            } else defaultServer
        } else defaultServer
    }

    protected suspend fun resumeSession() = withContext(Dispatchers.IO) {
        setAuth(auth)
        log.d { "Startup auth:\n$auth" }
        log.d { "User ID: $id" }
        log.v { "User:\n${userData.getUser(id)}" }
        refreshService = sessionRefresh()
    }

    suspend fun switchUser(newId: AtIdentifier) = withContext(Dispatchers.IO) {
        if (newId == id) return@withContext
        refreshSession() // Do a refresh to maximize lifetime of the old auth
        sessionTokens.value?.let { tokens -> // Store the old auth info
            session.auth?.withTokens(tokens)?.let { auth ->
                id?.let { did -> userData.setAuth(did, auth) }
            }
        }
        val newUser = userData.findUser(newId)
        if (newUser == null) {
            log.e { "Existing user $newId not found" }
            return@withContext
        }
        api.getSession().onSuccess {
            log.d { "New session:\n$it" }
            val newServer = extractServer(it.didDoc)
            if (newServer != newUser.server) {
                userData.removeUser(newId)
                userData.addUser(newUser.copy(server = newServer))
            }
        }.onFailure {
            log.e { "Failed to get session: $it" }
            setAuth(null)
        }
    }


    fun logout() = serviceScope.launch {
        endSession()
    }

    suspend fun endSession() = withContext(Dispatchers.IO) {
        api.deleteSession()
        setAuth(null)
    }

    suspend fun login(
        credentials: Credentials,
        userServer: Server = Server.BlueskySocial
    ): Result<AuthInfo> {
        return withContext(Dispatchers.IO) {
            api.createSession(
                CreateSessionRequest(
                    credentials.username.handle,
                    credentials.password
                )
            ).map { response ->
                AuthInfo(
                    accessJwt = response.accessJwt,
                    refreshJwt = response.refreshJwt,
                    handle = response.handle,
                    did = response.did,
                    didDoc = response.didDoc
                )
            }.onSuccess {
                id = it.did
                // If the didDoc has a PDS endpoint listed, we can use that instead of the overall server
                val newServer = extractServer(it.didDoc, userServer)
                userData.addUser(credentials, it.did, newServer)
                setAuth(it)
                refreshService = sessionRefresh()
            }
        }
    }
}