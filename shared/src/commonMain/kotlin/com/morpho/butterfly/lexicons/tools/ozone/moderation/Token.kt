package tools.ozone.moderation

import kotlinx.serialization.SerialName

public enum class Token {
  @SerialName("tools.ozone.moderation.defs#reviewOpen")
  REVIEW_OPEN,
  @SerialName("tools.ozone.moderation.defs#reviewEscalated")
  REVIEW_ESCALATED,
  @SerialName("tools.ozone.moderation.defs#reviewClosed")
  REVIEW_CLOSED,
  @SerialName("tools.ozone.moderation.defs#reviewNone")
  REVIEW_NONE,
}
