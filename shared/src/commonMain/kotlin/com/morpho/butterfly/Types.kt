package com.morpho.butterfly


import app.bsky.graph.StarterPackViewBasic
import com.atproto.label.SelfLabel
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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

@Serializable
abstract class Union<T> {
    abstract val value: T
}

open class StringUnionSerializer<U: Union<String>>(
    private val from: (String) -> U,
): KSerializer<U> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor
    override fun deserialize(decoder: Decoder): U {
        return from(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: U) {
        encoder.encodeString(value.value)
    }
}

object LabelValueSerializer: StringUnionSerializer<LabelValue>(
    from = { LabelValue.from(it) }
)

@Serializable(with = LabelValueSerializer::class)
sealed class LabelValue(override val value: String): Union<String>() {
    companion object {
        inline fun <reified U: LabelValue> from(value: String): U = when(value) {
            Warn.value -> Warn as U
            NoPromote.value -> NoPromote as U
            NoSelf.value -> NoSelf as U
            NoUnauthenticated.value -> NoUnauthenticated as U
            Hide.value -> Hide as U
            DMCAViolation.value -> DMCAViolation as U
            Doxxing.value -> Doxxing as U
            Porn.value -> Porn as U
            Sexual.value -> Sexual as U
            Nudity.value -> Nudity as U
            NSFL.value -> NSFL as U
            Gore.value -> Gore as U
            GraphicMedia.value -> GraphicMedia as U
            else -> Custom(value) as U
        }
    }

    data object Warn: LabelValue("!warn")
    data object NoPromote: LabelValue("!no-promote")
    data object NoUnauthenticated: LabelValue("!no-unauthenticated")
    data object NoSelf: LabelValue("!no-self")
    data object Hide: LabelValue("!hide")
    data object DMCAViolation: LabelValue("dmca-violation")
    data object Doxxing: LabelValue("doxxing")
    data object Porn: LabelValue("porn")
    data object Sexual: LabelValue("sexual")
    data object Nudity: LabelValue("nudity")
    data object NSFL: LabelValue("nsfl")
    data object Gore: LabelValue("gore")
    data object GraphicMedia: LabelValue("graphic-media")
    data class Custom(override val value: String): LabelValue(value)
}


