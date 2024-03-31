package com.morpho.butterfly.auth

import kotlinx.collections.immutable.ImmutableList

data class ServerInfo(
    val inviteCodeRequired: Boolean,
    val availableUserDomains: ImmutableList<String>,
    val privacyPolicy: String?,
    val termsOfService: String?,
)