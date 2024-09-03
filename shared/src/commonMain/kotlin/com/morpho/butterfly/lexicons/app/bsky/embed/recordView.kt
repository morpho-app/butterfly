package app.bsky.embed

import app.bsky.feed.GeneratorView
import app.bsky.graph.ListView
import app.bsky.labeler.LabelerView
import com.morpho.butterfly.valueClassSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface RecordViewRecordUnion {
  public class ViewRecordSerializer : KSerializer<ViewRecord> by valueClassSerializer(
    serialName = "app.bsky.embed.record#viewRecord",
    constructor = ::ViewRecord,
    valueProvider = ViewRecord::value,
    valueSerializerProvider = { RecordViewRecord.serializer() },
  )

  @Serializable(with = ViewRecordSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.record#viewRecord")
  public value class ViewRecord(
    public val `value`: RecordViewRecord,
  ) : RecordViewRecordUnion

  public class ViewNotFoundSerializer : KSerializer<ViewNotFound> by valueClassSerializer(
    serialName = "app.bsky.embed.record#viewNotFound",
    constructor = ::ViewNotFound,
    valueProvider = ViewNotFound::value,
    valueSerializerProvider = { RecordViewNotFound.serializer() },
  )

  @Serializable(with = ViewNotFoundSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.record#viewNotFound")
  public value class ViewNotFound(
    public val `value`: RecordViewNotFound,
  ) : RecordViewRecordUnion

  public class ViewBlockedSerializer : KSerializer<ViewBlocked> by valueClassSerializer(
    serialName = "app.bsky.embed.record#viewBlocked",
    constructor = ::ViewBlocked,
    valueProvider = ViewBlocked::value,
    valueSerializerProvider = { RecordViewBlocked.serializer() },
  )

  @Serializable(with = ViewBlockedSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.record#viewBlocked")
  public value class ViewBlocked(
    public val `value`: RecordViewBlocked,
  ) : RecordViewRecordUnion

  public class ViewDetachederializer : KSerializer<ViewDetached> by valueClassSerializer(
    serialName = "app.bsky.embed.record#viewDetached",
    constructor = ::ViewDetached,
    valueProvider = ViewDetached::value,
    valueSerializerProvider = { RecordViewDetached.serializer() },
  )

  @Serializable(with = ViewDetachederializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.record#viewDetached")
  public value class ViewDetached(
    public val `value`: RecordViewDetached,
  ) : RecordViewRecordUnion

  public class FeedGeneratorViewSerializer : KSerializer<FeedGeneratorView> by valueClassSerializer(
    serialName = "app.bsky.feed.defs#generatorView",
    constructor = ::FeedGeneratorView,
    valueProvider = FeedGeneratorView::value,
    valueSerializerProvider = { GeneratorView.serializer() },
  )

  @Serializable(with = FeedGeneratorViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.feed.defs#generatorView")
  public value class FeedGeneratorView(
    public val `value`: GeneratorView,
  ) : RecordViewRecordUnion

  public class GraphListViewSerializer : KSerializer<GraphListView> by valueClassSerializer(
    serialName = "app.bsky.graph.defs#listView",
    constructor = ::GraphListView,
    valueProvider = GraphListView::value,
    valueSerializerProvider = { ListView.serializer() },
  )

  @Serializable(with = GraphListViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.graph.defs#listView")
  public value class GraphListView(
    public val `value`: ListView,
  ) : RecordViewRecordUnion

  public class StarterPackViewSerializer : KSerializer<StarterPackView> by valueClassSerializer(
    serialName = "app.bsky.graph.defs#starterPackView",
    constructor = ::StarterPackView,
    valueProvider = StarterPackView::value,
    valueSerializerProvider = { app.bsky.graph.StarterPackView.serializer() },
  )

  @Serializable(with = StarterPackViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.graph.defs#starterPackView")
  public value class StarterPackView(
    public val `value`: app.bsky.graph.StarterPackView,
  ) : RecordViewRecordUnion

  public class StarterPackViewBasicSerializer : KSerializer<StarterPackViewBasic> by valueClassSerializer(
    serialName = "app.bsky.graph.defs#starterPackViewBasic",
    constructor = ::StarterPackViewBasic,
    valueProvider = StarterPackViewBasic::value,
    valueSerializerProvider = { app.bsky.graph.StarterPackViewBasic.serializer() },
  )

  @Serializable(with = StarterPackViewBasicSerializer::class)
  @JvmInline
  @SerialName("app.bsky.graph.defs#starterPackViewBasic")
  public value class StarterPackViewBasic(
    public val `value`: app.bsky.graph.StarterPackViewBasic,
  ) : RecordViewRecordUnion

  public class LabelerLabelerViewSerializer : KSerializer<LabelerLabelerView> by
  valueClassSerializer(
    serialName = "app.bsky.labeler.defs#labelerView",
    constructor = ::LabelerLabelerView,
    valueProvider = LabelerLabelerView::value,
    valueSerializerProvider = { LabelerView.serializer() },
  )

  @Serializable(with = LabelerLabelerViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.labeler.defs#labelerView")
  public value class LabelerLabelerView(
    public val `value`: LabelerView,
  ) : RecordViewRecordUnion

  @Serializable(with = VideoViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.embed.video#main")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : RecordViewRecordUnion

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
  ) : RecordViewRecordUnion
}

@Serializable
public data class RecordView(
  public val record: RecordViewRecordUnion,
)

