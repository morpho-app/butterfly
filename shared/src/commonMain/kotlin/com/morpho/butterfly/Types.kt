package com.morpho.butterfly

import app.bsky.graph.StarterPackViewBasic
import com.atproto.label.SelfLabel
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

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
@JvmInline
value class Cursor(val value: String?) {
    companion object {
        val Empty = Cursor(null)
    }
}

typealias FeedRequest<Data> = suspend (Cursor, Long?) -> Result<PagedResponse<Data>>

@Serializable
sealed interface PagedResponse<Data: Any>: Iterable<Data> {
    public val cursor: Cursor
    public val items: List<Data>

    @Serializable
    data class Feed<Data: Any>(
        override val cursor: Cursor,
        override val items: List<Data> = emptyList(),
    ): PagedResponse<Data>

    @Serializable
    data class Profile<Data: Any>(
        val subject: Data,
        override val cursor: Cursor,
        override val items: List<Data> = emptyList(),
    ): PagedResponse<Data>

    @Serializable
    data class FromRecord<Data: Any>(
        val uri: AtUri,
        val cid: Cid? = null,
        override val cursor: Cursor,
        override val items: List<Data> = emptyList(),
    ): PagedResponse<Data>

    override fun iterator(): Iterator<Data> {
        return items.listIterator()
    }
}
