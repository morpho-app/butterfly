package app.bsky.feed

import kotlin.jvm.JvmInline
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface ThreadViewPostParentUnion {
  public class ThreadViewPostSerializer : KSerializer<ThreadViewPost> by valueClassSerializer(
    serialName = "app.bsky.feed.defs#threadViewPost",
    constructor = ::ThreadViewPost,
    valueProvider = ThreadViewPost::value,
    valueSerializerProvider = { app.bsky.feed.ThreadViewPost.serializer() },
  )

  @Serializable(with = ThreadViewPostSerializer::class)
  @JvmInline
  @SerialName("app.bsky.feed.defs#threadViewPost")
  public value class ThreadViewPost(
    public val `value`: app.bsky.feed.ThreadViewPost,
  ) : ThreadViewPostParentUnion

  public class NotFoundPostSerializer : KSerializer<NotFoundPost> by valueClassSerializer(
    serialName = "app.bsky.feed.defs#notFoundPost",
    constructor = ::NotFoundPost,
    valueProvider = NotFoundPost::value,
    valueSerializerProvider = { app.bsky.feed.NotFoundPost.serializer() },
  )

  @Serializable(with = NotFoundPostSerializer::class)
  @JvmInline
  @SerialName("app.bsky.feed.defs#notFoundPost")
  public value class NotFoundPost(
    public val `value`: app.bsky.feed.NotFoundPost,
  ) : ThreadViewPostParentUnion

  public class BlockedPostSerializer : KSerializer<BlockedPost> by valueClassSerializer(
    serialName = "app.bsky.feed.defs#blockedPost",
    constructor = ::BlockedPost,
    valueProvider = BlockedPost::value,
    valueSerializerProvider = { app.bsky.feed.BlockedPost.serializer() },
  )

  @Serializable(with = BlockedPostSerializer::class)
  @JvmInline
  @SerialName("app.bsky.feed.defs#blockedPost")
  public value class BlockedPost(
    public val `value`: app.bsky.feed.BlockedPost,
  ) : ThreadViewPostParentUnion
}

@Serializable
public sealed interface ThreadViewPostReplyUnion {
  public class ThreadViewPostSerializer : KSerializer<ThreadViewPost> by valueClassSerializer(
    serialName = "app.bsky.feed.defs#threadViewPost",
    constructor = ::ThreadViewPost,
    valueProvider = ThreadViewPost::value,
    valueSerializerProvider = { app.bsky.feed.ThreadViewPost.serializer() },
  )

  @Serializable(with = ThreadViewPostSerializer::class)
  @JvmInline
  @SerialName("app.bsky.feed.defs#threadViewPost")
  public value class ThreadViewPost(
    public val `value`: app.bsky.feed.ThreadViewPost,
  ) : ThreadViewPostReplyUnion

  public class NotFoundPostSerializer : KSerializer<NotFoundPost> by valueClassSerializer(
    serialName = "app.bsky.feed.defs#notFoundPost",
    constructor = ::NotFoundPost,
    valueProvider = NotFoundPost::value,
    valueSerializerProvider = { app.bsky.feed.NotFoundPost.serializer() },
  )

  @Serializable(with = NotFoundPostSerializer::class)
  @JvmInline
  @SerialName("app.bsky.feed.defs#notFoundPost")
  public value class NotFoundPost(
    public val `value`: app.bsky.feed.NotFoundPost,
  ) : ThreadViewPostReplyUnion

  public class BlockedPostSerializer : KSerializer<BlockedPost> by valueClassSerializer(
    serialName = "app.bsky.feed.defs#blockedPost",
    constructor = ::BlockedPost,
    valueProvider = BlockedPost::value,
    valueSerializerProvider = { app.bsky.feed.BlockedPost.serializer() },
  )

  @Serializable(with = BlockedPostSerializer::class)
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
