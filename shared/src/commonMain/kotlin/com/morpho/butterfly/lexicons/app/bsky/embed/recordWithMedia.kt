package app.bsky.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface RecordWithMediaMediaUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.images")
  public value class Images(
    public val `value`: app.bsky.embed.Images,
  ) : RecordWithMediaMediaUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.external")
  public value class External(
    public val `value`: app.bsky.embed.External,
  ) : RecordWithMediaMediaUnion



  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video#view")
  public value class VideoViewVideo(
    public val `value`: app.bsky.embed.VideoViewVideo,
  ) : RecordWithMediaMediaUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : RecordWithMediaMediaUnion

}


@Serializable
public data class RecordWithMedia(
  public val record: Record,
  public val media: RecordWithMediaMediaUnion,
)
