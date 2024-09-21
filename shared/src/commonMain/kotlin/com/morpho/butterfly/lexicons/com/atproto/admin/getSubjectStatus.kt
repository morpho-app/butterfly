package com.atproto.admin

import com.atproto.repo.StrongRef
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface GetSubjectStatusResponseSubject {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : GetSubjectStatusResponseSubject

  @Serializable
  @JvmInline
  @SerialName("com.atproto.repo.strongRef")
  public value class RepoStrongRef(
    public val `value`: StrongRef,
  ) : GetSubjectStatusResponseSubject

  @Serializable
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
