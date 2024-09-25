package com.morpho.butterfly

import app.bsky.actor.ProfileView
import app.bsky.actor.ViewerState
import app.bsky.labeler.LabelerPolicies
import app.bsky.labeler.LabelerViewDetailed
import app.bsky.labeler.LabelerViewerState
import com.atproto.label.Blurs
import com.atproto.label.DefaultSetting
import com.atproto.label.Label
import com.atproto.label.LabelValueDefinition
import com.atproto.label.LabelValueDefinitionStrings
import com.atproto.label.Severity
import com.morpho.butterfly.model.Timestamp
import kotlinx.collections.immutable.persistentListOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class LabelerDefTest {
    companion object {
        val testLabelerToDecode = defString
        val testLabelerDef = labelerDef
    }

    @Test
    fun testLabelerDef() {

        val labelerDefDecode = json.decodeFromString<LabelerViewDetailed>(testLabelerToDecode)
        println(labelerDefDecode.toString())
        assertEquals(labelerDefDecode.policies.labelValues, testLabelerDef.policies.labelValues)
        when(labelerDefDecode.policies.labelValues.first()) {
            is LabelValue.Warn -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.Warn)
            is LabelValue.NoPromote -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.NoPromote)
            is LabelValue.NoSelf -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.NoSelf)
            is LabelValue.NoUnauthenticated -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.NoUnauthenticated)
            is LabelValue.Hide -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.Hide)
            is LabelValue.DMCAViolation -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.DMCAViolation)
            is LabelValue.Doxxing -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.Doxxing)
            is LabelValue.Porn -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.Porn)
            is LabelValue.Sexual -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.Sexual)
            is LabelValue.Nudity -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.Nudity)
            is LabelValue.NSFL -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.NSFL)
            is LabelValue.Gore -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.Gore)
            is LabelValue.GraphicMedia -> assertEquals(labelerDefDecode.policies.labelValues.first(), LabelValue.GraphicMedia)
            is LabelValue.Custom -> assertTrue(true)
        }

    }
}


val defString = """
    {
        "${'$'}type": "app.bsky.labeler.defs#labelerViewDetailed",
        "uri": "at://did:plc:ar7c4by46qjdydhdevvrndac/app.bsky.labeler.service/self",
        "cid": "bafyreigklczuekgj7q2pj3ys5ck3ah7v2ps5qbly6viwcj26vrmigbfyce",
        "creator": {
            "did": "did:plc:ar7c4by46qjdydhdevvrndac",
            "handle": "moderation.bsky.app",
            "displayName": "Bluesky Moderation Service",
            "associated": {
                "labeler": true
            },
            "viewer": {
                "muted": false,
                "blockedBy": false
            },
            "labels": [
                {
                    "cts": "2024-05-11T02:11:29.404Z",
                    "src": "did:plc:e4elbtctnfqocyfcml6h2lf7",
                    "uri": "did:plc:ar7c4by46qjdydhdevvrndac",
                    "val": "bluesky-elder",
                    "ver": 1
                }
            ],
            "createdAt": "2023-04-11T17:29:51.242Z",
            "description": "Official Bluesky moderation service. https://bsky.social/about/support/community-guidelines",
            "indexedAt": "2024-03-19T17:14:40.242Z"
        },
        "likeCount": 3,
        "viewer": {},
        "indexedAt": "2024-03-13T15:52:53.522Z",
        "labels": [],
        "policies": {
            "labelValueDefinitions": [
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "spam",
                    "locales": [
                        {
                            "description": "Unwanted, repeated, or unrelated actions that bother users.",
                            "lang": "en",
                            "name": "Spam"
                        }
                    ],
                    "severity": "inform"
                },
                {
                    "adultOnly": false,
                    "blurs": "none",
                    "defaultSetting": "hide",
                    "identifier": "impersonation",
                    "locales": [
                        {
                            "description": "Pretending to be someone else without permission.",
                            "lang": "en",
                            "name": "Impersonation"
                        }
                    ],
                    "severity": "inform"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "scam",
                    "locales": [
                        {
                            "description": "Scams, phishing & fraud.",
                            "lang": "en",
                            "name": "Scam"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "warn",
                    "identifier": "intolerant",
                    "locales": [
                        {
                            "description": "Discrimination against protected groups.",
                            "lang": "en",
                            "name": "Intolerance"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "warn",
                    "identifier": "self-harm",
                    "locales": [
                        {
                            "description": "Promotes self-harm, including graphic images, glorifying discussions, or triggering stories.",
                            "lang": "en",
                            "name": "Self-Harm"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "security",
                    "locales": [
                        {
                            "description": "May be unsafe and could harm your device, steal your info, or get your account hacked.",
                            "lang": "en",
                            "name": "Security Concerns"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "warn",
                    "identifier": "misleading",
                    "locales": [
                        {
                            "description": "Altered images/videos, deceptive links, or false statements.",
                            "lang": "en",
                            "name": "Misleading"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "threat",
                    "locales": [
                        {
                            "description": "Promotes violence or harm towards others, including threats, incitement, or advocacy of harm.",
                            "lang": "en",
                            "name": "Threats"
                        }
                    ],
                    "severity": "inform"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "unsafe-link",
                    "locales": [
                        {
                            "description": "Links to harmful sites with malware, phishing, or violating content that risk security and privacy.",
                            "lang": "en",
                            "name": "Unsafe link"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "illicit",
                    "locales": [
                        {
                            "description": "Promoting or selling potentially illicit goods, services, or activities.",
                            "lang": "en",
                            "name": "Illicit"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "warn",
                    "identifier": "misinformation",
                    "locales": [
                        {
                            "description": "Spreading false or misleading info, including unverified claims and harmful conspiracy theories.",
                            "lang": "en",
                            "name": "Misinformation"
                        }
                    ],
                    "severity": "inform"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "warn",
                    "identifier": "rumor",
                    "locales": [
                        {
                            "description": "Approach with caution, as these claims lack evidence from credible sources.",
                            "lang": "en",
                            "name": "Rumor"
                        }
                    ],
                    "severity": "inform"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "rude",
                    "locales": [
                        {
                            "description": "Rude or impolite, including crude language and disrespectful comments, without constructive purpose.",
                            "lang": "en",
                            "name": "Rude"
                        }
                    ],
                    "severity": "inform"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "extremist",
                    "locales": [
                        {
                            "description": "Radical views advocating violence, hate, or discrimination against individuals or groups.",
                            "lang": "en",
                            "name": "Extremist"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "warn",
                    "identifier": "sensitive",
                    "locales": [
                        {
                            "description": "May be upsetting, covering topics like substance abuse or mental health issues, cautioning sensitive viewers.",
                            "lang": "en",
                            "name": "Sensitive"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "engagement-farming",
                    "locales": [
                        {
                            "description": "Insincere content or bulk actions aimed at gaining followers, including frequent follows, posts, and likes.",
                            "lang": "en",
                            "name": "Engagement Farming"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": false,
                    "blurs": "content",
                    "defaultSetting": "hide",
                    "identifier": "inauthentic",
                    "locales": [
                        {
                            "description": "Bot or a person pretending to be someone else.",
                            "lang": "en",
                            "name": "Inauthentic Account"
                        }
                    ],
                    "severity": "alert"
                },
                {
                    "adultOnly": true,
                    "blurs": "media",
                    "defaultSetting": "show",
                    "identifier": "sexual-figurative",
                    "locales": [
                        {
                            "description": "Art with explicit or suggestive sexual themes, including provocative imagery or partial nudity.",
                            "lang": "en",
                            "name": "Sexually Suggestive (Cartoon)"
                        }
                    ],
                    "severity": "none"
                }
            ],
            "labelValues": [
                "!hide",
                "!warn",
                "porn",
                "sexual",
                "nudity",
                "sexual-figurative",
                "graphic-media",
                "self-harm",
                "sensitive",
                "extremist",
                "intolerant",
                "threat",
                "rude",
                "illicit",
                "security",
                "unsafe-link",
                "impersonation",
                "misinformation",
                "scam",
                "engagement-farming",
                "spam",
                "rumor",
                "misleading",
                "inauthentic"
            ]
        }
    }
""".trimIndent()


val labelerDef = LabelerViewDetailed(
    uri = AtUri("at://did:plc:ar7c4by46qjdydhdevvrndac/app.bsky.labeler.service/self"),
    cid = Cid("bafyreigklczuekgj7q2pj3ys5ck3ah7v2ps5qbly6viwcj26vrmigbfyce"),
    creator = ProfileView(
        did = Did("did:plc:ar7c4by46qjdydhdevvrndac"),
        handle = Handle("moderation.bsky.app"),
        displayName = "Bluesky Moderation Service",
        avatar = null,
        viewer = ViewerState(
            muted = false,
            blockedBy = false,
            blockingByList = null,
            blocking = null,
            following = null,
            followedBy = null,
            knownFollowers = null,
        ),
        labels = persistentListOf(
            Label(
                ver = 1.toLong(),
                src = Did("did:plc:e4elbtctnfqocyfcml6h2lf7"),
                `val` = "bluesky-elder",
                cts = Timestamp.parse("2024-05-11T02:11:29.404Z"),
                exp = null,
                sig = null,
                uri = AtUri("did:plc:ar7c4by46qjdydhdevvrndac"),
            )
        ),
        createdAt = Timestamp.parse("2023-04-11T17:29:51.242Z"),
        indexedAt = Timestamp.parse("2024-03-19T17:14:40.242Z"),
        description = "Official Bluesky moderation service. https://bsky.social/about/support/community-guidelines",
    ),
    likeCount = 3,
    viewer = LabelerViewerState(),
    indexedAt = Timestamp.parse("2024-03-13T15:52:53.522Z"),
    labels = persistentListOf(),
    policies = LabelerPolicies(
        labelValues = persistentListOf(
            LabelValue.Hide,
            LabelValue.Warn,
            LabelValue.Porn,
            LabelValue.Sexual,
            LabelValue.Nudity,
            LabelValue.from("sexual-figurative"),
            LabelValue.GraphicMedia,
            LabelValue.from("self-harm"),
            LabelValue.from("sensitive"),
            LabelValue.from("extremist"),
            LabelValue.from("intolerant"),
            LabelValue.from("threat"),
            LabelValue.from("rude"),
            LabelValue.from("illicit"),
            LabelValue.from("security"),
            LabelValue.from("unsafe-link"),
            LabelValue.from("impersonation"),
            LabelValue.from("misinformation"),
            LabelValue.from("scam"),
            LabelValue.from("engagement-farming"),
            LabelValue.from("spam"),
            LabelValue.from("rumor"),
            LabelValue.from("misleading"),
            LabelValue.from("inauthentic")
        ),
        labelValueDefinitions = persistentListOf(
            LabelValueDefinition(
                identifier = "spam",
                severity = Severity.INFORM,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Spam",
                        description = "Unwanted, repeated, or unrelated actions that bother users.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "impersonation",
                severity = Severity.INFORM,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Impersonation",
                        description = "Pretending to be someone else without permission.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "scam",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Scam",
                        description = "Scams, phishing & fraud.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "intolerant",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Intolerance",
                        description = "Discrimination against protected groups.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "self-harm",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Self-Harm",
                        description = "Promotes self-harm, including graphic images, glorifying discussions, or triggering stories.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "security",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Security Concerns",
                        description = "May be unsafe and could harm your device, steal your info, or get your account hacked.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "misleading",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Misleading",
                        description = "Altered images/videos, deceptive links, or false statements.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "threat",
                severity = Severity.INFORM,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Threats",
                        description = "Promotes violence or harm towards others, including threats, incitement, or advocacy of harm.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "unsafe-link",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Unsafe link",
                        description = "Links to harmful sites with malware, phishing, or violating content that risk security and privacy.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "illicit",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Illicit",
                        description = "Promoting or selling potentially illicit goods, services, or activities.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "misinformation",
                severity = Severity.INFORM,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Misinformation",
                        description = "Spreading false or misleading info, including unverified claims and harmful conspiracy theories.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "rumor",
                severity = Severity.INFORM,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Rumor",
                        description = "Approach with caution, as these claims lack evidence from credible sources.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "rude",
                severity = Severity.INFORM,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Rude",
                        description = "Rude or impolite, including crude language and disrespectful comments, without constructive purpose.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "extremist",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Extremist",
                        description = "Radical views advocating violence, hate, or discrimination against individuals or groups.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "sensitive",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Sensitive",
                        description = "May be upsetting, covering topics like substance abuse or mental health issues, cautioning sensitive viewers.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "engagement-farming",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Engagement Farming",
                        description = "Insincere content or bulk actions aimed at gaining followers, including frequent follows, posts, and likes.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "inauthentic",
                severity = Severity.ALERT,
                blurs = Blurs.CONTENT,
                defaultSetting = DefaultSetting.WARN,
                adultOnly = false,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Inauthentic Account",
                        description = "Bot or a person pretending to be someone else.",
                    )
                ),
            ),
            LabelValueDefinition(
                identifier = "sexual-figurative",
                severity = Severity.NONE,
                blurs = Blurs.MEDIA,
                defaultSetting = DefaultSetting.SHOW,
                adultOnly = true,
                locales = persistentListOf(
                    LabelValueDefinitionStrings(
                        lang = Language("en"),
                        name = "Sexually Suggestive (Cartoon)",
                        description = "Art with explicit or suggestive sexual themes, including provocative imagery or partial nudity.",
                    )
                ),
            ),
        )
    ),
)