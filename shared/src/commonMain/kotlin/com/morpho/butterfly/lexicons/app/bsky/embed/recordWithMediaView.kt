package app.bsky.embed

import com.morpho.butterfly.valueClassSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface RecordWithMediaViewMediaUnion {
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
  ) : RecordWithMediaViewMediaUnion

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
  ) : RecordWithMediaViewMediaUnion

  @Serializable(with = VideoViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.video#main")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : RecordWithMediaViewMediaUnion

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
  ) : RecordWithMediaViewMediaUnion
}
@Serializable
public data class RecordWithMediaView(
  public val record: RecordView,
  public val media: RecordWithMediaViewMediaUnion,
)
