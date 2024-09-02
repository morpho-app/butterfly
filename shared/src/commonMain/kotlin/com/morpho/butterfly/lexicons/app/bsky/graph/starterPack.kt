package com.morpho.butterfly.lexicons.app.bsky.graph

import app.bsky.actor.ProfileViewBasic
import app.bsky.graph.ListItemView
import app.bsky.graph.ListViewBasic
import app.bsky.richtext.Facet
import com.atproto.label.Label
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
public data class StarterPack(
    public val name: String,
    public val list: ReadOnlyList<AtUri> = persistentListOf(),
    public val createdAt: Timestamp, // Datetime
    public val description: String? = null,
    public val descriptionFacets: ReadOnlyList<Facet> = persistentListOf(),
    public val feeds: ReadOnlyList<FeedItem> = persistentListOf(),
) {
  init {
    require(name.count() in 1..500) {
      "name.count() must be <= 640, but was ${name.count()}"
    }
    require(description == null || description.count() <= 3000) {
        "description.count() must be <= 2_560, but was ${description?.count()}"
    }
      require(feeds.size <= 3) {
          "feeds.size must be <= 3, but was ${feeds.size}"
      }
  }
}

@Serializable
public data class FeedItem(
    public val uri: AtUri
)

@Serializable
public data class StarterPackViewBasic(
    public val uri: AtUri,
    public val cid: Cid,
    public val record: JsonElement,
    public val creator: ProfileViewBasic,
    public val indexedAt: Timestamp,
    public val listItemCount: Long? = null,
    public val joinedWeekCount: Long? = null,
    public val joinedAllTimeCount: Long? = null,
    public val labels: ReadOnlyList<Label> = persistentListOf(),
)

@Serializable
public data class StarterPackView(
    public val uri: AtUri,
    public val cid: Cid,
    public val record: JsonElement,
    public val creator: ProfileViewBasic,
    public val indexedAt: Timestamp,
    public val listItemCount: Long? = null,
    public val joinedWeekCount: Long? = null,
    public val joinedAllTimeCount: Long? = null,
    public val labels: ReadOnlyList<Label> = persistentListOf(),
    public val list: ListViewBasic? = null,
    public val listItemsSample: ReadOnlyList<ListItemView> = persistentListOf(),
)
