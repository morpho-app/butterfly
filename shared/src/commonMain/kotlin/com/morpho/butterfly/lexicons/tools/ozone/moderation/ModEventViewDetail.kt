package tools.ozone.moderation

import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface ModEventViewDetailEventUnion {
  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventTakedown")
  public value class ModEventTakedown(
    public val `value`: tools.ozone.moderation.ModEventTakedown,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReverseTakedown")
  public value class ModEventReverseTakedown(
    public val `value`: tools.ozone.moderation.ModEventReverseTakedown,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventComment")
  public value class ModEventComment(
    public val `value`: tools.ozone.moderation.ModEventComment,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReport")
  public value class ModEventReport(
    public val `value`: tools.ozone.moderation.ModEventReport,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventLabel")
  public value class ModEventLabel(
    public val `value`: tools.ozone.moderation.ModEventLabel,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventAcknowledge")
  public value class ModEventAcknowledge(
    public val `value`: tools.ozone.moderation.ModEventAcknowledge,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEscalate")
  public value class ModEventEscalate(
    public val `value`: tools.ozone.moderation.ModEventEscalate,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventMute")
  public value class ModEventMute(
    public val `value`: tools.ozone.moderation.ModEventMute,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEmail")
  public value class ModEventEmail(
    public val `value`: tools.ozone.moderation.ModEventEmail,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventResolveAppeal")
  public value class ModEventResolveAppeal(
    public val `value`: tools.ozone.moderation.ModEventResolveAppeal,
  ) : ModEventViewDetailEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventDivert")
  public value class ModEventDivert(
    public val `value`: tools.ozone.moderation.ModEventDivert,
  ) : ModEventViewDetailEventUnion
}

@Serializable
public sealed interface ModEventViewDetailSubjectUnion {
  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#repoView")
  public value class RepoView(
    public val `value`: tools.ozone.moderation.RepoView,
  ) : ModEventViewDetailSubjectUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#repoViewNotFound")
  public value class RepoViewNotFound(
    public val `value`: tools.ozone.moderation.RepoViewNotFound,
  ) : ModEventViewDetailSubjectUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#recordView")
  public value class RecordView(
    public val `value`: tools.ozone.moderation.RecordView,
  ) : ModEventViewDetailSubjectUnion

  @Serializable
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
