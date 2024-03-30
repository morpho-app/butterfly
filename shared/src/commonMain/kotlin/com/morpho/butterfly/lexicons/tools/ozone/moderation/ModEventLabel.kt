package tools.ozone.moderation

import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.model.ReadOnlyList

/**
 * Apply/Negate labels on a subject
 */
@Serializable
public data class ModEventLabel(
  public val comment: String? = null,
  public val createLabelVals: ReadOnlyList<String>,
  public val negateLabelVals: ReadOnlyList<String>,
)
