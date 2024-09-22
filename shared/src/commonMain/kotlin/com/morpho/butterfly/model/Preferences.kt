package app.bsky.actor

import com.morpho.butterfly.AtUri
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@Serializable
public sealed interface PreferencesUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#adultContentPref")
  public value class AdultContentPref(
    public val `value`: app.bsky.actor.AdultContentPref,
  ) : PreferencesUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#labelersPref")
  public value class LabelersPref(
    public val `value`: app.bsky.actor.LabelersPref,
  ) : PreferencesUnion


  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#contentLabelPref")
  public value class ContentLabelPref(
    public val `value`: app.bsky.actor.ContentLabelPref,
  ) : PreferencesUnion



  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#savedFeedsPref")
  public value class SavedFeedsPref(
    public val `value`: app.bsky.actor.SavedFeedsPref,
  ) : PreferencesUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#savedFeedsPrefV2")
  public value class SavedFeedsPrefV2(
    public val `value`: app.bsky.actor.SavedFeedsPrefV2,
  ) : PreferencesUnion


  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#bskyAppStatePref")
  public value class BskyAppStatePref(
    public val `value`: app.bsky.actor.BskyAppStatePref,
  ) : PreferencesUnion


  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#personalDetailsPref")
  public value class PersonalDetailsPref(
    public val `value`: app.bsky.actor.PersonalDetailsPref,
  ) : PreferencesUnion


  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#feedViewPref")
  public value class FeedViewPref(
    public val `value`: app.bsky.actor.FeedViewPref,
  ) : PreferencesUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#threadViewPref")
  public value class ThreadViewPref(
    public val `value`: app.bsky.actor.ThreadViewPref,
  ) : PreferencesUnion


  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#interestsPref")
  public value class InterestsPref(
    public val `value`: app.bsky.actor.InterestsPref,
  ) : PreferencesUnion


  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#mutedWordsPref")
  public value class MutedWordsPref(
    public val `value`: app.bsky.actor.MutedWordsPref,
  ) : PreferencesUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#hiddenPostsPref")
  public value class HiddenPostsPref(
    public val `value`: app.bsky.actor.HiddenPostsPref,
  ) : PreferencesUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.actor.defs#skyfeedBuilderFeedsPref")
  public value class SkyFeedBuilderFeedsPref(
    public val `value`: app.bsky.actor.SkyFeedBuilderFeedsPref,
  ) : PreferencesUnion

  @Serializable
  @JvmInline
  public value class UnknownPreference(public val `value`: JsonElement): PreferencesUnion

  @Serializable
  @SerialName("app.bsky.actor.defs#butterflyPreference")
  public open class ButterflyPreference: PreferencesUnion

}




@Serializable
@SerialName("app.bsky.actor.defs#skyfeedBuilderFeedsPref")
public data class SkyFeedBuilderFeedsPref(
  /**
   * List of feeds
   */
  public val feeds: ReadOnlyList<AtUri>,
)

@Serializable
@SerialName("app.bsky.actor.defs#bskyAppStatePref")
public data class BskyAppStatePref(
  public val activeProgressGuide: BskyAppProgressGuide? = null,
  public val queuedNudges: ReadOnlyList<String> = persistentListOf(),
)

@Serializable
@SerialName("app.bsky.actor.defs#bskyAppProgressGuide")
public data class BskyAppProgressGuide(
  public val guide: String
)