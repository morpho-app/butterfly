package tools.ozone.moderation

import com.atproto.admin.RepoRef
import com.atproto.repo.StrongRef
import com.morpho.butterfly.Cid
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface SubjectStatusViewSubjectUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.admin.defs#repoRef")
  public value class AdminRepoRef(
    public val `value`: RepoRef,
  ) : SubjectStatusViewSubjectUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.repo.strongRef")
  public value class RepoStrongRef(
    public val `value`: StrongRef,
  ) : SubjectStatusViewSubjectUnion
}

@Serializable
public data class SubjectStatusView(
  public val id: Long,
  public val subject: SubjectStatusViewSubjectUnion,
  public val subjectBlobCids: ReadOnlyList<Cid> = persistentListOf(),
  public val subjectRepoHandle: String? = null,
  /**
   * Timestamp referencing when the last update was made to the moderation status of the subject
   */
  public val updatedAt: Timestamp,
  /**
   * Timestamp referencing the first moderation status impacting event was emitted on the subject
   */
  public val createdAt: Timestamp,
  public val reviewState: Token,
  /**
   * Sticky comment on the subject.
   */
  public val comment: String? = null,
  public val muteUntil: Timestamp? = null,
  public val lastReviewedBy: Did? = null,
  public val lastReviewedAt: Timestamp? = null,
  public val lastReportedAt: Timestamp? = null,
  /**
   * Timestamp referencing when the author of the subject appealed a moderation action
   */
  public val lastAppealedAt: Timestamp? = null,
  public val takendown: Boolean? = null,
  /**
   * True indicates that the a previously taken moderator action was appealed against, by the author
   * of the content. False indicates last appeal was resolved by moderators.
   */
  public val appealed: Boolean? = null,
  public val suspendUntil: Timestamp? = null,
  public val tags: ReadOnlyList<String> = persistentListOf(),
)
