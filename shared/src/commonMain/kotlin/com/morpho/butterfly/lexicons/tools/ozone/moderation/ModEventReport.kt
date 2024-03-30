package tools.ozone.moderation

import com.atproto.moderation.Token
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Report a subject
 */
@Serializable
public data class ModEventReport(
  public val comment: String? = null,
  public val reportType: Token,
)
