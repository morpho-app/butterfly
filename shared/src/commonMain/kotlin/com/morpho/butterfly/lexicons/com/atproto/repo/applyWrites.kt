package com.atproto.repo

import com.morpho.butterfly.AtIdentifier
import com.morpho.butterfly.Cid
import com.morpho.butterfly.model.ReadOnlyList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline


@Serializable
public sealed interface WritesUnion {
  @Serializable
  @JvmInline
  @SerialName("com.atproto.repo.applyWrites#create")
  public value class Create(
    public val `value`: ApplyWritesCreate,
  ) : WritesUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.repo.applyWrites#update")
  public value class Update(
    public val `value`: ApplyWritesUpdate,
  ) : WritesUnion

  @Serializable
  @JvmInline
  @SerialName("com.atproto.repo.applyWrites#delete")
  public value class Delete(
    public val `value`: ApplyWritesDelete,
  ) : WritesUnion
}

@Serializable
public data class ApplyWritesRequest(
  /**
   * The handle or DID of the repo.
   */
  public val repo: AtIdentifier,
  /**
   * Validate the records?
   */
  public val validate: Boolean? = true,
  public val writes: ReadOnlyList<WritesUnion>,
  public val swapCommit: Cid? = null,
)
