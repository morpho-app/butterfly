package com.atproto.temp

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.Serializable

@Serializable
public data class CheckSignupQueueResponse(
  public val activated: Boolean,
  public val placeInQueue: Long? = null,
  public val estimatedTimeMs: Long? = null,
)
