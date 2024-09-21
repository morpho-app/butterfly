package app.bsky.feed

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
public sealed interface PostViewEmbedUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.images#view")
  public value class ImagesView(
    public val `value`: app.bsky.embed.ImagesView,
  ) : PostViewEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video#main")
  public value class VideoView(
    public val `value`: app.bsky.embed.VideoView,
  ) : PostViewEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.video#view")
  public value class VideoViewVideo(
    public val `value`: app.bsky.embed.VideoViewVideo,
  ) : PostViewEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.external#view")
  public value class ExternalView(
    public val `value`: app.bsky.embed.ExternalView,
  ) : PostViewEmbedUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.embed.record#view")
  public value class RecordView(
    public val `value`: app.bsky.embed.RecordView,
  ) : PostViewEmbedUnion

  @Serializable
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
