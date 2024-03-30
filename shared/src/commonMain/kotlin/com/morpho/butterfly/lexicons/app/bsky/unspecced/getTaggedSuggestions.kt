package app.bsky.unspecced

import kotlinx.serialization.Serializable
import com.morpho.butterfly.model.ReadOnlyList

@Serializable
public data class GetTaggedSuggestionsResponse(
  public val suggestions: ReadOnlyList<GetTaggedSuggestionsSuggestion>,
)
