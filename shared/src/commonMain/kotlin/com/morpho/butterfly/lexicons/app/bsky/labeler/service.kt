package app.bsky.labeler

import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface ServiceLabelsUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.label.defs#selfLabels")
  public value class SelfLabels(
    public val `value`: com.atproto.label.SelfLabels,
  ) : ServiceLabelsUnion
}

@Serializable
public data class Service(
  public val policies: LabelerPolicies,
  public val labels: ServiceLabelsUnion? = null,
  public val createdAt: Timestamp,
)
