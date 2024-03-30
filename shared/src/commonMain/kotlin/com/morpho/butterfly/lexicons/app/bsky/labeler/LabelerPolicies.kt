package app.bsky.labeler

import com.atproto.label.LabelValue
import com.atproto.label.LabelValueDefinition
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
public data class LabelerPolicies(
  /**
   * The label values which this labeler publishes. May include global or custom labels.
   */

  public val labelValues: ReadOnlyList<LabelValue>,
  /**
   * Label values created by this labeler and scoped exclusively to it. Labels defined here will
   * override global label definitions for this labeler.
   */
  public val labelValueDefinitions: ReadOnlyList<LabelValueDefinition> = persistentListOf(),
)