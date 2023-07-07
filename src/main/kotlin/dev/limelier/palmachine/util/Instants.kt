package dev.limelier.palmachine.util

import net.dv8tion.jda.api.utils.TimeFormat
import net.dv8tion.jda.api.utils.Timestamp
import java.time.Instant

fun Instant.timestamp(format: TimeFormat): Timestamp = format.atInstant(this)
