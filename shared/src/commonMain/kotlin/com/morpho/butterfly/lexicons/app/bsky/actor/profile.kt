package app.bsky.actor

import com.atproto.repo.StrongRef
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@Serializable

public sealed interface ProfileLabelsUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.label.defs#selfLabels")
  public value class SelfLabels(
    public val `value`: com.atproto.label.SelfLabels,
  ) : ProfileLabelsUnion
}

@Serializable
public data class Profile(
  public val displayName: String? = null,
  public val description: String? = null,
  public val avatar: JsonElement? = null,
  public val banner: JsonElement? = null,
  public val labels: ProfileLabelsUnion? = null,
  public val joinedViaStarterPack: StrongRef? = null,
  public val createdAt: Timestamp? = null,
  public val bridgyOriginalDescription: String? = null,
  public val bridgyOriginalUrl: String? = null,
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
