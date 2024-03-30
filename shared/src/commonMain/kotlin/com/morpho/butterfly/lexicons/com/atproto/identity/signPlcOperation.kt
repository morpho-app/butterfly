package com.atproto.identity

import kotlin.String
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import com.morpho.butterfly.model.ReadOnlyList

@Serializable
public data class SignPlcOperationRequest(
  /**
   * A token received through com.atproto.identity.requestPlcOperationSignature
   */
  public val token: String? = null,
  public val rotationKeys: ReadOnlyList<String> = persistentListOf(),
  public val alsoKnownAs: ReadOnlyList<String> = persistentListOf(),
  public val verificationMethods: JsonElement? = null,
  public val services: JsonElement? = null,
)

@Serializable
public data class SignPlcOperationResponse(
  /**
   * A signed DID PLC operation.
   */
  public val operation: JsonElement,
)
