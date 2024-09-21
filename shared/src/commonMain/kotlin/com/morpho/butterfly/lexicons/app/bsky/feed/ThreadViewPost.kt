package app.bsky.feed

import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface ThreadViewPostParentUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#threadViewPost")
  public value class ThreadViewPost(
    public val `value`: app.bsky.feed.ThreadViewPost,
  ) : ThreadViewPostParentUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#notFoundPost")
  public value class NotFoundPost(
    public val `value`: app.bsky.feed.NotFoundPost,
  ) : ThreadViewPostParentUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#blockedPost")
  public value class BlockedPost(
    public val `value`: app.bsky.feed.BlockedPost,
  ) : ThreadViewPostParentUnion
}

@Serializable
public sealed interface ThreadViewPostReplyUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#threadViewPost")
  public value class ThreadViewPost(
    public val `value`: app.bsky.feed.ThreadViewPost,
  ) : ThreadViewPostReplyUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#notFoundPost")
  public value class NotFoundPost(
    public val `value`: app.bsky.feed.NotFoundPost,
  ) : ThreadViewPostReplyUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#blockedPost")
  public value class BlockedPost(
    public val `value`: app.bsky.feed.BlockedPost,
  ) : ThreadViewPostReplyUnion
}

@Serializable
public data class ThreadViewPost(
  public val post: PostView,
  public val parent: ThreadViewPostParentUnion? = null,
  public val replies: ReadOnlyList<ThreadViewPostReplyUnion> = persistentListOf(),
  public val viewer: ViewerThreadState? = null,
)
