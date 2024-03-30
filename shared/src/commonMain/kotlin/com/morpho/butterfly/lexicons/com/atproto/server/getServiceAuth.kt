package com.atproto.server

import kotlin.Any
import kotlin.Pair
import kotlin.String
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList

@Serializable
public data class GetServiceAuthQuery(
  /**
   * The DID of the service that the token will be used to authenticate with
   */
  public val aud: Did,
) {
  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    add("aud" to aud)
  }.toImmutableList()
}

@Serializable
public data class GetServiceAuthResponse(
  public val token: String,
)
