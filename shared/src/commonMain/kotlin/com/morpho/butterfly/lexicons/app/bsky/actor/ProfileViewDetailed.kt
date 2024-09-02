package app.bsky.actor

import com.atproto.label.Label
import com.morpho.butterfly.Did
import com.morpho.butterfly.Handle
import com.morpho.butterfly.lexicons.app.bsky.graph.StarterPackViewBasic
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ProfileViewDetailed(
  public val did: Did,
  public val handle: Handle,
  public val displayName: String? = null,
  public val description: String? = null,
  public val avatar: String? = null,
  public val banner: String? = null,
  public val followersCount: Long? = null,
  public val followsCount: Long? = null,
  public val postsCount: Long? = null,
  public val associated: ProfileAssociated? = null,
  public val joinedViaStarterPack: StarterPackViewBasic? = null,
  public val indexedAt: Timestamp? = null,
  public val createdAt: Timestamp? = null,
  public val viewer: ViewerState? = null,
  public val labels: ReadOnlyList<Label> = persistentListOf(),
) {
  init {
    require(displayName == null || displayName.count() <= 640) {
      "displayName.count() must be <= 640, but was ${displayName?.count()}"
    }
    require(description == null || description.count() <= 2_560) {
      "description.count() must be <= 2_560, but was ${description?.count()}"
    }
  }
}


@Serializable
@SerialName("app.bsky.actor.defs#profileAssociated")
public data class ProfileAssociated(
  public val lists: Long? = null,
  public val feedGens: Long? = null,
  public val labeler: Boolean? = null,
  public val starterPacks: Long? = null,
  public val chat: ProfileAssociatedChat? = null,
)

@Serializable
@SerialName("app.bsky.actor.defs#profileAssociatedChat")
public data class ProfileAssociatedChat(
  public val allowIncoming: AllowIncoming,
)

@Serializable
public enum class AllowIncoming(val value: String) {
  @SerialName("all")
  All("all"),
  @SerialName("none")
  None("none"),
  @SerialName("following")
  Following("following"),
}
