package app.bsky.actor

import com.morpho.butterfly.Did
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("app.bsky.actor.defs#contentLabelPref")
public data class ContentLabelPref(
  public val labelerDid: Did? = null,
  public val label: String,
  public val visibility: Visibility,
)