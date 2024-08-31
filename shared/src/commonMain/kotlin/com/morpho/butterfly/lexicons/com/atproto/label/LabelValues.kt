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

public enum class LabelValue(val value: String) {
  @SerialName("!hide")
  HIDE("!hide"),
  @SerialName("!no-promote")
  NO_PROMOTE("!no-promote"),
  @SerialName("!warn")
  WARN("!warn"),
  @SerialName("!no-unauthenticated")
  NO_UNAUTHENTICATED("!no-unauthenticated"),
  @SerialName("dmca-violation")
  DMCA_VIOLATION("dmca-violation"),
  @SerialName("doxxing")
  DOXXING("doxxing"),
  @SerialName("porn")
  PORN("porn"),
  @SerialName("sexual")
  SEXUAL("sexual"),
  @SerialName("nudity")
  NUDITY("nudity"),
  @SerialName("nsfl")
  NSFL("nsfl"),
  @SerialName("gore")
  GORE("gore"),
  @SerialName("graphic-media")
  GRAPHIC_MEDIA("graphic-media"),
}
