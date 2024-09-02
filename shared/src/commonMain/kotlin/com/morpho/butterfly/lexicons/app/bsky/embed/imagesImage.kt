package app.bsky.embed

import com.morpho.butterfly.model.Blob
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ImagesImage(
  public val image: Blob,
  public val alt: String,
  public val aspectRatio: AspectRatio? = null,

) {

  init {
    when (image) {
      is Blob.StandardBlob -> require(image.mimeType.startsWith("image/")) {
        "file.mimeType must be 'image/*', but was ${image.mimeType}"
      }

      is Blob.LegacyBlob -> require(image.mimeType.startsWith("image/")) {
        "file.mimeType must be 'image/*', but was ${image.mimeType}"
      }
    }
  }
}
