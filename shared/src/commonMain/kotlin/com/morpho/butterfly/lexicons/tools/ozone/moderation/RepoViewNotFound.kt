package tools.ozone.moderation

import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did

@Serializable
public data class RepoViewNotFound(
  public val did: Did,
)
