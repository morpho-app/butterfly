package com.morpho.butterfly

import app.bsky.actor.*
import app.bsky.feed.*
import app.bsky.graph.*
import app.bsky.labeler.GetServicesQuery
import app.bsky.labeler.GetServicesResponseViewUnion
import app.bsky.labeler.LabelerView
import app.bsky.labeler.LabelerViewDetailed
import app.bsky.notification.GetUnreadCountQuery
import app.bsky.notification.ListNotificationsNotification
import app.bsky.notification.ListNotificationsQuery
import app.bsky.notification.UpdateSeenRequest
import com.atproto.identity.ResolveHandleQuery
import com.atproto.identity.UpdateHandleRequest
import com.atproto.label.*
import com.atproto.moderation.CreateReportRequest
import com.atproto.moderation.CreateReportResponse
import com.atproto.moderation.ReportRequestSubject
import com.atproto.repo.*
import com.atproto.server.CreateSessionRequest
import com.morpho.butterfly.auth.*
import com.morpho.butterfly.model.Blob
import com.morpho.butterfly.model.RecordType
import com.morpho.butterfly.model.RecordUnion
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.xrpc.JWTAuthPlugin
import com.morpho.butterfly.xrpc.XrpcBlueskyApi
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
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
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.serialization.*
import kotlinx.serialization.cbor.ByteString
import kotlinx.serialization.json.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import kotlin.collections.List
import kotlin.time.Duration

enum class TokenStatus {
    Valid,
    AccessExpired,
    RefreshExpired,
    RefreshFailed,
    NoAuth,
}

class ButterflyAgent: KoinComponent {
    val userData: UserRepository by inject()
    val session: SessionRepository by inject()

    companion object {
        val log = logging()
        val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    private var refreshService: Job? = null

    var id: Did? = null
        get() = session.auth?.did
        private set

    val server: Server
        get() = userData.getUser(id)?.server ?: Server.BlueskySocial

    private val auth: AuthInfo?
        get() =  session.auth ?: userData.getUser(id)?.auth ?: userData.firstUser()?.auth

    private suspend fun setAuth(auth: AuthInfo?) {
        session.auth = auth
        if(auth != null) {
            id = auth.did
            userData.setAuth(id!!, auth)
            sessionTokens.update { auth.toTokens() }
        } else if(id != null) {
            userData.setAuth(id!!, null)
            sessionTokens.update { null }
        } else {
            session.auth = null
            sessionTokens.update { null }
        }
    }

    val isLoggedIn: Boolean
        get() = auth != null

    private val sessionTokens = MutableStateFlow(
        if(checkTokens(auth) == TokenStatus.Valid) auth?.toTokens()
        else if(userData.getUser(id) != null
            && checkTokens(userData.getUser(id)?.auth) == TokenStatus.Valid)
            userData.getUser(id)?.auth?.toTokens()
        else if(userData.firstUser() != null
            && checkTokens(userData.firstUser()?.auth) == TokenStatus.Valid)
            userData.firstUser()?.auth?.toTokens()
        else null
    )

    private fun AuthInfo.toTokens() = BearerTokens(accessJwt, refreshJwt)

    private fun AuthInfo.withTokens(tokens: BearerTokens) = copy(
        accessJwt = tokens.accessToken,
        refreshJwt = tokens.refreshToken,
    )

    private var atpClient = HttpClient(CIO) {
        engine {
            pipelining = false
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS //LogLevel.ALL
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


    private fun checkTokens(auth: AuthInfo?): TokenStatus {
        if (auth == null) return TokenStatus.NoAuth
        val decoded = decodeJwt(auth.accessJwt)
        Butterfly.log.v { "Decoded auth: $decoded" }
        Butterfly.log.d { "Time: ${Clock.System.now()}" }
        Butterfly.log.d { "Expiry: ${decoded?.expiresAt}" }
        if (decoded?.expiresAt != null && decoded.expiresAt < Clock.System.now()) {
            val refreshDecoded = decodeJwt(auth.refreshJwt)
            Butterfly.log.v { "Refresh decoded: $refreshDecoded" }
            Butterfly.log.d { "Refresh expiry: ${refreshDecoded?.expiresAt}" }
            if (refreshDecoded?.expiresAt != null && refreshDecoded.expiresAt < Clock.System.now()) {
                Butterfly.log.d { "Refresh token expired at ${refreshDecoded.expiresAt}" }
                Butterfly.log.d { "Kicking to login screen" }
                return TokenStatus.RefreshFailed
            }
            Butterfly.log.d { "Access token expired at ${decoded.expiresAt}" }
            return TokenStatus.AccessExpired
        }
        return TokenStatus.Valid
    }
    var api: BlueskyApi = XrpcBlueskyApi(atpClient)


    private fun refreshSession() = serviceScope.launch {
        if(auth == null) return@launch
        api.refreshSession().onFailure {
            Butterfly.log.e { "Failed to refresh session: $it" }
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

    private fun sessionRefresh() = serviceScope.launch {
        while(true) {
            delay(Duration.parse("20m"))
            refreshSession()
            delay(Duration.parse("120m"))
        }
    }

    init { serviceScope.launch {
            when(checkTokens(auth)) {
                TokenStatus.Valid -> resumeSession()
                TokenStatus.AccessExpired -> {
                    log.d { "Refreshing..." }
                    refreshSession().invokeOnCompletion { serviceScope.launch { resumeSession() } }
                }
                else -> setAuth(null)
            }
        }
    }

    private fun extractServer(didDoc: JsonElement?): Server {
        return if (didDoc != null) {
            val service =
                didDoc.jsonObject["service"]?.jsonArray?.get(0)?.jsonObject?.get("serviceEndpoint")?.jsonPrimitive?.content
            if (service != null) {
                Server.CustomServer(service)
            } else server
        } else server
    }

    private suspend fun resumeSession() = withContext(Dispatchers.IO) {
        setAuth(auth)
        Butterfly.log.d { "Startup auth:\n$auth" }
        Butterfly.log.d { "User ID: $id" }
        Butterfly.log.v { "User:\n${userData.getUser(id)}" }
        refreshService = sessionRefresh()
    }

    suspend fun switchUser(newId: AtIdentifier) = withContext(Dispatchers.IO) {
        if (newId == id) return@withContext
        refreshSession() // Do a refresh to maximize lifetime of the old auth
        sessionTokens.value?.let { tokens -> // Store the old auth info
            session.auth?.withTokens(tokens)?.let { auth ->
                id?.let { did -> userData.setAuth(did, auth) } } }
        val newUser = userData.findUser(newId)
        if(newUser == null) {
            log.e { "Existing user $newId not found" }
            return@withContext
        }
        api.getSession().onSuccess {
            Butterfly.log.d { "New session:\n$it" }
            val newServer = extractServer(it.didDoc)
            if (newServer != newUser.server) {
                userData.removeUser(newId)
                userData.addUser(newUser.copy(server = newServer))
            }
        }.onFailure {
            Butterfly.log.e { "Failed to get session: $it" }
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

    suspend fun makeLoginRequest(credentials: Credentials, server: Server = Server.BlueskySocial): Result<AuthInfo> {
        return withContext(Dispatchers.IO) {
            api.createSession(CreateSessionRequest(credentials.username.handle, credentials.password)).map { response ->
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
                val newServer = extractServer(it.didDoc)
                userData.addUser(credentials, it.did, newServer)
                setAuth(it)
                refreshService = sessionRefresh()
            }
        }
    }

    suspend fun makeRecord(record: RecordUnion) : Result<StrongRef> {
        val did = id ?: return Result.failure(Error("Not logged in"))
        val timestamp : Timestamp = Clock.System.now()
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
        Butterfly.log.d {"Record request: $request"}
        return api.createRecord(request).onFailure { Butterfly.log.e { "Failed to create record: $it" } }
            .map { StrongRef(it.uri, it.cid) }
    }

    fun createRecord(record: RecordUnion) = CoroutineScope(Dispatchers.IO).launch { makeRecord(record) }

    fun deleteRecord(type: RecordType, uri: AtUri?, rkey: String? = null): Boolean {
        if (id == null) return false
        if (uri != null) {
            // If this is the right kind of uri for the record, we can use the last bit as the rkey
            val searchRkey = if(uri.atUri.contains(type.collection.nsid) && rkey == null) {
                getRkey(uri)
            } else rkey
            if (searchRkey != null) {
                serviceScope.launch { deleteRecord(type, searchRkey) }
            }
        }
        return true
    }

    suspend fun deleteRecord(type: RecordType, rkey: String): Result<Unit> {
        if (id == null) return Result.failure(Error("Not logged in"))
        Butterfly.log.v { "Deleting record $rkey of type $type" }
        return api.deleteRecord(DeleteRecordRequest(id!!, type.collection, rkey))
    }

    var prefs: BskyPreferences = BskyPreferences()
        private set

    fun preferences(): Flow<BskyPreferences> = flow {
        val oldPrefs = prefs
        getPreferences().onSuccess {
            prefs = it
            emit(prefs)
        }.onFailure {
            Butterfly.log.e { "Failed to get preferences: $it" }
            prefs = oldPrefs
            emit(prefs)
        }
    }

    suspend fun getPreferences(): Result<BskyPreferences> = withContext(Dispatchers.IO) {
        return@withContext api.getPreferences().map { it.toPreferences() }
    }

    suspend fun resolveHandle(handle: Handle): Result<Did> {
        return withContext(Dispatchers.IO) {
            api.resolveHandle(ResolveHandleQuery(handle)).map { it.did }
        }
    }

    suspend fun updateHandle(handle: Handle): Result<Unit> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.updateHandle(UpdateHandleRequest(handle)).map { Unit }
        }
    }

    suspend fun uploadBlob(blob: ByteArray, mimeType: String): Result<Blob> {
        return withContext(Dispatchers.IO) {
            api.uploadBlob(blob, mimeType).map {
                Blob.serializer().deserialize(it.blob)
            }
        }
    }

    suspend fun createModerationReport(
        reasonType: com.atproto.moderation.Token,
        reason: String? = null,
        subject: ReportRequestSubject,
    ): Result<CreateReportResponse> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.createReport(CreateReportRequest(reasonType, reason, subject)).map { it }
        }
    }

    suspend fun getTimeline(
        algorithm: String? = null,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PagedList<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getTimeline(GetTimelineQuery(algorithm, limit, cursor))
                .map { resp -> PagedList(resp.cursor, resp.feed) }
        }
    }
    suspend fun getTimeline(query: JsonElement, cursor: String? = null): Result<PagedList<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        try {
            val newQuery = json.decodeFromJsonElement<GetTimelineQuery>(query).copy(cursor = cursor)
            return withContext(Dispatchers.IO) {
                api.getTimeline(newQuery)
                    .map { resp -> PagedList(resp.cursor, resp.feed) }
            }
        } catch (e: Exception) {
            return Result.failure(Error("Invalid query: $e"))
        }
    }
    suspend fun getTimeline(query: JsonElement, ): Result<PagedList<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        try {
            val newQuery = json.decodeFromJsonElement<GetTimelineQuery>(query)
            return withContext(Dispatchers.IO) {
                api.getTimeline(newQuery).map { resp -> PagedList(resp.cursor, resp.feed) }
            }
        } catch (e: Exception) {
            return Result.failure(Error("Invalid query: $e"))
        }
    }

    suspend fun getAuthorFeed(
        actor: AtIdentifier,
        limit: Long? = 50,
        cursor: String? = null,
        filter: GetAuthorFeedFilter? = GetAuthorFeedFilter.POSTS_WITH_REPLIES,
    ): Result<PagedList<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getAuthorFeed(GetAuthorFeedQuery(actor, limit, cursor, filter))
                .map { resp -> PagedList(resp.cursor, resp.feed) }
        }
    }

    suspend fun getActorLikes(
        actor: AtIdentifier,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PagedList<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getActorLikes(GetActorLikesQuery(actor, limit, cursor))
                .map { resp -> PagedList(resp.cursor, resp.feed) }
        }
    }

    suspend fun getFollowers(
        actor: AtIdentifier,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<ProfileListResponse> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getFollowers(GetFollowersQuery(actor, limit, cursor))
                .map { resp -> ProfileListResponse(resp.subject, resp.cursor, resp.followers) }
        }
    }

    suspend fun getPostThread(
        uri: AtUri,
        depth: Long? = 6,
        parentHeight: Long? = 80,
    ): Result<GetPostThreadResponse> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getPostThread(GetPostThreadQuery(uri, depth, parentHeight))
        }
    }

    suspend fun getPosts(
        uris: List<AtUri>,
    ): Result<List<PostView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getPosts(GetPostsQuery(uris.toPersistentList())).map { it.posts }
        }
    }

    suspend fun getLikes(
        uri: AtUri,
        cid: Cid? = null,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PostQueryResponse<GetLikesLike>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getLikes(GetLikesQuery(uri, cid, limit, cursor))
                .map { resp ->
                    PostQueryResponse(resp.uri, resp.cid, resp.cursor, resp.likes)
                }
        }
    }

    suspend fun getRepostedBy(
        uri: AtUri,
        cid: Cid? = null,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PostQueryResponse<ProfileView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getRepostedBy(GetRepostedByQuery(uri, cid, limit, cursor))
                .map { resp ->
                    PostQueryResponse(resp.uri, resp.cid, resp.cursor, resp.repostedBy)
                }
        }
    }

    suspend fun getQuotes(
        uri: AtUri,
        cid: Cid? = null,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PostQueryResponse<PostView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getQuotes(GetQuotesQuery(uri, cid, limit, cursor))
                .map { resp ->
                    PostQueryResponse(resp.uri, resp.cid, resp.cursor, resp.posts)
                }
        }
    }

    suspend fun getFollows(
        actor: AtIdentifier,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<ProfileListResponse> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getFollows(GetFollowsQuery(actor, limit, cursor))
                .map { resp -> ProfileListResponse(resp.subject, resp.cursor, resp.follows) }
        }
    }

    suspend fun getProfile(
        actor: AtIdentifier,
    ): Result<ProfileViewDetailed> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getProfile(GetProfileQuery(actor))
        }
    }

    suspend fun getProfiles(
        actors: List<AtIdentifier>,
    ): Result<List<ProfileViewDetailed>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getProfiles(GetProfilesQuery(actors.toPersistentList())).map { it.profiles }
        }
    }

    suspend fun getSuggestions(
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PagedList<ProfileView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getSuggestions(GetSuggestionsQuery(limit, cursor))
                .map { resp -> PagedList(resp.cursor, resp.actors) }
        }
    }

    suspend fun searchActors(
        term: String? = null,
        q: String? = null,
        limit: Long? = 25,
        cursor: String? = null,
    ): Result<PagedList<ProfileView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.searchActors(SearchActorsQuery(term, q, limit, cursor))
                .map { resp -> PagedList(resp.cursor, resp.actors) }
        }
    }

    suspend fun searchActorsTypeahead(
        term: String? = null,
        q: String? = null,
        limit: Long? = 10,
    ): Result<List<ProfileViewBasic>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.searchActorsTypeahead(SearchActorsTypeaheadQuery(term, q, limit))
                .map { resp -> resp.actors }
        }
    }

    suspend fun listNotifications(
        limit: Long? = 50,
        cursor: String? = null,
        seenAt: Timestamp? = null,
    ): Result<PagedList<ListNotificationsNotification>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.listNotifications(ListNotificationsQuery(limit, cursor, seenAt))
                .map { resp -> PagedList(resp.cursor, resp.notifications) }
        }
    }

    suspend fun unreadNotificationsCount(
        seenAt: Timestamp? = null,
    ): Result<Long> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getUnreadCount(GetUnreadCountQuery(seenAt)).map { it.count }
        }
    }

    suspend fun getLabelers(
        dids: List<Did> = emptyList(),
    ): Result<List<LabelerView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getServices(GetServicesQuery(dids.toPersistentList())).map { resp ->
                resp.views.map { when(it) {
                    is GetServicesResponseViewUnion.LabelerView -> it.value
                    is GetServicesResponseViewUnion.LabelerViewDetailed -> {
                        LabelerView(it.value.uri, it.value.cid, it.value.creator,
                                    it.value.likeCount, it.value.viewer,
                                    it.value.indexedAt, it.value.labels)
                    }
                } }
            }
        }
    }

    suspend fun getLabelersDetailed(
        dids: List<Did> = emptyList(),
    ): Result<List<LabelerViewDetailed>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getServices(GetServicesQuery(dids.toPersistentList(), true)).map { resp ->
                resp.views.mapNotNull {
                    when (it) {
                        is GetServicesResponseViewUnion.LabelerView -> null
                        is GetServicesResponseViewUnion.LabelerViewDetailed -> it.value
                    }
                }
            }
        }
    }

    suspend fun updateProfile(updateFun: (ProfileUpdate?) -> ProfileUpdate) : Result<StrongRef> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        val repo = id!!
        /// TODO: Add the retry logic from the official client
        return withContext(Dispatchers.IO) {
            val resp = api.getRecord(GetRecordQuery(
                repo, Nsid("app.bsky.actor.profile"), "self"
            )).onFailure {
                return@withContext Result.failure(Error("Failed to get profile record: $it"))
            }.getOrNull()
            val cid = resp?.cid
            val uri = resp?.uri ?: return@withContext Result.failure(Error("No profile record found"))
            val existing = try {
                resp.`value`.let { json.decodeFromJsonElement<ProfileUpdate>(it) }
            } catch (e: Exception) {
                return@withContext Result.failure(Error("Failed to decode profile record: $e"))
            }
            val new = updateFun(existing)
            api.putRecord(PutRecordRequest(
                repo, Nsid("app.bsky.actor.profile"), "self",
                record = json.encodeToJsonElement(new), swapRecord = cid
            )).map {
                StrongRef(uri, it.cid)
            }
        }
    }

    suspend fun mute(actor: AtIdentifier) : Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext api.muteActor(MuteActorRequest(actor))
    }

    suspend fun unmute(actor: AtIdentifier) : Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext api.unmuteActor(UnmuteActorRequest(actor))
    }

    suspend fun muteList(list: AtUri) : Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext api.muteActorList(MuteActorListRequest(list))
    }

    suspend fun unmuteList(list: AtUri) : Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext api.unmuteActorList(UnmuteActorListRequest(list))
    }

    suspend fun blockModList(list: AtUri) : Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext makeRecord(RecordUnion.ListBlock(list)).map { }
    }

    suspend fun unblockModList(list: AtUri) : Result<Unit> = withContext(Dispatchers.IO) {
        val repo = id ?: return@withContext Result.failure(Error("Not logged in"))
        val listInfo = api.getList(GetListQuery(list)).onFailure {
            return@withContext Result.failure(Error("Failed to get list info: $it"))
        }.getOrNull() ?: return@withContext Result.failure(Error("List not found"))
        val blocked = listInfo.list.viewer?.blocked
        return@withContext if (blocked != null) {
            val rkey = getRkey(blocked)
            deleteRecord(RecordType.ListBlock, rkey)
        } else Result.success(Unit)
    }

    suspend fun updateSeenNotifications(
        seenAt: Timestamp = Clock.System.now()
    ) : Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext api.updateSeen(UpdateSeenRequest(seenAt))
    }
}

@Serializable
data class ProfileUpdate(
    val displayName: String? = null,
    val description: String? = null,
    val avatar: String? = null,
    val banner: String? = null,
    val labels: List<SelfLabel> = emptyList(),
    val joinedViaStarterPack: StarterPackViewBasic? = null,
    val createdAt: Timestamp? = null,
    val map: Map<String, JsonElement> = emptyMap(),
) {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("\$type")
    @EncodeDefault(EncodeDefault.Mode.ALWAYS) public val type: String = "app.bsky.actor.profile"
}

@Serializable
public data class BskyPreferences(
    val feedView: FeedViewPref? = null,
    val saved: List<SavedFeed> = emptyList(),
    val personalDetails: PersonalDetailsPref? = null,
    val modPrefs: ModerationPreferences = ModerationPreferences(),
    val threadPrefs: ThreadViewPref? = null,
    val interests: List<String> = emptyList(),
    val skyFeedBuilderFeeds: List<AtUri> = emptyList(),
    @Deprecated("use v2") val savedFeeds: SavedFeedsPref? = null,
    val timelineIndex: Int? = null, // extracted from v1 saved feeds for now
)

fun GetPreferencesResponse.toPreferences() : BskyPreferences {
    var newPrefs = BskyPreferences()
    var newModPrefs = newPrefs.modPrefs
    val labelPrefs = mutableListOf<ContentLabelPref>()
    val labelers = mutableListOf<Did>()
    val labelMap = labelers.associate {
        it.did to mutableMapOf<LabelValueID, Visibility>() }.toMutableMap()
    this.preferences.forEach { pref: PreferencesUnion ->
        when(pref) {
            is PreferencesUnion.FeedViewPref -> newPrefs = newPrefs.copy(feedView = pref.value)
            is PreferencesUnion.AdultContentPref -> newModPrefs = newModPrefs.copy(adultContentEnabled = pref.value.enabled)
            is PreferencesUnion.BskyAppStatePref -> {}
            is PreferencesUnion.ContentLabelPref -> labelPrefs.add(pref.value)
            is PreferencesUnion.HiddenPostsPref -> newModPrefs = newModPrefs.copy(hiddenPosts = pref.value.items)
            is PreferencesUnion.InterestsPref -> newPrefs = newPrefs.copy(interests = pref.value.tags)
            is PreferencesUnion.LabelersPref -> labelers.addAll(pref.value.labelers.map { it.did })
            is PreferencesUnion.MutedWordsPref -> newModPrefs = newModPrefs.copy(mutedWords = pref.value.items)
            is PreferencesUnion.PersonalDetailsPref -> newPrefs = newPrefs.copy(personalDetails = pref.value)
            is PreferencesUnion.SavedFeedsPref -> newPrefs = newPrefs.copy(savedFeeds = pref.value, timelineIndex = pref.value.timelineIndex)
            is PreferencesUnion.SavedFeedsPrefV2 -> newPrefs = newPrefs.copy(saved = pref.value.items)
            is PreferencesUnion.SkyFeedBuilderFeedsPref -> newPrefs = newPrefs.copy(skyFeedBuilderFeeds = pref.value.feeds)
            is PreferencesUnion.ThreadViewPref -> newPrefs = newPrefs.copy(threadPrefs = pref.value)
        }
    }
    for (pref in labelPrefs) {
        if (pref.labelerDid != null) {
            val labeler = labelers.firstOrNull { it == pref.labelerDid }
            if (labeler == null) continue
            val labelerMap = labelMap[labeler.did] ?: mutableMapOf()
            labelerMap[pref.label] = pref.visibility
            labelMap[labeler.did] = labelerMap
        } else {
            val prefMap = newModPrefs.labels.toMutableMap()
            prefMap[pref.label] = pref.visibility
            newModPrefs = newModPrefs.copy(labels = prefMap.toMap())
        }
    }
    newModPrefs = newModPrefs.copy(labelers = labelMap.mapValues { it.value.toMap() })
    return newPrefs.copy(modPrefs = newModPrefs)
}


@Serializable
data class PagedList<T>(
    val cursor: String?,
    val items: List<T> = emptyList(),
)

@Serializable
data class ProfileListResponse(
    val subject: ProfileView,
    val cursor: String?,
    val profiles: List<ProfileView>,
)

@Serializable
data class PostQueryResponse<T>(
    val uri: AtUri,
    val cid: Cid? = null,
    val cursor: String? = null,
    val posts: List<T> = emptyList(),
)
typealias LabelerID = String
typealias LabelValueID = String
@Serializable
data class ModerationPreferences(
    val adultContentEnabled: Boolean = false,
    val labels: Map<String, Visibility> = mapOf(
        LabelValue.HIDE.value to Hide.defaultSetting!!,
        LabelValue.WARN.value to Warn.defaultSetting!!,
        LabelValue.NO_UNAUTHENTICATED.value to NoUnauthed.defaultSetting!!,
        LabelValue.PORN.value to Porn.defaultSetting!!,
        LabelValue.SEXUAL.value to Sexual.defaultSetting!!,
        LabelValue.NUDITY.value to Nudity.defaultSetting!!,
        LabelValue.GRAPHIC_MEDIA.value to GraphicMedia.defaultSetting!!,
    ),
    val labelers: Map<LabelerID, Map<LabelValueID, Visibility>> = mapOf(), // DID -> labelValue -> setting
    val hiddenPosts: List<AtUri> = emptyList(),
    val mutedWords: List<MutedWord> = emptyList(),
)

@Parcelize
data class ContentHandling(
    val scope: Blurs,
    val action: LabelAction,
    val source: LabelDescription,
    val id: String,
    val icon: LabelIcon,
): Parcelable



@Parcelize

@Serializable
sealed interface LabelIcon: Parcelable {
    val labelerAvatar: String?


    @Serializable
    
    data class CircleBanSign(
        override val labelerAvatar: String?
    ): LabelIcon

    @Serializable
    
    data class Warning(
        override val labelerAvatar: String?
    ): LabelIcon

    @Serializable
    
    data class EyeSlash(
        override val labelerAvatar: String?
    ): LabelIcon

    @Serializable
    
    data class CircleInfo(
        override val labelerAvatar: String?
    ): LabelIcon

}

@Parcelize

@Serializable
sealed interface LabelDescription: Parcelable {
    val name: String
    val description: String

    @Parcelize
    
    @Serializable
    sealed interface Block: LabelDescription, Parcelable
    @Parcelize
    
    @Serializable
    data object Blocking: Block {
        override val name: String = "User Blocked"
        override val description: String = "You have blocked this user. You cannot view their content"

    }
    @Parcelize
    
    @Serializable
    data object BlockedBy: Block {
        override val name: String = "User Blocking You"
        override val description: String = "This user has blocked you. You cannot view their content."
    }
    @Parcelize
    
    @Serializable
    data class BlockList(
        val listName: String,
        val listUri: AtUri,
    ): Block {
        override val name: String = "User Blocked by $listName"
        override val description: String = "This user is on a block list you subscribe to. You cannot view their content."
    }
    @Parcelize
    
    @Serializable
    data object OtherBlocked: Block {
        override val name: String = "Content Not Available"
        override val description: String = "This content is not available because one of the users involved has blocked the other."
    }

    @Parcelize
    
    @Serializable
    sealed interface Muted: LabelDescription, Parcelable

    @Parcelize
    
    @Serializable
    data class MuteList(
        val listName: String,
        val listUri: AtUri,
    ): Muted {
        override val name: String = "User Muted by $listName"
        override val description: String = "This user is on a mute list you subscribe to."
    }
    @Parcelize
    
    @Serializable
    data object YouMuted: Muted {
        override val name: String = "Account Muted"
        override val description: String = "You have muted this user."
    }
    @Parcelize
    
    @Serializable
    data class MutedWord(val word: String): Muted {
        override val name: String = "Post Hidden by Muted Word"
        override val description: String = "This post contains the word or tag \"$word\". You've chosen to hide it."
    }

    @Parcelize
    
    @Serializable
    data class HiddenPost(val uri: AtUri): LabelDescription {
        override val name: String = "Post Hidden by You"
        override val description: String = "You have hidden this post."
    }

    @Parcelize
    
    @Serializable
    data class Label(
        override val name: String,
        override val description: String,
        val severity: Severity,
    ): LabelDescription
}

@Parcelize

@Serializable
sealed interface LabelSource: Parcelable {
    
    @Serializable
    data object User: LabelSource
    
    @Serializable
    data class List(
        val list: ListView,
    ): LabelSource
    
    @Serializable
    data class Labeler(
        val labeler: LabelerViewDetailed,
    ): LabelSource
}

@Parcelize

@Serializable
sealed interface LabelCause: Parcelable {
    val downgraded: Boolean
    val priority: Int
    val source: LabelSource
    
    @Serializable
    data class Blocking(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 3
    }
    
    @Serializable
    data class BlockedBy(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 4
    }

    
    @Serializable
    data class BlockOther(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 4
    }

    
    @Serializable
    data class Label(
        override val source: LabelSource,
        val label: BskyLabel,
        val labelDef: InterpretedLabelDefinition,
        val target: LabelTarget,
        val setting: DefaultSetting,
        val behaviour: ModBehaviour,
        val noOverride: Boolean,
        override val priority: Int,
        override val downgraded: Boolean,
    ): LabelCause {
        init {
            require(
                priority == 1 || priority == 2 || priority == 3 ||
                        priority == 5 || priority == 7 || priority == 8
            )
        }
    }

    
    @Serializable
    data class Muted(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 6
    }

    
    @Serializable
    data class MutedWord(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 6
    }

    
    @Serializable
    data class Hidden(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 6
    }

}

@Serializable
enum class LabelValueDefFlag {
    NoOverride,
    Adult,
    Unauthed,
    NoSelf,
}

@Parcelize
@Serializable

open class InterpretedLabelDefinition(
    val identifier: String,
    val configurable: Boolean,
    val severity: Severity,
    val whatToHide: Blurs,
    val defaultSetting: Visibility?,
    @Contextual
    val flags: List<LabelValueDefFlag> = persistentListOf(),
    val behaviours: ModBehaviours,
    val localizedName: String = "",
    val localizedDescription: String = "",
    @Contextual
    val allDescriptions: List<LabelValueDefinitionStrings> = persistentListOf(),
): Parcelable {
    companion object {

    }

    public fun toContentHandling(target: LabelTarget, avatar: String? = null): ContentHandling {
        val action = behaviours.forScope(whatToHide, target).minOrNull() ?: when(defaultSetting) {
            Visibility.HIDE -> LabelAction.Blur
            Visibility.WARN -> LabelAction.Alert
            Visibility.IGNORE -> LabelAction.Inform
            Visibility.SHOW -> LabelAction.None
            null -> LabelAction.None
        }
        return ContentHandling(
            id = identifier,
            scope = whatToHide,
            action = action,
            source = LabelDescription.Label(
                name = localizedName,
                description = localizedDescription,
                severity = severity,
            ),
            icon = when(severity) {
                Severity.ALERT -> LabelIcon.Warning(labelerAvatar = avatar)
                Severity.NONE -> LabelIcon.CircleInfo(labelerAvatar = avatar)
                Severity.INFORM -> LabelIcon.CircleInfo(labelerAvatar = avatar)
            }
        )
    }
}

val LABELS: PersistentMap<LabelValue, InterpretedLabelDefinition> = persistentMapOf(
    LabelValue.HIDE to Hide,
    LabelValue.WARN to Warn,
    LabelValue.NO_UNAUTHENTICATED to NoUnauthed,
    LabelValue.PORN to Porn,
    LabelValue.SEXUAL to Sexual,
    LabelValue.NUDITY to Nudity,
    LabelValue.GRAPHIC_MEDIA to GraphicMedia,
)

@Parcelize

@Serializable
data object Hide: InterpretedLabelDefinition(
    "!hide",
    false,
    Severity.ALERT,
    Blurs.CONTENT,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.NoSelf, LabelValueDefFlag.NoOverride),
    ModBehaviours(
        account = ModBehaviour(
            profileList = LabelAction.Blur,
            profileView = LabelAction.Blur,
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
    ),
    localizedName = "Hide",
    localizedDescription = "Hide",
)


@Serializable
data object Warn: InterpretedLabelDefinition(
    "!warn",
    false,
    Severity.NONE,
    Blurs.CONTENT,
    Visibility.WARN,
    persistentListOf(LabelValueDefFlag.NoSelf),
    ModBehaviours(
        account = ModBehaviour(
            profileList = LabelAction.Blur,
            profileView = LabelAction.Blur,
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
    ),
    localizedName = "Warn",
    localizedDescription = "Warn",
)

@Parcelize

@Serializable
data object NoUnauthed: InterpretedLabelDefinition(
    "!no-unauthenticated",
    false,
    Severity.NONE,
    Blurs.CONTENT,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.NoOverride, LabelValueDefFlag.Unauthed),
    ModBehaviours(
        account = ModBehaviour(
            profileList = LabelAction.Blur,
            profileView = LabelAction.Blur,
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
    ),
    localizedName = "No Unauthenticated",
    localizedDescription = "Do not show to unauthenticated users",
)


@Serializable
data object Porn: InterpretedLabelDefinition(
    "porn",
    true,
    Severity.NONE,
    Blurs.MEDIA,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.Adult),
    ModBehaviours(
        account = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentMedia = LabelAction.Blur,
        ),
    ),
    localizedName = "Sexually Explicit",
    localizedDescription = "This content is sexually explicit",
)


@Serializable
data object Sexual: InterpretedLabelDefinition(
    "sexual",
    true,
    Severity.NONE,
    Blurs.MEDIA,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.Adult),
    ModBehaviours(
        account = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentMedia = LabelAction.Blur,
        ),
    ),
    localizedName = "Suggestive",
    localizedDescription = "This content may be suggestive or sexual in nature",
)


@Serializable
data object Nudity: InterpretedLabelDefinition(
    "nudity",
    true,
    Severity.NONE,
    Blurs.MEDIA,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.Adult),
    ModBehaviours(
        account = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentMedia = LabelAction.Blur,
        ),
    ),
    localizedName = "Nudity",
    localizedDescription = "This content contains nudity, artistic or otherwise",
)


@Serializable
data object GraphicMedia: InterpretedLabelDefinition(
    "graphic-media",
    true,
    Severity.NONE,
    Blurs.MEDIA,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.Adult),
    ModBehaviours(
        account = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentMedia = LabelAction.Blur,
        ),
    ),
    localizedName = "Graphic Content",
    localizedDescription = "This content is graphic or violent in nature",
)

@OptIn(ExperimentalSerializationApi::class)
@Parcelize
@Serializable

data class BskyLabel(
    val version: Long?,
    val creator: Did,
    val uri: AtUri,
    val cid: Cid?,
    val value: String,
    val overwritesPrevious: Boolean?,
    val createdTimestamp: Timestamp,
    val expirationTimestamp: Timestamp?,
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @ByteString
    val signature: ByteArray?,
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BskyLabel

        if (version != other.version) return false
        if (creator != other.creator) return false
        if (uri != other.uri) return false
        if (cid != other.cid) return false
        if (value != other.value) return false
        if (overwritesPrevious != other.overwritesPrevious) return false
        if (createdTimestamp != other.createdTimestamp) return false
        if (expirationTimestamp != other.expirationTimestamp) return false
        if (signature != null) {
            if (other.signature == null) return false
            if (!signature.contentEquals(other.signature)) return false
        } else if (other.signature != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version?.hashCode() ?: 0
        result = 31 * result + creator.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + (cid?.hashCode() ?: 0)
        result = 31 * result + value.hashCode()
        result = 31 * result + (overwritesPrevious?.hashCode() ?: 0)
        result = 31 * result + createdTimestamp.hashCode()
        result = 31 * result + (expirationTimestamp?.hashCode() ?: 0)
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        return result
    }

    fun getLabelValue(): LabelValue? {
        return when (value) {
            LabelValue.PORN.value -> LabelValue.PORN
            LabelValue.GORE.value -> LabelValue.GORE
            LabelValue.NSFL.value -> LabelValue.NSFL
            LabelValue.SEXUAL.value -> LabelValue.SEXUAL
            LabelValue.GRAPHIC_MEDIA.value -> LabelValue.GRAPHIC_MEDIA
            LabelValue.NUDITY.value -> LabelValue.NUDITY
            LabelValue.DOXXING.value -> LabelValue.DOXXING
            LabelValue.DMCA_VIOLATION.value -> LabelValue.DMCA_VIOLATION
            LabelValue.NO_PROMOTE.value -> LabelValue.NO_PROMOTE
            LabelValue.NO_UNAUTHENTICATED.value -> LabelValue.NO_UNAUTHENTICATED
            LabelValue.WARN.value -> LabelValue.WARN
            LabelValue.HIDE.value -> LabelValue.HIDE
            else -> null
        }
    }
}

@Serializable
enum class LabelScope {
    Content,
    Media,
    None,
}

fun Blurs.toScope(): LabelScope {
    return when (this) {
        Blurs.CONTENT -> LabelScope.Content
        Blurs.MEDIA -> LabelScope.Media
        Blurs.NONE -> LabelScope.None
    }
}


@Serializable
enum class LabelAction {
    Blur,
    Alert,
    Inform,
    None
}


@Serializable
enum class LabelTarget {
    Account,
    Profile,
    Content
}

@Parcelize

@Serializable
open class ModBehaviour(
    val profileList: LabelAction = LabelAction.None,
    val profileView: LabelAction = LabelAction.None,
    val avatar: LabelAction = LabelAction.None,
    val banner: LabelAction = LabelAction.None,
    val displayName: LabelAction = LabelAction.None,
    val contentList: LabelAction = LabelAction.None,
    val contentView: LabelAction = LabelAction.None,
    val contentMedia: LabelAction = LabelAction.None,
): Parcelable {
    init {
        require(avatar != LabelAction.Inform)
        require(banner != LabelAction.Inform && banner != LabelAction.Alert)
        require(displayName != LabelAction.Inform && displayName != LabelAction.Alert)
        require(contentMedia != LabelAction.Inform && contentMedia != LabelAction.Alert)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ModBehaviour

        if (profileList != other.profileList) return false
        if (profileView != other.profileView) return false
        if (avatar != other.avatar) return false
        if (banner != other.banner) return false
        if (displayName != other.displayName) return false
        if (contentList != other.contentList) return false
        if (contentView != other.contentView) return false
        if (contentMedia != other.contentMedia) return false

        return true
    }

    override fun hashCode(): Int {
        var result = profileList.hashCode()
        result = 31 * result + profileView.hashCode()
        result = 31 * result + avatar.hashCode()
        result = 31 * result + banner.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + contentList.hashCode()
        result = 31 * result + contentView.hashCode()
        result = 31 * result + contentMedia.hashCode()
        return result
    }
}

@Parcelize

@Serializable
data class ModBehaviours(
    val account: ModBehaviour = ModBehaviour(),
    val profile: ModBehaviour = ModBehaviour(),
    val content: ModBehaviour = ModBehaviour(),
): Parcelable {
    fun forScope(scope: Blurs, target: LabelTarget): List<LabelAction> {
        return when (target) {
            LabelTarget.Account -> when (scope) {
                Blurs.CONTENT -> listOf(
                    account.contentList, account.contentView, account.avatar,
                    account.banner, account.profileList, account.profileView,
                    account.displayName
                )
                Blurs.MEDIA -> listOf(account.contentMedia, account.avatar, account.banner)
                Blurs.NONE -> listOf()
            }
            LabelTarget.Profile -> when (scope) {
                Blurs.CONTENT -> listOf(profile.contentList, profile.contentView, profile.displayName)
                Blurs.MEDIA -> listOf(profile.avatar, profile.banner, profile.contentMedia)
                Blurs.NONE -> listOf()
            }
            LabelTarget.Content -> when (scope) {
                Blurs.CONTENT -> listOf(content.contentList, content.contentView)
                Blurs.MEDIA -> listOf(
                    content.contentMedia,
                    content.avatar,
                    content.banner
                )
                Blurs.NONE -> listOf()
            }
        }
    }
}


@Serializable
open class DescribedBehaviours(
    val behaviours: ModBehaviours,
    val label: String,
    val description: String,
){

}



@Serializable
data object BlockBehaviour: ModBehaviour(
    profileList = LabelAction.Blur,
    profileView = LabelAction.Blur,
    avatar = LabelAction.Blur,
    banner = LabelAction.Blur,
    contentList = LabelAction.Blur,
    contentView = LabelAction.Blur,
)


@Serializable
data object MuteBehaviour: ModBehaviour(
    profileList = LabelAction.Inform,
    profileView = LabelAction.Alert,
    contentList = LabelAction.Blur,
    contentView = LabelAction.Inform,
)


@Serializable
data object MuteWordBehaviour: ModBehaviour(
    contentList = LabelAction.Blur,
    contentView = LabelAction.Blur,
)


@Serializable
data object HideBehaviour: ModBehaviour(
    contentList = LabelAction.Blur,
    contentView = LabelAction.Blur,
)


@Serializable
data object InappropriateMediaBehaviour: ModBehaviour(
    contentMedia = LabelAction.Blur,
)


@Serializable
data object InappropriateAvatarBehaviour: ModBehaviour(
    avatar = LabelAction.Blur,
)


@Serializable
data object InappropriateBannerBehaviour: ModBehaviour(
    banner = LabelAction.Blur,
)


@Serializable
data object InappropriateDisplayNameBehaviour: ModBehaviour(
    displayName = LabelAction.Blur,
)


@Serializable
val BlurAllMedia = ModBehaviours(
    content = InappropriateMediaBehaviour,
    profile = ModBehaviour(
        avatar = LabelAction.Blur,
        banner = LabelAction.Blur,
        contentMedia = LabelAction.Blur,
    ),
    account = ModBehaviour(
        avatar = LabelAction.Blur,
        banner = LabelAction.Blur,
        contentMedia = LabelAction.Blur,
    ),
)



@Serializable
data object NoopBehaviour: ModBehaviour()
