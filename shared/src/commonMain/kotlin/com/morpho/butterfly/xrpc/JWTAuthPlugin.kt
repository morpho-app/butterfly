package com.morpho.butterfly.xrpc

import com.atproto.server.RefreshSessionResponse
import com.morpho.butterfly.response.AtpError
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
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.util.AttributeKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.logging

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
        val log = logging("JWTAuthPlugin")

        private var justTriedARefresh = false

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
                if (result.response.status != BadRequest && result.response.status != Unauthorized) {
                    return@intercept result
                }


                // Cache the response in memory since we will need to decode it potentially more than once.
                result = result.save()

                val response = runCatching<AtpError> {
                    plugin.json.decodeFromString(result.response.bodyAsText())
                }


                if (response.getOrNull()?.error?.contains("ExpiredToken") == true
                    || response.getOrNull()?.error?.contains("InvalidToken") == true
                ) {

                    log.e {
                        "Error about to lead to a refresh:\n${response}"
                    }
                    log.e {
                        "Tokens:\n${plugin.authTokens.value?.accessToken}\n${plugin.authTokens.value?.refreshToken}"
                    }
                    if(justTriedARefresh) {
                        return@intercept result
                    }
                    val refreshResponse = scope.post("/xrpc/com.atproto.server.refreshSession") {
                        this.bearerAuth(plugin.authTokens.value?.refreshToken ?: "")
                    }
                    runCatching { refreshResponse.body<RefreshSessionResponse>() }.onFailure {
                        justTriedARefresh = true
                        log.e { "Failed to refresh session: $it" }
                    }.getOrNull()?.let { refreshed ->
                        val newAccessToken = refreshed.accessJwt
                        val newRefreshToken = refreshed.refreshJwt

                        plugin.authTokens.value = BearerTokens(newAccessToken, newRefreshToken)

                        context.headers.remove(Authorization)
                        context.bearerAuth(newAccessToken)
                        result = execute(context)
                        justTriedARefresh = false
                    }
                } else {
                    log.e {
                        "Error:\n${result.response}"
                    }
                }

                result
            }
        }
    }
}