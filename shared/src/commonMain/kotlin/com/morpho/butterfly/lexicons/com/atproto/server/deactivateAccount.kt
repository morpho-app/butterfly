package com.atproto.server

import kotlinx.serialization.Serializable
import sh.christian.ozone.api.model.Timestamp

@Serializable
public data class DeactivateAccountRequest(
  /**
   * A recommendation to server as to how long they should hold onto the deactivated account before
   * deleting.
   */
  public val deleteAfter: Timestamp? = null,
)
