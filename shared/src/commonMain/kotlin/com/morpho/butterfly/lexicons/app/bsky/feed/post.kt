package app.bsky.feed

import app.bsky.richtext.Facet
import com.morpho.butterfly.Language
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.valueClassSerializer
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface PostEmbedUnion {
  public class ImagesViewSerializer : KSerializer<ImagesView> by valueClassSerializer(
    serialName = "app.bsky.embed.images#view",
    constructor = ::ImagesView,
    valueProvider = ImagesView::value,
    valueSerializerProvider = { app.bsky.embed.ImagesView.serializer() },
  )

  @Serializable(with = ImagesViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.images#view")
  public value class ImagesView(
    public val `value`: app.bsky.embed.ImagesView,
  ) : PostEmbedUnion



  public class ImagesSerializer : KSerializer<Images> by valueClassSerializer(
    serialName = "app.bsky.embed.images",
    constructor = ::Images,
    valueProvider = Images::value,
    valueSerializerProvider = { app.bsky.embed.Images.serializer() },
  )

  @Serializable(with = ImagesSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.images")
  public value class Images(
    public val `value`: app.bsky.embed.Images,
  ) : PostEmbedUnion

  public class VideoViewVideoSerializer : KSerializer<VideoViewVideo> by valueClassSerializer(
    serialName = "app.bsky.embed.video#view",
    constructor = ::VideoViewVideo,
    valueProvider = VideoViewVideo::value,
    valueSerializerProvider = { app.bsky.embed.VideoViewVideo.serializer() },
  )

  @Serializable(with = VideoViewVideoSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.video#view")
  public value class VideoViewVideo(
    public val `value`: app.bsky.embed.VideoViewVideo,
  ) : PostEmbedUnion

  public class VideoViewSerializer : KSerializer<VideoView> by valueClassSerializer(
    serialName = "app.bsky.embed.video",
    constructor = ::VideoView,
    valueProvider = VideoView::value,
    valueSerializerProvider = { app.bsky.embed.VideoView.serializer() },
  )

  @Serializable(with = VideoViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.video")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : PostEmbedUnion



  public class ExternalSerializer : KSerializer<External> by valueClassSerializer(
    serialName = "app.bsky.embed.external",
    constructor = ::External,
    valueProvider = External::value,
    valueSerializerProvider = { app.bsky.embed.External.serializer() },
  )

  @Serializable(with = ExternalSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.external")
  public value class External(
    public val `value`: app.bsky.embed.External,
  ) : PostEmbedUnion

  public class RecordSerializer : KSerializer<Record> by valueClassSerializer(
    serialName = "app.bsky.embed.record",
    constructor = ::Record,
    valueProvider = Record::value,
    valueSerializerProvider = { app.bsky.embed.Record.serializer() },
  )

  @Serializable(with = RecordSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.record")
  public value class Record(
    public val `value`: app.bsky.embed.Record,
  ) : PostEmbedUnion

  public class RecordWithMediaSerializer : KSerializer<RecordWithMedia> by valueClassSerializer(
    serialName = "app.bsky.embed.recordWithMedia",
    constructor = ::RecordWithMedia,
    valueProvider = RecordWithMedia::value,
    valueSerializerProvider = { app.bsky.embed.RecordWithMedia.serializer() },
  )

  @Serializable(with = RecordWithMediaSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.recordWithMedia")
  public value class RecordWithMedia(
    public val `value`: app.bsky.embed.RecordWithMedia,
  ) : PostEmbedUnion
}

@Serializable
public sealed interface PostLabelsUnion {
  public class SelfLabelsSerializer : KSerializer<SelfLabels> by valueClassSerializer(
    serialName = "com.atproto.label.defs#selfLabels",
    constructor = ::SelfLabels,
    valueProvider = SelfLabels::value,
    valueSerializerProvider = { com.atproto.label.SelfLabels.serializer() },
  )

  @Serializable(with = SelfLabelsSerializer::class)
  @JvmInline
  @SerialName("com.atproto.label.defs#selfLabels")
  public value class SelfLabels(
    public val `value`: com.atproto.label.SelfLabels,
  ) : PostLabelsUnion
}
@Serializable
public data class Post(
  public val text: String,
  /**
   * Deprecated: replaced by app.bsky.richtext.facet.
   */
  public val entities: ReadOnlyList<PostEntity> = persistentListOf(),
  public val facets: ReadOnlyList<Facet> = persistentListOf(),
  public val reply: PostReplyRef? = null,
  public val embed: PostEmbedUnion? = null,
  public val langs: ReadOnlyList<Language> = persistentListOf(),
  public val labels: PostLabelsUnion? = null,
  /**
   * Additional non-inline tags describing this post.
   */
  public val tags: ReadOnlyList<String> = persistentListOf(),
  public val createdAt: Timestamp,
) {
  init {
    require(text.count() <= 3_000) {
      "text.count() must be <= 3_000, but was ${text.count()}"
    }
    require(langs.count() <= 3) {
      "langs.count() must be <= 3, but was ${langs.count()}"
    }
    require(tags.count() <= 8) {
      "tags.count() must be <= 8, but was ${tags.count()}"
    }
    require(tags.count() <= 640) {
      "tags.count() must be <= 640, but was ${tags.count()}"
    }
  }
}
