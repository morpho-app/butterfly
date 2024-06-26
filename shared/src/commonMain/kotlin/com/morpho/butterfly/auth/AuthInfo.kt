package com.morpho.butterfly.auth
import com.morpho.butterfly.Did
import com.morpho.butterfly.Handle
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class AuthInfo (
    val accessJwt: String,
    val refreshJwt: String,
    val handle: Handle,
    val did: Did,
    val didDoc: JsonElement? = null,
)

