package tools.ozone.moderation

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Keep a log of outgoing email to a user
 */
@Serializable
public data class ModEventEmail(
  /**
   * The subject line of the email sent to the user.
   */
  public val subjectLine: String,
  /**
   * The content of the email sent to the user.
   */
  public val content: String? = null,
  /**
   * Additional comment about the outgoing comm.
   */
  public val comment: String? = null,
)
