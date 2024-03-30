package com.atproto.sync

import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.ByteArray
import kotlin.String
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.ByteString

/**
 * A repo operation, ie a write of a single record. For creates and updates, cid is the record's CID
 * as of this operation. For deletes, it's null.
 */
@Serializable
public data class SubscribeReposRepoOp @OptIn(ExperimentalSerializationApi::class) constructor(
  val action: SubscribeReposAction,
  val path: String,
  @ByteString
  val cid: ByteArray? = null,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as SubscribeReposRepoOp

    if (action != other.action) return false
    if (path != other.path) return false
    if (cid != null) {
      if (other.cid == null) return false
      if (!cid.contentEquals(other.cid)) return false
    } else if (other.cid != null) return false

    return true
  }

  override fun hashCode(): Int {
    var result = action.hashCode()
    result = 31 * result + path.hashCode()
    result = 31 * result + (cid?.contentHashCode() ?: 0)
    return result
  }
}
