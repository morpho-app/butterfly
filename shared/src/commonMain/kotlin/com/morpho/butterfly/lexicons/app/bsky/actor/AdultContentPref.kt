package app.bsky.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("app.bsky.actor.defs#adultContentPref")
public data class AdultContentPref(
  public val enabled: Boolean,
)
