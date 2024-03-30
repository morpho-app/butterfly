package tools.ozone.communication

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.Timestamp

@Serializable
public data class TemplateView(
  public val id: String,
  /**
   * Name of the template.
   */
  public val name: String,
  /**
   * Content of the template, can contain markdown and variable placeholders.
   */
  public val subject: String? = null,
  /**
   * Subject of the message, used in emails.
   */
  public val contentMarkdown: String,
  public val disabled: Boolean,
  /**
   * DID of the user who last updated the template.
   */
  public val lastUpdatedBy: Did,
  public val createdAt: Timestamp,
  public val updatedAt: Timestamp,
)
