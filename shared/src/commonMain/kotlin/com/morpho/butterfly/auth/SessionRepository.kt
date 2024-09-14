package com.morpho.butterfly.auth

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath


class SessionRepository(dir: String) {
    private val authStore: KStore<AuthInfo> = storeOf(
        file = "$dir/jwt.json".toPath(),
        default = null,
        enableCache = true
    )

    var auth: AuthInfo?
        get() {
            return runBlocking { authStore.get() }
        }
        set(value) { runBlocking { authStore.set(value) } }

    fun auth(): Flow<AuthInfo?> = authStore.updates
}



