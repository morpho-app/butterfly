package app.bsky.feed

import com.morpho.butterfly.AtUri
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface SkeletonFeedPostReasonUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#skeletonReasonRepost")
  public value class SkeletonReasonRepost(
    public val `value`: app.bsky.feed.SkeletonReasonRepost,
  ) : SkeletonFeedPostReasonUnion
}

@Serializable
public data class SkeletonFeedPost(
  public val post: AtUri,
  public val reason: SkeletonFeedPostReasonUnion? = null,
)
