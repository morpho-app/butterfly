package tools.ozone.moderation

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Revert take down action on a subject
 */
@Serializable
public data class ModEventReverseTakedown(
  /**
   * Describe reasoning behind the reversal.
   */
  public val comment: String? = null,
)
