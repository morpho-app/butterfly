package com.atproto.sync

import kotlin.Any
import kotlin.Long
import kotlin.Pair
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface SubscribeReposMessageUnion {
  public class CommitSerializer : KSerializer<Commit> by valueClassSerializer(
    serialName = "com.atproto.sync.subscribeRepos#commit",
    constructor = ::Commit,
    valueProvider = Commit::value,
    valueSerializerProvider = { SubscribeReposCommit.serializer() },
  )

  @Serializable(with = CommitSerializer::class)
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#commit")
  public value class Commit(
    public val `value`: SubscribeReposCommit,
  ) : SubscribeReposMessageUnion

  public class IdentitySerializer : KSerializer<Identity> by valueClassSerializer(
    serialName = "com.atproto.sync.subscribeRepos#identity",
    constructor = ::Identity,
    valueProvider = Identity::value,
    valueSerializerProvider = { SubscribeReposIdentity.serializer() },
  )

  @Serializable(with = IdentitySerializer::class)
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#identity")
  public value class Identity(
    public val `value`: SubscribeReposIdentity,
  ) : SubscribeReposMessageUnion

  public class HandleSerializer : KSerializer<Handle> by valueClassSerializer(
    serialName = "com.atproto.sync.subscribeRepos#handle",
    constructor = ::Handle,
    valueProvider = Handle::value,
    valueSerializerProvider = { SubscribeReposHandle.serializer() },
  )

  @Serializable(with = HandleSerializer::class)
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#handle")
  public value class Handle(
    public val `value`: SubscribeReposHandle,
  ) : SubscribeReposMessageUnion

  public class MigrateSerializer : KSerializer<Migrate> by valueClassSerializer(
    serialName = "com.atproto.sync.subscribeRepos#migrate",
    constructor = ::Migrate,
    valueProvider = Migrate::value,
    valueSerializerProvider = { SubscribeReposMigrate.serializer() },
  )

  @Serializable(with = MigrateSerializer::class)
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#migrate")
  public value class Migrate(
    public val `value`: SubscribeReposMigrate,
  ) : SubscribeReposMessageUnion

  public class TombstoneSerializer : KSerializer<Tombstone> by valueClassSerializer(
    serialName = "com.atproto.sync.subscribeRepos#tombstone",
    constructor = ::Tombstone,
    valueProvider = Tombstone::value,
    valueSerializerProvider = { SubscribeReposTombstone.serializer() },
  )

  @Serializable(with = TombstoneSerializer::class)
  @JvmInline
  @SerialName("com.atproto.sync.subscribeRepos#tombstone")
  public value class Tombstone(
    public val `value`: SubscribeReposTombstone,
  ) : SubscribeReposMessageUnion

  public class InfoSerializer : KSerializer<Info> by valueClassSerializer(
    serialName = "com.atproto.sync.subscribeRepos#info",
    constructor = ::Info,
    valueProvider = Info::value,
    valueSerializerProvider = { SubscribeReposInfo.serializer() },
  )

  @Serializable(with = InfoSerializer::class)
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
