package com.atproto.admin

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class StatusAttr(
  public val applied: Boolean,
  public val ref: String? = null,
)
