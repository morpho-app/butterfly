package app.bsky.labeler

import app.bsky.actor.ProfileView
import com.atproto.label.Label
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
public data class LabelerViewDetailed(
  public val uri: AtUri,
  public val cid: Cid,
  public val creator: ProfileView,
  public val policies: LabelerPolicies,
  public val likeCount: Long? = null,
  public val viewer: LabelerViewerState? = null,
  public val indexedAt: Timestamp,
  public val labels: ReadOnlyList<Label> = persistentListOf(),
) {
  init {
    require(likeCount == null || likeCount >= 0) {
      "likeCount must be >= 0, but was $likeCount"
    }
  }
}
