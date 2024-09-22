package app.bsky.actor

import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("app.bsky.actor.defs#personalDetailsPref")
public data class PersonalDetailsPref(
  /**
   * The birth date of the owner of the account.
   */
  public val birthDate: Timestamp? = null,
)

