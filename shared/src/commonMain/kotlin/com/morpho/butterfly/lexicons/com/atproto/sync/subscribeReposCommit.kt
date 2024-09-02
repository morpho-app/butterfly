package com.atproto.sync

import com.morpho.butterfly.Did
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.ByteString

@Serializable
public data class SubscribeReposCommit @OptIn(ExperimentalSerializationApi::class) constructor(
  public val seq: Long,
  public val rebase: Boolean,
  public val tooBig: Boolean,
  public val repo: Did,
  @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
  @ByteString
  public val commit: ByteArray,
  @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
  @ByteString
  public val prev: ByteArray? = null,
  /**
   * The rev of the emitted commit
   */
  public val rev: String,
  /**
   * The rev of the last emitted commit from this repo
   */
  public val since: String? = null,
  /**
   * CAR file containing relevant blocks
   */
  @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
  @ByteString
  public val blocks: ByteArray,
  public val ops: ReadOnlyList<SubscribeReposRepoOp>,
  @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
  @ByteString
  public val blobs: ReadOnlyList<ByteArray>,
  public val time: Timestamp,
) {
  init {
    require(blocks.count() <= 1_000_000) {
      "blocks.count() must be <= 1_000_000, but was ${blocks.count()}"
    }
    require(ops.count() <= 200) {
      "ops.count() must be <= 200, but was ${ops.count()}"
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as SubscribeReposCommit

    if (seq != other.seq) return false
    if (rebase != other.rebase) return false
    if (tooBig != other.tooBig) return false
    if (repo != other.repo) return false
    if (!commit.contentEquals(other.commit)) return false
    if (prev != null) {
      if (other.prev == null) return false
      if (!prev.contentEquals(other.prev)) return false
    } else if (other.prev != null) return false
    if (rev != other.rev) return false
    if (since != other.since) return false
    if (!blocks.contentEquals(other.blocks)) return false
    if (ops != other.ops) return false
    if (blobs != other.blobs) return false
    if (time != other.time) return false

    return true
  }

  override fun hashCode(): Int {
    var result = seq.hashCode()
    result = 31 * result + rebase.hashCode()
    result = 31 * result + tooBig.hashCode()
    result = 31 * result + repo.hashCode()
    result = 31 * result + commit.contentHashCode()
    result = 31 * result + (prev?.contentHashCode() ?: 0)
    result = 31 * result + rev.hashCode()
    result = 31 * result + (since?.hashCode() ?: 0)
    result = 31 * result + blocks.contentHashCode()
    result = 31 * result + ops.hashCode()
    result = 31 * result + blobs.hashCode()
    result = 31 * result + time.hashCode()
    return result
  }
}
