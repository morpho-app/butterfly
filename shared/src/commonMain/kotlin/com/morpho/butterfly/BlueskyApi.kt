package com.morpho.butterfly

import app.bsky.actor.*
import app.bsky.feed.*
import app.bsky.graph.*
import app.bsky.labeler.GetServicesQuery
import app.bsky.labeler.GetServicesResponse
import app.bsky.notification.*
import app.bsky.unspecced.*
import com.atproto.admin.*
import com.atproto.identity.*
import com.atproto.label.QueryLabels
import com.atproto.label.QueryLabelsResponse
import com.atproto.label.SubscribeLabelsMessage
import com.atproto.label.SubscribeLabelsQuery
import com.atproto.moderation.CreateReportRequest
import com.atproto.moderation.CreateReportResponse
import com.atproto.repo.*
import com.atproto.repo.GetRecordResponse
import com.atproto.server.*
import com.atproto.server.DeleteAccountRequest
import com.atproto.sync.*
import com.atproto.sync.GetBlocksQuery
import com.atproto.temp.CheckSignupQueueResponse
import com.atproto.temp.FetchLabelsQueryParams
import com.atproto.temp.FetchLabelsResponse
import com.morpho.butterfly.auth.AuthInfo
import kotlinx.coroutines.flow.Flow
import tools.ozone.communication.*
import tools.ozone.moderation.*

interface BlueskyApi {
    /**
     * Allow a labeler to apply labels directly.
     */
    public suspend fun applyLabels(request: ApplyLabelsRequest): Result<Unit>

    /**
     * Apply a batch transaction of creates, updates, and deletes.
     */
    public suspend fun applyWrites(request: ApplyWritesRequest): Result<Unit>

    /**
     * Returns the status of an account, especially as pertaining to import or recovery. Can be called
     * many times over the course of an account migration. Requires auth and can only be called
     * pertaining to oneself.
     */
    public suspend fun checkAccountStatus(): Result<CheckAccountStatusResponse>

    /**
     * Check accounts location in signup queue.
     */
    public suspend fun checkSignupQueue(): Result<CheckSignupQueueResponse>


    /**
     * Confirm an email using a token from com.atproto.server.requestEmailConfirmation.
     */
    public suspend fun confirmEmail(request: ConfirmEmailRequest): Result<Unit>

    /**
     * Create an account.
     */
    public suspend fun createAccount(request: CreateAccountRequest):
            Result<CreateAccountResponse>

    /**
     * Create an app-specific password.
     */
    public suspend fun createAppPassword(request: CreateAppPasswordRequest):
            Result<CreateAppPasswordResponse>

    /**
     * Create an invite code.
     */
    public suspend fun createInviteCode(request: CreateInviteCodeRequest):
            Result<CreateInviteCodeResponse>

    /**
     * Create an invite code.
     */
    public suspend fun createInviteCodes(request: CreateInviteCodesRequest):
            Result<CreateInviteCodesResponse>

    /**
     * Create a new record.
     */
    public suspend fun createRecord(request: CreateRecordRequest): Result<CreateRecordResponse>

    /**
     * Report a repo or a record.
     */
    public suspend fun createReport(request: CreateReportRequest): Result<CreateReportResponse>

    /**
     * Create an authentication session.
     */
    public suspend fun createSession(request: CreateSessionRequest):
            Result<CreateSessionResponse>

    /**
     * Administrative action to create a new, re-usable communication (email for now) template.
     */
    public suspend fun createTemplate(request: CreateTemplateRequest):
            Result<CreateTemplateResponse>

    /**
     * Deactivates a currently active account. Stops serving of repo, and future writes to repo until
     * reactivated. Used to finalize account migration with the old host after the account has been
     * activated on the new host.
     */
    public suspend fun deactivateAccount(request: DeactivateAccountRequest): Result<Unit>

    /**
     * Delete a user account with a token and password.
     */
    public suspend fun deleteAccount(request: DeleteAccountRequest): Result<Unit>

    /**
     * Delete a record, or ensure it doesn't exist.
     */
    public suspend fun deleteRecord(request: DeleteRecordRequest): Result<Unit>

    /**
     * Delete the current session.
     */
    public suspend fun deleteSession(): Result<Unit>

    /**
     * Returns information about a given feed generator including TOS & offered feed URIs
     */
    public suspend fun describeFeedGenerator(): Result<DescribeFeedGeneratorResponse>

    /**
     * Get information about the repo, including the list of collections.
     */
    public suspend fun describeRepo(params: DescribeRepoQuery):
            Result<DescribeRepoResponse>

    /**
     * Get a document describing the service's accounts configuration.
     */
    public suspend fun describeServer(): Result<DescribeServerResponse>

    /**
     * Disable an account from receiving new invite codes, but does not invalidate existing codes
     */
    public suspend fun disableAccountInvites(request: DisableAccountInvitesRequest): Result<Unit>

    /**
     * Disable some set of codes and/or all codes associated with a set of users
     */
    public suspend fun disableInviteCodes(request: DisableInviteCodesRequest): Result<Unit>

    /**
     * Take a moderation action on an actor.
     */
    public suspend fun emitEvent(request: EmitEventRequest): Result<EmitEventResponse>

    /**
     * Re-enable an accounts ability to receive invite codes
     */
    public suspend fun enableAccountInvites(request: EnableAccountInvitesRequest): Result<Unit>

    @Deprecated("DEPRECATED: use queryLabels or subscribeLabels instead -- Fetch all labels from a labeler created after a certain date.")
    public suspend fun fetchLabels(params: FetchLabelsQueryParams): Result<FetchLabelsResponse>

    /**
     * Get details about an account.
     */
    public suspend fun getAccountInfo(params: GetAccountInfoQuery):
            Result<GetAccountInfoResponse>

    /**
     * Get details about some accounts.
     */
    public suspend fun getAccountInfos(params: GetAccountInfosQuery):
            Result<GetAccountInfosResponse>


    /**
     * Get all invite codes for a given account
     */
    public suspend fun getAccountInviteCodes(params: GetAccountInviteCodesQuery):
            Result<GetAccountInviteCodesResponse>

    /**
     * Retrieve a list of feeds created by a given actor
     */
    public suspend fun getActorFeeds(params: GetActorFeedsQuery):
            Result<GetActorFeedsResponse>

    /**
     * A view of the posts liked by an actor.
     */
    public suspend fun getActorLikes(params: GetActorLikesQuery):
            Result<GetActorLikesResponse>

    /**
     * A view of an actor's feed.
     */
    public suspend fun getAuthorFeed(params: GetAuthorFeedQuery):
            Result<GetAuthorFeedResponse>

    /**
     * Get a blob associated with a given repo.
     */
    public suspend fun getBlob(params: GetBlobQuery): Result<ByteArray>

    /**
     * Gets blocks from a given repo.
     */
    public suspend fun getBlocks(params: GetBlocksQuery): Result<ByteArray>

    /**
     * Who is the requester's account blocking?
     */
    public suspend fun getBlocks(params: app.bsky.graph.GetBlocksQuery): Result<GetBlocksResponse>

    @Deprecated("DEPRECATED - please use com.atproto.sync.getRepo instead")
    public suspend fun getCheckout(params: GetCheckoutQuery): Result<ByteArray>

    /**
     * Get details about a moderation event.
     */
    public suspend fun getEvent(params: GetEventQueryParams): Result<GetEventResponse>

    /**
     * Compose and hydrate a feed from a user's selected feed generator
     */
    public suspend fun getFeed(params: GetFeedQuery): Result<GetFeedResponse>

    /**
     * Get information about a specific feed offered by a feed generator, such as its online status
     */
    public suspend fun getFeedGenerator(params: GetFeedGeneratorQuery):
            Result<GetFeedGeneratorResponse>

    /**
     * Get information about a list of feed generators
     */
    public suspend fun getFeedGenerators(params: GetFeedGeneratorsQuery):
            Result<GetFeedGeneratorsResponse>

    /**
     * A skeleton of a feed provided by a feed generator
     */
    public suspend fun getFeedSkeleton(params: GetFeedSkeletonQuery):
            Result<GetFeedSkeletonResponse>

    /**
     * Who is following an actor?
     */
    public suspend fun getFollowers(params: GetFollowersQuery):
            Result<GetFollowersResponse>

    /**
     * Who do you follow already who is following an actor?
     */
    public suspend fun getKnownFollowers(params: GetKnownFollowersQuery):
            Result<GetKnownFollowersResponse>

    /**
     * Who is an actor following?
     */
    public suspend fun getFollows(params: GetFollowsQuery): Result<GetFollowsResponse>

    @Deprecated("DEPRECATED - please use com.atproto.sync.getLatestCommit instead")
    public suspend fun getHead(params: GetHeadQuery): Result<GetHeadResponse>

    /**
     * Admin view of invite codes
     */
    public suspend fun getInviteCodes(params: GetInviteCodesQuery):
            Result<GetInviteCodesResponse>

    /**
     * Gets the current commit CID & revision of the repo.
     */
    public suspend fun getLatestCommit(params: GetLatestCommitQuery):
            Result<GetLatestCommitResponse>

    public suspend fun getLikes(params: GetLikesQuery): Result<GetLikesResponse>

    /**
     * Fetch a list of actors
     */
    public suspend fun getList(params: GetListQuery): Result<GetListResponse>

    /**
     * Which lists is the requester's account blocking?
     */
    public suspend fun getListBlocks(params: GetListBlocksQuery):
            Result<GetListBlocksResponse>

    /**
     * A view of a recent posts from actors in a list
     */
    public suspend fun getListFeed(params: GetListFeedQuery): Result<GetListFeedResponse>

    /**
     * Which lists is the requester's account muting?
     */
    public suspend fun getListMutes(params: GetListMutesQuery):
            Result<GetListMutesResponse>

    /**
     * Fetch a list of lists that belong to an actor
     */
    public suspend fun getLists(params: GetListsQuery): Result<GetListsResponse>

    /**
     * Who does the viewer mute?
     */
    public suspend fun getMutes(params: GetMutesQuery): Result<GetMutesResponse>

    /**
     * DEPRECATED: will be removed soon, please find a feed generator alternative
     */
    public suspend fun getPopular(params: GetPopularQuery): Result<GetPopularResponse>

    /**
     * An unspecced view of globally popular feed generators
     */
    public suspend fun getPopularFeedGenerators(params: GetPopularFeedGeneratorsQuery):
            Result<GetPopularFeedGeneratorsResponse>

    public suspend fun getPostThread(params: GetPostThreadQuery):
            Result<GetPostThreadResponse>

    /**
     * A view of an actor's feed.
     */
    public suspend fun getPosts(params: GetPostsQuery): Result<GetPostsResponse>

    /**
     * Get the quotes on a post
     */
    public suspend fun getQuotes(params: GetQuotesQuery): Result<GetQuotesResponse>

    /**
     * Get private preferences attached to the account.
     */
    public suspend fun getPreferences(): Result<GetPreferencesResponse>

    public suspend fun getProfile(params: GetProfileQuery): Result<GetProfileResponse>

    public suspend fun getProfiles(params: GetProfilesQuery): Result<GetProfilesResponse>

    /**
     * Describe the credentials that should be included in the DID doc of an account that is migrating
     * to this service.
     */
    public suspend fun getRecommendedDidCredentials():
            Result<GetRecommendedDidCredentialsResponse>

    /**
     * Get a record.
     */
    public suspend fun getRecord(params: com.atproto.repo.GetRecordQuery): Result<GetRecordResponse>

    /**
     * Gets blocks needed for existence or non-existence of record.
     */
    public suspend fun getRecord(params: com.atproto.sync.GetRecordQuery): Result<ByteArray>

    /**
     * Get details about a record.
     */
    public suspend fun getRecord(params: tools.ozone.moderation.GetRecordQuery):
            Result<tools.ozone.moderation.GetRecordResponse>


    /**
     * Enumerates public relationships between one account, and a list of other accounts. Does not
     * require auth.
     */
    public suspend fun getRelationships(params: GetRelationshipsQuery):
            Result<GetRelationshipsResponse>

    /**
     * Gets the did's repo, optionally catching up from a specific revision.
     */
    public suspend fun getRepo(params: GetRepoQuery): Result<ByteArray>


    public suspend fun getRepostedBy(params: GetRepostedByQuery):
            Result<GetRepostedByResponse>

    /**
     * Get a signed token on behalf of the requesting DID for the requested service.
     */
    public suspend fun getServiceAuth(params: GetServiceAuthQuery):
            Result<GetServiceAuthResponse>

    /**
     * Get information about a list of labeler services.
     */
    public suspend fun getServices(params: GetServicesQuery): Result<GetServicesResponse>

    /**
     * Get information about the current session.
     */
    public suspend fun getSession(): Result<GetSessionResponse>

    /**
     * Get a list of suggested feeds for the viewer.
     */
    public suspend fun getSuggestedFeeds(params: GetSuggestedFeedsQuery):
            Result<GetSuggestedFeedsResponse>

    /**
     * Get suggested follows related to a given actor.
     */
    public suspend fun getSuggestedFollowsByActor(params: GetSuggestedFollowsByActorQuery):
            Result<GetSuggestedFollowsByActorResponse>

    /**
     * Get a list of actors suggested for following. Used in discovery UIs.
     */
    public suspend fun getSuggestions(params: GetSuggestionsQuery):
            Result<GetSuggestionsResponse>

    /**
     * Get a list of suggestions (feeds and users) tagged with categories
     */
    public suspend fun getTaggedSuggestions(): Result<GetTaggedSuggestionsResponse>


    /**
     * A view of the user's home timeline.
     */
    public suspend fun getTimeline(params: GetTimelineQuery): Result<GetTimelineResponse>

    /**
     * A skeleton of a timeline - UNSPECCED & WILL GO AWAY SOON
     */
    public suspend fun getTimelineSkeleton(params: GetTimelineSkeletonQuery):
            Result<GetTimelineSkeletonResponse>

    /**
     * Count the number of unread notifications for the requesting account. Requires auth.
     */
    public suspend fun getUnreadCount(params: GetUnreadCountQuery):
            Result<GetUnreadCountResponse>

    /**
     * Import a repo in the form of a CAR file. Requires Content-Length HTTP header to be set.
     */
    public suspend fun importRepo(request: ByteArray): Result<Unit>

    /**
     * List all app-specific passwords.
     */
    public suspend fun listAppPasswords(): Result<ListAppPasswordsResponse>

    /**
     * List blob cids since some revision
     */
    public suspend fun listBlobs(params: ListBlobsQuery): Result<ListBlobsResponse>

    /**
     * Returns a list of missing blobs for the requesting account. Intended to be used in the account
     * migration flow.
     */
    public suspend fun listMissingBlobs(params: ListMissingBlobsQuery):
            Result<ListMissingBlobsResponse>

    /**
     * Enumerate notifications for the requesting account. Requires auth.
     */
    public suspend fun listNotifications(params: ListNotificationsQuery):
            Result<ListNotificationsResponse>

    /**
     * List a range of records in a collection.
     */
    public suspend fun listRecords(params: ListRecordsQuery): Result<ListRecordsResponse>

    /**
     * List dids and root cids of hosted repos
     */
    public suspend fun listRepos(params: ListReposQuery): Result<ListReposResponse>

    /**
     * Get list of all communication templates.
     */
    public suspend fun listTemplates(): Result<ListTemplatesResponse>

    /**
     * Mute an actor by did or handle.
     */
    public suspend fun muteActor(request: MuteActorRequest): Result<Unit>

    /**
     * Mute a list of actors.
     */
    public suspend fun muteActorList(request: MuteActorListRequest): Result<Unit>

    /**
     * Notify a crawling service of a recent update. Often when a long break between updates causes
     * the connection with the crawling service to break.
     */
    public suspend fun notifyOfUpdate(request: NotifyOfUpdateRequest): Result<Unit>

    /**
     * Sets the private preferences attached to the account.
     */
    public suspend fun putPreferences(request: PutPreferencesRequest): Result<Unit>

    /**
     * Write a record, creating or updating it as needed.
     */
    public suspend fun putRecord(request: PutRecordRequest): Result<PutRecordResponse>


    /**
     * List moderation events related to a subject.
     */
    public suspend fun queryEvents(params: QueryEventsQueryParams): Result<QueryEventsResponse>

    /**
     * Find labels relevant to the provided AT-URI patterns. Public endpoint for moderation services,
     * though may return different or additional results with auth.
     */
    public suspend fun queryLabels(params: QueryLabels): Result<QueryLabelsResponse>

    /**
     * View moderation statuses of subjects (record or repo).
     */
    public suspend fun queryStatuses(params: QueryStatusesQueryParams):
            Result<QueryStatusesResponse>

    /**
     * Refresh an authentication session.
     */
    public suspend fun refreshSession(): Result<RefreshSessionResponse>

    /**
     * refresh with provided auth
     */
    public suspend fun refreshSession(auth: AuthInfo): Result<RefreshSessionResponse>

    /**
     * Register for push notifications with a service
     */
    public suspend fun registerPush(request: RegisterPushRequest): Result<Unit>

    /**
     * Initiate a user account deletion via email.
     */
    public suspend fun requestAccountDelete(): Result<Unit>

    /**
     * Request a service to persistently crawl hosted repos.
     */
    public suspend fun requestCrawl(request: RequestCrawlRequest): Result<Unit>

    /**
     * Request an email with a code to confirm ownership of email
     */
    public suspend fun requestEmailConfirmation(): Result<Unit>

    /**
     * Request a token in order to update email.
     */
    public suspend fun requestEmailUpdate(): Result<RequestEmailUpdateResponse>

    /**
     * Initiate a user account password reset via email.
     */
    public suspend fun requestPasswordReset(request: RequestPasswordResetRequest): Result<Unit>

    /**
     * Request a verification code to be sent to the supplied phone number
     */
    ///Not supported for privacy reasons

    /**
     * Request an email with a code to in order to request a signed PLC operation. Requires Auth.
     */
    public suspend fun requestPlcOperationSignature(): Result<Unit>

    /**
     * Reserve a repo signing key, for use with account creation. Necessary so that a DID PLC update
     * operation can be constructed during an account migraiton. Public and does not require auth;
     * implemented by PDS. NOTE: this endpoint may change when full account migration is implemented.
     */
    public suspend fun reserveSigningKey(request: ReserveSigningKeyRequest):
            Result<ReserveSigningKeyResponse>

    /**
     * Reset a user account password using a token.
     */
    public suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit>

    /**
     * Provides the DID of a repo.
     */
    public suspend fun resolveHandle(params: ResolveHandleQuery):
            Result<ResolveHandleResponse>


    /**
     * Revoke an app-specific password by name.
     */
    public suspend fun revokeAppPassword(request: RevokeAppPasswordRequest): Result<Unit>

    /**
     * Find actors (profiles) matching search criteria.
     */
    public suspend fun searchActors(params: SearchActorsQuery):
            Result<SearchActorsResponse>

    /**
     * Backend Actors (profile) search, returning only skeleton
     */
    public suspend fun searchActorsSkeleton(params: SearchActorsSkeletonQuery):
            Result<SearchActorsSkeletonResponse>

    /**
     * Find actor suggestions for a search term.
     */
    public suspend fun searchActorsTypeahead(params: SearchActorsTypeaheadQuery):
            Result<SearchActorsTypeaheadResponse>

    /**
     * Find posts matching search criteria
     */
    public suspend fun searchPosts(params: SearchPostsQuery): Result<SearchPostsResponse>

    /**
     * Backend Posts search, returning only skeleton
     */
    public suspend fun searchPostsSkeleton(params: SearchPostsSkeletonQuery):
            Result<SearchPostsSkeletonResponse>

    /**
     * Send email to a user's primary email address
     */
    public suspend fun sendEmail(request: SendEmailRequest): Result<SendEmailResponse>

    /**
     * Signs a PLC operation to update some value(s) in the requesting DID's document.
     */
    public suspend fun signPlcOperation(request: SignPlcOperationRequest):
            Result<SignPlcOperationResponse>

    /**
     * Validates a PLC operation to ensure that it doesn't violate a service's constraints or get the
     * identity into a bad state, then submits it to the PLC registry
     */
    public suspend fun submitPlcOperation(request: SubmitPlcOperationRequest): Result<Unit>


    /**
     * Subscribe to label updates
     */
    public suspend fun subscribeLabels(params: SubscribeLabelsQuery):
            Flow<Result<SubscribeLabelsMessage>>

    /**
     * Subscribe to repo updates
     */
    public suspend fun subscribeRepos(params: SubscribeReposQuery):
            Flow<Result<SubscribeReposMessage>>


    /**
     * Unmute an actor by did or handle.
     */
    public suspend fun unmuteActor(request: UnmuteActorRequest): Result<Unit>

    /**
     * Unmute a list of actors.
     */
    public suspend fun unmuteActorList(request: UnmuteActorListRequest): Result<Unit>

    /**
     * Administrative action to update an account's email
     */
    public suspend fun updateAccountEmail(request: UpdateAccountEmailRequest): Result<Unit>

    /**
     * Administrative action to update an account's handle
     */
    public suspend fun updateAccountHandle(request: UpdateAccountHandleRequest): Result<Unit>

    /**
     * Update the password for a user account as an administrator.
     */
    public suspend fun updateAccountPassword(request: UpdateAccountPasswordRequest): Result<Unit>

    /**
     * Update an account's email.
     */
    public suspend fun updateEmail(request: UpdateEmailRequest): Result<Unit>

    /**
     * Updates the handle of the account
     */
    public suspend fun updateHandle(request: UpdateHandleRequest): Result<Unit>

    /**
     * Notify server that the user has seen notifications.
     */
    public suspend fun updateSeen(request: UpdateSeenRequest): Result<Unit>

    /**
     * Update the service-specific admin status of a subject (account, record, or blob).
     */
    public suspend fun updateSubjectStatus(request: UpdateSubjectStatusRequest):
            Result<UpdateSubjectStatusResponse>

    /**
     * Administrative action to update an existing communication template. Allows passing partial
     * fields to patch specific fields only.
     */
    public suspend fun updateTemplate(request: UpdateTemplateRequest):
            Result<UpdateTemplateResponse>

    /**
     * Upload a new blob to be added to repo in a later request.
     */
    public suspend fun uploadBlob(request: ByteArray, mimeType: String): Result<UploadBlobResponse>
}

