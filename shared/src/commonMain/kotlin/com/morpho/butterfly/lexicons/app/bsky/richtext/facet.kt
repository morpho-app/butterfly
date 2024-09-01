package app.bsky.richtext

import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.valueClassSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface FacetFeatureUnion {
  public class MentionSerializer : KSerializer<Mention> by valueClassSerializer(
    serialName = "app.bsky.richtext.facet#mention",
    constructor = ::Mention,
    valueProvider = Mention::value,
    valueSerializerProvider = { FacetMention.serializer() },
  )

  @Serializable(with = MentionSerializer::class)
  @JvmInline
  @SerialName("app.bsky.richtext.facet#mention")
  public value class Mention(
    public val `value`: FacetMention,
  ) : FacetFeatureUnion

  public class LinkSerializer : KSerializer<Link> by valueClassSerializer(
    serialName = "app.bsky.richtext.facet#link",
    constructor = ::Link,
    valueProvider = Link::value,
    valueSerializerProvider = { FacetLink.serializer() },
  )

  @Serializable(with = LinkSerializer::class)
  @JvmInline
  @SerialName("app.bsky.richtext.facet#link")
  public value class Link(
    public val `value`: FacetLink,
  ) : FacetFeatureUnion

  public class TagSerializer : KSerializer<Tag> by valueClassSerializer(
    serialName = "app.bsky.richtext.facet#tag",
    constructor = ::Tag,
    valueProvider = Tag::value,
    valueSerializerProvider = { FacetTag.serializer() },
  )

  @Serializable(with = TagSerializer::class)
  @JvmInline
  @SerialName("app.bsky.richtext.facet#tag")
  public value class Tag(
    public val `value`: FacetTag,
  ) : FacetFeatureUnion

  public class PollBlueOptionFacetSerializer : KSerializer<PollBlueOption> by valueClassSerializer(
    serialName = "blue.poll.post.facet#option",
    constructor = ::PollBlueOption,
    valueProvider = PollBlueOption::value,
    valueSerializerProvider = { PollBlueOptionFacet.serializer() },
  )
  @Serializable(with = PollBlueOptionFacetSerializer::class)
  @JvmInline
  @SerialName("blue.poll.post.facet#option")
  public value class PollBlueOption(
    public val `value`: PollBlueOptionFacet,
  ) : FacetFeatureUnion

  public class PollBlueQuestionFacetSerializer : KSerializer<PollBlueQuestion> by valueClassSerializer(
    serialName = "blue.poll.post.facet#question",
    constructor = ::PollBlueQuestion,
    valueProvider = PollBlueQuestion::value,
    valueSerializerProvider = { PollBlueOptionFacet.serializer() },
  )
  @Serializable(with = PollBlueQuestionFacetSerializer::class)
  @JvmInline
  @SerialName("blue.poll.post.facet#question")
  public value class PollBlueQuestion(
    public val `value`: PollBlueOptionFacet,
  ) : FacetFeatureUnion

  public class BlueMojiFacetSerializer : KSerializer<BlueMojiFacet> by valueClassSerializer(
    serialName = "blue.moji.richtext.facet",
    constructor = ::BlueMojiFacet,
    valueProvider = BlueMojiFacet::value,
    valueSerializerProvider = { BlueMoji.serializer() },
  )
  @Serializable(with = BlueMojiFacetSerializer::class)
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
