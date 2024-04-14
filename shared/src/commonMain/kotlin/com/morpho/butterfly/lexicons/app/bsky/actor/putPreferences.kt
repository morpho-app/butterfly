package app.bsky.actor

import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.serialization.Serializable

@Serializable
public data class PutPreferencesRequest(
  public val preferences: ReadOnlyList<PreferencesUnion>,
)
