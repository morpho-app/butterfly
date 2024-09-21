package app.bsky.feed

import app.bsky.richtext.Facet
import com.morpho.butterfly.Language
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface PostEmbedUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.images#view")
  public value class ImagesView(
    public val `value`: app.bsky.embed.ImagesView,
  ) : PostEmbedUnion



  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.images")
  public value class Images(
    public val `value`: app.bsky.embed.Images,
  ) : PostEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video#view")
  public value class VideoViewVideo(
    public val `value`: app.bsky.embed.VideoViewVideo,
  ) : PostEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : PostEmbedUnion



  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.external")
  public value class External(
    public val `value`: app.bsky.embed.External,
  ) : PostEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.record")
  public value class Record(
    public val `value`: app.bsky.embed.Record,
  ) : PostEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.recordWithMedia")
  public value class RecordWithMedia(
    public val `value`: app.bsky.embed.RecordWithMedia,
  ) : PostEmbedUnion
}

@Serializable
public sealed interface PostLabelsUnion {
  @Serializable
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
