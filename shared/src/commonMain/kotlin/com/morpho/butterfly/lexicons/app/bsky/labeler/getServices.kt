package app.bsky.labeler

import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface GetServicesResponseViewUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.labeler.defs#labelerView")
  public value class LabelerView(
    public val `value`: app.bsky.labeler.LabelerView,
  ) : GetServicesResponseViewUnion

  @Serializable
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
