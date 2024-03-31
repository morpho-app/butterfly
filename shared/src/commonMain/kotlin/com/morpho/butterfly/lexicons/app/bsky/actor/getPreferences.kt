package app.bsky.actor

import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.serialization.Serializable

@Serializable
//@SerialName("app.bsky.actor.GetPreferencesResponse")
public data class GetPreferencesResponse(
  public val preferences: ReadOnlyList<PreferencesUnion>,
)
