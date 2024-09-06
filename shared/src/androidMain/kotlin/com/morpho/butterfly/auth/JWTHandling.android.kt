package com.morpho.butterfly.auth

import io.github.nefilim.kjwt.JWT
import kotlinx.datetime.toKotlinInstant


actual fun decodeJwt(jwt: String): DecodedJWT? {
    val decoded = JWT.decode(jwt)
    decoded.tap { decodedJWT ->
        return DecodedJWT(
            type = decodedJWT.jwt.header.type.toString(),
            algorithm = decodedJWT.jwt.header.algorithm.toString(),
            subject = decodedJWT.subject().orNull(),
            issuer = decodedJWT.issuer().orNull(),
            audience = decodedJWT.audience().orNull(),
            expiresAt = decodedJWT.expiresAt().orNull()?.toKotlinInstant(),
            notBefore = decodedJWT.notBefore().orNull()?.toKotlinInstant(),
            issuedAt = decodedJWT.issuedAt().orNull()?.toKotlinInstant(),
            jwtId = decodedJWT.jwtID().orNull(),
            scope = decodedJWT.claimValue("scope").orNull(),
            claims = decodedJWT.claimNames().associateWith { decodedJWT.claimValue(it).toString() }
        )
    }
    return null
}
