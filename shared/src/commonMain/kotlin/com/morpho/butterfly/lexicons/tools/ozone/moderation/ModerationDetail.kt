package tools.ozone.moderation

import kotlinx.serialization.Serializable

@Serializable
public data class ModerationDetail(
  public val subjectStatus: SubjectStatusView? = null,
)
