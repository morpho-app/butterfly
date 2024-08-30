package app.bsky.embed

import kotlinx.serialization.Serializable

@Serializable
public data class ImagesViewImage(
  public val thumb: String,
  public val fullsize: String,
  public val alt: String,
  public val aspectRatio: AspectRatio? = null,
)
