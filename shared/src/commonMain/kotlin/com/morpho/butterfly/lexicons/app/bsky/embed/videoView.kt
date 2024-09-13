package app.bsky.embed

import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.Language
import com.morpho.butterfly.model.Blob
import com.morpho.butterfly.model.ReadOnlyList
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VideoView(
    public val video: Blob,
    public val captions: ReadOnlyList<VideoCaption>? = null,
    public val alt: String? = null,
    public val aspectRatio: AspectRatio? = null,
) {
    init {
        require(alt == null || alt.count() <= 10000) {
            "alt.length must be <= 10000, but was ${alt?.length}"
        }
        when (video) {
            is Blob.StandardBlob -> require(video.mimeType == "video/mp4") {
                "file.mimeType must be 'text/vtt', but was ${video.mimeType}"
            }

            is Blob.LegacyBlob -> require(video.mimeType == "video/mp4") {
                "file.mimeType must be 'text/vtt', but was ${video.mimeType}"
            }
        }
    }
}

@Parcelize
@Serializable
@SerialName("caption")
public data class VideoCaption(
    public val lang: Language,
    public val file: Blob
): Parcelable {
    init {
        when (file) {
            is Blob.StandardBlob -> require(file.mimeType == "text/vtt") {
                "file.mimeType must be 'text/vtt', but was ${file.mimeType}"
            }

            is Blob.LegacyBlob -> require(file.mimeType == "text/vtt") {
                "file.mimeType must be 'text/vtt', but was ${file.mimeType}"
            }
        }
    }
}

@Parcelize
@Serializable
@SerialName("view")
public data class VideoViewVideo(
    public val cid: Cid,
    public val playlist: AtUri,
    public val thumbnail: AtUri,
    public val alt: String? = null,
    public val aspectRatio: AspectRatio? = null,
): Parcelable {
    init {
        require(alt == null || alt.count() <= 10000) {
            "alt.length must be <= 10000, but was ${alt?.length}"
        }
    }
}