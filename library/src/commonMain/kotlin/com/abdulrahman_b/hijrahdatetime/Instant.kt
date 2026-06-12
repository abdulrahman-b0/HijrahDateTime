package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import kotlinx.datetime.UtcOffset
import kotlin.time.Instant

expect fun Instant.Companion.parseHijriOrNull(value: String): Instant?

expect fun Instant.Companion.parseHijri(value: String): Instant

expect fun Instant.format(format: HijrahDateTimeFormat, offset: UtcOffset = UtcOffset.ZERO): String