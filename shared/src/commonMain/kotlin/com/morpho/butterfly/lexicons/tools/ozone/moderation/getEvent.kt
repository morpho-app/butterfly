package tools.ozone.moderation

import kotlin.Any
import kotlin.Long
import kotlin.Pair
import kotlin.String
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import com.morpho.butterfly.model.ReadOnlyList

@Serializable
public data class GetEventQueryParams(
  public val id: Long,
) {
  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    add("id" to id)
  }.toImmutableList()
}

public typealias GetEventResponse = ModEventViewDetail
