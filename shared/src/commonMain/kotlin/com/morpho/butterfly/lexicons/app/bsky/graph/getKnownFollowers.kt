package app.bsky.graph

import app.bsky.actor.ProfileView
import com.morpho.butterfly.AtIdentifier
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
public data class GetKnownFollowersQuery(
    public val actor: AtIdentifier,
    public val limit: Long? = 50,
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
        add("actor" to actor)
        add("limit" to limit)
        add("cursor" to cursor)
    }.toImmutableList()
}

@Serializable
public data class GetKnownFollowersResponse(
    public val subject: ProfileView,
    public val cursor: String? = null,
    public val followers: ReadOnlyList<ProfileView>,
)
