package com.morpho.butterfly.auth

import com.morpho.butterfly.Did
import com.morpho.butterfly.Handle
import kotlinx.serialization.Serializable

@Serializable
data class AtpUser(
    val handle: Handle,
    val id: Did,
    val server: Server,
) {
    var auth: AuthInfo? = null

    constructor(
        handle: Handle,
        id: Did,
        server: Server,
        auth: AuthInfo
    ) : this(handle,id, server) {
        this.auth = auth
    }

    constructor(
        credentials: Credentials,
        id: Did,
        server: Server
    ): this(credentials.username, id, server)

    constructor(
        credentials: Credentials,
        id: Did,
        server: Server,
        auth: AuthInfo
    ): this(credentials.username, id, server, auth)

}