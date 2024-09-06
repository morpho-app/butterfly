package com.morpho.butterfly.auth

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
public data class DecodedJWT(
    val type: String?,
    val algorithm: String?,
    val subject: String?,
    val issuer: String?,
    val audience: String?,
    val expiresAt: Instant?,
    val notBefore: Instant?,
    val issuedAt: Instant?,
    val jwtId: String?,
    val scope: String?,
    val claims: Map<String, String> = emptyMap(),
)


expect fun decodeJwt(jwt: String): DecodedJWT?
