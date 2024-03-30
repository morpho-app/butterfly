package app.bsky.unspecced

import kotlinx.serialization.SerialName

public enum class GetTaggedSuggestionsSubjectType {
  @SerialName("actor")
  ACTOR,
  @SerialName("feed")
  FEED,
}
