package app.bsky.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface RecordWithMediaViewMediaUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.images#view")
  public value class ImagesView(
    public val `value`: app.bsky.embed.ImagesView,
  ) : RecordWithMediaViewMediaUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.external#view")
  public value class ExternalView(
    public val `value`: app.bsky.embed.ExternalView,
  ) : RecordWithMediaViewMediaUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video#main")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : RecordWithMediaViewMediaUnion

  @Serializable
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
