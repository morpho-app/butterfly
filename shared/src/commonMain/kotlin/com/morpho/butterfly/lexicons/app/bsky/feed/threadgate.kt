package app.bsky.feed

import com.morpho.butterfly.AtUri
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface ThreadgateAllowUnion {
  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.threadgate#mentionRule")
  public value class MentionRule(
    public val `value`: ThreadgateMentionRule,
  ) : ThreadgateAllowUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.threadgate#followingRule")
  public value class FollowingRule(
    public val `value`: ThreadgateFollowingRule,
  ) : ThreadgateAllowUnion

  @Serializable
  @JvmInline
  @SerialName("app.bsky.feed.threadgate#listRule")
  public value class ListRule(
    public val `value`: ThreadgateListRule,
  ) : ThreadgateAllowUnion
}

@Serializable
public data class Threadgate(
  public val post: AtUri,
  public val allow: ReadOnlyList<ThreadgateAllowUnion> = persistentListOf(),
  public val createdAt: Timestamp,
) {
  init {
    require(allow.count() <= 5) {
      "allow.count() must be <= 5, but was ${allow.count()}"
    }
  }
}
