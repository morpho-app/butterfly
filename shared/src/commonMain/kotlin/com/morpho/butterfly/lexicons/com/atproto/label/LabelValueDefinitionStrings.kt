package com.atproto.label

import com.morpho.butterfly.Language
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Strings which describe the label in the UI, localized into a specific language.
 */
@Parcelize
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
): Parcelable {
  init {
    require(name.count() <= 640) {
      "name.count() must be <= 640, but was ${name.count()}"
    }
    require(description.count() <= 100_000) {
      "description.count() must be <= 100_000, but was ${description.count()}"
    }
  }
}
