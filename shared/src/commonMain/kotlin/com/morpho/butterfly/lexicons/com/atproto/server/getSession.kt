package com.atproto.server

import com.morpho.butterfly.Did
import com.morpho.butterfly.Handle
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class GetSessionResponse(
  public val handle: Handle,
  public val did: Did,
  public val email: String? = null,
  public val emailConfirmed: Boolean? = null,
  public val emailAuthFactor: Boolean? = null,
  public val didDoc: JsonElement? = null,
  public val active: Boolean? = null,
  public val status: AccountStatus? = null,
)
