package com.atproto.moderation

import com.atproto.admin.RepoRef
import com.atproto.repo.StrongRef
import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.valueClassSerializer



@Serializable
public sealed interface ReportRequestSubject {
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
  ) : ReportRequestSubject

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
  ) : ReportRequestSubject
}

@Serializable
public sealed interface ReportResponseSubject {
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
  ) : ReportResponseSubject

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
  ) : ReportResponseSubject
}

@Serializable
public data class CreateReportRequest(
  public val reasonType: Token,
  public val reason: String? = null,
  public val subject: ReportRequestSubject,
)

@Serializable
public data class CreateReportResponse(
  public val id: Long,
  public val reasonType: Token,
  public val reason: String? = null,
  public val subject: ReportResponseSubject,
  public val reportedBy: Did,
  public val createdAt: Timestamp,
) {
  init {
    require(reason == null || reason.count() <= 20_000) {
      "reason.count() must be <= 20_000, but was ${reason?.count()}"
    }
  }
}


