package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateComponentsSerializer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeArithmeticException
import kotlinx.datetime.DateTimeUnit
import kotlinx.serialization.Serializable
import platform.Foundation.*

@Serializable(with = HijrahDateComponentsSerializer::class)
actual class HijrahDate private constructor(
    override val nsCalendar: NSCalendar,
    override val nsDate: NSDate,
    skipValidation: Boolean //Internal flag to avoid infinite recursion in init block check.
): Comparable<HijrahDate>, ComponentAccessors.DateBased {

    init {
        @Suppress("ConvertTwoComparisonsToRangeCheck") //It shouldn't, it causes a recursive calls because the range class is constructing HijrahDate internally.
        if (!skipValidation && (this < MIN || this > MAX)) {
            throw IllegalArgumentException(
                "HijrahDate is out of range. Valid range is from ${MIN.format(HijrahDateTimeFormats.DATE_ISO)} to ${MAX.format(HijrahDateTimeFormats.DATE_ISO)}"
            )
        }
    }
    actual constructor(year: Int, month: Int, dayOfMonth: Int): this(createDate(year, month, dayOfMonth), skipValidation = false)

    private constructor(year: Int, month: Int, dayOfMonth: Int, skipValidation: Boolean): this(createDate(year, month, dayOfMonth), skipValidation = skipValidation)
    private constructor(calendarDatePair: Pair<NSCalendar, NSDate>, skipValidation: Boolean): this(calendarDatePair.first, calendarDatePair.second, skipValidation)

    constructor(calendar: NSCalendar, date: NSDate): this(calendar, date, skipValidation = false) {
        val components = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = date
        )
        if (components.year != year.toLong() || components.month != month.toLong() || components.day != dayOfMonth.toLong()) {
            throw IllegalArgumentException("Invalid date for Hijrah calendar: $year-$month-$dayOfMonth")
        }
    }

    actual override fun compareTo(other: HijrahDate): Int =
        nsDate.compare(other.nsDate).toInt()

    actual operator fun plus(period: DatePeriod): HijrahDate {
        return try {
            HijrahDate(
                calendar = nsCalendar,
                date = nsCalendar.dateByAddingComponents(period.toNSDateComponents(), nsDate, 0UL)
                    ?: throw DateTimeArithmeticException("Invalid date after addition: $period")
            )
        } catch (e: IllegalArgumentException) {
            throw DateTimeArithmeticException(e.message.toString(), e)
        }
    }

    actual operator fun minus(period: DatePeriod): HijrahDate {
        return try {
            HijrahDate(
                calendar = nsCalendar,
                date = nsCalendar.dateByAddingComponents(period.toNSDateComponents(-1), nsDate, 0UL)
                    ?: throw DateTimeArithmeticException("Invalid date after subtraction: $period")
            )
        } catch (e: IllegalArgumentException) {
            throw DateTimeArithmeticException(e.message.toString(), e)
        }
    }


    actual fun plus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate {
        val newDate = nsCalendar.dateByAddingComponents(unit.toNSDateComponents(value.toLong()), nsDate, options = 0UL) ?:
            throw DateTimeArithmeticException("Invalid date after addition: $value $unit")
        return try {
            HijrahDate(nsCalendar, newDate)
        } catch (e: IllegalArgumentException) {
            throw DateTimeArithmeticException(e.message.toString(), e)
        }
    }

    actual fun minus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate {
        val newDate = nsCalendar.dateByAddingComponents(unit.toNSDateComponents(-value.toLong()),
            nsDate, options = 0UL) ?:
                throw DateTimeArithmeticException("Invalid date after subtraction: $value $unit")
        return try {
            HijrahDate(nsCalendar, newDate)
        } catch (e: IllegalArgumentException) {
            throw DateTimeArithmeticException(e.message.toString(), e)
        }
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
            toDate = nsDate,
            options = 0UL
        )

        return components.day
    }

    actual fun format(format: HijrahDateTimeFormat): String {
        // Ensure the formatter uses this date's specific calendar instance
        // if it wasn't already set during build()
        format.nsFormatter.calendar = this.nsCalendar
        return format.nsFormatter.stringFromDate(nsDate)
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
        nsCalendar.rangeOfUnit(smaller = nsUnit, inUnit = inUnit, forDate = nsDate).let { range ->
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
                throw IllegalArgumentException("Could not parse `HijrahDate` from '$string' using the date format of '${format.nsFormatter.dateFormat}'")
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
            HijrahDate(MIN_YEAR, 1, 1, skipValidation = true)
        }

        @OptIn(ExperimentalForeignApi::class)
        actual val MAX: HijrahDate by lazy {
            HijrahDate(MAX_YEAR, 12, 30, skipValidation = true)
        }
        private const val SECONDS_PER_DAY = 86400.0
    }


}

