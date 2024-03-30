package tools.ozone.communication

import kotlinx.serialization.Serializable
import com.morpho.butterfly.model.ReadOnlyList

@Serializable
public data class ListTemplatesResponse(
  public val communicationTemplates: ReadOnlyList<TemplateView>,
)
