package app.bsky.actor

import com.morpho.butterfly.MutedWordTarget
import com.morpho.butterfly.model.ReadOnlyList
import com.morpho.butterfly.model.TID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
//@SerialName("app.bsky.actor.defs#mutedWordsPref")
public data class MutedWordsPref(
    public val items: ReadOnlyList<MutedWord>
)

@Serializable
//@SerialName("app.bsky.actor.defs#mutedWord")
public data class MutedWord(
    public val value: String,
    public val targets: ReadOnlyList<MutedWordTarget>,
    public val actorTarget: MuteTargetGroup? = null,
    public val expiresAt: String? = null, // ISO 8601 datetime string
    public val id: String = TID.next().toString()
)

public enum class MuteTargetGroup {
    @SerialName("all")
    ALL,
    @SerialName("exclude-following")
    EXCLUDE_FOLLOWING
}