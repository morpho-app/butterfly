package com.atproto.label

import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Language

/**
 * Strings which describe the label in the UI, localized into a specific language.
 */
@Serializable
public data class LabelValueDefinitionStrings(
  /**
   * The code of the language these strings are written in.
   */
  public val lang: Language,
  /**
   * A short human-readable name for the label.
   */
  public val name: String,
  /**
   * A longer description of what the label means and why it might be applied.
   */
  public val description: String,
) {
  init {
    require(name.count() <= 640) {
      "name.count() must be <= 640, but was ${name.count()}"
    }
    require(description.count() <= 100_000) {
      "description.count() must be <= 100_000, but was ${description.count()}"
    }
  }
}
