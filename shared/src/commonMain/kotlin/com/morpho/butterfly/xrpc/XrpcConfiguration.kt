package com.morpho.butterfly.xrpc

import com.morpho.butterfly.butterflySerializersModule
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun HttpClient.withXrpcConfiguration(): HttpClient = config {
  val jsonEnvironment = Json {
    ignoreUnknownKeys = true
    classDiscriminator = "${'$'}type"
    serializersModule = butterflySerializersModule
  }

  install(ContentNegotiation) {
    json(jsonEnvironment)
  }

  install(WebSockets)
}
