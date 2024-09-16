package com.morpho.butterfly

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import app.bsky.actor.PreferencesUnion
import app.bsky.actor.Visibility
import app.bsky.graph.ListViewBasic
import app.bsky.labeler.LabelerViewDetailed
import com.atproto.label.*
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.ByteString

typealias LabelerID = String
typealias LabelValueID = String

val BSKY_LABELER_DID = Did("did:plc:ar7c4by46qjdydhdevvrndac")

fun List<PreferencesUnion>.toLabelerDids(): List<Did> {
    return this.filterIsInstance<PreferencesUnion.LabelersPref>()
        .map { labelersPref -> labelersPref.value.labelers.map { it.did } }
        .flatten()
        .distinct()
}


data class ContentHandling(
    val scope: Blurs,
    val action: LabelAction,
    val source: LabelDescription,
    val id: String,
    val icon: LabelIcon,
)


@Immutable
@Serializable
sealed interface LabelIcon {
    val labelerAvatar: String?
    val icon: ImageVector

    @Serializable
    @Immutable
    data class CircleBanSign(
        override val labelerAvatar: String?
    ): LabelIcon {
        override val icon: ImageVector
            get() = Icons.Default.StopCircle
    }

    @Serializable
    @Immutable
    data class Warning(
        override val labelerAvatar: String? = null
    ): LabelIcon {
        override val icon: ImageVector
            get() = Icons.Default.Warning
    }

    @Serializable
    @Immutable
    data class EyeSlash(
        override val labelerAvatar: String? = null
    ): LabelIcon {
        override val icon: ImageVector
            get() = Icons.Default.HideImage
    }

    @Serializable
    @Immutable
    data class CircleInfo(
        override val labelerAvatar: String? = null
    ): LabelIcon {
        override val icon: ImageVector
            get() = Icons.Default.Info
    }

}


@Immutable
@Serializable
sealed interface LabelDescription {
    val name: String
    val description: String

    
    @Immutable
    @Serializable
    sealed interface Block: LabelDescription
    
    @Immutable
    @Serializable
    data object Blocking: Block {
        override val name: String = "User Blocked"
        override val description: String = "You have blocked this user. You cannot view their content"

    }
    
    @Immutable
    @Serializable
    data object BlockedBy: Block {
        override val name: String = "User Blocking You"
        override val description: String = "This user has blocked you. You cannot view their content."
    }
    
    @Immutable
    @Serializable
    data class BlockList(
        val listName: String,
        val listUri: AtUri,
    ): Block {
        override val name: String = "User Blocked by $listName"
        override val description: String = "This user is on a block list you subscribe to. You cannot view their content."
    }
    
    @Immutable
    @Serializable
    data object OtherBlocked: Block {
        override val name: String = "Content Not Available"
        override val description: String = "This content is not available because one of the users involved has blocked the other."
    }

    
    @Immutable
    @Serializable
    sealed interface Muted: LabelDescription

    
    @Immutable
    @Serializable
    data class MuteList(
        val listName: String,
        val listUri: AtUri,
    ): Muted {
        override val name: String = "User Muted by $listName"
        override val description: String = "This user is on a mute list you subscribe to."
    }
    
    @Immutable
    @Serializable
    data object YouMuted: Muted {
        override val name: String = "Account Muted"
        override val description: String = "You have muted this user."
    }
    
    @Immutable
    @Serializable
    data class MutedWord(val word: String): Muted {
        override val name: String = "Post Hidden by Muted Word"
        override val description: String = "This post contains the word or tag \"$word\". You've chosen to hide it."
    }

    
    @Immutable
    @Serializable
    data class HiddenPost(val uri: AtUri): LabelDescription {
        override val name: String = "Post Hidden by You"
        override val description: String = "You have hidden this post."
    }

    
    @Immutable
    @Serializable
    data class Label(
        override val name: String,
        override val description: String,
        val severity: Severity,
    ): LabelDescription
}

@Immutable
@Serializable
sealed interface LabelSource {
    @Immutable
    @Serializable
    data object User: LabelSource
    @Immutable
    @Serializable
    data class List(
        val list: ListViewBasic,
    ): LabelSource
    @Immutable
    @Serializable
    data class Labeler(
        val labeler: LabelerViewDetailed,
    ): LabelSource
}


@Immutable
@Serializable
sealed interface LabelCause {
    val downgraded: Boolean
    val priority: Int
    val source: LabelSource
    @Immutable
    @Serializable
    data class Blocking(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 3
    }
    @Immutable
    @Serializable
    data class BlockedBy(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 4
    }

    @Immutable
    @Serializable
    data class BlockOther(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 4
    }

    @Immutable
    @Serializable
    data class Label(
        override val source: LabelSource,
        val label: com.atproto.label.Label,
        val labelDef: InterpretedLabelDefinition,
        val target: Blurs,
        val setting: Visibility,
        val behaviour: ModBehaviour,
        val noOverride: Boolean,
        override val priority: Int,
        override val downgraded: Boolean,
    ): LabelCause {
        init {
            require(
                priority == 1 || priority == 2 || priority == 3 ||
                        priority == 5 || priority == 7 || priority == 8
            )
        }
    }

    @Immutable
    @Serializable
    data class Muted(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 6
    }

    @Immutable
    @Serializable
    data class MutedWord(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 6
    }

    @Immutable
    @Serializable
    data class Hidden(
        override val source: LabelSource,
        override val downgraded: Boolean,
    ): LabelCause {
        override val priority: Int = 6
    }

}
@Immutable
@Serializable
enum class LabelValueDefFlag {
    NoOverride,
    Adult,
    Unauthed,
    NoSelf,
}


@Serializable
@Immutable
open class InterpretedLabelDefinition(
    val identifier: String,
    val definedBy: String? = null,
    val configurable: Boolean,
    val severity: Severity,
    val whatToHide: Blurs,
    val defaultSetting: Visibility?,
    @Contextual
    val flags: List<LabelValueDefFlag> = persistentListOf(),
    val behaviours: ModBehaviours,
    val localizedName: String = "",
    val localizedDescription: String = "",
    @Contextual
    val allDescriptions: List<LabelValueDefinitionStrings> = persistentListOf(),
) {
    fun copy(
        identifier: String = this.identifier,
        definedBy: String? = this.definedBy,
        configurable: Boolean = this.configurable,
        severity: Severity = this.severity,
        whatToHide: Blurs = this.whatToHide,
        defaultSetting: Visibility? = this.defaultSetting,
        flags: List<LabelValueDefFlag> = this.flags,
        behaviours: ModBehaviours = this.behaviours,
        localizedName: String = this.localizedName,
        localizedDescription: String = this.localizedDescription,
        allDescriptions: List<LabelValueDefinitionStrings> = this.allDescriptions,
    ) = InterpretedLabelDefinition(
        identifier = identifier,
        definedBy = definedBy,
        configurable = configurable,
        severity = severity,
        whatToHide = whatToHide,
        defaultSetting = defaultSetting,
        flags = flags,
        behaviours = behaviours,
        localizedName = localizedName,
        localizedDescription = localizedDescription,
        allDescriptions = allDescriptions,
    )

    companion object {
        fun interpretLabelValueDefinition(def: LabelValueDefinition, definedBy: String? = null): InterpretedLabelDefinition {

            val alertOrInform = when(def.severity) {
                Severity.INFORM -> LabelAction.Inform
                Severity.ALERT -> LabelAction.Alert
                Severity.NONE -> LabelAction.None
            }
            val behaviours = when(def.blurs) {
                Blurs.CONTENT -> ModBehaviours(
                    account = ModBehaviour(
                        profileList = alertOrInform,
                        profileView = alertOrInform,
                        contentList = LabelAction.Blur,
                        contentView = if(def.adultOnly == true) LabelAction.Blur else alertOrInform,
                    ),
                    profile = ModBehaviour(
                        profileList = alertOrInform,
                        profileView = alertOrInform,
                        contentList = LabelAction.Blur,
                        contentView = if(def.adultOnly == true) LabelAction.Blur else alertOrInform,
                    ),
                    content = ModBehaviour(
                        contentList = LabelAction.Blur,
                        contentView = if(def.adultOnly == true) LabelAction.Blur else alertOrInform,
                    ))
                Blurs.MEDIA -> ModBehaviours(
                    account = ModBehaviour(
                        profileList = alertOrInform,
                        profileView = alertOrInform,
                        avatar = LabelAction.Blur,
                        banner = LabelAction.Blur,
                    ),
                    profile = ModBehaviour(
                        profileList = alertOrInform,
                        profileView = alertOrInform,
                        avatar = LabelAction.Blur,
                        banner = LabelAction.Blur,
                    ),
                    content = ModBehaviour(
                        contentMedia = LabelAction.Blur,
                    ))
                Blurs.NONE -> ModBehaviours(
                    account = ModBehaviour(
                        profileList = alertOrInform,
                        profileView = alertOrInform,
                        contentList = alertOrInform,
                        contentView = alertOrInform,
                    ),
                    profile = ModBehaviour(
                        profileList = alertOrInform,
                        profileView = alertOrInform,
                    ),
                    content = ModBehaviour(
                        contentList = alertOrInform,
                        contentView = alertOrInform,
                    ))
            }
            val setting = when(def.defaultSetting) {
                DefaultSetting.HIDE -> Visibility.HIDE
                DefaultSetting.IGNORE -> Visibility.IGNORE
                DefaultSetting.WARN -> Visibility.WARN
                null -> Visibility.SHOW
            }
            val flags = mutableListOf<LabelValueDefFlag>(LabelValueDefFlag.NoSelf)
            if(def.adultOnly == true) flags.add(LabelValueDefFlag.Adult)
            return InterpretedLabelDefinition(
                identifier = def.identifier,
                definedBy = definedBy,
                configurable = true,
                severity = def.severity,
                whatToHide = def.blurs,
                defaultSetting = setting,
                flags = flags.toList(),
                behaviours = behaviours,
                allDescriptions = def.locales.toList(),
            )
        }

        fun interpretLabelValueDefinitions(labeler: LabelerViewDetailed): Map<LabelValueID, InterpretedLabelDefinition> {
            val labelDefs = mutableMapOf<LabelValueID,InterpretedLabelDefinition>()
            for (labelValue in labeler.policies.labelValueDefinitions) {
                labelDefs[labelValue.identifier] = interpretLabelValueDefinition(labelValue, labeler.creator.did.did)
            }
            return labelDefs.toMap()
        }
    }

    public fun toContentHandling(target: LabelTarget, avatar: String? = null): ContentHandling {
        val action = behaviours.forScope(whatToHide, target).minOrNull() ?: when(defaultSetting) {
            Visibility.HIDE -> LabelAction.Blur
            Visibility.WARN -> LabelAction.Alert
            Visibility.IGNORE -> LabelAction.Inform
            Visibility.SHOW -> LabelAction.None
            null -> LabelAction.None
        }
        return ContentHandling(
            id = identifier,
            scope = whatToHide,
            action = action,
            source = LabelDescription.Label(
                name = localizedName,
                description = localizedDescription,
                severity = severity,
            ),
            icon = when(severity) {
                Severity.ALERT -> LabelIcon.Warning(labelerAvatar = avatar)
                Severity.NONE -> LabelIcon.CircleInfo(labelerAvatar = avatar)
                Severity.INFORM -> LabelIcon.CircleInfo(labelerAvatar = avatar)
            }
        )
    }
}

val LABELS: PersistentMap<LabelValue, InterpretedLabelDefinition> = persistentMapOf(
    LabelValue.HIDE to Hide,
    LabelValue.WARN to Warn,
    LabelValue.NO_UNAUTHENTICATED to NoUnauthed,
    LabelValue.PORN to Porn,
    LabelValue.SEXUAL to Sexual,
    LabelValue.NUDITY to Nudity,
    LabelValue.GRAPHIC_MEDIA to GraphicMedia,
)


@Immutable
@Serializable
data object Hide: InterpretedLabelDefinition(
    "!hide",
    definedBy = null,
    false,
    Severity.ALERT,
    Blurs.CONTENT,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.NoSelf, LabelValueDefFlag.NoOverride),
    ModBehaviours(
        account = ModBehaviour(
            profileList = LabelAction.Blur,
            profileView = LabelAction.Blur,
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
    ),
    localizedName = "Hide",
    localizedDescription = "Hide",
)


@Serializable
data object Warn: InterpretedLabelDefinition(
    "!warn",
    definedBy = null,
    false,
    Severity.NONE,
    Blurs.CONTENT,
    Visibility.WARN,
    persistentListOf(LabelValueDefFlag.NoSelf),
    ModBehaviours(
        account = ModBehaviour(
            profileList = LabelAction.Blur,
            profileView = LabelAction.Blur,
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
    ),
    localizedName = "Warn",
    localizedDescription = "Warn",
)


@Immutable
@Serializable
data object NoUnauthed: InterpretedLabelDefinition(
    "!no-unauthenticated",
    definedBy = null,
    false,
    Severity.NONE,
    Blurs.CONTENT,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.NoOverride, LabelValueDefFlag.Unauthed),
    ModBehaviours(
        account = ModBehaviour(
            profileList = LabelAction.Blur,
            profileView = LabelAction.Blur,
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
            displayName = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentList = LabelAction.Blur,
            contentView = LabelAction.Blur,
        ),
    ),
    localizedName = "No Unauthenticated",
    localizedDescription = "Do not show to unauthenticated users",
)


@Immutable
@Serializable
data object Porn: InterpretedLabelDefinition(
    "porn",
    definedBy = null,
    true,
    Severity.NONE,
    Blurs.MEDIA,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.Adult),
    ModBehaviours(
        account = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentMedia = LabelAction.Blur,
        ),
    ),
    localizedName = "Sexually Explicit",
    localizedDescription = "This content is sexually explicit",
)


@Immutable
@Serializable
data object Sexual: InterpretedLabelDefinition(
    "sexual",
    definedBy = null,
    true,
    Severity.NONE,
    Blurs.MEDIA,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.Adult),
    ModBehaviours(
        account = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentMedia = LabelAction.Blur,
        ),
    ),
    localizedName = "Suggestive",
    localizedDescription = "This content may be suggestive or sexual in nature",
)


@Immutable
@Serializable
data object Nudity: InterpretedLabelDefinition(
    "nudity",
    definedBy = null,
    true,
    Severity.NONE,
    Blurs.MEDIA,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.Adult),
    ModBehaviours(
        account = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentMedia = LabelAction.Blur,
        ),
    ),
    localizedName = "Nudity",
    localizedDescription = "This content contains nudity, artistic or otherwise",
)


@Immutable
@Serializable
data object GraphicMedia: InterpretedLabelDefinition(
    "graphic-media",
    definedBy = null,
    true,
    Severity.NONE,
    Blurs.MEDIA,
    Visibility.HIDE,
    persistentListOf(LabelValueDefFlag.Adult),
    ModBehaviours(
        account = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        profile = ModBehaviour(
            avatar = LabelAction.Blur,
            banner = LabelAction.Blur,
        ),
        content = ModBehaviour(
            contentMedia = LabelAction.Blur,
        ),
    ),
    localizedName = "Graphic Content",
    localizedDescription = "This content is graphic or violent in nature",
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Immutable
data class BskyLabel(
    val version: Long?,
    val creator: Did,
    val uri: AtUri,
    val cid: Cid?,
    val value: String,
    val overwritesPrevious: Boolean?,
    val createdTimestamp: Timestamp,
    val expirationTimestamp: Timestamp?,
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @ByteString
    val signature: ByteArray?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BskyLabel

        if (version != other.version) return false
        if (creator != other.creator) return false
        if (uri != other.uri) return false
        if (cid != other.cid) return false
        if (value != other.value) return false
        if (overwritesPrevious != other.overwritesPrevious) return false
        if (createdTimestamp != other.createdTimestamp) return false
        if (expirationTimestamp != other.expirationTimestamp) return false
        if (signature != null) {
            if (other.signature == null) return false
            if (!signature.contentEquals(other.signature)) return false
        } else if (other.signature != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version?.hashCode() ?: 0
        result = 31 * result + creator.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + (cid?.hashCode() ?: 0)
        result = 31 * result + value.hashCode()
        result = 31 * result + (overwritesPrevious?.hashCode() ?: 0)
        result = 31 * result + createdTimestamp.hashCode()
        result = 31 * result + (expirationTimestamp?.hashCode() ?: 0)
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        return result
    }

    fun getLabelValue(): LabelValue? {
        return when (value) {
            LabelValue.PORN.value -> LabelValue.PORN
            LabelValue.GORE.value -> LabelValue.GORE
            LabelValue.NSFL.value -> LabelValue.NSFL
            LabelValue.SEXUAL.value -> LabelValue.SEXUAL
            LabelValue.GRAPHIC_MEDIA.value -> LabelValue.GRAPHIC_MEDIA
            LabelValue.NUDITY.value -> LabelValue.NUDITY
            LabelValue.DOXXING.value -> LabelValue.DOXXING
            LabelValue.DMCA_VIOLATION.value -> LabelValue.DMCA_VIOLATION
            LabelValue.NO_PROMOTE.value -> LabelValue.NO_PROMOTE
            LabelValue.NO_UNAUTHENTICATED.value -> LabelValue.NO_UNAUTHENTICATED
            LabelValue.WARN.value -> LabelValue.WARN
            LabelValue.HIDE.value -> LabelValue.HIDE
            else -> null
        }
    }
}

@Immutable
@Serializable
enum class LabelAction {
    Blur,
    Alert,
    Inform,
    None
}

@Immutable
@Serializable
enum class LabelTarget {
    Account,
    Profile,
    Content
}

@Immutable

@Serializable
open class ModBehaviour(
    val profileList: LabelAction = LabelAction.None,
    val profileView: LabelAction = LabelAction.None,
    val avatar: LabelAction = LabelAction.None,
    val banner: LabelAction = LabelAction.None,
    val displayName: LabelAction = LabelAction.None,
    val contentList: LabelAction = LabelAction.None,
    val contentView: LabelAction = LabelAction.None,
    val contentMedia: LabelAction = LabelAction.None,
) {
    init {
        require(avatar != LabelAction.Inform)
        require(banner != LabelAction.Inform && banner != LabelAction.Alert)
        require(displayName != LabelAction.Inform && displayName != LabelAction.Alert)
        require(contentMedia != LabelAction.Inform && contentMedia != LabelAction.Alert)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ModBehaviour

        if (profileList != other.profileList) return false
        if (profileView != other.profileView) return false
        if (avatar != other.avatar) return false
        if (banner != other.banner) return false
        if (displayName != other.displayName) return false
        if (contentList != other.contentList) return false
        if (contentView != other.contentView) return false
        if (contentMedia != other.contentMedia) return false

        return true
    }

    override fun hashCode(): Int {
        var result = profileList.hashCode()
        result = 31 * result + profileView.hashCode()
        result = 31 * result + avatar.hashCode()
        result = 31 * result + banner.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + contentList.hashCode()
        result = 31 * result + contentView.hashCode()
        result = 31 * result + contentMedia.hashCode()
        return result
    }
}


@Immutable
@Serializable
data class ModBehaviours(
    val account: ModBehaviour = ModBehaviour(),
    val profile: ModBehaviour = ModBehaviour(),
    val content: ModBehaviour = ModBehaviour(),
) {
    fun forScope(scope: Blurs, target: LabelTarget): List<LabelAction> {
        return when (target) {
            LabelTarget.Account -> when (scope) {
                Blurs.CONTENT -> listOf(
                    account.contentList, account.contentView, account.avatar,
                    account.banner, account.profileList, account.profileView,
                    account.displayName
                )
                Blurs.MEDIA -> listOf(account.contentMedia, account.avatar, account.banner)
                Blurs.NONE -> listOf()
            }
            LabelTarget.Profile -> when (scope) {
                Blurs.CONTENT -> listOf(profile.contentList, profile.contentView, profile.displayName)
                Blurs.MEDIA -> listOf(profile.avatar, profile.banner, profile.contentMedia)
                Blurs.NONE -> listOf()
            }
            LabelTarget.Content -> when (scope) {
                Blurs.CONTENT -> listOf(content.contentList, content.contentView)
                Blurs.MEDIA -> listOf(
                    content.contentMedia,
                    content.avatar,
                    content.banner
                )
                Blurs.NONE -> listOf()
            }
        }
    }
}

@Immutable
@Serializable
data object BlockBehaviour: ModBehaviour(
    profileList = LabelAction.Blur,
    profileView = LabelAction.Blur,
    avatar = LabelAction.Blur,
    banner = LabelAction.Blur,
    contentList = LabelAction.Blur,
    contentView = LabelAction.Blur,
)

@Immutable
@Serializable
data object MuteBehaviour: ModBehaviour(
    profileList = LabelAction.Inform,
    profileView = LabelAction.Alert,
    contentList = LabelAction.Blur,
    contentView = LabelAction.Inform,
)

@Immutable
@Serializable
data object MuteWordBehaviour: ModBehaviour(
    contentList = LabelAction.Blur,
    contentView = LabelAction.Blur,
)

@Immutable
@Serializable
data object HideBehaviour: ModBehaviour(
    contentList = LabelAction.Blur,
    contentView = LabelAction.Blur,
)

@Immutable
@Serializable
data object InappropriateMediaBehaviour: ModBehaviour(
    contentMedia = LabelAction.Blur,
)

@Immutable
@Serializable
data object InappropriateAvatarBehaviour: ModBehaviour(
    avatar = LabelAction.Blur,
)

@Immutable
@Serializable
data object InappropriateBannerBehaviour: ModBehaviour(
    banner = LabelAction.Blur,
)

@Immutable
@Serializable
data object InappropriateDisplayNameBehaviour: ModBehaviour(
    displayName = LabelAction.Blur,
)


@Serializable
val BlurAllMedia = ModBehaviours(
    content = InappropriateMediaBehaviour,
    profile = ModBehaviour(
        avatar = LabelAction.Blur,
        banner = LabelAction.Blur,
        contentMedia = LabelAction.Blur,
    ),
    account = ModBehaviour(
        avatar = LabelAction.Blur,
        banner = LabelAction.Blur,
        contentMedia = LabelAction.Blur,
    ),
)


@Immutable
@Serializable
data object NoopBehaviour: ModBehaviour()
