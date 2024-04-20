package com.morpho.butterfly.auth

import com.morpho.butterfly.AtIdentifier
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.extensions.getOrEmpty
import io.github.xxfast.kstore.extensions.minus
import io.github.xxfast.kstore.extensions.plus
import io.github.xxfast.kstore.file.extensions.listStoreOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import okio.Path.Companion.toPath


interface UserRepository {
    suspend fun findUser(id: AtIdentifier): AtpUser?
    suspend fun setAuth(id: AtIdentifier, auth: AuthInfo): Boolean
    suspend fun getAuth(id: AtIdentifier): AuthInfo?
    suspend fun addUser(credentials: Credentials, server: Server)
    suspend fun addUser(atpUser: AtpUser)
    suspend fun addUsers(users: List<AtpUser>)
    suspend fun removeUser(id: AtIdentifier): Boolean

    fun firstUser(): AtpUser?
}

class UserRepositoryImpl(storageDir: String): UserRepository {
    private val _userStore: KStore<List<AtpUser>> = listStoreOf(
        file = "$storageDir/users.json".toPath(),
        enableCache = true
    )
    private val _users: Flow<List<AtpUser>?>
        get() = _userStore.updates.distinctUntilChanged()

    override fun firstUser(): AtpUser? = runBlocking(Dispatchers.IO) { _userStore.getOrEmpty().firstOrNull() }

    override suspend fun findUser(id: AtIdentifier) = coroutineScope {
        async { _users.firstOrNull()?.firstOrNull { it.id == id } }.await()
    }

    override suspend fun setAuth(id: AtIdentifier, auth: AuthInfo): Boolean = coroutineScope {
        return@coroutineScope async {
            val user =_users.firstOrNull()?.firstOrNull { it.id == id }
            if (user != null) {
                val update = launch(Dispatchers.IO) {
                    _userStore.minus(user)
                    user.auth = auth
                    _userStore.plus(user)
                }
                select { update.onJoin { !update.isCancelled } }
            } else false
        }.await()
    }

    override suspend fun getAuth(id: AtIdentifier): AuthInfo? = coroutineScope {
        async { _users.firstOrNull()?.firstOrNull { it.id == id }?.auth }.await()
    }
    override suspend fun addUser(credentials: Credentials, server: Server): Unit = coroutineScope  {
        launch(Dispatchers.IO) { _userStore.plus(AtpUser(credentials, server)) }
    }

    override suspend fun addUser(atpUser: AtpUser): Unit = coroutineScope {
        launch(Dispatchers.IO) { _userStore.plus(atpUser) }
    }

    override suspend fun addUsers(users: List<AtpUser>): Unit = coroutineScope  {
        launch(Dispatchers.IO) {
            users.map { user -> launch { _userStore.plus(user) } }
        }
    }

    override suspend fun removeUser(id: AtIdentifier): Boolean = coroutineScope  {
        val user = findUser(id)
        return@coroutineScope if (user != null) {
            launch(Dispatchers.IO) { _userStore.minus(user) }
            true
        } else false
    }

}