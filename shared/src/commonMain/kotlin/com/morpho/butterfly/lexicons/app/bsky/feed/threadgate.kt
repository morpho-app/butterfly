package app.bsky.feed

import kotlin.jvm.JvmInline
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface ThreadgateAllowUnion {
  public class MentionRuleSerializer : KSerializer<MentionRule> by valueClassSerializer(
    serialName = "app.bsky.feed.threadgate#mentionRule",
    constructor = ::MentionRule,
    valueProvider = MentionRule::value,
    valueSerializerProvider = { ThreadgateMentionRule.serializer() },
  )

  @Serializable(with = MentionRuleSerializer::class)
  @JvmInline
  @SerialName("app.bsky.feed.threadgate#mentionRule")
  public value class MentionRule(
    public val `value`: ThreadgateMentionRule,
  ) : ThreadgateAllowUnion

  public class FollowingRuleSerializer : KSerializer<FollowingRule> by valueClassSerializer(
    serialName = "app.bsky.feed.threadgate#followingRule",
    constructor = ::FollowingRule,
    valueProvider = FollowingRule::value,
    valueSerializerProvider = { ThreadgateFollowingRule.serializer() },
  )

  @Serializable(with = FollowingRuleSerializer::class)
  @JvmInline
  @SerialName("app.bsky.feed.threadgate#followingRule")
  public value class FollowingRule(
    public val `value`: ThreadgateFollowingRule,
  ) : ThreadgateAllowUnion

  public class ListRuleSerializer : KSerializer<ListRule> by valueClassSerializer(
    serialName = "app.bsky.feed.threadgate#listRule",
    constructor = ::ListRule,
    valueProvider = ListRule::value,
    valueSerializerProvider = { ThreadgateListRule.serializer() },
  )

  @Serializable(with = ListRuleSerializer::class)
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
