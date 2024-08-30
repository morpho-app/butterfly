package app.bsky.embed

import com.morpho.butterfly.AtUri
import kotlinx.serialization.Serializable

@Serializable
public data class RecordViewDetached(
    public val uri: AtUri,
    public val detached: Boolean,
)
