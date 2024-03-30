package tools.ozone.moderation

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Add a comment to a subject
 */
@Serializable
public data class ModEventComment(
  public val comment: String,
  /**
   * Make the comment persistent on the subject
   */
  public val sticky: Boolean? = null,
)
