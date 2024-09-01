package app.bsky.richtext

import com.atproto.label.SelfLabels
import com.morpho.butterfly.Cid
import com.morpho.butterfly.Did
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A hashtag.
 */
@Serializable
public data class FacetTag(
  public val tag: String,
) {
  init {
    require(tag.count() <= 640) {
      "tag.count() must be <= 640, but was ${tag.count()}"
    }
  }
}


@Serializable
public data class PollBlueOptionFacet(
  public val number: Int,
)

@Serializable
public data class BlueMoji(
  public val did: Did,
  public val formats: BlueMojiFormatUnion,
  public val name: String,
  public val alt: String? = null,
  public val adultOnly: Boolean? = false,
  public val labels: SelfLabels? = null,
)

@Serializable
public sealed interface BlueMojiFormatUnion {
  @Serializable
  @SerialName("blue.moji.richtext.facet#formats_v0")
  public data class BlueMojiFormatV0(
    @SerialName("png_128")
    public val png128: Cid? = null,
    @SerialName("webp_128")
    public val webp128: Cid? = null,
    @SerialName("gif_128")
    public val gif128: Cid? = null,
    @SerialName("apng_128")
    public val apng128: Boolean? = false,
    public val lottie: Boolean? = false,
  ) : BlueMojiFormatUnion
}
