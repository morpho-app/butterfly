package app.bsky.feed

import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
public data class GetQuotesQuery(
    public val uri: AtUri,
    public val cid: Cid? = null,
    public val limit: Long? = 50,
    public val cursor: String? = null,
) {
    init {
        require(limit == null || limit <= 100) {
            "limit must be <= 100, but was $limit"
        }
    }

    public fun asList(): ReadOnlyList<Pair<String, Any?>> = listOf(
        "uri" to uri,
        "cid" to cid,
        "limit" to limit,
        "cursor" to cursor,
    ).toImmutableList()

}

@Serializable
public data class GetQuotesResponse(
    public val uri: AtUri,
    public val cid: Cid? = null,
    public val cursor: String? = null,
    public val posts: ReadOnlyList<PostView>,
)
