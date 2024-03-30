package com.atproto.temp

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class RequestPhoneVerificationRequest(
  public val phoneNumber: String,
)
