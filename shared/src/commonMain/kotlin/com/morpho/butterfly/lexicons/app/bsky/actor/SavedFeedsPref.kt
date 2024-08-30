package app.bsky.actor

import com.morpho.butterfly.AtUri
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SavedFeedsPref(
  public val pinned: ReadOnlyList<AtUri>,
  public val saved: ReadOnlyList<AtUri>,
  public val timelineIndex: Int? = null,
)

@Serializable
public data class SavedFeedsPrefV2(
  public val items: ReadOnlyList<SavedFeed>,
)

@Serializable
@SerialName("savedFeed")
public data class SavedFeed(
  public val id: String,
  public val type: FeedType,
  public val value: String,
  public val pinned: Boolean,
)

public enum class FeedType {
  @SerialName("feed")
  FEED,
  @SerialName("list")
  LIST,
  @SerialName("timeline")
  TIMELINE,
}