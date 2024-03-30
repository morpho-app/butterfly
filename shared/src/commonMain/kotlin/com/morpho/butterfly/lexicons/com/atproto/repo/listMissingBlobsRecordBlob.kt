package com.atproto.repo

import kotlinx.serialization.Serializable
import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid

@Serializable
public data class ListMissingBlobsRecordBlob(
  public val cid: Cid,
  public val recordUri: AtUri,
)
