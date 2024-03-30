package tools.ozone.moderation

import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface BlobViewDetailsUnion {
  public class ImageDetailsSerializer : KSerializer<ImageDetails> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#imageDetails",
    constructor = ::ImageDetails,
    valueProvider = ImageDetails::value,
    valueSerializerProvider = { tools.ozone.moderation.ImageDetails.serializer() },
  )

  @Serializable(with = ImageDetailsSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#imageDetails")
  public value class ImageDetails(
    public val `value`: tools.ozone.moderation.ImageDetails,
  ) : BlobViewDetailsUnion

  public class VideoDetailsSerializer : KSerializer<VideoDetails> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#videoDetails",
    constructor = ::VideoDetails,
    valueProvider = VideoDetails::value,
    valueSerializerProvider = { tools.ozone.moderation.VideoDetails.serializer() },
  )

  @Serializable(with = VideoDetailsSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#videoDetails")
  public value class VideoDetails(
    public val `value`: tools.ozone.moderation.VideoDetails,
  ) : BlobViewDetailsUnion
}

@Serializable
public data class BlobView(
  public val cid: Cid,
  public val mimeType: String,
  public val size: Long,
  public val createdAt: Timestamp,
  public val details: BlobViewDetailsUnion? = null,
  public val moderation: Moderation? = null,
)
