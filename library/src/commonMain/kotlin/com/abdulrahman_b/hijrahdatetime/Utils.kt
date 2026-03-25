package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.FormatLocale
import com.abdulrahman_b.hijrahdatetime.format.FormatLocales
import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.format.NameStyle
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlin.time.Instant

expect fun LocalTime.format(format: HijrahDateTimeFormat): String

expect fun DayOfWeek.getDisplayName(nameStyle: NameStyle, locale: FormatLocale = FormatLocales.getDefault()): String

expect fun Instant.Companion.parseHijriOrNull(value: String): Instant?

expect fun Instant.Companion.parseHijri(value: String): Instant

