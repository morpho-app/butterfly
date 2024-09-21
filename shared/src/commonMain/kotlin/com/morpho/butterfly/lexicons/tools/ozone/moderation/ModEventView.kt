package tools.ozone.moderation

import com.atproto.admin.RepoRef
import com.atproto.repo.StrongRef
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface ModEventViewEventUnion {
  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventTakedown")
  public value class ModEventTakedown(
    public val `value`: tools.ozone.moderation.ModEventTakedown,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReverseTakedown")
  public value class ModEventReverseTakedown(
    public val `value`: tools.ozone.moderation.ModEventReverseTakedown,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventComment")
  public value class ModEventComment(
    public val `value`: tools.ozone.moderation.ModEventComment,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReport")
  public value class ModEventReport(
    public val `value`: tools.ozone.moderation.ModEventReport,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventLabel")
  public value class ModEventLabel(
    public val `value`: tools.ozone.moderation.ModEventLabel,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventAcknowledge")
  public value class ModEventAcknowledge(
    public val `value`: tools.ozone.moderation.ModEventAcknowledge,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEscalate")
  public value class ModEventEscalate(
    public val `value`: tools.ozone.moderation.ModEventEscalate,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventMute")
  public value class ModEventMute(
    public val `value`: tools.ozone.moderation.ModEventMute,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEmail")
  public value class ModEventEmail(
    public val `value`: tools.ozone.moderation.ModEventEmail,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventResolveAppeal")
  public value class ModEventResolveAppeal(
    public val `value`: tools.ozone.moderation.ModEventResolveAppeal,
  ) : ModEventViewEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventDivert")
  public value class ModEventDivert(
    public val `value`: tools.ozone.moderation.ModEventDivert,
  ) : ModEventViewEventUnion
}

@Serializable
public sealed interface ModEventViewSubjectUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : ModEventViewSubjectUnion

  @Serializable
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
