package tools.ozone.moderation

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Resolve appeal on a subject
 */
@Serializable
public data class ModEventResolveAppeal(
  /**
   * Describe resolution.
   */
  public val comment: String? = null,
)
