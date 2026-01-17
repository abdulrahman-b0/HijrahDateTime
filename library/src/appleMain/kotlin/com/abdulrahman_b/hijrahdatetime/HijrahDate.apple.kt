package com.abdulrahman_b.hijrahdatetime

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.serialization.Serializable
import platform.Foundation.*

@Serializable(with = HijrahDateSerializer::class)
actual class HijrahDate (
    override val calendarDatePair: Pair<NSCalendar, NSDate>,
): Comparable<HijrahDate>, ComponentAccessors.DateBased {


    actual constructor(year: Int, month: Int, dayOfMonth: Int): this(createDate(year, month, dayOfMonth))

    constructor(calendar: NSCalendar, date: NSDate): this(calendar to date)

    actual override fun compareTo(other: HijrahDate): Int =
        calendarDatePair.second.compare(other.calendarDatePair.second).toInt()

    actual operator fun plus(period: DatePeriod): HijrahDate {
        return HijrahDate(
            calendar = calendar,
            date = requireNotNull(calendar.dateByAddingComponents(period.toNSDateComponents(),
                calendarDatePair.second, 0UL)) {
                "Invalid date after addition: $period"
            }
        )
    }

    actual operator fun minus(period: DatePeriod): HijrahDate {
        return HijrahDate(
            calendar = calendar,
            date = requireNotNull(calendar.dateByAddingComponents(period.toNSDateComponents(-1),
                calendarDatePair.second, 0UL)) {
                "Invalid date after addition: $period"
            }
        )
    }


    actual fun plus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate {
        val newDate = calendar.dateByAddingComponents(unit.toNSDateComponents(value.toLong()),
            calendarDatePair.second, options = 0UL)
        requireNotNull(newDate) { "Invalid date after addition: $value $unit" }
        return HijrahDate(calendar, newDate)
    }

    actual fun minus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate {
        val newDate = calendar.dateByAddingComponents(unit.toNSDateComponents(-value.toLong()),
            calendarDatePair.second, options = 0UL)
        requireNotNull(newDate) { "Invalid date after subtraction: $value $unit" }
        return HijrahDate(calendar, newDate)
    }


    actual fun toEpochDays(): Long {
        // 1. Create a date representing the Unix Epoch (Jan 1, 1970)
        val epochDate = NSDate.dateWithTimeIntervalSince1970(0.0)

        // 2. Use the calendar to calculate the components (days) between epoch and current date
        val components = calendar.components(
            NSCalendarUnitDay,
            fromDate = epochDate,
            toDate = calendarDatePair.second,
            options = 0UL
        )

        return components.day
    }

    actual fun format(format: HijrahDateTimeFormat): String {
        // Ensure the formatter uses this date's specific calendar instance
        // if it wasn't already set during build()
        format.nsFormatter.calendar = this.calendar
        return format.nsFormatter.stringFromDate(calendarDatePair.second)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun range(unit: DateTimeUnit.DateBased): ValueRange {
        val unit = unit.toNSCalendarUnit()
        calendar.rangeOfUnit(unit, inUnit = NSCalendarUnitEra, forDate = calendarDatePair.second).let { range ->
            range.useContents {
                return ValueRange(minimum = location.toLong(), maximum = (location + length - 1u).toLong())
            }
        }
    }

    actual companion object {
        actual fun parse(
            string: String,
            format: HijrahDateTimeFormat,
        ): HijrahDate {
            return requireNotNull(parseOrNull(string, format)) {
                "Invalid date: $string"
            }
        }

        actual fun parseOrNull(
            string: String,
            format: HijrahDateTimeFormat,
        ): HijrahDate? {
            format.nsFormatter.calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
            return format.nsFormatter.dateFromString(string)?.let { nSDate ->
                HijrahDate(calendar = format.nsFormatter.calendar, date = nSDate)
            }
        }

        private fun createDate(year: Int, month: Int, dayOfMonth: Int): Pair<NSCalendar, NSDate> {
            val nsCalendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
            val components = NSDateComponents().apply {
                this.year = year.toLong()
                this.month = month.toLong()
                this.day = dayOfMonth.toLong()
            }
            return nsCalendar to requireNotNull(nsCalendar.dateFromComponents(components)) {
                "Invalid date components: $year-$month-$dayOfMonth"
            }
        }

        actual fun fromEpochDays(epochDay: Long): HijrahDate {
            val nsCalendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
            // One day has 86400 seconds.
            // NSDate expects TimeInterval (Double) in seconds since 1970-01-01 00:00:00 UTC
            val secondsSince1970 = epochDay * SECONDS_PER_DAY
            val nsDate = NSDate.dateWithTimeIntervalSince1970(secondsSince1970)

            return HijrahDate(nsCalendar, nsDate)
        }

        @OptIn(ExperimentalForeignApi::class)
        actual val MIN: HijrahDate by lazy {
            val calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
            val yearRange = calendar.rangeOfUnit(NSCalendarUnitYear, inUnit = NSCalendarUnitEra, forDate = NSDate())
            // .location is the start of the range (minimum year)
            val minYear = yearRange.useContents { location.toInt() }

            val components = NSDateComponents().apply {
                this.year = minYear.toLong(); this.month = 1; this.day = 1
            }
            val date = requireNotNull(calendar.dateFromComponents(components)) {
                "Failed to create NSDate for minimum HijrahDate"
            }
            HijrahDate(calendar, date)
        }

        @OptIn(ExperimentalForeignApi::class)
        actual val MAX: HijrahDate by lazy {
            val calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
            val yearRange = calendar.rangeOfUnit(NSCalendarUnitYear, inUnit = NSCalendarUnitEra, forDate = NSDate())
            // .length is the number of units; location + length - 1 is the last valid year
            val maxYear = yearRange.useContents { (location + length - 1u).toInt() }

            val components = NSDateComponents().apply {
                this.year = maxYear.toLong()
                this.month = 12
            }
            // Get the last day of the last month
            val lastMonthDate = requireNotNull(calendar.dateFromComponents(components))
            val dayRange = calendar.rangeOfUnit(NSCalendarUnitDay, inUnit = NSCalendarUnitMonth, forDate = lastMonthDate)
            components.day = dayRange.useContents { (location + length - 1u).toLong() }

            val date = requireNotNull(calendar.dateFromComponents(components))
            HijrahDate(calendar, date)
        }
        private const val SECONDS_PER_DAY = 86400.0
    }


}

