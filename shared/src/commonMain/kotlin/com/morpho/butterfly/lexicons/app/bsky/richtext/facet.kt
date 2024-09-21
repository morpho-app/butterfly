package app.bsky.richtext

import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface FacetFeatureUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.richtext.facet#mention")
  public value class Mention(
    public val `value`: FacetMention,
  ) : FacetFeatureUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.richtext.facet#link")
  public value class Link(
    public val `value`: FacetLink,
  ) : FacetFeatureUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.richtext.facet#tag")
  public value class Tag(
    public val `value`: FacetTag,
  ) : FacetFeatureUnion

  @Serializable
  @JvmInline
  @SerialName("blue.poll.post.facet#option")
  public value class PollBlueOption(
    public val `value`: PollBlueOptionFacet,
  ) : FacetFeatureUnion

  @Serializable
  @JvmInline
  @SerialName("blue.poll.post.facet#question")
  public value class PollBlueQuestion(
    public val `value`: PollBlueQuestionFacet,
  ) : FacetFeatureUnion

  @Serializable
  @JvmInline
  @SerialName("blue.moji.richtext.facet")
  public value class BlueMojiFacet(
    public val `value`: BlueMoji,
  ) : FacetFeatureUnion
}


@Serializable
public data class Facet(
  public val index: FacetByteSlice,
  public val features: ReadOnlyList<FacetFeatureUnion>,
)
