package com.morpho.butterfly.xrpc

import app.bsky.actor.GetPreferencesResponse
import app.bsky.actor.GetProfileQuery
import app.bsky.actor.GetProfilesQuery
import app.bsky.actor.GetProfilesResponse
import app.bsky.actor.GetSuggestionsQuery
import app.bsky.actor.GetSuggestionsResponse
import app.bsky.actor.ProfileViewDetailed
import app.bsky.actor.PutPreferencesRequest
import app.bsky.actor.SearchActorsQuery
import app.bsky.actor.SearchActorsResponse
import app.bsky.actor.SearchActorsTypeaheadQuery
import app.bsky.actor.SearchActorsTypeaheadResponse
import app.bsky.feed.DescribeFeedGeneratorResponse
import app.bsky.feed.GetActorFeedsQuery
import app.bsky.feed.GetActorFeedsResponse
import app.bsky.feed.GetActorLikesQuery
import app.bsky.feed.GetActorLikesResponse
import app.bsky.feed.GetAuthorFeedQuery
import app.bsky.feed.GetAuthorFeedResponse
import app.bsky.feed.GetFeedGeneratorQuery
import app.bsky.feed.GetFeedGeneratorResponse
import app.bsky.feed.GetFeedGeneratorsQuery
import app.bsky.feed.GetFeedGeneratorsResponse
import app.bsky.feed.GetFeedQuery
import app.bsky.feed.GetFeedResponse
import app.bsky.feed.GetFeedSkeletonQuery
import app.bsky.feed.GetFeedSkeletonResponse
import app.bsky.feed.GetLikesQuery
import app.bsky.feed.GetLikesResponse
import app.bsky.feed.GetListFeedQuery
import app.bsky.feed.GetListFeedResponse
import app.bsky.feed.GetPostThreadQuery
import app.bsky.feed.GetPostThreadResponse
import app.bsky.feed.GetPostsQuery
import app.bsky.feed.GetPostsResponse
import app.bsky.feed.GetQuotesQuery
import app.bsky.feed.GetQuotesResponse
import app.bsky.feed.GetRepostedByQuery
import app.bsky.feed.GetRepostedByResponse
import app.bsky.feed.GetSuggestedFeedsQuery
import app.bsky.feed.GetSuggestedFeedsResponse
import app.bsky.feed.GetTimelineQuery
import app.bsky.feed.GetTimelineResponse
import app.bsky.feed.SearchPostsQuery
import app.bsky.feed.SearchPostsResponse
import app.bsky.graph.GetBlocksResponse
import app.bsky.graph.GetFollowersQuery
import app.bsky.graph.GetFollowersResponse
import app.bsky.graph.GetFollowsQuery
import app.bsky.graph.GetFollowsResponse
import app.bsky.graph.GetKnownFollowersQuery
import app.bsky.graph.GetKnownFollowersResponse
import app.bsky.graph.GetListBlocksQuery
import app.bsky.graph.GetListBlocksResponse
import app.bsky.graph.GetListMutesQuery
import app.bsky.graph.GetListMutesResponse
import app.bsky.graph.GetListQuery
import app.bsky.graph.GetListResponse
import app.bsky.graph.GetListsQuery
import app.bsky.graph.GetListsResponse
import app.bsky.graph.GetMutesQuery
import app.bsky.graph.GetMutesResponse
import app.bsky.graph.GetRelationshipsQuery
import app.bsky.graph.GetRelationshipsResponse
import app.bsky.graph.GetSuggestedFollowsByActorQuery
import app.bsky.graph.GetSuggestedFollowsByActorResponse
import app.bsky.graph.MuteActorListRequest
import app.bsky.graph.MuteActorRequest
import app.bsky.graph.UnmuteActorListRequest
import app.bsky.graph.UnmuteActorRequest
import app.bsky.labeler.GetServicesQuery
import app.bsky.labeler.GetServicesResponse
import app.bsky.notification.GetUnreadCountQuery
import app.bsky.notification.GetUnreadCountResponse
import app.bsky.notification.ListNotificationsQuery
import app.bsky.notification.ListNotificationsResponse
import app.bsky.notification.RegisterPushRequest
import app.bsky.notification.UpdateSeenRequest
import app.bsky.unspecced.ApplyLabelsRequest
import app.bsky.unspecced.GetPopularFeedGeneratorsQuery
import app.bsky.unspecced.GetPopularFeedGeneratorsResponse
import app.bsky.unspecced.GetPopularQuery
import app.bsky.unspecced.GetPopularResponse
import app.bsky.unspecced.GetTaggedSuggestionsResponse
import app.bsky.unspecced.GetTimelineSkeletonQuery
import app.bsky.unspecced.GetTimelineSkeletonResponse
import app.bsky.unspecced.SearchActorsSkeletonQuery
import app.bsky.unspecced.SearchActorsSkeletonResponse
import app.bsky.unspecced.SearchPostsSkeletonQuery
import app.bsky.unspecced.SearchPostsSkeletonResponse
import com.atproto.admin.DisableAccountInvitesRequest
import com.atproto.admin.DisableInviteCodesRequest
import com.atproto.admin.EnableAccountInvitesRequest
import com.atproto.admin.GetAccountInfoQuery
import com.atproto.admin.GetAccountInfoResponse
import com.atproto.admin.GetAccountInfosQuery
import com.atproto.admin.GetAccountInfosResponse
import com.atproto.admin.GetInviteCodesQuery
import com.atproto.admin.GetInviteCodesResponse
import com.atproto.admin.SendEmailRequest
import com.atproto.admin.SendEmailResponse
import com.atproto.admin.UpdateAccountEmailRequest
import com.atproto.admin.UpdateAccountHandleRequest
import com.atproto.admin.UpdateAccountPasswordRequest
import com.atproto.admin.UpdateSubjectStatusRequest
import com.atproto.admin.UpdateSubjectStatusResponse
import com.atproto.identity.GetRecommendedDidCredentialsResponse
import com.atproto.identity.ResolveHandleQuery
import com.atproto.identity.ResolveHandleResponse
import com.atproto.identity.SignPlcOperationRequest
import com.atproto.identity.SignPlcOperationResponse
import com.atproto.identity.SubmitPlcOperationRequest
import com.atproto.identity.UpdateHandleRequest
import com.atproto.label.QueryLabels
import com.atproto.label.QueryLabelsResponse
import com.atproto.label.SubscribeLabelsMessage
import com.atproto.label.SubscribeLabelsQuery
import com.atproto.moderation.CreateReportRequest
import com.atproto.moderation.CreateReportResponse
import com.atproto.repo.ApplyWritesRequest
import com.atproto.repo.CreateRecordRequest
import com.atproto.repo.CreateRecordResponse
import com.atproto.repo.DeleteRecordRequest
import com.atproto.repo.DescribeRepoQuery
import com.atproto.repo.DescribeRepoResponse
import com.atproto.repo.ListMissingBlobsQuery
import com.atproto.repo.ListMissingBlobsResponse
import com.atproto.repo.ListRecordsQuery
import com.atproto.repo.ListRecordsResponse
import com.atproto.repo.PutRecordRequest
import com.atproto.repo.PutRecordResponse
import com.atproto.repo.UploadBlobResponse
import com.atproto.server.CheckAccountStatusResponse
import com.atproto.server.ConfirmEmailRequest
import com.atproto.server.CreateAccountRequest
import com.atproto.server.CreateAccountResponse
import com.atproto.server.CreateAppPasswordRequest
import com.atproto.server.CreateAppPasswordResponse
import com.atproto.server.CreateInviteCodeRequest
import com.atproto.server.CreateInviteCodeResponse
import com.atproto.server.CreateInviteCodesRequest
import com.atproto.server.CreateInviteCodesResponse
import com.atproto.server.CreateSessionRequest
import com.atproto.server.CreateSessionResponse
import com.atproto.server.DeactivateAccountRequest
import com.atproto.server.DeleteAccountRequest
import com.atproto.server.DescribeServerResponse
import com.atproto.server.GetAccountInviteCodesQuery
import com.atproto.server.GetAccountInviteCodesResponse
import com.atproto.server.GetServiceAuthQuery
import com.atproto.server.GetServiceAuthResponse
import com.atproto.server.GetSessionResponse
import com.atproto.server.ListAppPasswordsResponse
import com.atproto.server.RefreshSessionResponse
import com.atproto.server.RequestEmailUpdateResponse
import com.atproto.server.RequestPasswordResetRequest
import com.atproto.server.ReserveSigningKeyRequest
import com.atproto.server.ReserveSigningKeyResponse
import com.atproto.server.ResetPasswordRequest
import com.atproto.server.RevokeAppPasswordRequest
import com.atproto.server.UpdateEmailRequest
import com.atproto.sync.GetBlobQuery
import com.atproto.sync.GetCheckoutQuery
import com.atproto.sync.GetHeadQuery
import com.atproto.sync.GetHeadResponse
import com.atproto.sync.GetLatestCommitQuery
import com.atproto.sync.GetLatestCommitResponse
import com.atproto.sync.ListBlobsQuery
import com.atproto.sync.ListBlobsResponse
import com.atproto.sync.ListReposQuery
import com.atproto.sync.ListReposResponse
import com.atproto.sync.NotifyOfUpdateRequest
import com.atproto.sync.RequestCrawlRequest
import com.atproto.sync.SubscribeReposMessage
import com.atproto.sync.SubscribeReposQuery
import com.atproto.temp.CheckSignupQueueResponse
import com.atproto.temp.FetchLabelsQueryParams
import com.atproto.temp.FetchLabelsResponse
import com.morpho.butterfly.BlueskyApi
import com.morpho.butterfly.auth.AuthInfo
import com.morpho.butterfly.butterflySerializersModule
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.modules.SerializersModule
import tools.ozone.communication.CreateTemplateRequest
import tools.ozone.communication.CreateTemplateResponse
import tools.ozone.communication.ListTemplatesResponse
import tools.ozone.communication.UpdateTemplateRequest
import tools.ozone.communication.UpdateTemplateResponse
import tools.ozone.moderation.EmitEventRequest
import tools.ozone.moderation.EmitEventResponse
import tools.ozone.moderation.GetEventQueryParams
import tools.ozone.moderation.GetEventResponse
import tools.ozone.moderation.GetRecordQuery
import tools.ozone.moderation.QueryEventsQueryParams
import tools.ozone.moderation.QueryEventsResponse
import tools.ozone.moderation.QueryStatusesQueryParams
import tools.ozone.moderation.QueryStatusesResponse
import app.bsky.graph.GetBlocksQuery as GraphGetBlocksQuery
import com.atproto.repo.GetRecordQuery as RepoGetRecordQuery
import com.atproto.repo.GetRecordResponse as RepoGetRecordResponse
import com.atproto.sync.GetBlocksQuery as SyncGetBlocksQuery
import com.atproto.sync.GetRecordQuery as SyncGetRecordQuery
import com.atproto.sync.GetRepoQuery as SyncGetRepoQuery

public class XrpcBlueskyApi(
  httpClient: HttpClient,
  polySerializersModule: SerializersModule = butterflySerializersModule,
) : BlueskyApi {
  private val client: HttpClient = httpClient.withXrpcConfiguration(
    polySerializersModule = polySerializersModule
  )

  /**
   * Allow a labeler to apply labels directly.
   */
  override suspend fun applyLabels(request: ApplyLabelsRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/app.bsky.unspecced.applyLabels",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Apply a batch transaction of creates, updates, and deletes.
   */
  override suspend fun applyWrites(request: ApplyWritesRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.repo.applyWrites",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Returns the status of an account, especially as pertaining to import or recovery. Can be called
   * many times over the course of an account migration. Requires auth and can only be called
   * pertaining to oneself.
   */
  override suspend fun checkAccountStatus(): Result<CheckAccountStatusResponse> {
    return client.query(
      path = "/xrpc/com.atproto.server.checkAccountStatus",
    ).toAtpResult()
  }

  /**
   * Check accounts location in signup queue.
   */
  override suspend fun checkSignupQueue(): Result<CheckSignupQueueResponse> {
    return client.query(
      path = "/xrpc/com.atproto.temp.checkSignupQueue",
    ).toAtpResult()
  }

  /**
   * Confirm an email using a token from com.atproto.server.requestEmailConfirmation.
   */
  override suspend fun confirmEmail(request: ConfirmEmailRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.confirmEmail",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Create an account.
   */
  override suspend fun createAccount(request: CreateAccountRequest):
      Result<CreateAccountResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.createAccount",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Create an app-specific password.
   */
  override suspend fun createAppPassword(request: CreateAppPasswordRequest):
      Result<CreateAppPasswordResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.createAppPassword",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Create an invite code.
   */
  override suspend fun createInviteCode(request: CreateInviteCodeRequest):
      Result<CreateInviteCodeResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.createInviteCode",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Create an invite code.
   */
  override suspend fun createInviteCodes(request: CreateInviteCodesRequest):
      Result<CreateInviteCodesResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.createInviteCodes",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Create a new record.
   */
  override suspend fun createRecord(request: CreateRecordRequest):
      Result<CreateRecordResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.repo.createRecord",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Report a repo or a record.
   */
  override suspend fun createReport(request: CreateReportRequest):
      Result<CreateReportResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.moderation.createReport",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Create an authentication session.
   */
  override suspend fun createSession(request: CreateSessionRequest):
      Result<CreateSessionResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.createSession",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Administrative action to create a new, re-usable communication (email for now) template.
   */
  override suspend fun createTemplate(request: CreateTemplateRequest): Result<CreateTemplateResponse> {
    return client.procedure(
      path = "/xrpc/tools.ozone.communication.createTemplate",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Deactivates a currently active account. Stops serving of repo, and future writes to repo until
   * reactivated. Used to finalize account migration with the old host after the account has been
   * activated on the new host.
   */
  override suspend fun deactivateAccount(request: DeactivateAccountRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.deactivateAccount",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Delete a user account with a token and password.
   */
  override suspend fun deleteAccount(request: DeleteAccountRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.deleteAccount",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Delete a record, or ensure it doesn't exist.
   */
  override suspend fun deleteRecord(request: DeleteRecordRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.repo.deleteRecord",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Delete the current session.
   */
  override suspend fun deleteSession(): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.deleteSession",
    ).toAtpResult()
  }

  /**
   * Returns information about a given feed generator including TOS & offered feed URIs
   */
  override suspend fun describeFeedGenerator(): Result<DescribeFeedGeneratorResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.describeFeedGenerator",
    ).toAtpResult()
  }

  /**
   * Get information about the repo, including the list of collections.
   */
  override suspend fun describeRepo(params: DescribeRepoQuery):
      Result<DescribeRepoResponse> {
    return client.query(
      path = "/xrpc/com.atproto.repo.describeRepo",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get a document describing the service's accounts configuration.
   */
  override suspend fun describeServer(): Result<DescribeServerResponse> {
    return client.query(
      path = "/xrpc/com.atproto.server.describeServer",
    ).toAtpResult()
  }

  /**
   * Disable an account from receiving new invite codes, but does not invalidate existing codes
   */
  override suspend fun disableAccountInvites(request: DisableAccountInvitesRequest):
      Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.admin.disableAccountInvites",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Disable some set of codes and/or all codes associated with a set of users
   */
  override suspend fun disableInviteCodes(request: DisableInviteCodesRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.admin.disableInviteCodes",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Take a moderation action on an actor.
   */
  override suspend fun emitEvent(request: EmitEventRequest): Result<EmitEventResponse> {
    return client.procedure(
      path = "/xrpc/tools.ozone.moderation.emitEvent",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Re-enable an accounts ability to receive invite codes
   */
  override suspend fun enableAccountInvites(request: EnableAccountInvitesRequest):
      Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.admin.enableAccountInvites",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  @Deprecated("DEPRECATED: use queryLabels or subscribeLabels instead -- Fetch all labels from a labeler created after a certain date.")
  override suspend fun fetchLabels(params: FetchLabelsQueryParams): Result<FetchLabelsResponse> {
    return client.query(
      path = "/xrpc/com.atproto.temp.fetchLabels",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get details about an account.
   */
  override suspend fun getAccountInfo(params: GetAccountInfoQuery): Result<GetAccountInfoResponse> {
    return client.query(
      path = "/xrpc/com.atproto.admin.getAccountInfo",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get details about some accounts.
   */
  override suspend fun getAccountInfos(params: GetAccountInfosQuery): Result<GetAccountInfosResponse> {
    return client.query(
      path = "/xrpc/com.atproto.admin.getAccountInfos",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get all invite codes for a given account
   */
  override suspend fun getAccountInviteCodes(params: GetAccountInviteCodesQuery):
      Result<GetAccountInviteCodesResponse> {
    return client.query(
      path = "/xrpc/com.atproto.server.getAccountInviteCodes",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Retrieve a list of feeds created by a given actor
   */
  override suspend fun getActorFeeds(params: GetActorFeedsQuery):
      Result<GetActorFeedsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getActorFeeds",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * A view of the posts liked by an actor.
   */
  override suspend fun getActorLikes(params: GetActorLikesQuery):
      Result<GetActorLikesResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getActorLikes",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * A view of an actor's feed.
   */
  override suspend fun getAuthorFeed(params: GetAuthorFeedQuery):
      Result<GetAuthorFeedResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getAuthorFeed",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get a blob associated with a given repo.
   */
  override suspend fun getBlob(params: GetBlobQuery): Result<ByteArray> {
    return client.query(
      path = "/xrpc/com.atproto.sync.getBlob",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Gets blocks from a given repo.
   */
  override suspend fun getBlocks(params: SyncGetBlocksQuery): Result<ByteArray> {
    return client.query(
      path = "/xrpc/com.atproto.sync.getBlocks",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Who is the requester's account blocking?
   */
  override suspend fun getBlocks(params: GraphGetBlocksQuery):
      Result<GetBlocksResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getBlocks",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * DEPRECATED - please use com.atproto.sync.getRepo instead
   */
  @Deprecated("DEPRECATED - please use com.atproto.sync.getRepo instead")
  override suspend fun getCheckout(params: GetCheckoutQuery): Result<ByteArray> {
    return client.query(
      path = "/xrpc/com.atproto.sync.getCheckout",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get details about a moderation event.
   */
  override suspend fun getEvent(params: GetEventQueryParams): Result<GetEventResponse> {
    return client.query(
      path = "/xrpc/tools.ozone.moderation.getEvent",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Compose and hydrate a feed from a user's selected feed generator
   */
  override suspend fun getFeed(params: GetFeedQuery): Result<GetFeedResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getFeed",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get information about a specific feed offered by a feed generator, such as its online status
   */
  override suspend fun getFeedGenerator(params: GetFeedGeneratorQuery):
      Result<GetFeedGeneratorResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getFeedGenerator",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get information about a list of feed generators
   */
  override suspend fun getFeedGenerators(params: GetFeedGeneratorsQuery):
      Result<GetFeedGeneratorsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getFeedGenerators",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * A skeleton of a feed provided by a feed generator
   */
  override suspend fun getFeedSkeleton(params: GetFeedSkeletonQuery):
      Result<GetFeedSkeletonResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getFeedSkeleton",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Who is following an actor?
   */
  override suspend fun getFollowers(params: GetFollowersQuery):
      Result<GetFollowersResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getFollowers",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Who among your followers is following an actor?
   */
  override suspend fun getKnownFollowers(params: GetKnownFollowersQuery):
          Result<GetKnownFollowersResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getKnownFollowers",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Who is an actor following?
   */
  override suspend fun getFollows(params: GetFollowsQuery): Result<GetFollowsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getFollows",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * DEPRECATED - please use com.atproto.sync.getLatestCommit instead
   */
  @Deprecated("DEPRECATED - please use com.atproto.sync.getLatestCommit instead")
  override suspend fun getHead(params: GetHeadQuery): Result<GetHeadResponse> {
    return client.query(
      path = "/xrpc/com.atproto.sync.getHead",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Admin view of invite codes
   */
  override suspend fun getInviteCodes(params: GetInviteCodesQuery):
      Result<GetInviteCodesResponse> {
    return client.query(
      path = "/xrpc/com.atproto.admin.getInviteCodes",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Gets the current commit CID & revision of the repo.
   */
  override suspend fun getLatestCommit(params: GetLatestCommitQuery):
      Result<GetLatestCommitResponse> {
    return client.query(
      path = "/xrpc/com.atproto.sync.getLatestCommit",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  override suspend fun getLikes(params: GetLikesQuery): Result<GetLikesResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getLikes",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Fetch a list of actors
   */
  override suspend fun getList(params: GetListQuery): Result<GetListResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getList",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Which lists is the requester's account blocking?
   */
  override suspend fun getListBlocks(params: GetListBlocksQuery):
      Result<GetListBlocksResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getListBlocks",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * A view of a recent posts from actors in a list
   */
  override suspend fun getListFeed(params: GetListFeedQuery):
      Result<GetListFeedResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getListFeed",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Which lists is the requester's account muting?
   */
  override suspend fun getListMutes(params: GetListMutesQuery):
      Result<GetListMutesResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getListMutes",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Fetch a list of lists that belong to an actor
   */
  override suspend fun getLists(params: GetListsQuery): Result<GetListsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getLists",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Who does the viewer mute?
   */
  override suspend fun getMutes(params: GetMutesQuery): Result<GetMutesResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getMutes",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * DEPRECATED: will be removed soon, please find a feed generator alternative
   */
  override suspend fun getPopular(params: GetPopularQuery): Result<GetPopularResponse> {
    return client.query(
      path = "/xrpc/app.bsky.unspecced.getPopular",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * An unspecced view of globally popular feed generators
   */
  override suspend fun getPopularFeedGenerators(params: GetPopularFeedGeneratorsQuery):
      Result<GetPopularFeedGeneratorsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.unspecced.getPopularFeedGenerators",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  override suspend fun getPostThread(params: GetPostThreadQuery):
      Result<GetPostThreadResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getPostThread",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * A view of an actor's feed.
   */
  override suspend fun getPosts(params: GetPostsQuery): Result<GetPostsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getPosts",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get the quotes for a post
   */
  override suspend fun getQuotes(params: GetQuotesQuery): Result<GetQuotesResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getQuotes",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get private preferences attached to the account.
   */
  override suspend fun getPreferences(): Result<GetPreferencesResponse> {
    return client.query(
      path = "/xrpc/app.bsky.actor.getPreferences",
    ).toAtpResult()
  }

  override suspend fun getProfile(params: GetProfileQuery): Result<ProfileViewDetailed> {
    return client.query(
      path = "/xrpc/app.bsky.actor.getProfile",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  override suspend fun getProfiles(params: GetProfilesQuery):
      Result<GetProfilesResponse> {
    return client.query(
      path = "/xrpc/app.bsky.actor.getProfiles",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Describe the credentials that should be included in the DID doc of an account that is migrating
   * to this service.
   */
  override suspend fun getRecommendedDidCredentials(): Result<GetRecommendedDidCredentialsResponse> {
    return client.query(
      path = "/xrpc/com.atproto.identity.getRecommendedDidCredentials",
    ).toAtpResult()
  }

  /**
   * Get a record.
   */
  override suspend fun getRecord(params: RepoGetRecordQuery):
      Result<RepoGetRecordResponse> {
    return client.query(
      path = "/xrpc/com.atproto.repo.getRecord",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Gets blocks needed for existence or non-existence of record.
   */
  override suspend fun getRecord(params: SyncGetRecordQuery): Result<ByteArray> {
    return client.query(
      path = "/xrpc/com.atproto.sync.getRecord",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get details about a record.
   */
  override suspend fun getRecord(params: GetRecordQuery): Result<tools.ozone.moderation.GetRecordResponse> {
    return client.query(
      path = "/xrpc/tools.ozone.moderation.getRecord",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Enumerates public relationships between one account, and a list of other accounts. Does not
   * require auth.
   */
  override suspend fun getRelationships(params: GetRelationshipsQuery): Result<GetRelationshipsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getRelationships",
      queryParams = params.asList(),
    ).toAtpResult()
  }


  /**
   * Gets the did's repo, optionally catching up from a specific revision.
   */
  override suspend fun getRepo(params: SyncGetRepoQuery): Result<ByteArray> {
    return client.query(
      path = "/xrpc/com.atproto.sync.getRepo",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  override suspend fun getRepostedBy(params: GetRepostedByQuery):
      Result<GetRepostedByResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getRepostedBy",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get a signed token on behalf of the requesting DID for the requested service.
   */
  override suspend fun getServiceAuth(params: GetServiceAuthQuery): Result<GetServiceAuthResponse> {
    return client.query(
      path = "/xrpc/com.atproto.server.getServiceAuth",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get information about a list of labeler services.
   */
  override suspend fun getServices(params: GetServicesQuery): Result<GetServicesResponse> {
    return client.query(
      path = "/xrpc/app.bsky.labeler.getServices",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get information about the current session.
   */
  override suspend fun getSession(): Result<GetSessionResponse> {
    return client.query(
      path = "/xrpc/com.atproto.server.getSession",
    ).toAtpResult()
  }

  /**
   * Get a list of suggested feeds for the viewer.
   */
  override suspend fun getSuggestedFeeds(params: GetSuggestedFeedsQuery):
      Result<GetSuggestedFeedsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getSuggestedFeeds",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get suggested follows related to a given actor.
   */
  override suspend fun getSuggestedFollowsByActor(params: GetSuggestedFollowsByActorQuery):
      Result<GetSuggestedFollowsByActorResponse> {
    return client.query(
      path = "/xrpc/app.bsky.graph.getSuggestedFollowsByActor",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get a list of actors suggested for following. Used in discovery UIs.
   */
  override suspend fun getSuggestions(params: GetSuggestionsQuery):
      Result<GetSuggestionsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.actor.getSuggestions",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get a list of suggestions (feeds and users) tagged with categories
   */
  override suspend fun getTaggedSuggestions(): Result<GetTaggedSuggestionsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.unspecced.getTaggedSuggestions",
    ).toAtpResult()
  }

  /**
   * A view of the user's home timeline.
   */
  override suspend fun getTimeline(params: GetTimelineQuery):
      Result<GetTimelineResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.getTimeline",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * A skeleton of a timeline - UNSPECCED & WILL GO AWAY SOON
   */
  override suspend fun getTimelineSkeleton(params: GetTimelineSkeletonQuery):
      Result<GetTimelineSkeletonResponse> {
    return client.query(
      path = "/xrpc/app.bsky.unspecced.getTimelineSkeleton",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  override suspend fun getUnreadCount(params: GetUnreadCountQuery):
      Result<GetUnreadCountResponse> {
    return client.query(
      path = "/xrpc/app.bsky.notification.getUnreadCount",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Import a repo in the form of a CAR file. Requires Content-Length HTTP header to be set.
   */
  override suspend fun importRepo(request: ByteArray): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.repo.importRepo",
      body = request,
      encoding = "application/vnd.ipld.car",
    ).toAtpResult()
  }

  /**
   * List all app-specific passwords.
   */
  override suspend fun listAppPasswords(): Result<ListAppPasswordsResponse> {
    return client.query(
      path = "/xrpc/com.atproto.server.listAppPasswords",
    ).toAtpResult()
  }

  /**
   * List blob cids since some revision
   */
  override suspend fun listBlobs(params: ListBlobsQuery): Result<ListBlobsResponse> {
    return client.query(
      path = "/xrpc/com.atproto.sync.listBlobs",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Returns a list of missing blobs for the requesting account. Intended to be used in the account
   * migration flow.
   */
  override suspend fun listMissingBlobs(params: ListMissingBlobsQuery): Result<ListMissingBlobsResponse> {
    return client.query(
      path = "/xrpc/com.atproto.repo.listMissingBlobs",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  override suspend fun listNotifications(params: ListNotificationsQuery):
      Result<ListNotificationsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.notification.listNotifications",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * List a range of records in a collection.
   */
  override suspend fun listRecords(params: ListRecordsQuery):
      Result<ListRecordsResponse> {
    return client.query(
      path = "/xrpc/com.atproto.repo.listRecords",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * List dids and root cids of hosted repos
   */
  override suspend fun listRepos(params: ListReposQuery): Result<ListReposResponse> {
    return client.query(
      path = "/xrpc/com.atproto.sync.listRepos",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Get list of all communication templates.
   */
  override suspend fun listTemplates(): Result<ListTemplatesResponse> {
    return client.query(
      path = "/xrpc/tools.ozone.communication.listTemplates",
    ).toAtpResult()
  }

  /**
   * Mute an actor by did or handle.
   */
  override suspend fun muteActor(request: MuteActorRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/app.bsky.graph.muteActor",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Mute a list of actors.
   */
  override suspend fun muteActorList(request: MuteActorListRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/app.bsky.graph.muteActorList",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Notify a crawling service of a recent update. Often when a long break between updates causes
   * the connection with the crawling service to break.
   */
  override suspend fun notifyOfUpdate(request: NotifyOfUpdateRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.sync.notifyOfUpdate",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Sets the private preferences attached to the account.
   */
  override suspend fun putPreferences(request: PutPreferencesRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/app.bsky.actor.putPreferences",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Write a record, creating or updating it as needed.
   */
  override suspend fun putRecord(request: PutRecordRequest): Result<PutRecordResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.repo.putRecord",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * List moderation events related to a subject.
   */
  override suspend fun queryEvents(params: QueryEventsQueryParams): Result<QueryEventsResponse> {
    return client.query(
      path = "/xrpc/tools.ozone.moderation.queryEvents",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Find labels relevant to the provided URI patterns.
   */
  override suspend fun queryLabels(params: QueryLabels):
      Result<QueryLabelsResponse> {
    return client.query(
      path = "/xrpc/com.atproto.label.queryLabels",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * View moderation statuses of subjects (record or repo).
   */
  override suspend fun queryStatuses(params: QueryStatusesQueryParams): Result<QueryStatusesResponse> {
    return client.query(
      path = "/xrpc/tools.ozone.moderation.queryStatuses",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Refresh an authentication session.
   */
  override suspend fun refreshSession(): Result<RefreshSessionResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.refreshSession",
    ).toAtpResult()
  }

  override suspend fun refreshSession(auth: AuthInfo): Result<RefreshSessionResponse> {
    return client.post("/xrpc/com.atproto.server.refreshSession") {
      this.bearerAuth(auth.refreshJwt)
    }.toAtpResult()
  }

  /**
   * Register for push notifications with a service
   */
  override suspend fun registerPush(request: RegisterPushRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/app.bsky.notification.registerPush",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Initiate a user account deletion via email.
   */
  override suspend fun requestAccountDelete(): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.requestAccountDelete",
    ).toAtpResult()
  }

  /**
   * Request a service to persistently crawl hosted repos.
   */
  override suspend fun requestCrawl(request: RequestCrawlRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.sync.requestCrawl",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Request an email with a code to confirm ownership of email
   */
  override suspend fun requestEmailConfirmation(): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.requestEmailConfirmation",
    ).toAtpResult()
  }

  /**
   * Request a token in order to update email.
   */
  override suspend fun requestEmailUpdate(): Result<RequestEmailUpdateResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.requestEmailUpdate",
    ).toAtpResult()
  }

  /**
   * Initiate a user account password reset via email.
   */
  override suspend fun requestPasswordReset(request: RequestPasswordResetRequest):
      Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.requestPasswordReset",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Request an email with a code to in order to request a signed PLC operation. Requires Auth.
   */
  override suspend fun requestPlcOperationSignature(): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.identity.requestPlcOperationSignature",
    ).toAtpResult()
  }

  /**
   * Reserve a repo signing key, for use with account creation. Necessary so that a DID PLC update
   * operation can be constructed during an account migraiton. Public and does not require auth;
   * implemented by PDS. NOTE: this endpoint may change when full account migration is implemented.
   */
  override suspend fun reserveSigningKey(request: ReserveSigningKeyRequest): Result<ReserveSigningKeyResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.reserveSigningKey",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Reset a user account password using a token.
   */
  override suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.resetPassword",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Provides the DID of a repo.
   */
  override suspend fun resolveHandle(params: ResolveHandleQuery):
      Result<ResolveHandleResponse> {
    return client.query(
      path = "/xrpc/com.atproto.identity.resolveHandle",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Revoke an app-specific password by name.
   */
  override suspend fun revokeAppPassword(request: RevokeAppPasswordRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.revokeAppPassword",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Find actors (profiles) matching search criteria.
   */
  override suspend fun searchActors(params: SearchActorsQuery):
      Result<SearchActorsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.actor.searchActors",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Backend Actors (profile) search, returning only skeleton
   */
  override suspend fun searchActorsSkeleton(params: SearchActorsSkeletonQuery):
      Result<SearchActorsSkeletonResponse> {
    return client.query(
      path = "/xrpc/app.bsky.unspecced.searchActorsSkeleton",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Find actor suggestions for a search term.
   */
  override suspend fun searchActorsTypeahead(params: SearchActorsTypeaheadQuery):
      Result<SearchActorsTypeaheadResponse> {
    return client.query(
      path = "/xrpc/app.bsky.actor.searchActorsTypeahead",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Find posts matching search criteria
   */
  override suspend fun searchPosts(params: SearchPostsQuery):
      Result<SearchPostsResponse> {
    return client.query(
      path = "/xrpc/app.bsky.feed.searchPosts",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Backend Posts search, returning only skeleton
   */
  override suspend fun searchPostsSkeleton(params: SearchPostsSkeletonQuery):
      Result<SearchPostsSkeletonResponse> {
    return client.query(
      path = "/xrpc/app.bsky.unspecced.searchPostsSkeleton",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Send email to a user's primary email address
   */
  override suspend fun sendEmail(request: SendEmailRequest): Result<SendEmailResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.admin.sendEmail",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Signs a PLC operation to update some value(s) in the requesting DID's document.
   */
  override suspend fun signPlcOperation(request: SignPlcOperationRequest): Result<SignPlcOperationResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.identity.signPlcOperation",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Validates a PLC operation to ensure that it doesn't violate a service's constraints or get the
   * identity into a bad state, then submits it to the PLC registry
   */
  override suspend fun submitPlcOperation(request: SubmitPlcOperationRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.identity.submitPlcOperation",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Subscribe to label updates
   */
  override suspend fun subscribeLabels(params: SubscribeLabelsQuery):
      Flow<Result<SubscribeLabelsMessage>> {
    return client.subscription(
      path = "/xrpc/com.atproto.label.subscribeLabels",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Subscribe to repo updates
   */
  override suspend fun subscribeRepos(params: SubscribeReposQuery):
      Flow<Result<SubscribeReposMessage>> {
    return client.subscription(
      path = "/xrpc/com.atproto.sync.subscribeRepos",
      queryParams = params.asList(),
    ).toAtpResult()
  }

  /**
   * Unmute an actor by did or handle.
   */
  override suspend fun unmuteActor(request: UnmuteActorRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/app.bsky.graph.unmuteActor",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Unmute a list of actors.
   */
  override suspend fun unmuteActorList(request: UnmuteActorListRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/app.bsky.graph.unmuteActorList",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Administrative action to update an account's email
   */
  override suspend fun updateAccountEmail(request: UpdateAccountEmailRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.admin.updateAccountEmail",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Administrative action to update an account's handle
   */
  override suspend fun updateAccountHandle(request: UpdateAccountHandleRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.admin.updateAccountHandle",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Update the password for a user account as an administrator.
   */
  override suspend fun updateAccountPassword(request: UpdateAccountPasswordRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.admin.updateAccountPassword",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Update an account's email.
   */
  override suspend fun updateEmail(request: UpdateEmailRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.server.updateEmail",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Updates the handle of the account
   */
  override suspend fun updateHandle(request: UpdateHandleRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/com.atproto.identity.updateHandle",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Notify server that the user has seen notifications.
   */
  override suspend fun updateSeen(request: UpdateSeenRequest): Result<Unit> {
    return client.procedure(
      path = "/xrpc/app.bsky.notification.updateSeen",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Update the service-specific admin status of a subject (account, record, or blob).
   */
  override suspend fun updateSubjectStatus(request: UpdateSubjectStatusRequest): Result<UpdateSubjectStatusResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.admin.updateSubjectStatus",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Administrative action to update an existing communication template. Allows passing partial
   * fields to patch specific fields only.
   */
  override suspend fun updateTemplate(request: UpdateTemplateRequest): Result<UpdateTemplateResponse> {
    return client.procedure(
      path = "/xrpc/tools.ozone.communication.updateTemplate",
      body = request,
      encoding = "application/json",
    ).toAtpResult()
  }

  /**
   * Upload a new blob to be added to repo in a later request.
   */
  override suspend fun uploadBlob(request: ByteArray, mimeType: String): Result<UploadBlobResponse> {
    return client.procedure(
      path = "/xrpc/com.atproto.repo.uploadBlob",
      body = request,
      encoding = mimeType,
    ).toAtpResult()
  }
}
