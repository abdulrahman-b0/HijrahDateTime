package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.FormatLocale
import com.abdulrahman_b.hijrahdatetime.format.FormatLocales
import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.format.NameStyle
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

expect fun LocalTime.format(format: HijrahDateTimeFormat): String

expect fun DayOfWeek.getDisplayName(nameStyle: NameStyle, locale: FormatLocale = FormatLocales.getDefault()): String

