package app.bsky.unspecced

import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Uri

@Serializable
public data class GetTaggedSuggestionsSuggestion(
  public val tag: String,
  public val subjectType: GetTaggedSuggestionsSubjectType,
  public val subject: Uri,
)
