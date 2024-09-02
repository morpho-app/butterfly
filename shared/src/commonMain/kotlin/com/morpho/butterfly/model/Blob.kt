package com.morpho.butterfly.model

import kotlinx.serialization.*
import kotlinx.serialization.cbor.ByteString
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@SerialName("blob")
@Serializable(with = BlobSerializer::class)
sealed interface Blob {

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    data class StandardBlob(
        @ByteString val ref: BlobRef,
        val mimeType: String,
        val size: Long,
        @SerialName("\$type")
        @EncodeDefault(EncodeDefault.Mode.ALWAYS) public val type: String = "blob",
    ) : Blob

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    data class LegacyBlob(
        val cid: String,
        val mimeType: String,
        @SerialName("\$type")
        @EncodeDefault(EncodeDefault.Mode.ALWAYS) public val type: String = "blob",
    ) : Blob
}

@Serializable
data class BlobRef(
    @SerialName("\$link")
    val link: String,
)

class BlobSerializer : JsonContentPolymorphicSerializer<Blob>(Blob::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Blob> {
        return if (element.jsonObject.containsKey("ref")) {
            Blob.StandardBlob.serializer()
        } else {
            Blob.LegacyBlob.serializer()
        }
    }
}