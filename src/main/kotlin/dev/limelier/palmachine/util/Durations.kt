package dev.limelier.palmachine.util

import java.time.Duration

fun Duration.humanHMS(): String = buildString {
    val hours = toHoursPart()
    if (hours > 0) {
        append("$hours ")
        append(if (hours == 1) "hour" else "hours")
    }

    val minutes = toMinutesPart()
    if (minutes > 0) {
        if (hours > 0) append(", ")
        append("$minutes ")
        append(if (minutes == 1) "minute" else "minutes")
    }

    if (hours > 0 || minutes > 0) {
        append(" and ")
    }

    val seconds = toSecondsPart()
    append("$seconds ")
    append(if (seconds == 1) "second" else "seconds")
}
