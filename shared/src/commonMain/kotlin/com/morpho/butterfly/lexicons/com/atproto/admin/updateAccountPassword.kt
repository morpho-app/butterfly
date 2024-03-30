package com.atproto.admin

import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did

@Serializable
public data class UpdateAccountPasswordRequest(
  public val did: Did,
  public val password: String,
)
