package app.bsky.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("app.bsky.actor.defs#threadViewPref")
public data class ThreadViewPref(
  /**
   * Sorting mode.
   */
  public val sort: Sort? = null,
  /**
   * Show followed users at the top of all replies.
   */
  public val prioritizeFollowedUsers: Boolean? = null,
)
