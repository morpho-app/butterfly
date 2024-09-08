package app.bsky.embed

import com.morpho.butterfly.valueClassSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface RecordWithMediaMediaUnion {
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
  ) : RecordWithMediaMediaUnion

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
  ) : RecordWithMediaMediaUnion



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
  ) : RecordWithMediaMediaUnion

  @Serializable(with = VideoViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.video")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : RecordWithMediaMediaUnion

  public class VideoViewSerializer : KSerializer<VideoView> by valueClassSerializer(
    serialName = "app.bsky.embed.video",
    constructor = ::VideoView,
    valueProvider = VideoView::value,
    valueSerializerProvider = { app.bsky.embed.VideoView.serializer() },
  )
}


@Serializable
public data class RecordWithMedia(
  public val record: Record,
  public val media: RecordWithMediaMediaUnion,
)
