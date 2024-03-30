package com.atproto.server

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable
import com.morpho.butterfly.Cid

@Serializable
public data class CheckAccountStatusResponse(
  public val activated: Boolean,
  public val validDid: Boolean,
  public val repoCommit: Cid,
  public val repoRev: String,
  public val repoBlocks: Long,
  public val indexedRecords: Long,
  public val privateStateValues: Long,
  public val expectedBlobs: Long,
  public val importedBlobs: Long,
)
