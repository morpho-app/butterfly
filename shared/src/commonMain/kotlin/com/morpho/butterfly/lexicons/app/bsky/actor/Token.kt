package app.bsky.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

public enum class Visibility(val value: String, @Transient val ordering: Int) {
  @SerialName("show")
  SHOW("show" , 2),
  @SerialName("warn")
  WARN("warn" , 1),
  @SerialName("hide")
  HIDE("hide", 0),
  @SerialName("ignore")
  IGNORE("ignore", 2);
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
