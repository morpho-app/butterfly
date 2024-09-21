package app.bsky.feed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface FeedViewPostReasonUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#reasonRepost")
  public value class ReasonRepost(
    public val `value`: app.bsky.feed.ReasonRepost,
  ) : FeedViewPostReasonUnion
}

@Serializable
public data class FeedViewPost(
  public val post: PostView,
  public val reply: ReplyRef? = null,
  public val reason: FeedViewPostReasonUnion? = null,
)
