package app.bsky.graph

import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.Timestamp

@Serializable
public data class ListViewBasic(
    public val uri: AtUri,
    public val cid: Cid,
    public val name: String,
    public val purpose: ListType,
    public val avatar: String? = null,
    public val viewer: ListViewerState? = null,
    public val indexedAt: Timestamp? = null,
) {
  init {
    require(name.isNotEmpty()) {
      "name.count() must be >= 1, but was ${name.count()}"
    }
    require(name.count() <= 64) {
      "name.count() must be <= 64, but was ${name.count()}"
    }
  }
}
