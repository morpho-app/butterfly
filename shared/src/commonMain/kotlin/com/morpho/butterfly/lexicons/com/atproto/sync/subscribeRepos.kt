package com.atproto.sync

import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface SubscribeReposMessageUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#commit")
  public value class Commit(
    public val `value`: SubscribeReposCommit,
  ) : SubscribeReposMessageUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#identity")
  public value class Identity(
    public val `value`: SubscribeReposIdentity,
  ) : SubscribeReposMessageUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#handle")
  public value class Handle(
    public val `value`: SubscribeReposHandle,
  ) : SubscribeReposMessageUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#migrate")
  public value class Migrate(
    public val `value`: SubscribeReposMigrate,
  ) : SubscribeReposMessageUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#tombstone")
  public value class Tombstone(
    public val `value`: SubscribeReposTombstone,
  ) : SubscribeReposMessageUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#info")
  public value class Info(
    public val `value`: SubscribeReposInfo,
  ) : SubscribeReposMessageUnion
}
@Serializable
public data class SubscribeReposQuery(
  /**
   * The last known event to backfill from.
   */
  public val cursor: Long? = null,
) {
  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    add("cursor" to cursor)
  }.toImmutableList()
}

public typealias SubscribeReposMessage = SubscribeReposMessageUnion
