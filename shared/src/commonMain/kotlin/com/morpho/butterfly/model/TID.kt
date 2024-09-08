package com.morpho.butterfly.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.jvm.JvmStatic
import kotlin.math.floor
import kotlin.random.Random
import kotlin.time.Duration.Companion.microseconds



class TID(str: String) {
    val str: String
    init {
        require(str.length == TID_LENGTH) {
            "TID length must be $TID_LENGTH, but was ${str.length}"
        }
        this.str = str.replace("-", "")
    }

    companion object {

        const val TID_LENGTH = 64
        @JvmStatic
        private var lastTimestamp = Instant.DISTANT_PAST

        @JvmStatic
        private var clockId: Double? = null

        @JvmStatic
        fun next(prev: TID? = null): TID {
            val now = if(lastTimestamp > Clock.System.now()) lastTimestamp else Clock.System.now()
            lastTimestamp = now
            if(clockId == null) {
                clockId = floor(Random.nextDouble() * 32.0)
            }
            val tid = fromTime(now, clockId!!)
            println("tid: $tid")
            return if (prev == null || tid.newerThan(prev)) {
                tid
            } else fromTime(prev.timestamp().plus(1.microseconds), clockId!!)
        }

        @JvmStatic
        fun fromTime(time: Instant, clockId: Double): TID {
            val timestamp: ULong = time.toEpochMilliseconds()
                .toULong() * 1000.toULong() + time.nanosecondsOfSecond
                    .toULong() / 1000.toULong()
            val str = "${s32encode(timestamp.toDouble())}${s32encode(clockId).padStart(2, '2')}"
            return TID(str)
        }

        @JvmStatic
        fun oldestFirst(a: TID, b: TID): Int {
            return a.compareTo(b)
        }

        @JvmStatic
        fun newestFirst(a: TID, b: TID): Int {
            return b.compareTo(a)
        }

        @JvmStatic
        fun nextStr(prev: String): String {
            return next(TID(prev)).str
        }

    }

    fun timestamp(): Instant {
        val timestamp = s32decode(str.substring(0, 11))
        val timestampSeconds = timestamp / 1000.0 / 1000.0
        val nanoseconds = (timestampSeconds % 1.0) * 100_000_000.0
        return Instant.fromEpochSeconds(timestampSeconds.toLong(), nanoseconds.toLong())
    }

    fun clockId(): Double {
        return s32decode(str.substring(11, 13))
    }

    fun formatted(): String {
        val str = this.toString()
        return "${str.substring(0, 4)}-${str.substring(4, 7)}-${str.substring(7, 11)}-${str.substring(11, 13)}"
    }

    override fun toString(): String {
        return str
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TID) return false
        return str == other.str
    }

    operator fun compareTo(other: TID): Int {
        return if (str < other.str) -1 else if (str > other.str) 1 else 0
    }

    fun newerThan(other: TID): Boolean {
        return this > other
    }

    fun olderThan(other: TID): Boolean {
        return this < other
    }

    override fun hashCode(): Int {
        return str.hashCode()
    }
}

@Suppress("SpellCheckingInspection")
const val S32_CHAR = "234567abcdefghijklmnopqrstuvwxyz"

fun s32encode(value: Double): String {
    var s = ""
    var i = value
    while(i > 0) {
        val c = i.toLong() % 32
        i = floor(i / 32)
        s = S32_CHAR[c.toInt()] + s
    }
    return s
}



fun s32decode(s: String): Double {
    var i = 0.0
    for (c in s) {
        i = i * 32 + S32_CHAR.indexOf(c)
    }
    return i
}