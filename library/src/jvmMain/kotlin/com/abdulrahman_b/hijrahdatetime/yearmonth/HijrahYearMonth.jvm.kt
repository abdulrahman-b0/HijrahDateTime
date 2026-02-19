package com.abdulrahman_b.hijrahdatetime.yearmonth

import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField

actual fun HijrahYearMonth.Companion.parse(text: String): HijrahYearMonth {
    val formatter = HijrahYearMonth.Format.javaFormatter
    val accessor = formatter.parse(text)
    return HijrahYearMonth(
        accessor.get(ChronoField.YEAR), accessor.get(ChronoField.MONTH_OF_YEAR)
    )
}

actual fun HijrahYearMonth.Companion.parseOrNull(text: String): HijrahYearMonth? {
    return try {
        parse(text)
    } catch (_: DateTimeParseException) {
        null
    }
}