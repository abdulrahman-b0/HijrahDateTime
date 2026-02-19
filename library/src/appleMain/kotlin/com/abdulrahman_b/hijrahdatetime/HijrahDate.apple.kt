package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateComponentsSerializer
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateTimeComponentsSerializer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.toNSDate
import kotlinx.serialization.Serializable
import platform.Foundation.*
import kotlin.time.Clock

@Serializable(with = HijrahDateComponentsSerializer::class)
actual class HijrahDate (
    override val calendarDatePair: Pair<NSCalendar, NSDate>,
): Comparable<HijrahDate>, ComponentAccessors.DateBased {

    init {
        if (this !in MIN..MAX) throw DateTimeException(
            "HijrahDate is out of range. Valid range is from ${MIN.format(HijrahDateTimeFormats.DATE_ISO)} to ${MAX.format(HijrahDateTimeFormats.DATE_ISO)}"
        )
    }


    actual constructor(year: Int, month: Int, dayOfMonth: Int): this(createDate(year, month, dayOfMonth))

    constructor(calendar: NSCalendar, date: NSDate): this(calendar to date) {
        val components = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = date
        )
        if (components.year != year.toLong() || components.month != month.toLong() || components.day != dayOfMonth.toLong()) {
            throw IllegalArgumentException("Invalid date for Hijrah calendar: $year-$month-$dayOfMonth")
        }
    }

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
        val utcCalendar = NSCalendar.currentCalendar.apply {
            timeZone = NSTimeZone.timeZoneWithAbbreviation("UTC")!!
        }

        // 2. Use the calendar to calculate the components (days) between epoch and current date
        val components = utcCalendar.components(
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HijrahDate) return false
        return this.toEpochDays() == other.toEpochDays()
    }

    override fun hashCode(): Int = this.toEpochDays().hashCode()

    override fun toString(): String = format(HijrahDateTimeFormats.DATE_ISO)

    @OptIn(ExperimentalForeignApi::class)
    actual fun range(unit: DateTimeUnit.DateBased): ValueRange {
        val nsUnit = unit.toNSCalendarUnit()
        val inUnit = when (nsUnit) {
            NSCalendarUnitDay -> NSCalendarUnitMonth
            NSCalendarUnitMonth -> NSCalendarUnitYear
            else -> NSCalendarUnitEra
        }
        calendar.rangeOfUnit(smaller = nsUnit, inUnit = inUnit, forDate = calendarDatePair.second).let { range ->
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
            return parseOrNull(string, format) ?:
                throw DateTimeParseException("Could not parse `HijrahDate` from '$string' using the date format of '${format.nsFormatter.dateFormat}'")
        }

        actual fun parseOrNull(
            string: String,
            format: HijrahDateTimeFormat,
        ): HijrahDate? {
            format.nsFormatter.calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
            return format.nsFormatter.dateFromString(string)?.let { nSDate ->
                HijrahDate(calendar = format.nsFormatter.calendar, date = nSDate)
            }?.also {
                if (it > MAX) throw DateTimeException(
                    "HijrahDate is out of range. Max is ${MAX.format(format)}"
                )
            }
        }

        private fun createDate(year: Int, month: Int, dayOfMonth: Int): Pair<NSCalendar, NSDate> {
            val nsCalendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
            nsCalendar.timeZone = NSTimeZone.timeZoneWithAbbreviation("UTC")!!
            val components = NSDateComponents().apply {
                this.year = year.toLong()
                this.month = month.toLong()
                this.day = dayOfMonth.toLong()
            }
            val date = nsCalendar.dateFromComponents(components)
            requireNotNull(date) { "Invalid date components: $year-$month-$dayOfMonth" }

            val validated = nsCalendar.components(
                NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
                fromDate = date
            )
            if (validated.year != year.toLong() || validated.month != month.toLong() || validated.day != dayOfMonth.toLong()) {
                throw IllegalArgumentException("Invalid date for Hijrah calendar: $year-$month-$dayOfMonth")
            }
            return nsCalendar to date
        }

        actual fun fromEpochDays(epochDay: Long): HijrahDate {
            val nsCalendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
//            nsCalendar.timeZone = NSTimeZone.timeZoneWithAbbreviation("UTC")!!
            // One day has 86400 seconds.
            // NSDate expects TimeInterval (Double) in seconds since 1970-01-01 00:00:00 UTC
            val secondsSince1970 = epochDay * SECONDS_PER_DAY
            val nsDate = NSDate.dateWithTimeIntervalSince1970(secondsSince1970)

            return HijrahDate(nsCalendar, nsDate)
        }

        private const val MIN_YEAR = 1300
        private const val MAX_YEAR = 1600

        @OptIn(ExperimentalForeignApi::class)
        actual val MIN: HijrahDate by lazy {
            //Hardcoded value to match the JVM behavior. I want the library behavior to be consistent as much as possible.
            HijrahDate(MIN_YEAR, 1, 1)
        }

        @OptIn(ExperimentalForeignApi::class)
        actual val MAX: HijrahDate by lazy {
            HijrahDate(MAX_YEAR, 12, 1).withLastDayOfMonth()
        }
        private const val SECONDS_PER_DAY = 86400.0
    }


}

