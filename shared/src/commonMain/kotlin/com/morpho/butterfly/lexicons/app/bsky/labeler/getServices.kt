package app.bsky.labeler

import kotlin.Any
import kotlin.Boolean
import kotlin.Pair
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface GetServicesResponseViewUnion {
  public class LabelerViewSerializer : KSerializer<LabelerView> by valueClassSerializer(
    serialName = "app.bsky.labeler.defs#labelerView",
    constructor = ::LabelerView,
    valueProvider = LabelerView::value,
    valueSerializerProvider = { app.bsky.labeler.LabelerView.serializer() },
  )

  @Serializable(with = LabelerViewSerializer::class)
  @JvmInline
  @SerialName("app.bsky.labeler.defs#labelerView")
  public value class LabelerView(
    public val `value`: app.bsky.labeler.LabelerView,
  ) : GetServicesResponseViewUnion

  public class LabelerViewDetailedSerializer : KSerializer<LabelerViewDetailed> by
      valueClassSerializer(
    serialName = "app.bsky.labeler.defs#labelerViewDetailed",
    constructor = ::LabelerViewDetailed,
    valueProvider = LabelerViewDetailed::value,
    valueSerializerProvider = { app.bsky.labeler.LabelerViewDetailed.serializer() },
  )

  @Serializable(with = LabelerViewDetailedSerializer::class)
  @JvmInline
  @SerialName("app.bsky.labeler.defs#labelerViewDetailed")
  public value class LabelerViewDetailed(
    public val `value`: app.bsky.labeler.LabelerViewDetailed,
  ) : GetServicesResponseViewUnion
}

@Serializable
public data class GetServicesQuery(
  public val dids: ReadOnlyList<Did>,
  public val detailed: Boolean? = false,
) {
  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    dids.forEach {
      add("dids" to it)
    }
    add("detailed" to detailed)
  }.toImmutableList()
}

@Serializable
public data class GetServicesResponse(
  public val views: ReadOnlyList<GetServicesResponseViewUnion>,
)
