package com.atproto.identity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class SubmitPlcOperationRequest(
  public val operation: JsonElement,
)
