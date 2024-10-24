@file:Suppress("MemberVisibilityCanBePrivate", "unused")
@file:JvmName("HijrahDateUtils")

package com.abdulrahman_b.hijrahDateTime.extensions

import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters
import com.abdulrahman_b.hijrahDateTime.time.HijrahMonth
import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronologyFormatter
import java.time.Instant
import java.time.OffsetTime
import java.time.ZoneId
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField


inline val HijrahDate.year: Int get() = get(ChronoField.YEAR)

inline val HijrahDate.monthValue: Int get() = get(ChronoField.MONTH_OF_YEAR)

inline val HijrahDate.month: HijrahMonth get() = HijrahMonth.of(monthValue)

inline val HijrahDate.dayOfYear: Int get() = get(ChronoField.DAY_OF_YEAR)

inline val HijrahDate.dayOfMonth: Int get() = get(ChronoField.DAY_OF_MONTH)

inline val HijrahDate.dayOfWeek: Int get() = get(ChronoField.DAY_OF_WEEK)

/**
 * Obtains an [OffsetHijrahDateTime] with the specified [OffsetTime].
 */
fun HijrahDate.atTime(offsetTime: OffsetTime): OffsetHijrahDateTime {
    val datetime = HijrahDateTime.of(this, offsetTime.toLocalTime())
    return OffsetHijrahDateTime.of(datetime, offsetTime.offset)
}

/**
 * A factory for creating instances of [HijrahDate].
 * This class provides functions to create an instance of [HijrahDate] from different sources, such as a string parse or an [Instant].
 */
object HijrahDateFactory {

    /**
     * Creates an instance of [HijrahDate] from the specified [epochDay].
     *
     * @param epochDay the epoch-day to use, not null
     * @return the created [HijrahDate], not null
     */
    fun ofEpochDay(epochDay: Long): HijrahDate {
        return HijrahChronology.INSTANCE.dateEpochDay(epochDay)
    }

    /**
     * Creates an instance of [HijrahDate] from the specified [year] and [dayOfYear].
     *
     * @param year the year to use, not null
     * @param dayOfYear the day-of-year to use, not null
     */
    fun ofYearDay(year: Int, dayOfYear: Int): HijrahDate {
        return HijrahChronology.INSTANCE.dateYearDay(year, dayOfYear)
    }

    /**
     * Creates an instance of [HijrahDate] from the specified [Instant] and [ZoneId].
     *
     * @param instant  the instant to use, not null
     * @param zone  the zone to use, not null
     *
     * @return the created [HijrahDate], not null
     */
    fun ofInstant(instant: Instant, zone: ZoneId): HijrahDate {
        val offset = zone.rules.getOffset(instant)
        val localSecond = instant.epochSecond + offset.totalSeconds
        val localEpochDay = Math.floorDiv(localSecond, SECONDS_PER_DAY)
        return ofEpochDay(localEpochDay)
    }

    /**
     * Parses a string to obtain an instance of [HijrahDate].
     * This parser uses the default formatter, [HijrahDateTimeFormatters.HIJRAH_LOCAL_DATE].
     *
     * @param text  the text to parse, not null
     * @return the parsed local date-time, not null
     */
    fun parse(text: CharSequence) = parse(text, HijrahDateTimeFormatters.HIJRAH_LOCAL_DATE)

    /**
     * Parses a string to obtain an instance of [HijrahDate].
     * This parser uses the specified formatter.
     *
     * @param text  the text to parse, not null
     * @param formatter  the formatter to use, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    fun parse(text: CharSequence, formatter: DateTimeFormatter): HijrahDate {
        requireHijrahChronologyFormatter(formatter)
        return formatter.parse(text, HijrahDate::from)
    }

    private const val SECONDS_PER_DAY = 86400

}