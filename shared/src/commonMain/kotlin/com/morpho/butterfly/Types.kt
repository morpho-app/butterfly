package com.morpho.butterfly

import app.bsky.actor.ProfileView
import app.bsky.graph.StarterPackViewBasic
import com.atproto.label.SelfLabel
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

enum class HiddenPostAction {
    Hide,
    Unhide,
}

@Serializable
data class ProfileUpdate(
    val displayName: String? = null,
    val description: String? = null,
    val avatar: String? = null,
    val banner: String? = null,
    val labels: List<SelfLabel> = emptyList(),
    val joinedViaStarterPack: StarterPackViewBasic? = null,
    val createdAt: Timestamp? = null,
    val map: Map<String, JsonElement> = emptyMap(),
) {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("\$type")
    @EncodeDefault(EncodeDefault.Mode.ALWAYS) public val type: String = "app.bsky.actor.profile"
}



@Serializable
data class PagedList<T>(
    val cursor: String?,
    val items: List<T> = emptyList(),
)

@Serializable
data class ProfileListResponse(
    val subject: ProfileView,
    val cursor: String?,
    val profiles: List<ProfileView>,
)

@Serializable
data class PostQueryResponse<T>(
    val uri: AtUri,
    val cid: Cid? = null,
    val cursor: String? = null,
    val posts: List<T> = emptyList(),
)
