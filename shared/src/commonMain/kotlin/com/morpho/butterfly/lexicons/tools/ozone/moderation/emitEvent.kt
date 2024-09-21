package tools.ozone.moderation

import com.atproto.admin.RepoRef
import com.atproto.repo.StrongRef
import com.morpho.butterfly.Cid
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface EmitEventRequestEventUnion {
  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventTakedown")
  public value class Takedown(
    public val `value`: ModEventTakedown,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventAcknowledge")
  public value class Acknowledge(
    public val `value`: ModEventAcknowledge,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEscalate")
  public value class Escalate(
    public val `value`: ModEventEscalate,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventComment")
  public value class Comment(
    public val `value`: ModEventComment,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventLabel")
  public value class Label(
    public val `value`: ModEventLabel,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReport")
  public value class Report(
    public val `value`: ModEventReport,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventMute")
  public value class Mute(
    public val `value`: ModEventMute,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReverseTakedown")
  public value class ReverseTakedown(
    public val `value`: ModEventReverseTakedown,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventUnmute")
  public value class Unmute(
    public val `value`: ModEventUnmute,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEmail")
  public value class Email(
    public val `value`: ModEventEmail,
  ) : EmitEventRequestEventUnion

  @Serializable
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventTag")
  public value class Tag(
    public val `value`: ModEventTag,
  ) : EmitEventRequestEventUnion
}

@Serializable
public sealed interface EmitEventRequestSubjectUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : EmitEventRequestSubjectUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.repo.strongRef")
  public value class RepoStrongRef(
    public val `value`: StrongRef,
  ) : EmitEventRequestSubjectUnion
}

@Serializable
public data class EmitEventRequest(
  public val event: EmitEventRequestEventUnion,
  public val subject: EmitEventRequestSubjectUnion,
  public val subjectBlobCids: ReadOnlyList<Cid> = persistentListOf(),
  public val createdBy: Did,
)

public typealias EmitEventResponse = ModEventView
