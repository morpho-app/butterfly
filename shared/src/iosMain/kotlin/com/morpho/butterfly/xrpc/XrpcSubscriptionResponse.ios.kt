package com.morpho.butterfly.xrpc

import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

internal actual fun <T : Any> getSerializer(
    kClass: KClass<T>,
    frame: XrpcSubscriptionFrame,
): KSerializer<out T> {
    throw UnsupportedOperationException("Subscriptions are not supported yet on iOS.")
}