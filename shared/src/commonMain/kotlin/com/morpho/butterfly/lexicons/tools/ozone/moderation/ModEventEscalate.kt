package tools.ozone.moderation

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ModEventEscalate(
  public val comment: String? = null,
)
