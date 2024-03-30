package tools.ozone.communication

import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did

@Serializable
public data class CreateTemplateRequest(
  /**
   * Name of the template.
   */
  public val name: String,
  /**
   * Content of the template, markdown supported, can contain variable placeholders.
   */
  public val contentMarkdown: String,
  /**
   * Subject of the message, used in emails.
   */
  public val subject: String,
  /**
   * DID of the user who is creating the template.
   */
  public val createdBy: Did? = null,
)

public typealias CreateTemplateResponse = TemplateView
