package tools.ozone.moderation

import kotlin.Any
import kotlin.Boolean
import kotlin.Long
import kotlin.Pair
import kotlin.String
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did
import com.morpho.butterfly.Uri
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp

@Serializable
public data class QueryEventsQueryParams(
  /**
   * The types of events (fully qualified string in the format of
   * tools.ozone.moderation.defs#modEvent<name>) to filter by. If not specified, all events are
   * returned.
   */
  public val types: ReadOnlyList<String> = persistentListOf(),
  public val createdBy: Did? = null,
  /**
   * Sort direction for the events. Defaults to descending order of created at timestamp.
   */
  public val sortDirection: String? = "desc",
  /**
   * Retrieve events created after a given timestamp
   */
  public val createdAfter: Timestamp? = null,
  /**
   * Retrieve events created before a given timestamp
   */
  public val createdBefore: Timestamp? = null,
  public val subject: Uri? = null,
  /**
   * If true, events on all record types (posts, lists, profile etc.) owned by the did are returned
   */
  public val includeAllUserRecords: Boolean? = false,
  public val limit: Long? = 50,
  /**
   * If true, only events with comments are returned
   */
  public val hasComment: Boolean? = null,
  /**
   * If specified, only events with comments containing the keyword are returned
   */
  public val comment: String? = null,
  /**
   * If specified, only events where all of these labels were added are returned
   */
  public val addedLabels: ReadOnlyList<String> = persistentListOf(),
  /**
   * If specified, only events where all of these labels were removed are returned
   */
  public val removedLabels: ReadOnlyList<String> = persistentListOf(),
  /**
   * If specified, only events where all of these tags were added are returned
   */
  public val addedTags: ReadOnlyList<String> = persistentListOf(),
  /**
   * If specified, only events where all of these tags were removed are returned
   */
  public val removedTags: ReadOnlyList<String> = persistentListOf(),
  public val reportTypes: ReadOnlyList<String> = persistentListOf(),
  public val cursor: String? = null,
) {
  init {
    require(limit == null || limit >= 1) {
      "limit must be >= 1, but was $limit"
    }
    require(limit == null || limit <= 100) {
      "limit must be <= 100, but was $limit"
    }
  }

  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    types.forEach {
      add("types" to it)
    }
    add("createdBy" to createdBy)
    add("sortDirection" to sortDirection)
    add("createdAfter" to createdAfter)
    add("createdBefore" to createdBefore)
    add("subject" to subject)
    add("includeAllUserRecords" to includeAllUserRecords)
    add("limit" to limit)
    add("hasComment" to hasComment)
    add("comment" to comment)
    addedLabels.forEach {
      add("addedLabels" to it)
    }
    removedLabels.forEach {
      add("removedLabels" to it)
    }
    addedTags.forEach {
      add("addedTags" to it)
    }
    removedTags.forEach {
      add("removedTags" to it)
    }
    reportTypes.forEach {
      add("reportTypes" to it)
    }
    add("cursor" to cursor)
  }.toImmutableList()
}

@Serializable
public data class QueryEventsResponse(
  public val cursor: String? = null,
  public val events: ReadOnlyList<ModEventView>,
)
