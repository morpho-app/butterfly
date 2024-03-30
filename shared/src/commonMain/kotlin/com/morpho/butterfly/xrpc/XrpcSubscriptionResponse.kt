package com.morpho.butterfly.xrpc

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.serializer
import com.morpho.butterfly.cbor.ByteArrayInput
import com.morpho.butterfly.cbor.CborDecoder
import com.morpho.butterfly.cbor.CborReader
import com.morpho.butterfly.response.AtpError
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

data class XrpcSubscriptionResponse(
  val bytes: ByteArray,
) {
  @ExperimentalSerializationApi
  inline fun <reified T : Any> body(): T = body(T::class)

  @ExperimentalSerializationApi
  fun <T : Any> body(kClass: KClass<T>): T {
    val frame = decodeFromByteArray(XrpcSubscriptionFrame.serializer(), bytes)

    val payloadPosition = bytes.drop(1).indexOfFirst { it.toInt().isCborMapStart() } + 1
    val payloadBytes = bytes.drop(payloadPosition).toByteArray()

    if (frame.op == 1 && frame.t != null) {
      val serializer = getSerializer(kClass, frame)
      return decodeFromByteArray(serializer, payloadBytes)
    } else {
      val maybeError = runCatching { decodeFromByteArray(AtpError.serializer(), payloadBytes) }.getOrNull()
      throw XrpcSubscriptionParseException(maybeError)
    }
  }

  @ExperimentalSerializationApi
  private fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
    val stream = ByteArrayInput(bytes)
    val reader = CborReader(cbor, CborDecoder(stream))
    return reader.decodeSerializableValue(deserializer)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is XrpcSubscriptionResponse) return false

    return bytes.contentEquals(other.bytes)
  }

  override fun hashCode(): Int {
    return bytes.contentHashCode()
  }

  override fun toString(): String {
    return "XrpcSubscriptionResponse(bytes=${bytes.contentToString()})"
  }

  private companion object {
    @ExperimentalSerializationApi
    val cbor = Cbor { ignoreUnknownKeys = true }
  }
}

internal fun Int.isCborMapStart(): Boolean = (this and 0b11100000) == 0b10100000

internal expect fun <T : Any> getSerializer(
  kClass: KClass<T>,
  frame: XrpcSubscriptionFrame,
): KSerializer<out T>