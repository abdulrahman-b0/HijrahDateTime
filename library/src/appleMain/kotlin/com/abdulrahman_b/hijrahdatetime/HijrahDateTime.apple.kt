package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateTimeComponentsSerializer
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import platform.Foundation.*
import kotlin.time.Instant

@Serializable(with = HijrahDateTimeComponentsSerializer::class)
actual class HijrahDateTime(
    override val calendarDatePair: Pair<NSCalendar, NSDate>,
) : Comparable<HijrahDateTime>, ComponentAccessors.DateTimeBased {

    actual val date: HijrahDate = HijrahDate(calendarDatePair)
    actual val time: LocalTime = LocalTime(
        hour = calendar.component(NSCalendarUnitHour, calendarDatePair.second).toInt(),
        minute = calendar.component(NSCalendarUnitMinute, calendarDatePair.second).toInt(),
        second = calendar.component(NSCalendarUnitSecond, calendarDatePair.second).toInt(),
        nanosecond = calendar.component(NSCalendarUnitNanosecond, calendarDatePair.second).toInt()
    )

    actual constructor(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hour: Int,
        minute: Int,
        second: Int,
        nanosecond: Int,
    ) : this(createDate(year, month, dayOfMonth, hour, minute, second, nanosecond))

    constructor(calendar: NSCalendar, date: NSDate): this(calendar to date) {
        val components = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                    NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond or NSCalendarUnitNanosecond,
            fromDate = date
        )
        if (components.year != year.toLong() || components.month != month.toLong() || components.day != dayOfMonth.toLong() ||
            components.hour != hour.toLong() || components.minute != minute.toLong() || components.second != second.toLong()
        ) {
            throw IllegalArgumentException("Invalid date-time for Hijrah calendar")
        }
    }

    actual override operator fun compareTo(other: HijrahDateTime): Int =
        calendarDatePair.second.compare(other.calendarDatePair.second).toInt()

    actual fun toInstant(timeZone: FixedOffsetTimeZone): Instant {
        return calendarDatePair.second.toKotlinInstant()
    }


    actual fun format(format: HijrahDateTimeFormat): String {
        // Ensure the formatter uses this date's specific calendar instance
        // if it wasn't already set during build()
        format.nsFormatter.calendar = this.calendar
        return format.nsFormatter.stringFromDate(calendarDatePair.second)
    }

    actual fun toLocalDateTime(): LocalDateTime {
        val isoCalendar = NSCalendar(NSCalendarIdentifierGregorian).apply {
            timeZone = NSTimeZone.timeZoneWithAbbreviation("UTC")!!
        }
        val date = calendarDatePair.second
        val components = isoCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                    NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond or NSCalendarUnitNanosecond,
            fromDate = date
        )
        return LocalDateTime(
            year = components.year.toInt(),
            monthNumber = components.month.toInt(),
            dayOfMonth = components.day.toInt(),
            hour = components.hour.toInt(),
            minute = components.minute.toInt(),
            second = components.second.toInt(),
            nanosecond = components.nanosecond.toInt()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HijrahDateTime) return false
        return this.calendarDatePair.second.isEqualToDate(other.calendarDatePair.second)
    }

    override fun hashCode(): Int = this.calendarDatePair.second.hashCode()

    override fun toString(): String = "HijrahDateTime($date, $time)"


    actual companion object {
        actual fun parse(string: String, format: HijrahDateTimeFormat): HijrahDateTime {
            return parseOrNull(string, format) ?:
                throw DateTimeParseException("Could not parse `HijrahDateTime` from '$string' using the date format of '${format.nsFormatter.dateFormat}'")
        }

        actual fun parseOrNull(
            string: String,
            format: HijrahDateTimeFormat
        ): HijrahDateTime? {
            return format.nsFormatter.dateFromString(string)?.let { nSDate ->
                HijrahDateTime(format.nsFormatter.calendar to nSDate)
            }
        }

        private fun createDate(
            year: Int,
            month: Int,
            dayOfMonth: Int,
            hour: Int,
            minute: Int,
            second: Int,
            nanosecond: Int,
        ): Pair<NSCalendar, NSDate> {
            val nsCalendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
            nsCalendar.timeZone = NSTimeZone.timeZoneWithAbbreviation("UTC")!!
            val components = NSDateComponents().apply {
                this.year = year.toLong()
                this.month = month.toLong()
                this.day = dayOfMonth.toLong()
                this.hour = hour.toLong()
                this.minute = minute.toLong()
                this.second = second.toLong()
                this.nanosecond = nanosecond.toLong()
            }
            val date = nsCalendar.dateFromComponents(components)
            requireNotNull(date) { "Invalid date-time components" }

            val validated = nsCalendar.components(
                NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                        NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond,
                fromDate = date
            )
            if (validated.year != year.toLong() || validated.month != month.toLong() || validated.day != dayOfMonth.toLong() ||
                validated.hour != hour.toLong() || validated.minute != minute.toLong() || validated.second != second.toLong()
            ) {
                throw IllegalArgumentException("Invalid date-time for Hijrah calendar")
            }
            return nsCalendar to date
        }
    }

}

actual fun Instant.toHijrahDateTime(timeZone: TimeZone): HijrahDateTime {
    val nsDate = this.toNSDate()
    val nsCalendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
    nsCalendar.timeZone = timeZone.toNSTimeZone()
    return HijrahDateTime(nsCalendar, nsDate)
}

actual fun LocalDateTime.toHijrahDateTime(): HijrahDateTime {
    val nsDate = this.toInstant(TimeZone.currentSystemDefault()).toNSDate()
    val nsCalendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
    nsCalendar.timeZone = TimeZone.currentSystemDefault().toNSTimeZone()
    return HijrahDateTime(nsCalendar, nsDate)
}