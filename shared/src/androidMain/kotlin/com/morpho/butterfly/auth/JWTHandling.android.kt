package com.morpho.butterfly.auth
import com.philjay.jwt.*
import kotlinx.datetime.Instant
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import com.morpho.butterfly.json as jsonCodec


@OptIn(ExperimentalEncodingApi::class)
private val decoder = object : Base64Decoder {
    override fun decode(bytes: ByteArray): ByteArray {

        return Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT_OPTIONAL).decode(bytes)
    }

    override fun decode(string: String): ByteArray {
        return Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT_OPTIONAL).decode(string)
    }
}

class ATJWTHeader(val typ: String): JWTAuthHeader("ES256K") {
    init {
        require(typ == "at+jwt").let { "typ must be 'at+jwt', but was $typ" }
    }
}

class ATJWTAuthPayload(
    val scope: String, val aud: String,
    val jti: String, val sub: String,
    val exp: Long, issuedAt: Long
): JWTAuthPayload("", issuedAt)
private val jsonDecoder = object : JsonDecoder<ATJWTHeader, ATJWTAuthPayload> {

    override fun headerFrom(json: String): ATJWTHeader {
        val header = jsonCodec.parseToJsonElement(json)
        return ATJWTHeader(header.jsonObject["typ"]!!.jsonPrimitive.content)
    }

    override fun payloadFrom(json: String): ATJWTAuthPayload {
        val payload = jsonCodec.parseToJsonElement(json)
        return ATJWTAuthPayload(
            payload.jsonObject["scope"]?.jsonPrimitive?.content ?: "",
            payload.jsonObject["aud"]?.jsonPrimitive?.content ?: "",
            payload.jsonObject["jti"]?.jsonPrimitive?.content ?: "",
            payload.jsonObject["sub"]?.jsonPrimitive?.content ?: "",
            payload.jsonObject["exp"]?.jsonPrimitive?.long ?: 0,
            payload.jsonObject["iat"]?.jsonPrimitive?.long ?: 0,
        )
    }
}

actual fun decodeJwt(jwt: String): DecodedJWT? {
    val decoded = JWT.decode(jwt, jsonDecoder, decoder)
    return if (decoded != null) {
        DecodedJWT(
            type = decoded.header.typ,
            algorithm = decoded.header.alg,
            subject = decoded.payload.sub,
            issuer = decoded.payload.iss,
            audience = decoded.payload.aud,
            expiresAt = Instant.fromEpochSeconds(decoded.payload.exp),
            issuedAt = Instant.fromEpochSeconds(decoded.payload.iat),
            jwtId = decoded.payload.jti,
            scope = decoded.payload.scope,
        )
    } else null
}