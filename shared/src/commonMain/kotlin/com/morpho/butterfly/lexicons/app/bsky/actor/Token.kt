package app.bsky.actor

import kotlinx.serialization.SerialName

public enum class Visibility(val value: String) {
  @SerialName("show")
  SHOW("show"),
  @SerialName("warn")
  WARN("warn"),
  @SerialName("hide")
  HIDE("hide"),
  @SerialName("ignore")
  IGNORE("ignore");
}

public enum class Sort {
  @SerialName("oldest")
  OLDEST,
  @SerialName("newest")
  NEWEST,
  @SerialName("most-likes")
  MOST_LIKES,
  @SerialName("random")
  RANDOM;
}
