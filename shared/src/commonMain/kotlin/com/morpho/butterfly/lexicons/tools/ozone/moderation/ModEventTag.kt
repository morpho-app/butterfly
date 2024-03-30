package tools.ozone.moderation

import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.model.ReadOnlyList

/**
 * Add/Remove a tag on a subject
 */
@Serializable
public data class ModEventTag(
  /**
   * Tags to be added to the subject. If already exists, won't be duplicated.
   */
  public val add: ReadOnlyList<String>,
  /**
   * Tags to be removed to the subject. Ignores a tag If it doesn't exist, won't be duplicated.
   */
  public val remove: ReadOnlyList<String>,
  /**
   * Additional comment about added/removed tags.
   */
  public val comment: String? = null,
)
