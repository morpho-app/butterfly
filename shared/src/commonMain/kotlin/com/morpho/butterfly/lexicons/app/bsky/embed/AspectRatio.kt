package app.bsky.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("aspectRatio")
public data class AspectRatio(
    public val width: Long,
    public val height: Long,
) {
    init {
        require(width > 0) {
            "width must be > 0, but was $width"
        }
        require(height > 0) {
            "height must be > 0, but was $height"
        }
    }
}
