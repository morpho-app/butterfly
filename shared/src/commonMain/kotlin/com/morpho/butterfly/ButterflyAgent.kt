package com.morpho.butterfly

import app.bsky.actor.ContentLabelPref
import app.bsky.actor.FeedViewPref
import app.bsky.actor.GetProfileQuery
import app.bsky.actor.GetProfilesQuery
import app.bsky.actor.GetSuggestionsQuery
import app.bsky.actor.HiddenPostsPref
import app.bsky.actor.LabelerPrefItem
import app.bsky.actor.LabelersPref
import app.bsky.actor.MutedWord
import app.bsky.actor.MutedWordsPref
import app.bsky.actor.PreferencesUnion
import app.bsky.actor.ProfileView
import app.bsky.actor.ProfileViewBasic
import app.bsky.actor.ProfileViewDetailed
import app.bsky.actor.PutPreferencesRequest
import app.bsky.actor.SavedFeed
import app.bsky.actor.SavedFeedsPrefV2
import app.bsky.actor.SearchActorsQuery
import app.bsky.actor.SearchActorsTypeaheadQuery
import app.bsky.actor.ThreadViewPref
import app.bsky.actor.Visibility
import app.bsky.feed.FeedViewPost
import app.bsky.feed.GetActorLikesQuery
import app.bsky.feed.GetAuthorFeedFilter
import app.bsky.feed.GetAuthorFeedQuery
import app.bsky.feed.GetLikesLike
import app.bsky.feed.GetLikesQuery
import app.bsky.feed.GetPostThreadQuery
import app.bsky.feed.GetPostThreadResponse
import app.bsky.feed.GetPostsQuery
import app.bsky.feed.GetQuotesQuery
import app.bsky.feed.GetRepostedByQuery
import app.bsky.feed.GetTimelineQuery
import app.bsky.feed.Like
import app.bsky.feed.PostView
import app.bsky.feed.Repost
import app.bsky.graph.Block
import app.bsky.graph.Follow
import app.bsky.graph.GetFollowersQuery
import app.bsky.graph.GetFollowsQuery
import app.bsky.graph.GetListQuery
import app.bsky.graph.Listblock
import app.bsky.graph.MuteActorListRequest
import app.bsky.graph.MuteActorRequest
import app.bsky.graph.UnmuteActorListRequest
import app.bsky.graph.UnmuteActorRequest
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
import com.atproto.moderation.CreateReportRequest
import com.atproto.moderation.CreateReportResponse
import com.atproto.moderation.ReportRequestSubject
import com.atproto.repo.CreateRecordRequest
import com.atproto.repo.DeleteRecordRequest
import com.atproto.repo.GetRecordQuery
import com.atproto.repo.PutRecordRequest
import com.atproto.repo.StrongRef
import com.morpho.butterfly.auth.AuthInfo
import com.morpho.butterfly.auth.Credentials
import com.morpho.butterfly.auth.Server
import com.morpho.butterfly.auth.SessionRepository
import com.morpho.butterfly.auth.UserRepository
import com.morpho.butterfly.model.Blob
import com.morpho.butterfly.model.RecordType
import com.morpho.butterfly.model.RecordUnion
import com.morpho.butterfly.model.TID
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.lighthousegames.logging.logging


open class ButterflyAgent(
    userData: UserRepository,
    session: SessionRepository,
): AtpAgent(userData, session) {
    var prefs: BskyPreferences = BskyPreferences()
        protected set

    var labelers: List<Did> = emptyList()
        protected set

    protected val appLabelers: List<Did> = listOf(BSKY_LABELER_DID)

    suspend fun createRecord(record: RecordUnion) : Result<StrongRef> {
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
        log.d {"Record request: $request"}
        return api.createRecord(request).onFailure { log.e { "Failed to create record: $it" } }
            .map { StrongRef(it.uri, it.cid) }
    }

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

    companion object {
        val log = logging("ButterflyAgent")
    }

    suspend fun deleteRecord(type: RecordType, rkey: String): Result<Unit> {
        if (id == null) return Result.failure(Error("Not logged in"))
        log.v { "Deleting record $rkey of type $type" }
        return api.deleteRecord(DeleteRecordRequest(id!!, type.collection, rkey))
    }

    override suspend fun resumeSession() {
        super.resumeSession()
        getPreferences()
    }

    override suspend fun login(
        credentials: Credentials,
        userServer: Server
    ): Result<AuthInfo> {
        val result = super.login(credentials, userServer)
        if (result.isSuccess) {
            getPreferences()
        }
        return result
    }

    fun post(post: app.bsky.feed.Post) = CoroutineScope(Dispatchers.IO).launch { createRecord(RecordUnion.MakePost(post)) }
    fun deletePost(uri: AtUri) = CoroutineScope(Dispatchers.IO).launch { deleteRecord(RecordType.Post, uri) }

    fun like(post: StrongRef) = CoroutineScope(Dispatchers.IO).launch { createRecord(RecordUnion.Like(post)) }
    fun deleteLike(uri: AtUri)  { deleteRecord(RecordType.Like, uri) }

    fun repost(post: StrongRef) = CoroutineScope(Dispatchers.IO).launch { createRecord(RecordUnion.Repost(post)) }
    fun deleteRepost(uri: AtUri) { deleteRecord(RecordType.Repost, uri) }

    fun block(subject: Did) = CoroutineScope(Dispatchers.IO).launch { createRecord(RecordUnion.Block(subject)) }
    fun unblock(uri: AtUri) { deleteRecord(RecordType.Block, uri) }

    fun follow(subject: Did) = CoroutineScope(Dispatchers.IO).launch { createRecord(RecordUnion.Follow(subject)) }
    fun deleteFollow(uri: AtUri) { deleteRecord(RecordType.Follow, uri) }

    suspend fun getPreferences(): Result<BskyPreferences> = withContext(Dispatchers.IO) {
        return@withContext api.getPreferences().map { prefs ->
            labelers = prefs.preferences.toLabelerDids()
            prefs
        }.map {
            val newPrefs = it.toPreferences()
            prefs = newPrefs
            newPrefs
        }
    }

    suspend fun resolveHandle(handle: AtIdentifier): Result<Did> {
        return withContext(Dispatchers.IO) {
            if (handle is Did) return@withContext Result.success(handle)
            api.resolveHandle(ResolveHandleQuery(Handle(handle.toString()))).map { it.did }
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
    ): Result<PagedResponse.Feed<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getTimeline(GetTimelineQuery(algorithm, limit, cursor))
                .map { resp -> PagedResponse.Feed(Cursor(resp.cursor), resp.feed) }
        }
    }
    suspend fun getTimeline(
        query: JsonElement, cursor: String? = null
    ): Result<PagedResponse.Feed<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        try {
            val newQuery = json.decodeFromJsonElement<GetTimelineQuery>(query)
                .copy(cursor = cursor)
            return withContext(Dispatchers.IO) {
                api.getTimeline(newQuery)
                    .map { resp -> PagedResponse.Feed(Cursor(resp.cursor), resp.feed) }
            }
        } catch (e: Exception) {
            return Result.failure(Error("Invalid query: $e"))
        }
    }
    suspend fun getTimeline(query: JsonElement): Result<PagedResponse.Feed<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        try {
            val newQuery = json.decodeFromJsonElement<GetTimelineQuery>(query)
            return withContext(Dispatchers.IO) {
                api.getTimeline(newQuery).map { resp ->
                    PagedResponse.Feed(Cursor(resp.cursor), resp.feed) }
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
    ): Result<PagedResponse.Feed<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getAuthorFeed(GetAuthorFeedQuery(actor, limit, cursor, filter))
                .map { resp -> PagedResponse.Feed(Cursor(resp.cursor), resp.feed) }
        }
    }

    suspend fun getActorLikes(
        actor: AtIdentifier,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PagedResponse.Feed<FeedViewPost>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getActorLikes(GetActorLikesQuery(actor, limit, cursor))
                .map { resp -> PagedResponse.Feed(Cursor(resp.cursor), resp.feed) }
        }
    }

    suspend fun getFollowers(
        actor: AtIdentifier,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PagedResponse.Profile<ProfileView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getFollowers(GetFollowersQuery(actor, limit, cursor))
                .map { resp ->
                    PagedResponse.Profile(resp.subject, Cursor(resp.cursor), resp.followers) }
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
    ): Result<PagedResponse.FromRecord<GetLikesLike>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getLikes(GetLikesQuery(uri, cid, limit, cursor))
                .map { resp ->
                    PagedResponse.FromRecord(resp.uri, resp.cid, Cursor(resp.cursor), resp.likes)
                }
        }
    }

    suspend fun getRepostedBy(
        uri: AtUri,
        cid: Cid? = null,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PagedResponse.FromRecord<ProfileView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getRepostedBy(GetRepostedByQuery(uri, cid, limit, cursor))
                .map { resp ->
                    PagedResponse.FromRecord(resp.uri, resp.cid, Cursor(resp.cursor), resp.repostedBy)
                }
        }
    }

    suspend fun getQuotes(
        uri: AtUri,
        cid: Cid? = null,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PagedResponse.FromRecord<PostView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getQuotes(GetQuotesQuery(uri, cid, limit, cursor))
                .map { resp ->
                    PagedResponse.FromRecord(resp.uri, resp.cid, Cursor(resp.cursor), resp.posts)
                }
        }
    }

    suspend fun getFollows(
        actor: AtIdentifier,
        limit: Long? = 50,
        cursor: String? = null,
    ): Result<PagedResponse.Profile<ProfileView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getFollows(GetFollowsQuery(actor, limit, cursor))
                .map { resp -> PagedResponse.Profile(resp.subject, Cursor(resp.cursor), resp.follows) }
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
    ): Result<PagedResponse.Feed<ProfileView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.getSuggestions(GetSuggestionsQuery(limit, cursor))
                .map { resp -> PagedResponse.Feed(Cursor(resp.cursor), resp.actors) }
        }
    }

    suspend fun searchActors(
        term: String? = null,
        q: String? = null,
        limit: Long? = 25,
        cursor: String? = null,
    ): Result<PagedResponse.Feed<ProfileView>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.searchActors(SearchActorsQuery(term, q, limit, cursor))
                .map { resp -> PagedResponse.Feed(Cursor(resp.cursor), resp.actors) }
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
    ): Result<PagedResponse.Feed<ListNotificationsNotification>> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        return withContext(Dispatchers.IO) {
            api.listNotifications(ListNotificationsQuery(limit, cursor, seenAt))
                .map { resp -> PagedResponse.Feed(Cursor(resp.cursor), resp.notifications) }
        }
    }

    suspend fun unreadNotificationsCount(
        seenAt: Timestamp? = null,
    ): Result<Long> {
        if (!isLoggedIn) return Result.failure(Error("Not logged in"))
        runCatching {
            return withContext(Dispatchers.IO) {
                api.getUnreadCount(GetUnreadCountQuery(false,seenAt)).map { it.count }
            }
        }.getOrElse {
            return Result.failure(Error("Failed to get unread notifications count: $it"))
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

    suspend fun getLabelDefinitions(prefs: BskyPreferences): Map<LabelerID, Map<LabelValueID, InterpretedLabelDefinition>> {
        val dids: MutableList<LabelValueID> = appLabelers.map { it.did }.toMutableList()
        dids.addAll(prefs.modPrefs.labelers.map { it.key })
        return getLabelDefinitions(dids)
    }

    suspend fun getLabelDefinitions(prefs: ModerationPreferences): Map<LabelerID, Map<LabelValueID, InterpretedLabelDefinition>> {
        val dids: MutableList<LabelValueID> = appLabelers.map { it.did }.toMutableList()
        dids.addAll(prefs.labelers.map { it.key })
        return getLabelDefinitions(dids)
    }

    suspend fun getLabelDefinitions(prefs: List<LabelValueID>): Map<LabelerID, Map<LabelValueID, InterpretedLabelDefinition>> {
        val labelDefs = getLabelersDetailed(prefs.map { Did(it) }).map { labelers ->
            val labelDefs = mutableMapOf<LabelerID, Map<LabelValueID, InterpretedLabelDefinition>>()
            for (labeler in labelers) {
                labelDefs[labeler.creator.did.did] = InterpretedLabelDefinition.interpretLabelValueDefinitions(labeler)
            }
            labelDefs.toMap()
        }.onFailure { return emptyMap() }.getOrNull() ?: emptyMap()
        return labelDefs
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
        return@withContext createRecord(RecordUnion.ListBlock(list)).map { }
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
    ): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext api.updateSeen(UpdateSeenRequest(seenAt))
    }

    fun updateSavedFeeds(feedsToUpdate: List<SavedFeed>) = serviceScope.launch {
        val update: (List<SavedFeed>) -> List<SavedFeed> = { feeds ->
            feeds.map { savedFeed ->
                val updatedVersion = feedsToUpdate.firstOrNull { it.id == savedFeed.id }
                if (updatedVersion != null) {
                    savedFeed.copy(pinned = updatedVersion.pinned)
                } else savedFeed
            }
        }
        updateSavedFeedsV2Prefs(update)
    }

    fun overwriteSavedFeeds(savedFeeds: List<SavedFeed>) = serviceScope.launch {
        val uniqueFeeds = mutableMapOf<String, SavedFeed>()
        savedFeeds.forEach { savedFeed ->
            if(uniqueFeeds.containsKey(savedFeed.id)) {
                uniqueFeeds.remove(savedFeed.id)
            }
            uniqueFeeds[savedFeed.id] = savedFeed
        }
        updateSavedFeedsV2Prefs { _ -> uniqueFeeds.values.toList() }
    }

    fun addSavedFeeds(savedFeeds: List<SavedFeed>) = serviceScope.launch {
        updateSavedFeedsV2Prefs {feeds ->
            feeds + savedFeeds
        }
    }

    fun removeSavedFeeds(ids: List<String>) = serviceScope.launch {
        updateSavedFeedsV2Prefs { feeds ->
            feeds.filter { !ids.contains(it.id) }
        }
    }

    suspend fun updateMutedWord(word: MutedWord) = withContext(Dispatchers.IO) {
        updatePreferences { prefs ->
            val mutedWords = (prefs.lastOrNull {
                it is PreferencesUnion.MutedWordsPref
            } as? PreferencesUnion.MutedWordsPref)?.value?.items?.toList()
            if (mutedWords != null) {
                 val updatedMutedWords = mutedWords.map { existingItem ->
                    if (matchMutedWord(existingItem, word)) {
                        existingItem.copy(
                            value = word.value,
                            targets = word.targets.ifEmpty { existingItem.targets },
                            actorTarget = word.actorTarget ?: existingItem.actorTarget,
                            expiresAt = word.expiresAt ?: existingItem.expiresAt,
                            id = TID.next().toString()
                        )
                    } else word
                }
                val updatedPrefs = prefs.filter { it !is PreferencesUnion.MutedWordsPref }
                    .plus(PreferencesUnion.MutedWordsPref(
                        MutedWordsPref(updatedMutedWords.toPersistentList())
                    ))
                return@updatePreferences updatedPrefs
            } else return@updatePreferences prefs

        }
    }

    suspend fun removeMutedWord(word: MutedWord) = withContext(Dispatchers.IO) {
        updatePreferences { prefs ->
            val mutedWords = (prefs.lastOrNull {
                it is PreferencesUnion.MutedWordsPref
            } as? PreferencesUnion.MutedWordsPref)?.value?.items?.toList()
            if (mutedWords != null) {
                val updatedMutedWords = mutedWords.filter { existingItem ->
                    !matchMutedWord(existingItem, word)
                }
                val updatedPrefs = prefs.filter { it !is PreferencesUnion.MutedWordsPref }
                    .plus(PreferencesUnion.MutedWordsPref(
                        MutedWordsPref(updatedMutedWords.toPersistentList())
                    ))
                return@updatePreferences updatedPrefs
            } else return@updatePreferences prefs
        }
    }

    suspend fun removeMutedWords(words: List<MutedWord>) = withContext(Dispatchers.IO) {
        updatePreferences { prefs ->
            val mutedWords = (prefs.lastOrNull {
                it is PreferencesUnion.MutedWordsPref
            } as? PreferencesUnion.MutedWordsPref)?.value?.items?.toList()
            if (mutedWords != null) {
                val updatedMutedWords = mutedWords.filter { existingItem ->
                    words.none { matchMutedWord(existingItem, it) }
                }
                val updatedPrefs = prefs.filter { it !is PreferencesUnion.MutedWordsPref }
                    .plus(PreferencesUnion.MutedWordsPref(
                        MutedWordsPref(updatedMutedWords.toPersistentList())
                    ))
                return@updatePreferences updatedPrefs
            } else return@updatePreferences prefs

        }
    }

    fun hidePost(postUri: AtUri) = serviceScope.launch {
        updateHiddenPost(postUri, HiddenPostAction.Hide)
    }

    fun unhidePost(postUri: AtUri) = serviceScope.launch {
        updateHiddenPost(postUri, HiddenPostAction.Unhide)
    }

    fun setFeedViewPrefs(feed: String, feedViewPref: FeedViewPref) = serviceScope.launch {
        updatePreferences { prefs ->
            val existingPref = (prefs.lastOrNull {
                it is PreferencesUnion.FeedViewPref && it.value.feed == feed
            } as? PreferencesUnion.FeedViewPref)?.value
            if (existingPref != null) {
                val updatedPref = existingPref.copy(
                    feed = feed,
                    hideReplies = feedViewPref.hideReplies ?: existingPref.hideReplies,
                    hideRepliesByUnfollowed = feedViewPref.hideRepliesByUnfollowed ?: existingPref.hideRepliesByUnfollowed,
                    hideRepliesByLikeCount = feedViewPref.hideRepliesByLikeCount ?: existingPref.hideRepliesByLikeCount,
                    hideReposts = feedViewPref.hideReposts ?: existingPref.hideReposts,
                    hideQuotePosts = feedViewPref.hideQuotePosts ?: existingPref.hideQuotePosts,
                    lab_mergeFeedEnabled = feedViewPref.lab_mergeFeedEnabled ?: existingPref.lab_mergeFeedEnabled
                )
                val updatedPrefs = prefs.filter { it !is PreferencesUnion.FeedViewPref }
                    .plus(PreferencesUnion.FeedViewPref(updatedPref))
                return@updatePreferences updatedPrefs
            } else return@updatePreferences prefs
        }
    }

    fun setThreadViewPrefs(threadViewPref: ThreadViewPref) = serviceScope.launch {
        updatePreferences { prefs ->
            val existingPref = (prefs.lastOrNull {
                it is PreferencesUnion.ThreadViewPref
            } as? PreferencesUnion.ThreadViewPref)?.value
            if (existingPref != null) {
                val updatedPref = existingPref.copy(
                    sort = threadViewPref.sort ?: existingPref.sort,
                    prioritizeFollowedUsers = threadViewPref.prioritizeFollowedUsers ?: existingPref.prioritizeFollowedUsers
                )
                val updatedPrefs = prefs.filter { it !is PreferencesUnion.ThreadViewPref }
                    .plus(PreferencesUnion.ThreadViewPref(updatedPref))
                return@updatePreferences updatedPrefs
            } else return@updatePreferences prefs
        }
    }

    fun setContentLabelPref(
        key: String,
        value: Visibility,
        labelerDid: Did? = null
    ) = serviceScope.launch {
        updatePreferences { prefs ->
            var labelPref = (prefs.lastOrNull {
                it is PreferencesUnion.ContentLabelPref
                        && it.value.labelerDid == labelerDid
                        && it.value.label == key
            } as? PreferencesUnion.ContentLabelPref)?.value
            labelPref = labelPref?.copy(
                visibility = value,
            ) ?: ContentLabelPref(
                labelerDid = labelerDid,
                label = key,
                visibility = value,
            )
            var updatedPrefs = prefs.filter { it !is PreferencesUnion.ContentLabelPref }
            if(labelPref.labelerDid == null) {
                val legacyLabelValue = labelPref.getLegacyLabel()
                if(labelPref.isLegacyLabel()) {
                    val legacyLabelPref = prefs.lastOrNull {
                        it is PreferencesUnion.ContentLabelPref
                                && it.value.labelerDid == null
                                && it.value.label == legacyLabelValue
                    } as? PreferencesUnion.ContentLabelPref
                    val updatedLegacyPref = legacyLabelPref?.value?.copy(visibility = value)
                        ?: ContentLabelPref(
                            labelerDid = null,
                            label = legacyLabelValue,
                            visibility = value,
                        )
                    updatedPrefs = updatedPrefs.plus(PreferencesUnion.ContentLabelPref(updatedLegacyPref))
                }
            }
            updatedPrefs = updatedPrefs.plus(PreferencesUnion.ContentLabelPref(labelPref))
            return@updatePreferences updatedPrefs
        }
    }

    fun addLabeler(did: Did) = serviceScope.launch {
        updatePreferences { prefs ->
            var labelersPref = (prefs.lastOrNull {
                it is PreferencesUnion.LabelersPref }
                    as? PreferencesUnion.LabelersPref)?.value?.labelers?.toList() ?: emptyList()
            if (labelersPref.none { it.did == did }) {
                labelersPref = labelersPref + LabelerPrefItem(did)
            }
            val updatedPrefs = prefs.filter { it !is PreferencesUnion.LabelersPref }
                .plus(PreferencesUnion.LabelersPref(LabelersPref(labelersPref.toPersistentList())))
            return@updatePreferences updatedPrefs
        }.map { prefs ->
            labelers = prefs.toLabelerDids()
        }
    }

    fun removeLabeler(did: Did) = serviceScope.launch {
        updatePreferences { prefs ->
            var labelersPref = (prefs.lastOrNull {
                it is PreferencesUnion.LabelersPref }
                    as? PreferencesUnion.LabelersPref)?.value?.labelers?.toList() ?: emptyList()
            if (labelersPref.any { it.did == did }) {
                labelersPref = labelersPref.filter { it.did != did }
            }
            val updatedPrefs = prefs.filter { it !is PreferencesUnion.LabelersPref }
                .plus(PreferencesUnion.LabelersPref(LabelersPref(labelersPref.toPersistentList())))
            return@updatePreferences updatedPrefs
        }.map { prefs ->
            labelers = prefs.toLabelerDids()
        }
    }

    protected val prefsMutex = Mutex()

    protected suspend fun updatePreferences(
        updateFun: (List<PreferencesUnion>) -> List<PreferencesUnion>?
    ): Result<List<PreferencesUnion>> = withContext(Dispatchers.IO) {
        return@withContext prefsMutex.withLock {
            val prefs = api.getPreferences().map { response ->
                // Uncomment this line if there are incorrect keys in the prefs
                // that you put there by mistake
                // It should clean up the prefs that are not recognized by the server
                response.preferences //.filter { it !is PreferencesUnion.UnknownPreference }
            }.onFailure {
                return@withLock Result.failure(Error("Failed to get preferences: $it"))
            }.getOrNull() ?: return@withLock Result.failure(Error("Preferences not found"))
            val newPrefs = updateFun(prefs) ?: return@withLock Result.success(prefs)
            api.putPreferences(PutPreferencesRequest(newPrefs.toPersistentList())).onFailure {
                return@withLock Result.failure(Error("Failed to update preferences: $it"))
            }.getOrNull() ?: return@withLock Result.failure(Error("Preferences not sent"))
            return@withLock Result.success(prefs)
        }
    }

    protected suspend fun updateSavedFeedsV2Prefs(
        update: (List<SavedFeed>) -> List<SavedFeed>
    ): List<SavedFeed> = withContext(Dispatchers.IO) {
        var maybeMutatedSavedFeeds = listOf<SavedFeed>()
        updatePreferences { prefs ->
            var existingPref = (prefs.lastOrNull {
                it is PreferencesUnion.SavedFeedsPrefV2
            } as? PreferencesUnion.SavedFeedsPrefV2)?.value?.items?.toList()
            if (existingPref != null) {
                maybeMutatedSavedFeeds = update(existingPref)
                existingPref = existingPref + maybeMutatedSavedFeeds
            } else {
                maybeMutatedSavedFeeds = update(emptyList())
                existingPref = maybeMutatedSavedFeeds
            }

            val pinned = existingPref.filter { it.pinned }
            val saved = existingPref.filter { !it.pinned }
            existingPref = pinned + saved

            val updatedPrefs = prefs.filter { it !is PreferencesUnion.SavedFeedsPrefV2 }
                .plus(PreferencesUnion.SavedFeedsPrefV2(SavedFeedsPrefV2(existingPref.toPersistentList())))
            return@updatePreferences updatedPrefs
        }
        return@withContext maybeMutatedSavedFeeds
    }

    protected suspend fun updateHiddenPost(
        postUri: AtUri,
        action: HiddenPostAction
    ): Result<Unit> = withContext(Dispatchers.IO) {
        updatePreferences { prefs ->
            var hiddenPosts = (prefs.lastOrNull {
                it is PreferencesUnion.HiddenPostsPref
            } as? PreferencesUnion.HiddenPostsPref)?.value?.items?.toList()
            if (hiddenPosts != null) {
                hiddenPosts = if (action == HiddenPostAction.Hide) {
                    (hiddenPosts + postUri).distinct()
                } else {
                    hiddenPosts.filter { it != postUri }
                }
            } else if(action == HiddenPostAction.Hide) {
                hiddenPosts = listOf(postUri)
            }
            val updatedPrefs = prefs.filter { it !is PreferencesUnion.HiddenPostsPref }
                .plus(PreferencesUnion.HiddenPostsPref(HiddenPostsPref(
                    hiddenPosts?.toPersistentList() ?: persistentListOf()
                )))
            return@updatePreferences updatedPrefs
        }.map {  }
    }
}

