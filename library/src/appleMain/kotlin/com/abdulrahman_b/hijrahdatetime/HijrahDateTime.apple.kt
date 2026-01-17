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

    constructor(calendar: NSCalendar, date: NSDate): this(calendar to date)

    actual override operator fun compareTo(other: HijrahDateTime): Int =
        calendarDatePair.second.compare(other.calendarDatePair.second).toInt()

    actual fun toInstant(timeZone: FixedOffsetTimeZone): Instant {
        // We must ensure the calendar used for conversion respects the provided offset
        val tempCalendar = calendar.copy() as NSCalendar
        tempCalendar.timeZone = NSTimeZone.timeZoneForSecondsFromGMT(timeZone.offset.totalSeconds.toLong())

        // date represents the absolute point in time.
        // toKotlinInstant() from kotlinx.datetime is safe here as NSDate is always UTC based.
        return calendarDatePair.second.toKotlinInstant()
    }


    actual fun format(format: HijrahDateTimeFormat): String {
        // Ensure the formatter uses this date's specific calendar instance
        // if it wasn't already set during build()
        format.nsFormatter.calendar = this.calendar
        return format.nsFormatter.stringFromDate(calendarDatePair.second)
    }

    actual fun toLocalDateTime(): LocalDateTime {
        return calendarDatePair.second.toKotlinInstant().toLocalDateTime(calendar.timeZone.toKotlinTimeZone())
    }


    actual companion object {
        actual fun parse(string: String, format: HijrahDateTimeFormat): HijrahDateTime {
            return requireNotNull(parseOrNull(string, format)) {
                "Invalid date: $string"
            }
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
            return nsCalendar to requireNotNull(nsCalendar.dateFromComponents(components)) {
                "Invalid date components: $year-$month-$dayOfMonth-$hour:$minute:$second.$nanosecond"
            }
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