package tools.ozone.moderation

import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtUri

@Serializable
public data class RecordViewNotFound(
  public val uri: AtUri,
)
