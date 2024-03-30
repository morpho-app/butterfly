package com.atproto.identity

import kotlin.String
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import com.morpho.butterfly.model.ReadOnlyList

@Serializable
public data class GetRecommendedDidCredentialsResponse(
  public val rotationKeys: ReadOnlyList<String> = persistentListOf(),
  public val alsoKnownAs: ReadOnlyList<String> = persistentListOf(),
  public val verificationMethods: JsonElement? = null,
  public val services: JsonElement? = null,
)
