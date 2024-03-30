package tools.ozone.moderation

import com.atproto.admin.RepoRef
import com.atproto.repo.StrongRef
import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface ModEventViewEventUnion {
  public class ModEventTakedownSerializer : KSerializer<ModEventTakedown> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventTakedown",
    constructor = ::ModEventTakedown,
    valueProvider = ModEventTakedown::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventTakedown.serializer() },
  )

  @Serializable(with = ModEventTakedownSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventTakedown")
  public value class ModEventTakedown(
    public val `value`: tools.ozone.moderation.ModEventTakedown,
  ) : ModEventViewEventUnion

  public class ModEventReverseTakedownSerializer : KSerializer<ModEventReverseTakedown> by
      valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventReverseTakedown",
    constructor = ::ModEventReverseTakedown,
    valueProvider = ModEventReverseTakedown::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventReverseTakedown.serializer() },
  )

  @Serializable(with = ModEventReverseTakedownSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReverseTakedown")
  public value class ModEventReverseTakedown(
    public val `value`: tools.ozone.moderation.ModEventReverseTakedown,
  ) : ModEventViewEventUnion

  public class ModEventCommentSerializer : KSerializer<ModEventComment> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventComment",
    constructor = ::ModEventComment,
    valueProvider = ModEventComment::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventComment.serializer() },
  )

  @Serializable(with = ModEventCommentSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventComment")
  public value class ModEventComment(
    public val `value`: tools.ozone.moderation.ModEventComment,
  ) : ModEventViewEventUnion

  public class ModEventReportSerializer : KSerializer<ModEventReport> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventReport",
    constructor = ::ModEventReport,
    valueProvider = ModEventReport::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventReport.serializer() },
  )

  @Serializable(with = ModEventReportSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReport")
  public value class ModEventReport(
    public val `value`: tools.ozone.moderation.ModEventReport,
  ) : ModEventViewEventUnion

  public class ModEventLabelSerializer : KSerializer<ModEventLabel> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventLabel",
    constructor = ::ModEventLabel,
    valueProvider = ModEventLabel::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventLabel.serializer() },
  )

  @Serializable(with = ModEventLabelSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventLabel")
  public value class ModEventLabel(
    public val `value`: tools.ozone.moderation.ModEventLabel,
  ) : ModEventViewEventUnion

  public class ModEventAcknowledgeSerializer : KSerializer<ModEventAcknowledge> by
      valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventAcknowledge",
    constructor = ::ModEventAcknowledge,
    valueProvider = ModEventAcknowledge::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventAcknowledge.serializer() },
  )

  @Serializable(with = ModEventAcknowledgeSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventAcknowledge")
  public value class ModEventAcknowledge(
    public val `value`: tools.ozone.moderation.ModEventAcknowledge,
  ) : ModEventViewEventUnion

  public class ModEventEscalateSerializer : KSerializer<ModEventEscalate> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventEscalate",
    constructor = ::ModEventEscalate,
    valueProvider = ModEventEscalate::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventEscalate.serializer() },
  )

  @Serializable(with = ModEventEscalateSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEscalate")
  public value class ModEventEscalate(
    public val `value`: tools.ozone.moderation.ModEventEscalate,
  ) : ModEventViewEventUnion

  public class ModEventMuteSerializer : KSerializer<ModEventMute> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventMute",
    constructor = ::ModEventMute,
    valueProvider = ModEventMute::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventMute.serializer() },
  )

  @Serializable(with = ModEventMuteSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventMute")
  public value class ModEventMute(
    public val `value`: tools.ozone.moderation.ModEventMute,
  ) : ModEventViewEventUnion

  public class ModEventEmailSerializer : KSerializer<ModEventEmail> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventEmail",
    constructor = ::ModEventEmail,
    valueProvider = ModEventEmail::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventEmail.serializer() },
  )

  @Serializable(with = ModEventEmailSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEmail")
  public value class ModEventEmail(
    public val `value`: tools.ozone.moderation.ModEventEmail,
  ) : ModEventViewEventUnion

  public class ModEventResolveAppealSerializer : KSerializer<ModEventResolveAppeal> by
      valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventResolveAppeal",
    constructor = ::ModEventResolveAppeal,
    valueProvider = ModEventResolveAppeal::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventResolveAppeal.serializer() },
  )

  @Serializable(with = ModEventResolveAppealSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventResolveAppeal")
  public value class ModEventResolveAppeal(
    public val `value`: tools.ozone.moderation.ModEventResolveAppeal,
  ) : ModEventViewEventUnion

  public class ModEventDivertSerializer : KSerializer<ModEventDivert> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventDivert",
    constructor = ::ModEventDivert,
    valueProvider = ModEventDivert::value,
    valueSerializerProvider = { tools.ozone.moderation.ModEventDivert.serializer() },
  )

  @Serializable(with = ModEventDivertSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventDivert")
  public value class ModEventDivert(
    public val `value`: tools.ozone.moderation.ModEventDivert,
  ) : ModEventViewEventUnion
}

@Serializable
public sealed interface ModEventViewSubjectUnion {
  public class AdminRepoRefSerializer : KSerializer<AdminRepoRef> by valueClassSerializer(
    serialName = "com.atproto.admin.defs#repoRef",
    constructor = ::AdminRepoRef,
    valueProvider = AdminRepoRef::value,
    valueSerializerProvider = { RepoRef.serializer() },
  )

  @Serializable(with = AdminRepoRefSerializer::class)
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : ModEventViewSubjectUnion

  public class RepoStrongRefSerializer : KSerializer<RepoStrongRef> by valueClassSerializer(
    serialName = "com.atproto.repo.strongRef",
    constructor = ::RepoStrongRef,
    valueProvider = RepoStrongRef::value,
    valueSerializerProvider = { StrongRef.serializer() },
  )

  @Serializable(with = RepoStrongRefSerializer::class)
  @JvmInline
  @SerialName("com.atproto.repo.strongRef")
  public value class RepoStrongRef(
    public val `value`: StrongRef,
  ) : ModEventViewSubjectUnion
}

@Serializable
public data class ModEventView(
  public val id: Long,
  public val event: ModEventViewEventUnion,
  public val subject: ModEventViewSubjectUnion,
  public val subjectBlobCids: ReadOnlyList<String>,
  public val createdBy: Did,
  public val createdAt: Timestamp,
  public val creatorHandle: String? = null,
  public val subjectHandle: String? = null,
)
