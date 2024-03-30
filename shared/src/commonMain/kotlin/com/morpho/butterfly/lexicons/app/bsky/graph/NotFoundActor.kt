package app.bsky.graph

import kotlin.Boolean
import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtIdentifier

/**
 * indicates that a handle or DID could not be resolved
 */
@Serializable
public data class NotFoundActor(
  public val actor: AtIdentifier,
  public val notFound: Boolean,
)
