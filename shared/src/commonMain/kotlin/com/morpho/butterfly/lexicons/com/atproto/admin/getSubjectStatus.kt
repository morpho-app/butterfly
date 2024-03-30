package com.atproto.admin

import com.atproto.repo.StrongRef
import kotlin.Any
import kotlin.Pair
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface GetSubjectStatusResponseSubject {
  public class AdminRepoRefSerializer : KSerializer<AdminRepoRef> by valueClassSerializer(
    serialName = "com.atproto.admin.defs#repoRef",
    constructor = ::AdminRepoRef,
    valueProvider = AdminRepoRef::value,
    valueSerializerProvider = { RepoRef.serializer() },
  )

  @Serializable(with = AdminRepoRefSerializer::class)
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : GetSubjectStatusResponseSubject

  public class RepoStrongRefSerializer : KSerializer<RepoStrongRef> by valueClassSerializer(
    serialName = "com.atproto.repo.strongRef",
    constructor = ::RepoStrongRef,
    valueProvider = RepoStrongRef::value,
    valueSerializerProvider = { StrongRef.serializer() },
  )

  @Serializable(with = RepoStrongRefSerializer::class)
  @JvmInline
  @SerialName("com.atproto.repo.strongRef")
  public value class RepoStrongRef(
    public val `value`: StrongRef,
  ) : GetSubjectStatusResponseSubject

  public class AdminRepoBlobRefSerializer : KSerializer<AdminRepoBlobRef> by valueClassSerializer(
    serialName = "com.atproto.admin.defs#repoBlobRef",
    constructor = ::AdminRepoBlobRef,
    valueProvider = AdminRepoBlobRef::value,
    valueSerializerProvider = { RepoBlobRef.serializer() },
  )

  @Serializable(with = AdminRepoBlobRefSerializer::class)
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoBlobRef")
  public value class AdminRepoBlobRef(
    public val `value`: RepoBlobRef,
  ) : GetSubjectStatusResponseSubject
}

@Serializable
public data class GetSubjectStatusQueryParams(
  public val did: Did? = null,
  public val uri: AtUri? = null,
  public val blob: Cid? = null,
) {
  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    add("did" to did)
    add("uri" to uri)
    add("blob" to blob)
  }.toImmutableList()
}

@Serializable
public data class GetSubjectStatusResponse(
  public val subject: GetSubjectStatusResponseSubject,
  public val takedown: StatusAttr? = null,
)
