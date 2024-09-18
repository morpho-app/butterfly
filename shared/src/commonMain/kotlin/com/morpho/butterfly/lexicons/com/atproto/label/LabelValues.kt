package com.atproto.label

import app.bsky.actor.Visibility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public enum class Severity {
  @SerialName("inform")
  INFORM,
  @SerialName("alert")
  ALERT,
  @SerialName("none")
  NONE,
  @SerialName("warn")
  WARN,
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
  @SerialName("show")
  SHOW,
}

fun DefaultSetting.toVisibility(): Visibility {
  return when(this) {
    DefaultSetting.IGNORE -> Visibility.IGNORE
    DefaultSetting.WARN -> Visibility.WARN
    DefaultSetting.HIDE -> Visibility.HIDE
    DefaultSetting.SHOW -> Visibility.SHOW
  }
}


@Serializable
public enum class LabelValues(val value: String) {
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
