package com.atproto.label

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.model.ReadOnlyList

/**
 * Declares a label value and its expected interpretations and behaviors.
 */
@Serializable
public data class LabelValueDefinition(
  /**
   * The value of the label being defined. Must only include lowercase ascii and the '-' character
   * ([a-z-]+).
   */
  public val identifier: String,
  /**
   * How should a client visually convey this label? 'inform' means neutral and informational;
   * 'alert' means negative and warning; 'none' means show nothing.
   */
  public val severity: Severity,
  /**
   * What should this label hide in the UI, if applied? 'content' hides all of the target; 'media'
   * hides the images/video/audio; 'none' hides nothing.
   */
  public val blurs: Blurs,
  /**
   * The default setting for this label.
   */
  public val defaultSetting: DefaultSetting? = DefaultSetting.WARN,
  /**
   * Does the user need to have adult content enabled in order to configure this label?
   */
  public val adultOnly: Boolean? = null,
  public val locales: ReadOnlyList<LabelValueDefinitionStrings>,
) {
  init {
    require(identifier.count() <= 100) {
      "identifier.count() must be <= 100, but was ${identifier.count()}"
    }
  }
}
