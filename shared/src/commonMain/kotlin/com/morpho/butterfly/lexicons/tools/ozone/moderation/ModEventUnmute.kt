package tools.ozone.moderation

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Unmute action on a subject
 */
@Serializable
public data class ModEventUnmute(
  /**
   * Describe reasoning behind the reversal.
   */
  public val comment: String? = null,
)
