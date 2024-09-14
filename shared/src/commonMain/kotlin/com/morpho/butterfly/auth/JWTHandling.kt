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
    val issuedAt: Instant?,
    val jwtId: String?,
    val scope: String?,
)

enum class TokenStatus {
    Valid,
    AccessExpired,
    RefreshExpired,
    RefreshFailed,
    NoAuth,
}

expect fun decodeJwt(jwt: String): DecodedJWT?
