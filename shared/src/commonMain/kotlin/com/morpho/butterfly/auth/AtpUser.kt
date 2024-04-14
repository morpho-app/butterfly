package com.morpho.butterfly.auth

import com.morpho.butterfly.AtIdentifier
import kotlinx.serialization.Serializable

@Serializable
data class AtpUser(
    val id: AtIdentifier,
    val server: Server,
) {
    var auth: AuthInfo? = null

    constructor(
        id: AtIdentifier,
        server: Server,
        auth: AuthInfo
    ) : this(id, server) {
        this.auth = auth
    }

    constructor(
        credentials: Credentials,
        server: Server,
    ) : this(credentials.username, server)


    constructor(
        credentials: Credentials,
        server: Server,
        auth: AuthInfo,
    ) : this(credentials.username, server, auth)

}