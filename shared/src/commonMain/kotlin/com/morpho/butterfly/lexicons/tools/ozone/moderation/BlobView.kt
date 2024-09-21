package tools.ozone.moderation

import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface BlobViewDetailsUnion {
  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#imageDetails")
  public value class ImageDetails(
    public val `value`: tools.ozone.moderation.ImageDetails,
  ) : BlobViewDetailsUnion

  @Serializable
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
