package com.atproto.admin

import com.atproto.repo.StrongRef
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface UpdateSubjectStatusRequestSubject {
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
  ) : UpdateSubjectStatusRequestSubject

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
  ) : UpdateSubjectStatusRequestSubject

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
  ) : UpdateSubjectStatusRequestSubject
}

@Serializable
public sealed interface UpdateSubjectStatusResponseSubject {
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
  ) : UpdateSubjectStatusResponseSubject

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
  ) : UpdateSubjectStatusResponseSubject

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
