package app.bsky.feed

import app.bsky.richtext.Facet
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface GeneratorLabelsUnion {
  public class SelfLabelsSerializer : KSerializer<SelfLabels> by valueClassSerializer(
    serialName = "com.atproto.label.defs#selfLabels",
    constructor = ::SelfLabels,
    valueProvider = SelfLabels::value,
    valueSerializerProvider = { com.atproto.label.SelfLabels.serializer() },
  )

  @Serializable(with = SelfLabelsSerializer::class)
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
  public val labels: GeneratorLabelsUnion? = null,
  public val createdAt: Timestamp,
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
