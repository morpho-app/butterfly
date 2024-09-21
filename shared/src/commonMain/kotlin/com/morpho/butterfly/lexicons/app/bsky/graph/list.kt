package app.bsky.graph

import app.bsky.richtext.Facet
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@Serializable
public sealed interface ListLabelsUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.label.defs#selfLabels")
  public value class SelfLabels(
    public val `value`: com.atproto.label.SelfLabels,
  ) : ListLabelsUnion
}

@Serializable
public data class List(
    public val purpose: ListType,
    public val name: String,
    public val description: String? = null,
    public val descriptionFacets: ReadOnlyList<Facet> = persistentListOf(),
    public val avatar: JsonElement? = null,
    public val labels: ListLabelsUnion? = null,
    public val createdAt: Timestamp,
) {
  init {
    require(name.isNotEmpty()) {
      "name.count() must be >= 1, but was ${name.count()}"
    }
    require(name.count() <= 64) {
      "name.count() must be <= 64, but was ${name.count()}"
    }
    require(description == null || description.count() <= 3_000) {
      "description.count() must be <= 3_000, but was ${description?.count()}"
    }
  }
}
