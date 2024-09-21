package com.morpho.butterfly

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Parcelize
@Serializable
@JvmInline
value class Uri(
    val uri: String,
): Parcelable {
    override fun toString(): String = uri
}


/**
 * The [AT URI scheme](https://atproto.com/specs/at-uri-scheme) (`at://`) makes it easy to reference individual records
 * in a specific repository, identified by either [Did] or [Handle]. AT URIs can also be used to reference a collection
 * within a repository, or an entire repository (aka, an identity).
 */
@Parcelize
@Serializable
@JvmInline
value class AtUri(
    val atUri: String,
): Parcelable {
    constructor(
        id: AtIdentifier,
        app: Nsid,
        record: String = "",
    ) : this("at://${id}/${app}${if (record.isNotEmpty()) "/${record}" else ""}")
    override fun toString(): String = atUri

    val isProfileFeed: Boolean
        get() = isProfileFeed(this)

    val isProfileContent: Boolean
        get() = isProfileContent(this)

    fun toParts(): Result<UriParts> {
        return parseAtUri(atUri)
    }

    companion object {


        fun parseAtUri(uri: String): Result<UriParts> {
            return if (uri.startsWith("at://")) {
                uri.substringAfterLast("at://").split("/").let {
                    if (Did.Regex.matches(it[0])) Result.success(UriParts(Did(it[0]), Nsid(it[1]), it[2]))
                    else if(Handle.Regex.matches(it[0])) Result.success(UriParts(Handle(it[0]), Nsid(it[1]), it[2]))
                    else Result.failure(Error("Invalid identifier: ${it[0]}" ))
                }
            } else if (uri.startsWith("https://bsky.app/")) {
                uri.substringAfter("https://bsky.app/").split("/").let {
                    val collection = when(it[1]) {
                        "post" -> {"app.bsky.feed.post"}
                        "lists" -> {"app.bsky.graph.lists"}
                        "feed" -> {"app.bsky.feed.generator"}
                        else -> return Result.failure(Error("Unhandled collection: ${it[1]}"))
                    }
                    if (Did.Regex.matches(it[0])) Result.success(UriParts(Did(it[0]), Nsid(collection), it[2]))
                    else if(Handle.Regex.matches(it[0])) Result.success(UriParts(Handle(it[0]), Nsid(collection), it[2]))
                    else Result.failure(Error("Invalid identifier: ${it[0]}" ))
                }
            } else {
                return Result.failure(Error("Unhandled URI format: $uri"))
            }
        }

        fun isProfileFeed(uri: AtUri): Boolean {
            return (uri.atUri.matches(ProfilePostsUriRegex) ||
                    uri.atUri.matches(ProfileRepliesUriRegex) ||
                    uri.atUri.matches(ProfileMediaUriRegex) ||
                    uri.atUri.matches(ProfileLikesUriRegex))
        }

        fun isProfileContent(uri: AtUri): Boolean {
            return (uri.atUri.matches(ProfilePostsUriRegex) ||
                    uri.atUri.matches(ProfileRepliesUriRegex) ||
                    uri.atUri.matches(ProfileMediaUriRegex) ||
                    uri.atUri.matches(ProfileLikesUriRegex) ||
                    uri.atUri.matches(ProfileUserListsUriRegex) ||
                    uri.atUri.matches(ProfileModServiceUriRegex) ||
                    uri.atUri.matches(ProfileFeedsListUriRegex))
        }
        fun profileUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.profile")
        }
        fun profilePostsUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.profile.posts")
        }
        fun profileRepliesUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.profile.replies")
        }

        fun profileMediaUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.profile.media")
        }

        fun profileLikesUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.profile.likes")
        }

        fun profileUserListsUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.profile.lists")
        }

        fun profileModServiceUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.profile.labelServices")
        }

        fun profileFeedsListUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.profile.feeds")
        }

        fun followsUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.follows")
        }

        fun followersUri(id: AtIdentifier): AtUri {
            return AtUri("at://${id}/app.morpho.followers")
        }

        fun userListUri(id: AtIdentifier, listId: String): AtUri {
            return AtUri("at://${id}/app.morpho.list/${listId}")
        }

        fun listFeedUri(id: AtIdentifier, listId: String): AtUri {
            return AtUri("at://${id}/app.bsky.graph.list/${listId}")
        }

        fun myUserListUri(listId: String): AtUri {
            return AtUri("at://me/app.morpho.list/${listId}")
        }

        val HOME_URI: AtUri = AtUri("at://app.morpho.home")
        val MY_PROFILE_URI: AtUri = AtUri("at://me/app.morpho.profile")

        val ProfilePostsUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.morpho.profile.posts")
        val ProfileRepliesUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.morpho.profile.replies")
        val ProfileMediaUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.morpho.profile.media")
        val ProfileLikesUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.morpho.profile.likes")
        val ProfileUserListsUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.morpho.profile.lists")
        val ProfileModServiceUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.morpho.profile.labelService")
        val ProfileFeedsListUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.morpho.profile.feeds")
        val ListFeedUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.bsky.graph.list/([a-zA-Z0-9]{64})")
        val FollowsUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.morpho.follows")
        val FollowersUriRegex = Regex("at://(me|${Did.Regex}|${Handle.Regex})/app.morpho.followers")
    }
}

@Parcelize
@Serializable
data class UriParts(
    val repo: AtIdentifier,
    val collection: Nsid,
    val rkey: String,
): Parcelable

/**
 * The AT Protocol uses [Decentralized Identifiers](https://atproto.com/specs/did) (DIDs) as persistent, long-term
 * account identifiers. DID is a W3C standard, with many standardized and proposed DID method implementations.
 */
@Parcelize
@Serializable
@JvmInline
value class Did(
    val did: String,
): AtIdentifier, Parcelable {

    constructor(
        method: String = "plc",
        id: String,
    ): this("did:$method:$id")

    init {
        require(Regex.matches(did)) {
            "'$did' is not a valid DID."
        }
    }

    override fun orEmpty(): String {
        return did
    }

    override fun toString(): String = did

    companion object {
        val Regex = Regex("^did:[a-z]+:[a-zA-Z0-9._:%-]*[a-zA-Z0-9._-]$")
    }
}

/**
 * [Handles](https://atproto.com/specs/handle) are a less-permanent identifier for accounts. The mechanism for verifying
 * the link between an account handle and an account [Did] relies on DNS, and possibly connections to a network host, so
 * every handle must be a valid network hostname. Almost every valid "hostname" is also a valid handle, though there are
 * a small number of exceptions.
 */
@Parcelize
@Serializable
@JvmInline
value class Handle(
    val handle: String,
): AtIdentifier, Parcelable {
    init {
        require(Regex.matches(handle)) {
            "'$handle' is not a valid handle."
        }
    }

    override fun orEmpty(): String {
        return handle
    }

    override fun toString(): String = handle

    companion object {
        val Regex = Regex("^([a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?$")
    }
}

/**
 * A string type which is either a [Did] or a [Handle]. Mostly used in XRPC query parameters. It is unambiguous whether
 * an AtIdentifier is a handle or a DID because a DID always starts with `did:`, and the colon character (`:`) is not
 * an allowed in handles.
 */
@Parcelize
@Serializable(with = AtIdentifierSerializer::class)
sealed interface AtIdentifier: Parcelable {
    abstract fun orEmpty(): String
}

/**
 * [Namespaced Identifiers](https://atproto.com/specs/nsid) (NSIDs) are used to reference Lexicon schemas for records,
 * XRPC endpoints, and more. The basic structure and semantics of an NSID are a fully-qualified hostname in Reverse
 * Domain-Name Order, followed by a simple name. The hostname part is the domain authority, and the final segment is the
 * name.
 */
@Parcelize
@Serializable
@JvmInline
value class Nsid(
    val nsid: String,
): Parcelable {
    init {
        require(Regex.matches(nsid)) {
            "'$nsid' is not a valid namespace identifier."
        }
    }

    val domainAuthority: String
        get() = nsid.substringBeforeLast('.')

    val name: String
        get() = nsid.substringAfterLast('.')

    override fun toString(): String = nsid

    companion object {
        val Regex = Regex(
            "^[a-zA-Z]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+" +
                    "(\\.[a-zA-Z]([a-zA-Z]{0,61}[a-zA-Z])?)$"
        )
    }
}

/**
 * Links are encoded as [IPFS Content Identifiers](https://atproto.com/specs/data-model#link-and-cid-formats) (CIDs),
 * which have both binary and string representations. CIDs include a metadata code which indicates whether it links to a
 * node (DAG-CBOR) or arbitrary binary data.
 */
@Parcelize
@Serializable
@JvmInline
value class Cid(
    val cid: String,
): Parcelable {
    override fun toString(): String = cid
}

/**
 * An [IETF Language Tag](https://en.wikipedia.org/wiki/IETF_language_tag) string, compliant with
 * [BCP 47](https://www.rfc-editor.org/info/bcp47), defined in [RFC 5646](https://www.rfc-editor.org/rfc/rfc5646.txt)
 * ("Tags for Identifying Languages"). This is the same standard used to identify languages in HTTP, HTML, and other web
 * standards. The Lexicon string must validate as a "well-formed" language tag, as defined in the RFC. Clients should
 * ignore language strings which are "well-formed" but not "valid" according to the RFC.
 */
@Parcelize
@Serializable
@JvmInline
value class Language(
    val tag: String,
): Parcelable {
    override fun toString(): String = tag
}
@Parcelize
@JvmInline
@Serializable
public value class MutedWordTarget(
    val mutedWordTarget: String
): Parcelable {
    override fun toString(): String = mutedWordTarget
}

public val mutedWordContent = MutedWordTarget("content")
public val mutedWordTag = MutedWordTarget("tag")