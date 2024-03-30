package tools.ozone.moderation

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Divert a record's blobs to a 3rd party service for further scanning/tagging
 */
@Serializable
public data class ModEventDivert(
  public val comment: String? = null,
)
