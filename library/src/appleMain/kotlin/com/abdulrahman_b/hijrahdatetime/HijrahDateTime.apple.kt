package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateTimeComponentsSerializer
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierIslamicUmmAlQura
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitNanosecond
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.compare

@Serializable(with = HijrahDateTimeComponentsSerializer::class)
actual class HijrahDateTime(
    private val nsCalendar: NSCalendar,
    private val nsDate: NSDate
) : Comparable<HijrahDateTime> {

    actual val date: HijrahDate = HijrahDate(nsCalendar, nsDate)

    actual val year get() = date.year
    actual val month get() = date.month
    actual val day get() = date.day
    actual val dayOfWeek get() = date.dayOfWeek
    actual val dayOfYear get() = date.dayOfYear

    actual val hour = nsCalendar.component(NSCalendarUnitHour, nsDate).toInt()
    actual val minute = nsCalendar.component(NSCalendarUnitMinute, nsDate).toInt()
    actual val second = nsCalendar.component(NSCalendarUnitSecond, nsDate).toInt()
    actual val nanosecond = nsCalendar.component(NSCalendarUnitNanosecond, nsDate).toInt()



    actual val time: LocalTime get() = LocalTime(
        hour = nsCalendar.component(NSCalendarUnitHour, nsDate).toInt(),
        minute = nsCalendar.component(NSCalendarUnitMinute, nsDate).toInt(),
        second = nsCalendar.component(NSCalendarUnitSecond, nsDate).toInt(),
        nanosecond = nsCalendar.component(NSCalendarUnitNanosecond, nsDate).toInt()
    )

    actual constructor(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int,
        nanosecond: Int,
    ) : this(createDate(year, month, day, hour, minute, second, nanosecond))
    
    private constructor(calendarDatePair: Pair<NSCalendar, NSDate>) : this(calendarDatePair.first, calendarDatePair.second)

    init {
        val components = nsCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                    NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond or NSCalendarUnitNanosecond,
            fromDate = nsDate
        )
        if (components.year != year.toLong() || components.month != month.number.toLong() || components.day != day.toLong() ||
            components.hour != hour.toLong() || components.minute != minute.toLong() || components.second != second.toLong()
        ) {
            throw IllegalArgumentException("Invalid date-time for Hijrah calendar")
        }
    }

    actual override operator fun compareTo(other: HijrahDateTime): Int =
        nsDate.compare(other.nsDate).toInt()

    actual fun format(format: HijrahDateTimeFormat): String {
        // Ensure the formatter uses this date's specific calendar instance
        // if it wasn't already set during build()
        format.nsFormatter.calendar = this.nsCalendar
        return format.nsFormatter.stringFromDate(nsDate)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HijrahDateTime) return false
        return date == other.date && time == other.time
    }

    override fun hashCode(): Int = this.nsDate.hashCode()

    override fun toString(): String = "HijrahDateTime(date=$date, time=$time)"


    actual companion object {
        actual fun parse(string: String, format: HijrahDateTimeFormat): HijrahDateTime {
            return parseOrNull(string, format) ?:
                throw IllegalArgumentException("Could not parse `HijrahDateTime` from '$string' using the date format of '${format.nsFormatter.dateFormat}'")
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
                throw IllegalArgumentException("Invalid date-time for Hijrah calendar: $year-$month-$dayOfMonth $hour:$minute:$second")
            }
            return nsCalendar to date
        }
    }

}
