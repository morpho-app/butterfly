package com.morpho.butterfly.xrpc

import com.morpho.butterfly.butterflySerializersModule
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

fun HttpClient.withXrpcConfiguration(
  polySerializersModule: SerializersModule = butterflySerializersModule
): HttpClient = config {
  val jsonEnvironment = Json {
    ignoreUnknownKeys = true
    classDiscriminator = "${'$'}type"
    serializersModule = polySerializersModule
  }

  install(ContentNegotiation) {
    json(jsonEnvironment)
  }

  install(WebSockets)
}
