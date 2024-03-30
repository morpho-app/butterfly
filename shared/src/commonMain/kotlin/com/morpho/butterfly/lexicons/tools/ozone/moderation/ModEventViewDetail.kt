package tools.ozone.moderation

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface ModEventViewDetailEventUnion {
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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion

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
  ) : ModEventViewDetailEventUnion
}

@Serializable
public sealed interface ModEventViewDetailSubjectUnion {
  public class RepoViewSerializer : KSerializer<RepoView> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#repoView",
    constructor = ::RepoView,
    valueProvider = RepoView::value,
    valueSerializerProvider = { tools.ozone.moderation.RepoView.serializer() },
  )

  @Serializable(with = RepoViewSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#repoView")
  public value class RepoView(
    public val `value`: tools.ozone.moderation.RepoView,
  ) : ModEventViewDetailSubjectUnion

  public class RepoViewNotFoundSerializer : KSerializer<RepoViewNotFound> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#repoViewNotFound",
    constructor = ::RepoViewNotFound,
    valueProvider = RepoViewNotFound::value,
    valueSerializerProvider = { tools.ozone.moderation.RepoViewNotFound.serializer() },
  )

  @Serializable(with = RepoViewNotFoundSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#repoViewNotFound")
  public value class RepoViewNotFound(
    public val `value`: tools.ozone.moderation.RepoViewNotFound,
  ) : ModEventViewDetailSubjectUnion

  public class RecordViewSerializer : KSerializer<RecordView> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#recordView",
    constructor = ::RecordView,
    valueProvider = RecordView::value,
    valueSerializerProvider = { tools.ozone.moderation.RecordView.serializer() },
  )

  @Serializable(with = RecordViewSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#recordView")
  public value class RecordView(
    public val `value`: tools.ozone.moderation.RecordView,
  ) : ModEventViewDetailSubjectUnion

  public class RecordViewNotFoundSerializer : KSerializer<RecordViewNotFound> by
      valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#recordViewNotFound",
    constructor = ::RecordViewNotFound,
    valueProvider = RecordViewNotFound::value,
    valueSerializerProvider = { tools.ozone.moderation.RecordViewNotFound.serializer() },
  )

  @Serializable(with = RecordViewNotFoundSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#recordViewNotFound")
  public value class RecordViewNotFound(
    public val `value`: tools.ozone.moderation.RecordViewNotFound,
  ) : ModEventViewDetailSubjectUnion
}

@Serializable
public data class ModEventViewDetail(
  public val id: Long,
  public val event: ModEventViewDetailEventUnion,
  public val subject: ModEventViewDetailSubjectUnion,
  public val subjectBlobs: ReadOnlyList<BlobView>,
  public val createdBy: Did,
  public val createdAt: Timestamp,
)
