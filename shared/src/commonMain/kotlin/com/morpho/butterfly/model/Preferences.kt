package app.bsky.actor

import com.morpho.butterfly.AtUri
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.valueClassSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface PreferencesUnion {
  public class AdultContentPrefSerializer : KSerializer<AdultContentPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#adultContentPref",
    constructor = ::AdultContentPref,
    valueProvider = AdultContentPref::value,
    valueSerializerProvider = { app.bsky.actor.AdultContentPref.serializer() },
  )

  @Serializable(with = AdultContentPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#adultContentPref")
  public value class AdultContentPref(
    public val `value`: app.bsky.actor.AdultContentPref,
  ) : PreferencesUnion

  public class LabelersPrefSerializer : KSerializer<LabelersPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#labelersPref",
    constructor = ::LabelersPref,
    valueProvider = LabelersPref::value,
    valueSerializerProvider = { app.bsky.actor.LabelersPref.serializer() },
  )

  @Serializable(with = LabelersPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#labelersPref")
  public value class LabelersPref(
    public val `value`: app.bsky.actor.LabelersPref,
  ) : PreferencesUnion


  public class ContentLabelPrefSerializer : KSerializer<ContentLabelPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#contentLabelPref",
    constructor = ::ContentLabelPref,
    valueProvider = ContentLabelPref::value,
    valueSerializerProvider = { app.bsky.actor.ContentLabelPref.serializer() },
  )

  @Serializable(with = ContentLabelPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#contentLabelPref")
  public value class ContentLabelPref(
    public val `value`: app.bsky.actor.ContentLabelPref,
  ) : PreferencesUnion

  public class SavedFeedsPrefSerializer : KSerializer<SavedFeedsPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#savedFeedsPref",
    constructor = ::SavedFeedsPref,
    valueProvider = SavedFeedsPref::value,
    valueSerializerProvider = { app.bsky.actor.SavedFeedsPref.serializer() },
  )

  @Serializable(with = SavedFeedsPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#savedFeedsPref")
  public value class SavedFeedsPref(
    public val `value`: app.bsky.actor.SavedFeedsPref,
  ) : PreferencesUnion

  public class SavedFeedsPrefV2Serializer : KSerializer<SavedFeedsPrefV2> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#savedFeedsPrefV2",
    constructor = ::SavedFeedsPrefV2,
    valueProvider = SavedFeedsPrefV2::value,
    valueSerializerProvider = { app.bsky.actor.SavedFeedsPrefV2.serializer() },
  )

  @Serializable(with = SavedFeedsPrefV2Serializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#savedFeedsPrefV2")
  public value class SavedFeedsPrefV2(
    public val `value`: app.bsky.actor.SavedFeedsPrefV2,
  ) : PreferencesUnion

  public class PersonalDetailsPrefSerializer : KSerializer<PersonalDetailsPref> by
  valueClassSerializer(
    serialName = "app.bsky.actor.defs#personalDetailsPref",
    constructor = ::PersonalDetailsPref,
    valueProvider = PersonalDetailsPref::value,
    valueSerializerProvider = { app.bsky.actor.PersonalDetailsPref.serializer() },
  )

  @Serializable(with = BskyAppStatePrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#bskyAppStatePref")
  public value class BskyAppStatePref(
    public val `value`: app.bsky.actor.BskyAppStatePref,
  ) : PreferencesUnion

  public class BskyAppStatePrefSerializer : KSerializer<BskyAppStatePref> by
  valueClassSerializer(
    serialName = "app.bsky.actor.defs#bskyAppStatePref",
    constructor = ::BskyAppStatePref,
    valueProvider = BskyAppStatePref::value,
    valueSerializerProvider = { app.bsky.actor.BskyAppStatePref.serializer() },
  )

  @Serializable(with = PersonalDetailsPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#personalDetailsPref")
  public value class PersonalDetailsPref(
    public val `value`: app.bsky.actor.PersonalDetailsPref,
  ) : PreferencesUnion

  public class FeedViewPrefSerializer : KSerializer<FeedViewPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#feedViewPref",
    constructor = ::FeedViewPref,
    valueProvider = FeedViewPref::value,
    valueSerializerProvider = { app.bsky.actor.FeedViewPref.serializer() },
  )


  @Serializable(with = FeedViewPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#feedViewPref")
  public value class FeedViewPref(
    public val `value`: app.bsky.actor.FeedViewPref,
  ) : PreferencesUnion

  public class ThreadViewPrefSerializer : KSerializer<ThreadViewPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#threadViewPref",
    constructor = ::ThreadViewPref,
    valueProvider = ThreadViewPref::value,
    valueSerializerProvider = { app.bsky.actor.ThreadViewPref.serializer() },
  )

  @Serializable(with = ThreadViewPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#threadViewPref")
  public value class ThreadViewPref(
    public val `value`: app.bsky.actor.ThreadViewPref,
  ) : PreferencesUnion

  public class InterestsPrefSerializer : KSerializer<InterestsPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#interestsPref",
    constructor = ::InterestsPref,
    valueProvider = InterestsPref::value,
    valueSerializerProvider = { app.bsky.actor.InterestsPref.serializer() },
  )

  @Serializable(with = InterestsPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#interestsPref")
  public value class InterestsPref(
    public val `value`: app.bsky.actor.InterestsPref,
  ) : PreferencesUnion

  public class MutedWordsPrefSerializer : KSerializer<MutedWordsPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#mutedWordsPref",
    constructor = ::MutedWordsPref,
    valueProvider = MutedWordsPref::value,
    valueSerializerProvider = { app.bsky.actor.MutedWordsPref.serializer() },
  )

  @Serializable(with = MutedWordsPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#mutedWordsPref")
  public value class MutedWordsPref(
    public val `value`: app.bsky.actor.MutedWordsPref,
  ) : PreferencesUnion

  public class HiddenPostsPrefSerializer : KSerializer<HiddenPostsPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#hiddenPostsPref",
    constructor = ::HiddenPostsPref,
    valueProvider = HiddenPostsPref::value,
    valueSerializerProvider = { app.bsky.actor.HiddenPostsPref.serializer() },
  )

  @Serializable(with = HiddenPostsPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#hiddenPostsPref")
  public value class HiddenPostsPref(
    public val `value`: app.bsky.actor.HiddenPostsPref,
  ) : PreferencesUnion
  public class SkyFeedBuilderFeedsPrefSerializer : KSerializer<SkyFeedBuilderFeedsPref> by valueClassSerializer(
    serialName = "app.bsky.actor.defs#skyfeedBuilderFeedsPref",
    constructor = ::SkyFeedBuilderFeedsPref,
    valueProvider = SkyFeedBuilderFeedsPref::value,
    valueSerializerProvider = { app.bsky.actor.SkyFeedBuilderFeedsPref.serializer() },
  )

  @Serializable(with = SkyFeedBuilderFeedsPrefSerializer::class)
  @JvmInline
  @SerialName("app.bsky.actor.defs#skyfeedBuilderFeedsPref")
  public value class SkyFeedBuilderFeedsPref(
    public val `value`: app.bsky.actor.SkyFeedBuilderFeedsPref,
  ) : PreferencesUnion
}

@Serializable
public data class SkyFeedBuilderFeedsPref(
  /**
   * List of feeds
   */
  public val feeds: ReadOnlyList<AtUri>,
)

@Serializable
public data class BskyAppStatePref(
  public val activeProgressGuide: BskyAppProgressGuide,
  public val queuedNudges: ReadOnlyList<String>,
)

@Serializable
@SerialName("bskyAppProgressGuide")
public data class BskyAppProgressGuide(
  public val guide: String
)