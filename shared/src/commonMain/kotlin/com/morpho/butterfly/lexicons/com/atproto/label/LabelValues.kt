package com.atproto.label

import kotlinx.serialization.SerialName

public enum class Severity {
  @SerialName("inform")
  INFORM,
  @SerialName("alert")
  ALERT,
  @SerialName("none")
  NONE,
}

public enum class Blurs {
  @SerialName("content")
  CONTENT,
  @SerialName("media")
  MEDIA,
  @SerialName("none")
  NONE,
}

public enum class DefaultSetting {
  @SerialName("ignore")
  IGNORE,
  @SerialName("warn")
  WARN,
  @SerialName("hide")
  HIDE,
}

public enum class LabelValue {
  @SerialName("!hide")
  HIDE,
  @SerialName("!no-promote")
  NO_PROMOTE,
  @SerialName("!warn")
  WARN,
  @SerialName("!no-unauthenticated")
  NO_UNAUTHENTICATED,
  @SerialName("dmca-violation")
  DMCA_VIOLATION,
  @SerialName("doxxing")
  DOXXING,
  @SerialName("porn")
  PORN,
  @SerialName("sexual")
  SEXUAL,
  @SerialName("nudity")
  NUDITY,
  @SerialName("nsfl")
  NSFL,
  @SerialName("gore")
  GORE,
}
