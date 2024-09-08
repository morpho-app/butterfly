package com.morpho.butterfly.storage

import kotlinx.serialization.Serializable

@Serializable
data class RkeyCacheEntry(
    var likeKey: String = "",
    var repostKey: String = "",
    var postKey: String = "",
    var blockKey: String = "",
    var followKey: String = "",
    var listBlockKey: String = "",
)