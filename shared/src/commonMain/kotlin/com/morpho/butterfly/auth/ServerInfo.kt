package com.morpho.butterfly.auth

import com.morpho.butterfly.model.ReadOnlyList

data class ServerInfo(
    val inviteCodeRequired: Boolean,
    val availableUserDomains: ReadOnlyList<String>,
    val privacyPolicy: String?,
    val termsOfService: String?,
)