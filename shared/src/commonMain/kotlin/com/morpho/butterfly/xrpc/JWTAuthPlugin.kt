package com.morpho.butterfly.xrpc

import com.atproto.server.RefreshSessionResponse
import com.morpho.butterfly.response.AtpException
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.body
import io.ktor.client.call.save
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.plugin
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.util.AttributeKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json

class JWTAuthPlugin(
    private val json: Json,
    private val authTokens: MutableStateFlow<BearerTokens?>,
) {
    class Config(
        var json: Json = Json { ignoreUnknownKeys = true },
        var authTokens: MutableStateFlow<BearerTokens?> = MutableStateFlow(null),
    )

    companion object : HttpClientPlugin<Config, JWTAuthPlugin> {
        override val key = AttributeKey<JWTAuthPlugin>("JWTAuthPlugin")

        override fun prepare(block: Config.() -> Unit): JWTAuthPlugin {
            val config = Config().apply(block)
            return JWTAuthPlugin(config.json, config.authTokens)
        }

        override fun install(
            plugin: JWTAuthPlugin,
            scope: HttpClient,
        ) {
            scope.plugin(HttpSend).intercept { context ->
                if(context.url.toString().contains("com.atproto.server.refreshSession")) {
                    plugin.authTokens.value?.refreshToken?.let { context.bearerAuth(it) }
                } else if (!context.headers.contains(Authorization)) {
                    plugin.authTokens.value?.accessToken?.let { context.bearerAuth(it) }
                }

                var result: HttpClientCall = execute(context)
                if (result.response.status != BadRequest) {
                    return@intercept result
                }

                // Cache the response in memory since we will need to decode it potentially more than once.
                result = result.save()

                val response = runCatching<AtpException> {
                    plugin.json.decodeFromString(result.response.bodyAsText())
                }

                if (response.getOrNull()?.error?.error == "ExpiredToken") {
                    val refreshResponse = scope.post("/xrpc/com.atproto.server.refreshSession") {
                        plugin.authTokens.value?.refreshToken?.let { bearerAuth(it) }
                    }
                    runCatching { refreshResponse.body<RefreshSessionResponse>() }.getOrNull()?.let { refreshed ->
                        val newAccessToken = refreshed.accessJwt
                        val newRefreshToken = refreshed.refreshJwt

                        plugin.authTokens.value = BearerTokens(newAccessToken, newRefreshToken)

                        context.headers.remove(Authorization)
                        context.bearerAuth(newAccessToken)
                        result = execute(context)
                    }
                }

                result
            }
        }
    }
}