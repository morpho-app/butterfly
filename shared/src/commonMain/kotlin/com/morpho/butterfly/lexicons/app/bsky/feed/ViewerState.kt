package app.bsky.feed

import com.morpho.butterfly.AtUri
import kotlinx.serialization.Serializable


@Serializable
public data class ViewerState(
  public val repost: AtUri? = null,
  public val like: AtUri? = null,
  public val threadMuted: Boolean? = null,
  public val replyDisabled: Boolean? = null,
  public val embeddingDisabled: Boolean? = null,
)
