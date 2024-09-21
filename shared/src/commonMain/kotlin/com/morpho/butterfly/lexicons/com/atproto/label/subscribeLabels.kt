package com.atproto.label

import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface SubscribeLabelsMessageUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.label.subscribeLabels#labels")
  public value class Labels(
    public val `value`: SubscribeLabelsLabels,
  ) : SubscribeLabelsMessageUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.label.subscribeLabels#info")
  public value class Info(
    public val `value`: SubscribeLabelsInfo,
  ) : SubscribeLabelsMessageUnion
}


@Serializable
public data class SubscribeLabelsQuery(
  /**
   * The last known event to backfill from.
   */
  public val cursor: Long? = null,
) {
  public fun asList(): ReadOnlyList<Pair<String, Any?>> = buildList {
    add("cursor" to cursor)
  }.toImmutableList()
}

public typealias SubscribeLabelsMessage = SubscribeLabelsMessageUnion
