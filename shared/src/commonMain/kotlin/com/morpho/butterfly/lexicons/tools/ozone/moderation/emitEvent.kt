package tools.ozone.moderation

import com.atproto.admin.RepoRef
import com.atproto.repo.StrongRef
import kotlin.jvm.JvmInline
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Cid
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.valueClassSerializer

@Serializable
public sealed interface EmitEventRequestEventUnion {
  public class TakedownSerializer : KSerializer<Takedown> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventTakedown",
    constructor = ::Takedown,
    valueProvider = Takedown::value,
    valueSerializerProvider = { ModEventTakedown.serializer() },
  )

  @Serializable(with = TakedownSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventTakedown")
  public value class Takedown(
    public val `value`: ModEventTakedown,
  ) : EmitEventRequestEventUnion

  public class AcknowledgeSerializer : KSerializer<Acknowledge> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventAcknowledge",
    constructor = ::Acknowledge,
    valueProvider = Acknowledge::value,
    valueSerializerProvider = { ModEventAcknowledge.serializer() },
  )

  @Serializable(with = AcknowledgeSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventAcknowledge")
  public value class Acknowledge(
    public val `value`: ModEventAcknowledge,
  ) : EmitEventRequestEventUnion

  public class EscalateSerializer : KSerializer<Escalate> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventEscalate",
    constructor = ::Escalate,
    valueProvider = Escalate::value,
    valueSerializerProvider = { ModEventEscalate.serializer() },
  )

  @Serializable(with = EscalateSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEscalate")
  public value class Escalate(
    public val `value`: ModEventEscalate,
  ) : EmitEventRequestEventUnion

  public class CommentSerializer : KSerializer<Comment> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventComment",
    constructor = ::Comment,
    valueProvider = Comment::value,
    valueSerializerProvider = { ModEventComment.serializer() },
  )

  @Serializable(with = CommentSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventComment")
  public value class Comment(
    public val `value`: ModEventComment,
  ) : EmitEventRequestEventUnion

  public class LabelSerializer : KSerializer<Label> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventLabel",
    constructor = ::Label,
    valueProvider = Label::value,
    valueSerializerProvider = { ModEventLabel.serializer() },
  )

  @Serializable(with = LabelSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventLabel")
  public value class Label(
    public val `value`: ModEventLabel,
  ) : EmitEventRequestEventUnion

  public class ReportSerializer : KSerializer<Report> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventReport",
    constructor = ::Report,
    valueProvider = Report::value,
    valueSerializerProvider = { ModEventReport.serializer() },
  )

  @Serializable(with = ReportSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReport")
  public value class Report(
    public val `value`: ModEventReport,
  ) : EmitEventRequestEventUnion

  public class MuteSerializer : KSerializer<Mute> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventMute",
    constructor = ::Mute,
    valueProvider = Mute::value,
    valueSerializerProvider = { ModEventMute.serializer() },
  )

  @Serializable(with = MuteSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventMute")
  public value class Mute(
    public val `value`: ModEventMute,
  ) : EmitEventRequestEventUnion

  public class ReverseTakedownSerializer : KSerializer<ReverseTakedown> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventReverseTakedown",
    constructor = ::ReverseTakedown,
    valueProvider = ReverseTakedown::value,
    valueSerializerProvider = { ModEventReverseTakedown.serializer() },
  )

  @Serializable(with = ReverseTakedownSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventReverseTakedown")
  public value class ReverseTakedown(
    public val `value`: ModEventReverseTakedown,
  ) : EmitEventRequestEventUnion

  public class UnmuteSerializer : KSerializer<Unmute> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventUnmute",
    constructor = ::Unmute,
    valueProvider = Unmute::value,
    valueSerializerProvider = { ModEventUnmute.serializer() },
  )

  @Serializable(with = UnmuteSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventUnmute")
  public value class Unmute(
    public val `value`: ModEventUnmute,
  ) : EmitEventRequestEventUnion

  public class EmailSerializer : KSerializer<Email> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventEmail",
    constructor = ::Email,
    valueProvider = Email::value,
    valueSerializerProvider = { ModEventEmail.serializer() },
  )

  @Serializable(with = EmailSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventEmail")
  public value class Email(
    public val `value`: ModEventEmail,
  ) : EmitEventRequestEventUnion

  public class TagSerializer : KSerializer<Tag> by valueClassSerializer(
    serialName = "tools.ozone.moderation.defs#modEventTag",
    constructor = ::Tag,
    valueProvider = Tag::value,
    valueSerializerProvider = { ModEventTag.serializer() },
  )

  @Serializable(with = TagSerializer::class)
  @JvmInline
  @SerialName("tools.ozone.moderation.defs#modEventTag")
  public value class Tag(
    public val `value`: ModEventTag,
  ) : EmitEventRequestEventUnion
}

@Serializable
public sealed interface EmitEventRequestSubjectUnion {
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
  ) : EmitEventRequestSubjectUnion

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
