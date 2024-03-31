package com.morpho.butterfly.response

import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.Serializable
import morpho.app.api.response.StatusCode


public class AtpException(
  val statusCode: StatusCode,
  val headers: Map<String, String>,
  val error: AtpError?
) : IOException("XRPC request failed: ${statusCode::class.simpleName}, Error: ${{ error?.error }} | ${{ error?.message }}")

@Serializable
public data class AtpError(
  val error: String?,
  val message: String?,
)