package com.atproto.temp

import com.atproto.label.Label
import kotlin.Any
import kotlin.Long
import kotlin.Pair
import kotlin.String
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import sh.christian.ozone.api.model.ReadOnlyList

@Serializable
public data class FetchLabelsQueryParams(
  public val since: Long? = null,
  public val limit: Long? = 50,
) {
  init {
    require(limit == null || limit >= 1) {
      "limit must be >= 1, but was $limit"
    }
    require(limit == null || limit <= 250) {
      "limit must be <= 250, but was $limit"
    }
  }

  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    add("since" to since)
    add("limit" to limit)
  }.toImmutableList()
}

@Serializable
public data class FetchLabelsResponse(
  public val labels: ReadOnlyList<Label>,
)
