package app.bsky.feed

import app.bsky.actor.ProfileViewBasic
import com.atproto.label.Label
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.valueClassSerializer
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@Serializable
public sealed interface PostViewEmbedUnion {
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
  ) : PostViewEmbedUnion

  @Serializable(with = VideoViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.video#main")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : PostViewEmbedUnion

  public class VideoViewSerializer : KSerializer<VideoView> by valueClassSerializer(
    serialName = "app.bsky.embed.video#main",
    constructor = ::VideoView,
    valueProvider = VideoView::value,
    valueSerializerProvider = { app.bsky.embed.VideoView.serializer() },
  )

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
  ) : PostViewEmbedUnion





  public class ExternalViewSerializer : KSerializer<ExternalView> by valueClassSerializer(
    serialName = "app.bsky.embed.external#view",
    constructor = ::ExternalView,
    valueProvider = ExternalView::value,
    valueSerializerProvider = { app.bsky.embed.ExternalView.serializer() },
  )

  @Serializable(with = ExternalViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.external#view")
  public value class ExternalView(
    public val `value`: app.bsky.embed.ExternalView,
  ) : PostViewEmbedUnion

  public class RecordViewSerializer : KSerializer<RecordView> by valueClassSerializer(
    serialName = "app.bsky.embed.record#view",
    constructor = ::RecordView,
    valueProvider = RecordView::value,
    valueSerializerProvider = { app.bsky.embed.RecordView.serializer() },
  )

  @Serializable(with = RecordViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.record#view")
  public value class RecordView(
    public val `value`: app.bsky.embed.RecordView,
  ) : PostViewEmbedUnion

  public class RecordWithMediaViewSerializer : KSerializer<RecordWithMediaView> by
  valueClassSerializer(
    serialName = "app.bsky.embed.recordWithMedia#view",
    constructor = ::RecordWithMediaView,
    valueProvider = RecordWithMediaView::value,
    valueSerializerProvider = { app.bsky.embed.RecordWithMediaView.serializer() },
  )

  @Serializable(with = RecordWithMediaViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.recordWithMedia#view")
  public value class RecordWithMediaView(
    public val `value`: app.bsky.embed.RecordWithMediaView,
  ) : PostViewEmbedUnion
}

@Serializable
public data class PostView(
  public val uri: AtUri,
  public val cid: Cid,
  public val author: ProfileViewBasic,
  public val record: JsonElement,
  public val embed: PostViewEmbedUnion? = null,
  public val replyCount: Long? = null,
  public val repostCount: Long? = null,
  public val likeCount: Long? = null,
  public val indexedAt: Timestamp,
  public val viewer: ViewerState? = null,
  public val labels: ReadOnlyList<Label> = persistentListOf(),
  public val threadgate: ThreadgateView? = null,
)
