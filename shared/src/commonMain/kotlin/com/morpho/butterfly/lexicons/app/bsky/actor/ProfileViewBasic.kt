package app.bsky.actor

import com.atproto.label.Label
import com.morpho.butterfly.Did
import com.morpho.butterfly.Handle
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
public data class ProfileViewBasic(
  public val did: Did,
  public val handle: Handle,
  public val displayName: String? = null,
  public val avatar: String? = null,
  public val viewer: ViewerState? = null,
  public val associated: ProfileAssociated? = null,
  public val labels: ReadOnlyList<Label> = persistentListOf(),
  public val createdAt: Timestamp? = null, // Datetime
  public val bridgyOriginalDescription: String? = null,
  public val bridgyOriginalUrl: String? = null,
) {
  init {
    require(displayName == null || displayName.count() <= 640) {
      "displayName.count() must be <= 640, but was ${displayName?.count()}"
    }
  }
}

