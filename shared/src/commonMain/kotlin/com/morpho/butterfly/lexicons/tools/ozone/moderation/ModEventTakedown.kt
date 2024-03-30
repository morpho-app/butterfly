package tools.ozone.moderation

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Take down a subject permanently or temporarily
 */
@Serializable
public data class ModEventTakedown(
  public val comment: String? = null,
  /**
   * Indicates how long the takedown should be in effect before automatically expiring.
   */
  public val durationInHours: Long? = null,
)
