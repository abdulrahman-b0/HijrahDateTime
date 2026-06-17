package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormats
import com.abdulrahman_b.hijrahdatetime.internal.compareDates
import com.abdulrahman_b.hijrahdatetime.internal.getDayOfWeek
import com.abdulrahman_b.hijrahdatetime.internal.getDayOfYear
import com.abdulrahman_b.hijrahdatetime.internal.getMaxHijrahDate
import com.abdulrahman_b.hijrahdatetime.internal.getMinHijrahDate
import com.abdulrahman_b.hijrahdatetime.internal.validateYearAndDay
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateComponentsSerializer
import kotlinx.serialization.Serializable
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierIslamicUmmAlQura
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents

@Suppress("unused")
@Serializable(with = HijrahDateComponentsSerializer::class)
actual class HijrahDate private constructor(
    actual val year: Int,
    actual val month: HijrahMonth,
    actual val day: Int,
): Comparable<HijrahDate> {

    private val calendar by lazy {
        NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
    }


    actual val dayOfWeek get() = getDayOfWeek()
    actual val dayOfYear get() = getDayOfYear()


    actual constructor(year: Int, month: Int, day: Int) : this(year, HijrahMonth.of(month), day) {
        validateYearAndDay()
    }

    internal constructor(nsCalendar: NSCalendar, nsDate: NSDate) : this(
        year = nsCalendar.component(NSCalendarUnitYear, nsDate).toInt(),
        month = nsCalendar.component(NSCalendarUnitMonth, nsDate).toInt(),
        day = nsCalendar.component(NSCalendarUnitDay, nsDate).toInt()
    )



    actual override fun compareTo(other: HijrahDate): Int = compareDates(this, other)



    actual fun format(format: HijrahDateTimeFormat): String {
        // Ensure the formatter uses this date's specific calendar instance
        // if it wasn't already set during build()

        val nsDate = requireNotNull(calendar.dateFromComponents(toNSDateComponents())) {
            "Failed to convert HijrahDate to NSDate"
        }

        format.nsFormatter.calendar = calendar
        return format.nsFormatter.stringFromDate(nsDate)
    }

    fun toNSDateComponents(): NSDateComponents {
        val thisDate = this

        return NSDateComponents().apply {
            this.year = thisDate.year.toLong()
            this.month = thisDate.month.number.toLong()
            this.day = thisDate.day.toLong()
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HijrahDate) return false
        return year == other.year &&
                month.number == other.month.number &&
                day == other.day
    }

    override fun hashCode(): Int = toEpochDays().hashCode()

    override fun toString(): String = format(HijrahDateTimeFormats.DATE_ISO)

    actual companion object {
        actual fun parse(
            string: String,
            format: HijrahDateTimeFormat,
        ): HijrahDate {
            return parseOrNull(string, format)
                ?: throw IllegalArgumentException("Could not parse `HijrahDate` from '$string' using the date format of '${format.nsFormatter.dateFormat}'")
        }

        actual fun parseOrNull(
            string: String,
            format: HijrahDateTimeFormat,
        ): HijrahDate? {
            format.nsFormatter.calendar = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura)
            return format.nsFormatter.dateFromString(string)?.let { nSDate ->
                HijrahDate(nsCalendar = format.nsFormatter.calendar, nsDate = nSDate)
            }
        }

        actual val MIN: HijrahDate get() = getMinHijrahDate()
        actual val MAX: HijrahDate get() = getMaxHijrahDate()

    }


}

