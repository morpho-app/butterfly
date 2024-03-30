package com.atproto.sync

import kotlin.Long
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.Timestamp

/**
 * Represents a change to an account's identity. Could be an updated handle, signing key, or pds
 * hosting endpoint. Serves as a prod to all downstream services to refresh their identity cache.
 */
@Serializable
public data class SubscribeReposIdentity(
  public val seq: Long,
  public val did: Did,
  public val time: Timestamp,
)
