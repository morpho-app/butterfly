package tools.ozone.moderation

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Mute incoming reports on a subject
 */
@Serializable
public data class ModEventMute(
  public val comment: String? = null,
  /**
   * Indicates how long the subject should remain muted.
   */
  public val durationInHours: Long,
)
