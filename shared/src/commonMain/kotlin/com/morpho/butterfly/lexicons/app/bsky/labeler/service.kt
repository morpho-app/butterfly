package app.bsky.labeler

import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface ServiceLabelsUnion {
  public class SelfLabelsSerializer : KSerializer<SelfLabels> by valueClassSerializer(
    serialName = "com.atproto.label.defs#selfLabels",
    constructor = ::SelfLabels,
    valueProvider = SelfLabels::value,
    valueSerializerProvider = { com.atproto.label.SelfLabels.serializer() },
  )

  @Serializable(with = SelfLabelsSerializer::class)
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
