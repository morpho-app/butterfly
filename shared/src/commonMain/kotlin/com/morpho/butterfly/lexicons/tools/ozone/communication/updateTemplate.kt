package tools.ozone.communication

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did

@Serializable
public data class UpdateTemplateRequest(
  /**
   * ID of the template to be updated.
   */
  public val id: String,
  /**
   * Name of the template.
   */
  public val name: String? = null,
  /**
   * Content of the template, markdown supported, can contain variable placeholders.
   */
  public val contentMarkdown: String? = null,
  /**
   * Subject of the message, used in emails.
   */
  public val subject: String? = null,
  /**
   * DID of the user who is updating the template.
   */
  public val updatedBy: Did? = null,
  public val disabled: Boolean? = null,
)

public typealias UpdateTemplateResponse = TemplateView
