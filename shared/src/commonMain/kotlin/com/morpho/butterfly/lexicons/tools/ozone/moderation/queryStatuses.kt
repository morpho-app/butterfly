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
public data class QueryStatusesQueryParams(
  public val subject: Uri? = null,
  /**
   * Search subjects by keyword from comments
   */
  public val comment: String? = null,
  /**
   * Search subjects reported after a given timestamp
   */
  public val reportedAfter: Timestamp? = null,
  /**
   * Search subjects reported before a given timestamp
   */
  public val reportedBefore: Timestamp? = null,
  /**
   * Search subjects reviewed after a given timestamp
   */
  public val reviewedAfter: Timestamp? = null,
  /**
   * Search subjects reviewed before a given timestamp
   */
  public val reviewedBefore: Timestamp? = null,
  /**
   * By default, we don't include muted subjects in the results. Set this to true to include them.
   */
  public val includeMuted: Boolean? = null,
  /**
   * Specify when fetching subjects in a certain state
   */
  public val reviewState: String? = null,
  public val ignoreSubjects: ReadOnlyList<Uri> = persistentListOf(),
  /**
   * Get all subject statuses that were reviewed by a specific moderator
   */
  public val lastReviewedBy: Did? = null,
  public val sortField: String? = "lastReportedAt",
  public val sortDirection: String? = "desc",
  /**
   * Get subjects that were taken down
   */
  public val takendown: Boolean? = null,
  /**
   * Get subjects in unresolved appealed status
   */
  public val appealed: Boolean? = null,
  public val limit: Long? = 50,
  public val tags: ReadOnlyList<String> = persistentListOf(),
  public val excludeTags: ReadOnlyList<String> = persistentListOf(),
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
    add("subject" to subject)
    add("comment" to comment)
    add("reportedAfter" to reportedAfter)
    add("reportedBefore" to reportedBefore)
    add("reviewedAfter" to reviewedAfter)
    add("reviewedBefore" to reviewedBefore)
    add("includeMuted" to includeMuted)
    add("reviewState" to reviewState)
    ignoreSubjects.forEach {
      add("ignoreSubjects" to it)
    }
    add("lastReviewedBy" to lastReviewedBy)
    add("sortField" to sortField)
    add("sortDirection" to sortDirection)
    add("takendown" to takendown)
    add("appealed" to appealed)
    add("limit" to limit)
    tags.forEach {
      add("tags" to it)
    }
    excludeTags.forEach {
      add("excludeTags" to it)
    }
    add("cursor" to cursor)
  }.toImmutableList()
}

@Serializable
public data class QueryStatusesResponse(
  public val cursor: String? = null,
  public val subjectStatuses: ReadOnlyList<SubjectStatusView>,
)
