package app.bsky.feed

import app.bsky.graph.ListViewBasic
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@Serializable
public sealed interface GetPostThreadResponseThreadUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#threadViewPost")
  public value class ThreadViewPost(
    public val `value`: app.bsky.feed.ThreadViewPost,
  ) : GetPostThreadResponseThreadUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.defs#notFoundPost")
  public value class NotFoundPost(
    public val `value`: app.bsky.feed.NotFoundPost,
  ) : GetPostThreadResponseThreadUnion

  @Serializable
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
  public val threadGate: ThreadGateView? = null,
)

@Serializable
@SerialName("threadGateView")
public data class ThreadGateView(
  public val uri: AtUri,
  public val cid: Cid,
  public val record: JsonElement,
  public val lists: ReadOnlyList<ListViewBasic>  = persistentListOf(),
)