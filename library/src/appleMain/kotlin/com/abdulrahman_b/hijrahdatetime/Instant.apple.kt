package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierIslamicUmmAlQura
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.localeWithLocaleIdentifier
import platform.Foundation.timeZoneForSecondsFromGMT
import kotlin.time.Instant

actual fun Instant.Companion.parseHijriOrNull(value: String): Instant? {
    // Creating a dedicated ISO formatter for Hijri parsing
    val formatter = NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd'T'HH:mm:ssZZZZZ" // The absolute standard for ISO with offset
        calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
        locale = NSLocale.localeWithLocaleIdentifier("en_US_POSIX") // Essential for fixed-format parsing
    }
    return formatter.dateFromString(value)?.toKotlinInstant()?.also {
        //Strict parsing to avoid having out-of-range values, this will throw an exception if the date is out of hijri date range
        //This means parsing year of 2026 won't work, which protects accidentally parsing gregorian dates as hijri dates and leading to incorrect instant.
        try {
            it.toHijrahDateTime(TimeZone.UTC)
        } catch (_: Exception) {
            return null
        }

    }
}

actual fun Instant.Companion.parseHijri(value: String): Instant {
    return requireNotNull(parseHijriOrNull(value)) { "Invalid Hijri date: $value" }
}


actual fun Instant.format(
    format: HijrahDateTimeFormat,
    offset: UtcOffset
): String {
    val formatter = format.nsFormatter
    // Apply the offset by setting the timezone
    formatter.calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
    formatter.timeZone = NSTimeZone.timeZoneForSecondsFromGMT(offset.totalSeconds.toLong())

    return formatter.stringFromDate(toNSDate())
}