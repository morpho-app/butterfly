package app.bsky.embed

import app.bsky.feed.GeneratorView
import app.bsky.graph.ListView
import app.bsky.labeler.LabelerView
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface RecordViewRecordUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.record#viewRecord")
  public value class ViewRecord(
    public val `value`: RecordViewRecord,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.record#viewNotFound")
  public value class ViewNotFound(
    public val `value`: RecordViewNotFound,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.record#viewBlocked")
  public value class ViewBlocked(
    public val `value`: RecordViewBlocked,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.record#viewDetached")
  public value class ViewDetached(
    public val `value`: RecordViewDetached,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#generatorView")
  public value class FeedGeneratorView(
    public val `value`: GeneratorView,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.graph.defs#listView")
  public value class GraphListView(
    public val `value`: ListView,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.graph.defs#starterPackView")
  public value class StarterPackView(
    public val `value`: app.bsky.graph.StarterPackView,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.graph.defs#starterPackViewBasic")
  public value class StarterPackViewBasic(
    public val `value`: app.bsky.graph.StarterPackViewBasic,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.labeler.defs#labelerView")
  public value class LabelerLabelerView(
    public val `value`: LabelerView,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video#main")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : RecordViewRecordUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video#view")
  public value class VideoViewVideo(
    public val `value`: app.bsky.embed.VideoViewVideo,
  ) : RecordViewRecordUnion
}

@Serializable
public data class RecordView(
  public val record: RecordViewRecordUnion,
)

