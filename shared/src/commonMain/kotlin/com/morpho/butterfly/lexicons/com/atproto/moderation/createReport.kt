package com.atproto.moderation

import com.atproto.admin.RepoRef
import com.atproto.repo.StrongRef
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline


@Serializable
public sealed interface ReportRequestSubject {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : ReportRequestSubject

  @Serializable
  @JvmInline
  @SerialName("com.atproto.repo.strongRef")
  public value class RepoStrongRef(
    public val `value`: StrongRef,
  ) : ReportRequestSubject
}

@Serializable
public sealed interface ReportResponseSubject {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : ReportResponseSubject

  @Serializable
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


