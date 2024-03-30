package app.bsky.feed

import kotlin.Any
import kotlin.Long
import kotlin.Pair
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface GetPostThreadResponseThreadUnion {
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
  ) : GetPostThreadResponseThreadUnion

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
  ) : GetPostThreadResponseThreadUnion

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
  ) : GetPostThreadResponseThreadUnion
}
@Serializable
public data class GetPostThreadQuery(
  public val uri: AtUri,
  public val depth: Long? = 6,
  public val parentHeight: Long? = 80,
) {
  init {
    require(depth == null || depth >= 0) {
      "depth must be >= 0, but was $depth"
    }
    require(depth == null || depth <= 1_000) {
      "depth must be <= 1_000, but was $depth"
    }
    require(parentHeight == null || parentHeight >= 0) {
      "parentHeight must be >= 0, but was $parentHeight"
    }
    require(parentHeight == null || parentHeight <= 1_000) {
      "parentHeight must be <= 1_000, but was $parentHeight"
    }
  }

  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    add("uri" to uri)
    add("depth" to depth)
    add("parentHeight" to parentHeight)
  }.toImmutableList()
}

@Serializable
public data class GetPostThreadResponse(
  public val thread: GetPostThreadResponseThreadUnion,
)
