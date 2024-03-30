package tools.ozone.moderation

import kotlinx.serialization.Serializable

@Serializable
public data class Moderation(
  public val subjectStatus: SubjectStatusView? = null,
)
