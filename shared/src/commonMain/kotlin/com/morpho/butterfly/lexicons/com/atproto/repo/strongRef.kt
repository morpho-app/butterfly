package com.atproto.repo

import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
public data class StrongRef(
  public val uri: AtUri,
  public val cid: Cid,
): Parcelable
