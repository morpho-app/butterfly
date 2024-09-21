package com.atproto.admin

import com.atproto.repo.StrongRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface UpdateSubjectStatusRequestSubject {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : UpdateSubjectStatusRequestSubject

  @Serializable
  @JvmInline
  @SerialName("com.atproto.repo.strongRef")
  public value class RepoStrongRef(
    public val `value`: StrongRef,
  ) : UpdateSubjectStatusRequestSubject

  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoBlobRef")
  public value class AdminRepoBlobRef(
    public val `value`: RepoBlobRef,
  ) : UpdateSubjectStatusRequestSubject
}

@Serializable
public sealed interface UpdateSubjectStatusResponseSubject {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : UpdateSubjectStatusResponseSubject

  @Serializable
  @JvmInline
  @SerialName("com.atproto.repo.strongRef")
  public value class RepoStrongRef(
    public val `value`: StrongRef,
  ) : UpdateSubjectStatusResponseSubject

  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoBlobRef")
  public value class AdminRepoBlobRef(
    public val `value`: RepoBlobRef,
  ) : UpdateSubjectStatusResponseSubject
}

@Serializable
public data class UpdateSubjectStatusRequest(
  public val subject: UpdateSubjectStatusRequestSubject,
  public val takedown: StatusAttr? = null,
)

@Serializable
public data class UpdateSubjectStatusResponse(
  public val subject: UpdateSubjectStatusResponseSubject,
  public val takedown: StatusAttr? = null,
)
