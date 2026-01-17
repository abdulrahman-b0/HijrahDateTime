package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.*
import kotlin.time.Instant

expect fun LocalTime.format(format: HijrahDateTimeFormat): String

expect fun DayOfWeek.getDisplayName(nameStyle: NameStyle, locale: FormatLocale = FormatLocale.getDefault()): String

expect fun Instant.Companion.parseHijriOrNull(value: String): Instant?

expect fun Instant.Companion.parseHijri(value: String): Instant

