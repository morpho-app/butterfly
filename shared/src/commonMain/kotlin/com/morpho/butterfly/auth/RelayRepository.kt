package com.morpho.butterfly.auth



import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath


class RelayRepository(
    private val dir: String,
    private val key: String = ""
) {
    private val store: KStore<Server> = storeOf(
        file = "$dir/server_$key.json".toPath(),
        default = Server.BlueskySocial,
        enableCache = true
    )

    var server: Server
        get() {
            return runBlocking {
                val server = store.get()
                if(server != null) return@runBlocking server else return@runBlocking Server.BlueskySocial
            }
        }
        set(value) { runBlocking { store.set(value) } }

    fun server(): Flow<Server?> = store.updates
}