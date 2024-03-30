package com.atproto.server

import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did

@Serializable
public data class ReserveSigningKeyRequest(
  /**
   * The DID to reserve a key for.
   */
  public val did: Did? = null,
)

@Serializable
public data class ReserveSigningKeyResponse(
  /**
   * The public key for the reserved signing key, in did:key serialization.
   */
  public val signingKey: String,
)
