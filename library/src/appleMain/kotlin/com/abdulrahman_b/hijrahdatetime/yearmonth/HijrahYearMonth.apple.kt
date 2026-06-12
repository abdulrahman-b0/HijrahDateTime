package com.abdulrahman_b.hijrahdatetime.yearmonth

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierIslamicUmmAlQura
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter


actual fun HijrahYearMonth.Companion.parse(text: String): HijrahYearMonth {
    val calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
    val formatter = NSDateFormatter()
    formatter.calendar = calendar
    formatter.dateFormat = "yyyy-MM"
    val date = requireNotNull(formatter.dateFromString(text))
    return HijrahYearMonth(
        year = calendar.component(NSCalendarUnitYear, date).toInt(),
        month = calendar.component(NSCalendarUnitMonth, date).toInt()
    )
}
actual fun HijrahYearMonth.Companion.parseOrNull(text: String): HijrahYearMonth? = try { parse(text) } catch(e: Exception) { null }


/** Converts this [HijrahYearMonth] to [NSDateComponents]. */
fun HijrahYearMonth.toNSDateComponents(): NSDateComponents {
    return NSDateComponents().apply {
        this.calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
        this.year = this@toNSDateComponents.year.toLong()
        this.month = this@toNSDateComponents.month.number.toLong()
    }
}