package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateTimeComponentsSerializer
import kotlinx.datetime.DateTimeArithmeticException
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toNSDate
import kotlinx.datetime.toNSTimeZone
import kotlinx.serialization.Serializable
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierGregorian
import platform.Foundation.NSCalendarIdentifierIslamicUmmAlQura
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitDayOfYear
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitNanosecond
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSTimeZone
import platform.Foundation.compare
import platform.Foundation.timeZoneWithAbbreviation
import kotlin.time.Instant

@Serializable(with = HijrahDateTimeComponentsSerializer::class)
actual class HijrahDateTime(
    private val nsCalendar: NSCalendar,
    private val nsDate: NSDate
) : Comparable<HijrahDateTime> {

    actual val year = nsCalendar.component(NSCalendarUnitYear, nsDate).toInt()
    actual val month = HijrahMonth.of(nsCalendar.component(NSCalendarUnitMonth, nsDate).toInt())
    actual val day = nsCalendar.component(NSCalendarUnitDay, nsDate).toInt()
    actual val dayOfWeek = getDayOfWeak(nsCalendar, nsDate)
    actual val dayOfYear = nsCalendar.component(NSCalendarUnitDayOfYear, nsDate).toInt()

    actual val hour = nsCalendar.component(NSCalendarUnitHour, nsDate).toInt()
    actual val minute = nsCalendar.component(NSCalendarUnitMinute, nsDate).toInt()
    actual val second = nsCalendar.component(NSCalendarUnitSecond, nsDate).toInt()
    actual val nanosecond = nsCalendar.component(NSCalendarUnitNanosecond, nsDate).toInt()

    actual val date: HijrahDate by lazy {
        HijrahDate(nsCalendar, nsDate)
    }

    actual val time: LocalTime get() = LocalTime(
        hour = nsCalendar.component(NSCalendarUnitHour, nsDate).toInt(),
        minute = nsCalendar.component(NSCalendarUnitMinute, nsDate).toInt(),
        second = nsCalendar.component(NSCalendarUnitSecond, nsDate).toInt(),
        nanosecond = nsCalendar.component(NSCalendarUnitNanosecond, nsDate).toInt()
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

    actual fun toInstant(timeZone: FixedOffsetTimeZone): Instant {
        val tz = timeZone.toNSTimeZone()
        val calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura).apply {
            this.timeZone = tz
        }

        val components = NSDateComponents().apply {
            this.year = this@HijrahDateTime.year.toLong()
            this.month = this@HijrahDateTime.month.number.toLong()
            this.day = this@HijrahDateTime.day.toLong()
            this.hour = this@HijrahDateTime.hour.toLong()
            this.minute = this@HijrahDateTime.minute.toLong()
            this.second = this@HijrahDateTime.second.toLong()
            this.nanosecond = this@HijrahDateTime.nanosecond.toLong()
            this.timeZone = tz
        }

        val date = calendar.dateFromComponents(components)
            ?: throw DateTimeArithmeticException("Could not convert HijrahDateTime to Instant: $this")

        return date.toKotlinInstant()
    }


    actual fun format(format: HijrahDateTimeFormat): String {
        // Ensure the formatter uses this date's specific calendar instance
        // if it wasn't already set during build()
        format.nsFormatter.calendar = this.nsCalendar
        return format.nsFormatter.stringFromDate(nsDate)
    }

    actual fun toLocalDateTime(): LocalDateTime {
        val isoCalendar = NSCalendar(NSCalendarIdentifierGregorian).apply {
            timeZone = NSTimeZone.timeZoneWithAbbreviation("UTC")!!
        }
        val date = nsDate
        val components = isoCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                    NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond or NSCalendarUnitNanosecond,
            fromDate = date
        )
        return LocalDateTime(
            year = components.year.toInt(),
            month = Month(components.month.toInt()),
            day = components.day.toInt(),
            hour = components.hour.toInt(),
            minute = components.minute.toInt(),
            second = components.second.toInt(),
            nanosecond = components.nanosecond.toInt()
        )
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

actual fun LocalDateTime.toHijrahDateTime(): HijrahDateTime {
    val nsDate = this.toInstant(TimeZone.currentSystemDefault()).toNSDate()
    val nsCalendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
    nsCalendar.timeZone = TimeZone.currentSystemDefault().toNSTimeZone()
    return HijrahDateTime(nsCalendar, nsDate)
}
