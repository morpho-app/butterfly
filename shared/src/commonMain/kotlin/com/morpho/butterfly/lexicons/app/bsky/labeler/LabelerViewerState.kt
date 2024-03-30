package app.bsky.labeler

import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtUri

@Serializable
public data class LabelerViewerState(
  public val like: AtUri? = null,
)
