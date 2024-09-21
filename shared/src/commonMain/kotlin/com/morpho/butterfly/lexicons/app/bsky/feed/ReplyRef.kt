package app.bsky.feed

import app.bsky.actor.ProfileViewBasic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface ReplyRefRootUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#postView")
  public value class PostView(
    public val `value`: app.bsky.feed.PostView,
  ) : ReplyRefRootUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#notFoundPost")
  public value class NotFoundPost(
    public val `value`: app.bsky.feed.NotFoundPost,
  ) : ReplyRefRootUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#blockedPost")
  public value class BlockedPost(
    public val `value`: app.bsky.feed.BlockedPost,
  ) : ReplyRefRootUnion
}

@Serializable
public sealed interface ReplyRefParentUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#postView")
  public value class PostView(
    public val `value`: app.bsky.feed.PostView,
  ) : ReplyRefParentUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#notFoundPost")
  public value class NotFoundPost(
    public val `value`: app.bsky.feed.NotFoundPost,
  ) : ReplyRefParentUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#blockedPost")
  public value class BlockedPost(
    public val `value`: app.bsky.feed.BlockedPost,
  ) : ReplyRefParentUnion
}

@Serializable
public data class ReplyRef(
  public val root: ReplyRefRootUnion,
  public val parent: ReplyRefParentUnion,
  public val grandparentAuthor: ProfileViewBasic? = null,
)
