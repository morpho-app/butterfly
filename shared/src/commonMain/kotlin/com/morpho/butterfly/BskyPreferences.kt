package com.morpho.butterfly

import app.bsky.actor.*
import kotlinx.serialization.Serializable


@Serializable
public data class BskyPreferences(
    val feedView: FeedViewPref? = null,
    val saved: List<SavedFeed> = emptyList(),
    val personalDetails: PersonalDetailsPref? = null,
    val modPrefs: ModerationPreferences = ModerationPreferences(),
    val threadPrefs: ThreadViewPref? = null,
    val interests: List<String> = emptyList(),
    val skyFeedBuilderFeeds: List<AtUri> = emptyList(),
    @Deprecated("use v2") val savedFeeds: SavedFeedsPref? = null,
    val timelineIndex: Int? = null, // extracted from v1 saved feeds for now
    val languages: List<Language> = emptyList(),
)

@Serializable
data class ModerationPreferences(
    val adultContentEnabled: Boolean = false,
    val labels: Map<String, Visibility> = LABELS.mapValues { it.value.defaultSetting!! }.mapKeys { it.key.value },
    val labelers: Map<LabelerID, Map<LabelValueID, Visibility>> = mapOf(), // DID -> labelValue -> setting
    val hiddenPosts: List<AtUri> = emptyList(),
    val mutedWords: List<MutedWord> = emptyList(),
)

fun matchMutedWord(existingWord: MutedWord, newWord: MutedWord): Boolean {
    val existingId = existingWord.id
    val matchById = existingId != null && existingId === newWord.id
    val matchByWord = existingWord.value == newWord.value
    return matchById || matchByWord
}

fun GetPreferencesResponse.toPreferences() : BskyPreferences {
    var newPrefs = BskyPreferences()
    var newModPrefs = newPrefs.modPrefs
    val labelPrefs = mutableListOf<ContentLabelPref>()
    val labelers = mutableListOf<Did>()
    val labelMap = labelers.associate {
        it.did to mutableMapOf<LabelValueID, Visibility>() }.toMutableMap()
    this.preferences.forEach { pref: PreferencesUnion ->
        when(pref) {
            is PreferencesUnion.FeedViewPref -> newPrefs = newPrefs.copy(feedView = pref.value)
            is PreferencesUnion.AdultContentPref -> newModPrefs = newModPrefs.copy(adultContentEnabled = pref.value.enabled)
            is PreferencesUnion.BskyAppStatePref -> {}
            is PreferencesUnion.ContentLabelPref -> labelPrefs.add(pref.value.legacyToNew())
            is PreferencesUnion.HiddenPostsPref -> newModPrefs = newModPrefs.copy(hiddenPosts = pref.value.items)
            is PreferencesUnion.InterestsPref -> newPrefs = newPrefs.copy(interests = pref.value.tags)
            is PreferencesUnion.LabelersPref -> labelers.addAll(pref.value.labelers.map { it.did })
            is PreferencesUnion.MutedWordsPref -> newModPrefs = newModPrefs.copy(mutedWords = pref.value.items)
            is PreferencesUnion.PersonalDetailsPref -> newPrefs = newPrefs.copy(personalDetails = pref.value)
            is PreferencesUnion.SavedFeedsPref -> newPrefs = newPrefs.copy(savedFeeds = pref.value, timelineIndex = pref.value.timelineIndex)
            is PreferencesUnion.SavedFeedsPrefV2 -> newPrefs = newPrefs.copy(saved = pref.value.items)
            is PreferencesUnion.SkyFeedBuilderFeedsPref -> newPrefs = newPrefs.copy(skyFeedBuilderFeeds = pref.value.feeds)
            is PreferencesUnion.ThreadViewPref -> newPrefs = newPrefs.copy(threadPrefs = pref.value)
        }
    }
    for (pref in labelPrefs) {
        if (pref.labelerDid != null) {
            val labeler = labelers.firstOrNull { it == pref.labelerDid }
            if (labeler == null) continue
            val labelerMap = labelMap[labeler.did] ?: mutableMapOf()
            labelerMap[pref.label] = pref.visibility
            labelMap[labeler.did] = labelerMap
        } else {
            val prefMap = newModPrefs.labels.toMutableMap()
            prefMap[pref.label] = pref.visibility
            newModPrefs = newModPrefs.copy(labels = prefMap.toMap())
        }
    }
    newModPrefs = newModPrefs.copy(labelers = labelMap.mapValues { it.value.toMap() })
    return newPrefs.copy(modPrefs = newModPrefs)
}

fun ContentLabelPref.isLegacyLabel(): Boolean {
    return label.contains("graphic-media") ||
            label.contains("porn") ||
            label.contains("sexual")
}

fun ContentLabelPref.getLegacyLabel(): String {
    return label.replace("graphic-media", "gore")
        .replace("porn", "nsfw")
        .replace("sexual", "suggestive")
}

fun ContentLabelPref.legacyToNew(): ContentLabelPref {
    return ContentLabelPref(
        labelerDid = labelerDid,
        label = if(isLegacyLabel()) {
            label.replace( "gore", "graphic-media")
                .replace("nsfw", "porn")
                .replace("suggestive", "sexual")
        } else label,
        visibility = visibility,
    )
}