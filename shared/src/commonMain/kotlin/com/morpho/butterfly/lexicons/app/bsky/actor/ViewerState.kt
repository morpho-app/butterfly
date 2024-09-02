package app.bsky.actor

import app.bsky.graph.ListViewBasic
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
public data class ViewerState(
  public val muted: Boolean? = null,
  public val mutedByList: ListViewBasic? = null,
  public val blockedBy: Boolean? = null,
  public val blockingByList: ListViewBasic? = null,
  public val blocking: AtUri? = null,
  public val following: AtUri? = null,
  public val followedBy: AtUri? = null,
  public val knownFollowers: KnownFollowers? = null,
)

@Serializable
public data class KnownFollowers(
  public val count: Long,
  public val followers: ReadOnlyList<ProfileViewBasic> = persistentListOf(),
) {
    init {
        require(followers.size in 0..5) {
            "list must be between 0 and 5 (inclusive, but was $count"
        }
    }
}