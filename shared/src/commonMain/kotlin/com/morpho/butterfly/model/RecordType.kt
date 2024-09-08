package com.morpho.butterfly.model


import com.atproto.repo.StrongRef
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Did
import com.morpho.butterfly.Nsid
import kotlinx.serialization.Serializable

sealed interface RecordUnion {
    val type: RecordType

    @Serializable
    data class MakePost(
        val post: app.bsky.feed.Post
    ) : RecordUnion {
        override val type = RecordType.Post
    }

    @Serializable
    data class Like(val subject: StrongRef) : RecordUnion {
        override val type = RecordType.Like
    }

    @Serializable
    data class Repost(val subject: StrongRef) : RecordUnion {
        override val type = RecordType.Repost
    }

    data class Block(val subject: Did) : RecordUnion {
        override val type = RecordType.Block
    }

    data class Follow(val subject: Did) : RecordUnion {
        override val type = RecordType.Follow
    }

    data class ListBlock(val subject: AtUri) : RecordUnion {
        override val type = RecordType.ListBlock
    }

}

@Suppress("SpellCheckingInspection")
enum class RecordType(val collection: Nsid) {
    Post(Nsid("app.bsky.feed.post")),
    Like(Nsid("app.bsky.feed.like")),
    Repost(Nsid("app.bsky.feed.repost")),
    Block(Nsid("app.bsky.graph.block")),
    Follow(Nsid("app.bsky.graph.follow")),
    ListBlock(Nsid("app.bsky.graph.listblock")),
}