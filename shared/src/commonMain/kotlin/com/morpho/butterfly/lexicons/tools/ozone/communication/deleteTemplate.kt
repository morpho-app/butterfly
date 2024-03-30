package tools.ozone.communication

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class DeleteTemplateRequest(
  public val id: String,
)
