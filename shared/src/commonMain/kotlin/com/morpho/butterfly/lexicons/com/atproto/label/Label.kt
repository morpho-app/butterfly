package com.atproto.label

import com.morpho.butterfly.AtUri
import com.morpho.butterfly.Cid
import com.morpho.butterfly.Did
import com.morpho.butterfly.model.Timestamp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.ByteString

/**
 * Metadata tag on an atproto resource (eg, repo or record).
 */
@Serializable
public data class Label @OptIn(ExperimentalSerializationApi::class) constructor(
  /**
   * The AT Protocol version of the label object.
   */
  public val ver: Long? = null,
  /**
   * DID of the actor who created this label.
   */
  public val src: Did,
  /**
   * AT URI of the record, repository (account), or other resource that this label applies to.
   */
  public val uri: AtUri,
  /**
   * Optionally, CID specifying the specific version of 'uri' resource this label applies to.
   */
  public val cid: Cid? = null,
  /**
   * The short string name of the value or type of this label.
   */
  public val `val`: String,
  /**
   * If true, this is a negation label, overwriting a previous label.
   */
  public val neg: Boolean? = null,
  /**
   * Timestamp when this label was created.
   */
  public val cts: Timestamp,
  /**
   * Timestamp at which this label expires (no longer applies).
   */
  public val exp: Timestamp? = null,
  /**
   * Signature of dag-cbor encoded label.
   */
  @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
  @ByteString
  public val sig: ByteArray? = null,
) {
  init {
    require(`val`.count() <= 128) {
      "val.count() must be <= 128, but was ${`val`.count()}"
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as Label

    if (ver != other.ver) return false
    if (src != other.src) return false
    if (uri != other.uri) return false
    if (cid != other.cid) return false
    if (`val` != other.`val`) return false
    if (neg != other.neg) return false
    if (cts != other.cts) return false
    if (exp != other.exp) return false
    if (sig != null) {
      if (other.sig == null) return false
      if (!sig.contentEquals(other.sig)) return false
    } else if (other.sig != null) return false

    return true
  }

  override fun hashCode(): Int {
    var result = ver?.hashCode() ?: 0
    result = 31 * result + src.hashCode()
    result = 31 * result + uri.hashCode()
    result = 31 * result + (cid?.hashCode() ?: 0)
    result = 31 * result + `val`.hashCode()
    result = 31 * result + (neg?.hashCode() ?: 0)
    result = 31 * result + cts.hashCode()
    result = 31 * result + (exp?.hashCode() ?: 0)
    result = 31 * result + (sig?.contentHashCode() ?: 0)
    return result
  }
}