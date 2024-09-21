package app.bsky.embed

import app.bsky.actor.ProfileViewBasic
import com.atproto.label.Label
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@Serializable
public sealed interface RecordViewRecordEmbedUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.images#view")
  public value class ImagesView(
    public val `value`: app.bsky.embed.ImagesView,
  ) : RecordViewRecordEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.external#view")
  public value class ExternalView(
    public val `value`: app.bsky.embed.ExternalView,
  ) : RecordViewRecordEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.record#view")
  public value class RecordView(
    public val `value`: app.bsky.embed.RecordView,
  ) : RecordViewRecordEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.recordWithMedia#view")
  public value class RecordWithMediaView(
    public val `value`: app.bsky.embed.RecordWithMediaView,
  ) : RecordViewRecordEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video#main")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : RecordViewRecordEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video#view")
  public value class VideoViewVideo(
    public val `value`: app.bsky.embed.VideoViewVideo,
  ) : RecordViewRecordEmbedUnion
}

@Serializable
public data class RecordViewRecord(
  public val uri: AtUri,
  public val cid: Cid,
  public val author: ProfileViewBasic,
  /**
   * The record data itself.
   */
  public val `value`: JsonElement,
  public val labels: ReadOnlyList<Label> = persistentListOf(),
  public val embeds: ReadOnlyList<RecordViewRecordEmbedUnion> = persistentListOf(),
  public val indexedAt: Timestamp,
)
