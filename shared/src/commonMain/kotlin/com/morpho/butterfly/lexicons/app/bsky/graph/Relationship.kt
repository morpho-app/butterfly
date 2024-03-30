package app.bsky.graph

import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Did

/**
 * lists the bi-directional graph relationships between one actor (not indicated in the object), and
 * the target actors (the DID included in the object)
 */
@Serializable
public data class Relationship(
  public val did: Did,
  /**
   * if the actor follows this DID, this is the AT-URI of the follow record
   */
  public val following: AtUri? = null,
  /**
   * if the actor is followed by this DID, contains the AT-URI of the follow record
   */
  public val followedBy: AtUri? = null,
)
