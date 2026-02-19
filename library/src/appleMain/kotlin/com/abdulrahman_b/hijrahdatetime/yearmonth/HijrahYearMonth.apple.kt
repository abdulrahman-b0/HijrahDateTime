package com.abdulrahman_b.hijrahdatetime.yearmonth

import com.abdulrahman_b.hijrahdatetime.DateTimeParseException
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear

actual fun HijrahYearMonth.Companion.parseOrNull(text: String): HijrahYearMonth? {
    val formatter = HijrahYearMonth.Format.nsFormatter
    val calendar = formatter.calendar
    val date = formatter.dateFromString(text) ?: return null

    return HijrahYearMonth(
        year = calendar.component(NSCalendarUnitYear, date).toInt(),
        month = calendar.component(NSCalendarUnitMonth, date).toInt()
    )
}

actual fun HijrahYearMonth.Companion.parse(text: String): HijrahYearMonth {
    return parseOrNull(text) ?:
        throw DateTimeParseException("Could not parse `HijrahYearMonth` from the string value of '$text' using the format '${HijrahYearMonth.Format.nsFormatter.dateFormat}'")
}