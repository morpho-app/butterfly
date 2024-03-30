package com.atproto.admin

import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.Did

@Serializable
public data class RepoBlobRef(
  public val did: Did,
  public val cid: Cid,
  public val recordUri: AtUri? = null,
)
