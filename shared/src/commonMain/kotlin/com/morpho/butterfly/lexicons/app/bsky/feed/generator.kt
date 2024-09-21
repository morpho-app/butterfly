package app.bsky.feed

import app.bsky.richtext.Facet
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@Serializable
public sealed interface GeneratorLabelsUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.label.defs#selfLabels")
  public value class SelfLabels(
    public val `value`: com.atproto.label.SelfLabels,
  ) : GeneratorLabelsUnion
}

@Serializable
public data class Generator(
  public val did: Did,
  public val displayName: String,
  public val description: String? = null,
  public val descriptionFacets: ReadOnlyList<Facet> = persistentListOf(),
  public val avatar: JsonElement? = null,
  public val createdAt: Timestamp,
  public val acceptsInteractions: Boolean? = null,
  public val labels: GeneratorLabelsUnion? = null,
) {
  init {
    require(displayName.count() <= 240) {
      "displayName.count() must be <= 240, but was ${displayName.count()}"
    }
    require(description == null || description.count() <= 3_000) {
      "description.count() must be <= 3_000, but was ${description?.count()}"
    }
  }
}
