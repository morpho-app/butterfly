package com.atproto.server

import com.morpho.butterfly.Did
import com.morpho.butterfly.Handle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateSessionRequest(
  /**
   * Handle or other identifier supported by the server for the authenticating user.
   */
  public val identifier: String,
  public val password: String,
  public val authFactorToken: String? = null,
)

@Serializable
public data class CreateSessionResponse(
  public val accessJwt: String,
  public val refreshJwt: String,
  public val handle: Handle,
  public val did: Did,
  public val didDoc: JsonElement? = null,
  public val email: String? = null,
  public val emailConfirmed: Boolean? = null,
  public val emailAuthFactor: Boolean? = null,
  public val active: Boolean? = null,
  public val status: AccountStatus? = null,
)

public enum class AccountStatus {
  @SerialName("takendown")
  TAKENDOWN,
  @SerialName("suspended")
  SUSPENDED,
  @SerialName("deactivated")
  DEACTIVATED,
}