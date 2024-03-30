package com.atproto.admin

import kotlin.Any
import kotlin.Pair
import kotlin.String
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList

@Serializable
public data class GetAccountInfosQuery(
  public val dids: ReadOnlyList<Did>,
) {
  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    dids.forEach {
      add("dids" to it)
    }
  }.toImmutableList()
}

@Serializable
public data class GetAccountInfosResponse(
  public val infos: ReadOnlyList<AccountView>,
)
