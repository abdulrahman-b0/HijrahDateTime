package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toKotlinInstant
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierIslamicUmmAlQura
import platform.Foundation.NSCalendarUnit
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitNanosecond
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitWeekOfYear
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.localeWithLocaleIdentifier
import kotlin.time.Instant

actual fun LocalTime.format(format: HijrahDateTimeFormat): String {

    val dateWithTime = requireNotNull(
        format.nsFormatter.calendar.dateBySettingHour(
            h = hour.toLong(),
            minute = minute.toLong(),
            second = second.toLong(),
            ofDate = NSDate(),
            options = 0UL
        )
    ) { "Invalid time components: $hour:$minute:$second" }

    return format.nsFormatter.stringFromDate(dateWithTime)
}

internal fun DateTimeUnit.toNSCalendarUnit(): NSCalendarUnit {
    return when (this) {
        DateTimeUnit.NANOSECOND -> NSCalendarUnitNanosecond
        DateTimeUnit.SECOND -> NSCalendarUnitSecond
        DateTimeUnit.MINUTE -> NSCalendarUnitMinute
        DateTimeUnit.HOUR -> NSCalendarUnitHour
        DateTimeUnit.DAY -> NSCalendarUnitDay
        DateTimeUnit.WEEK -> NSCalendarUnitWeekOfYear
        DateTimeUnit.MONTH -> NSCalendarUnitMonth
        DateTimeUnit.YEAR -> NSCalendarUnitYear
        else -> throw IllegalArgumentException("Unsupported DateTimeUnit: The unit $this cannot be mapped to NSCalendarUnit")
    }
}

internal fun DateTimeUnit.toNSDateComponents(value: Long): NSDateComponents {
    val components = NSDateComponents()
    when (this) {
        DateTimeUnit.NANOSECOND -> components.nanosecond = value
        DateTimeUnit.MICROSECOND -> components.nanosecond = value * 1_000
        DateTimeUnit.MILLISECOND -> components.nanosecond = value * 1_000_000
        DateTimeUnit.SECOND -> components.second = value
        DateTimeUnit.MINUTE -> components.minute = value
        DateTimeUnit.HOUR -> components.hour = value
        DateTimeUnit.DAY -> components.day = value
        DateTimeUnit.WEEK -> components.day = value * 7
        DateTimeUnit.MONTH -> components.month = value
        DateTimeUnit.YEAR -> components.year = value
        DateTimeUnit.CENTURY -> components.year = value * 100
        else -> throw IllegalArgumentException("Unsupported DateTimeUnit: $this")
    }
    return components
}

actual fun Instant.Companion.parseHijriOrNull(value: String): Instant? {
    // Creating a dedicated ISO formatter for Hijri parsing
    val formatter = NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd'T'HH:mm:ssZZZZZ" // The absolute standard for ISO with offset
        calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
        locale = NSLocale.localeWithLocaleIdentifier("en_US_POSIX") // Essential for fixed-format parsing
    }
    return formatter.dateFromString(value)?.toKotlinInstant()
}

actual fun Instant.Companion.parseHijri(value: String): Instant {
    return requireNotNull(parseHijriOrNull(value)) { "Invalid Hijri date: $value" }
}

actual fun DayOfWeek.getDisplayName(
    nameStyle: NameStyle,
    locale: FormatLocale
): String {
    val formatter = NSDateFormatter().apply {
        this.locale = locale
    }

    val names = when (nameStyle) {
        NameStyle.FULL -> formatter.standaloneWeekdaySymbols
        NameStyle.ABBREVIATED -> formatter.shortStandaloneWeekdaySymbols
    }

    // kotlinx.datetime.DayOfWeek: Monday (1) .. Sunday (7)
    // NSDateFormatter symbols: Sunday (index 0) .. Saturday (index 6)
    val appleIndex = if (this.isoDayNumber == 7) 0 else this.isoDayNumber

    return names[appleIndex] as String
}


internal fun DateTimePeriod.toNSDateComponents(multiplier: Int = 1): NSDateComponents = NSDateComponents().apply {
    this.year = years.toLong() * multiplier
    this.month = months.toLong() * multiplier
    this.day = days.toLong() * multiplier
    this.hour = hours.toLong() * multiplier
    this.minute = minutes.toLong() * multiplier
    this.second = seconds.toLong() * multiplier
    this.nanosecond = nanoseconds.toLong() * multiplier
}

internal fun getDayOfWeak(calendar: NSCalendar, date: NSDate): DayOfWeek {

    val sundayIndexed = calendar.component(NSCalendarUnitWeekday, date).toInt()
    // Apple: 1=Sun, 2=Mon... 7=Sat.
    // kotlinx.datetime.DayOfWeek: Mon=1, Tue=2... Sun=7
    return when(sundayIndexed) {
        1 -> DayOfWeek.SUNDAY
        2 -> DayOfWeek.MONDAY
        3 -> DayOfWeek.TUESDAY
        4 -> DayOfWeek.WEDNESDAY
        5 -> DayOfWeek.THURSDAY
        6 -> DayOfWeek.FRIDAY
        else -> DayOfWeek.SATURDAY
    }
}