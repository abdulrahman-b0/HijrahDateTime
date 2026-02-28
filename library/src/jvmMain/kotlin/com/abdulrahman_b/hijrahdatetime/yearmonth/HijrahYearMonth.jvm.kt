package com.abdulrahman_b.hijrahdatetime.yearmonth

import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField

private val Format by lazy {

    DateTimeFormatter.ofPattern("yyyy-MM").withChronology(HijrahChronology.INSTANCE)
}

actual fun HijrahYearMonth.Companion.parse(text: String): HijrahYearMonth {
    val temporal = Format.javaFormatter.parse(text)
    return HijrahYearMonth(year = temporal.get(ChronoField.YEAR), month = temporal.get(ChronoField.MONTH_OF_YEAR))
}

actual fun HijrahYearMonth.Companion.parseOrNull(text: String): HijrahYearMonth? = try {
    parse(text)
} catch (_: DateTimeParseException) {
    null
}