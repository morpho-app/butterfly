package app.bsky.graph

import app.bsky.actor.ProfileView
import app.bsky.richtext.Facet
import com.atproto.label.Label
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
public data class ListView(
  public val uri: AtUri,
  public val cid: Cid,
  public val creator: ProfileView,
  public val name: String,
  public val purpose: ListType,
  public val description: String? = null,
  public val descriptionFacets: ReadOnlyList<Facet> = persistentListOf(),
  public val avatar: String? = null,
  public val viewer: ListViewerState? = null,
  public val indexedAt: Timestamp,
  public val labels: ReadOnlyList<Label> = persistentListOf(),
  public val items: ReadOnlyList<ProfileView> =  persistentListOf(),
) {
  init {
    require(name.isNotEmpty()) {
      "name.count() must be >= 1, but was ${name.count()}"
    }
    require(name.count() <= 64) {
      "name.count() must be <= 64, but was ${name.count()}"
    }
    require(description == null || description.count() <= 3_000) {
      "description.count() must be <= 3_000, but was ${description?.count()}"
    }
  }
}
