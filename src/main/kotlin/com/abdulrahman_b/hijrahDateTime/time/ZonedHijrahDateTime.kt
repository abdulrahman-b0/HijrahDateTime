@file:Suppress("unused")
package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters
import com.abdulrahman_b.hijrahDateTime.serializers.ZonedHijrahDateTimeSerializer
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronologyFormatter
import kotlinx.serialization.Serializable
import java.io.Serial
import java.time.Clock
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.chrono.ChronoZonedDateTime
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalField
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange

/**
 * A date-time with a time-zone in the Hijrah calendar system,
 * such as `1446-04-18T17:31:30+03:00 Asia/Riyadh`.
 * 
 * [ZonedHijrahDateTime] is an immutable representation of a date-time with a time-zone.
 * This class stores all date and time fields, to a precision of nanoseconds,
 * and a time-zone, with a zone offset used to handle ambiguous local date-times.
 * For example, the value
 * "2nd Shawwal 1445 at 13:45.30.123456789 +03:00 in the Asia/Riyadh time-zone"
 * can be stored in a [ZonedHijrahDateTime].
 * 
 * This class handles conversion from the local time-line of [HijrahDateTime]
 * to the instant time-line of [Instant].
 * The difference between the two time-lines is the offset from UTC/Greenwich,
 * represented by a [ZoneOffset].
 *
 * Converting between the two time-lines involves calculating the offset using the
 * [ZoneRules] rules accessed from the [ZoneId].
 * Obtaining the offset for an instant is simple, as there is exactly one valid
 * offset for each instant.
 * For Gaps, the general strategy is that if the local date-time falls in the
 * middle of a Gap, then the resulting zoned date-time will have a local date-time
 * shifted forwards by the length of the Gap, resulting in a date-time in the later
 * offset, typically "summer" time.
 *
 * For Overlaps, the general strategy is that if the local date-time falls in the
 * middle of an Overlap, then the previous offset will be retained. If there is no
 * previous offset, or the previous offset is invalid, then the earlier offset is
 * used, typically "summer" time.. Two additional methods,
 * [withEarlierOffsetAtOverlap] and [withLaterOffsetAtOverlap],
 * help manage the case of an overlap.
 *
 * In terms of design, this class should be viewed primarily as the combination
 * of a [HijrahDateTime] and a [ZoneId]. The [ZoneOffset] is
 * a vital, but secondary, piece of information, used to ensure that the class
 * represents an instant, especially during a daylight savings overlap.

 * This class is immutable and thread-safe.
 *
 */
@JvmInline
@Serializable(with = ZonedHijrahDateTimeSerializer::class)
value class ZonedHijrahDateTime internal constructor(
    private val dateTime: ChronoZonedDateTime<HijrahDate>
): ChronoZonedDateTime<HijrahDate> by dateTime, java.io.Serializable {

    @get:Serial
    private val serialVersionUid get() = 1L

    val year get() = get(ChronoField.YEAR)
    val monthValue get() = get(ChronoField.MONTH_OF_YEAR)
    val month get() = HijrahMonth.of(monthValue)
    val dayOfYear get() = get(ChronoField.DAY_OF_YEAR)
    val dayOfMonth get() = get(ChronoField.DAY_OF_MONTH)
    val dayOfWeek get() = get(ChronoField.DAY_OF_WEEK)
    val hour get() = get(ChronoField.HOUR_OF_DAY)
    val minuteOfHour get() = get(ChronoField.MINUTE_OF_HOUR)
    val secondOfMinute get() = get(ChronoField.SECOND_OF_MINUTE)
    val nanoOfSecond get() = get(ChronoField.NANO_OF_SECOND)
    val nanoOfDay get() = getLong(ChronoField.NANO_OF_DAY)

    override fun range(field: TemporalField): ValueRange {
        return dateTime.range(field)
    }

    /**
     * Returns an object of the same type as this object with the specified period added.
     *
     * This method returns a new object based on this one with the specified period added.
     * For example, on a [HijrahDate], this could be used to add a number of years, months or days.
     * The returned object will have the same observable type as this object.

     * @param amountToAdd  the amount of the specified unit to add, may be negative
     * @param unit  the unit of the amount to add, not null
     * @return an object of the same type with the specified period added, not null
     * @throws DateTimeException if the unit cannot be added
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun plus(amountToAdd: Long, unit: TemporalUnit): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(amountToAdd, unit))
    }

    /**
     * Returns an object of the same type as this object with the specified period subtracted.
     *
     * This method returns a new object based on this one with the specified period subtracted.
     * For example, on a [HijrahDate], this could be used to add a number of years, months or days.
     * The returned object will have the same observable type as this object.

     * @param amountToSubtract  the amount of the specified unit to add, may be negative
     * @param unit  the unit of the amount to add, not null
     * @return an object of the same type with the specified period added, not null
     * @throws DateTimeException if the unit cannot be added
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun minus(amountToSubtract: Long, unit: TemporalUnit): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.minus(amountToSubtract, unit))
    }


    override fun plus(amount: TemporalAmount): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(amount))
    }

    override fun minus(amount: TemporalAmount): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.minus(amount))
    }


    /**
     * Returns an adjusted object of the same type as this object with the adjustment made.
     *
     * This adjusts this date-time according to the rules of the specified adjuster.
     * A simple adjuster might simply set the one of the fields, such as the year field.
     * A more complex adjuster might set the date to the last day of the month.
     * A selection of common adjustments is provided in [java.time.temporal.TemporalAdjusters].
     * These include finding the "last day of the month" and "next Wednesday".
     * The adjuster is responsible for handling special cases, such as the varying
     * lengths of month and leap years.
     *
     * Some example code indicating how and why this method is used:
     * ```
     *      date = date.with(HijrahMonth.JUMADA_AL_THANI);        // most key classes implement TemporalAdjuster
     *      date = date.with(lastDayOfMonth());  // static import from Adjusters
     *      date = date.with(next(WEDNESDAY));   // static import from Adjusters and DayOfWeek
     *```

     * @param adjuster  the adjuster to use, not null
     * @return an object of the same type with the specified adjustment made, not null
     * @throws DateTimeException if unable to make the adjustment
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun with(adjuster: TemporalAdjuster): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.with(adjuster))
    }

    /**
     * Returns an object of the same type as this object with the specified field altered.
     *
     * This returns a new object based on this one with the value for the specified field changed.
     * For example, on a [HijrahDate], this could be used to set the year, month or day-of-month.
     * The returned object will have the same observable type as this object.

     * If the field is not a [ChronoField], then the result of this method
     * is obtained by invoking {@code TemporalField.adjustInto(Temporal, long)}
     * passing `this` as the first argument.

     * @param field  the field to set in the result, not null
     * @param newValue  the new value of the field in the result
     * @return an object of the same type with the specified field set, not null
     * @throws DateTimeException if the field cannot be set
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun with(field: TemporalField, newValue: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.with(field, newValue))
    }

    override fun format(formatter: DateTimeFormatter): String {
        requireHijrahChronologyFormatter(formatter)
        return dateTime.format(formatter)
    }

    override fun until(endExclusive: Temporal, unit: TemporalUnit): Long {
        return dateTime.until(endExclusive, unit)
    }

    /**
     *  Returns a copy of this date-time with the specified nano-of-second added.
     *
     *  This is equivalent to `plus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result, from 0 to 999,999,999
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested nanosecond, not null
     */
    fun plusNanos(nanos: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(nanos, ChronoUnit.NANOS))
    }


    /**
     *  Returns a copy of this date-time with the specified second-of-minute added.
     *
     *  This is equivalent to `plus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result, from 0 to 59
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested second, not null
     */
    fun plusSeconds(seconds: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(seconds, ChronoUnit.SECONDS))
    }

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour added.
     *
     *  This is equivalent to `plus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result, from 0 to 59
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested minute, not null
     */
    fun plusMinutes(minutes: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(minutes, ChronoUnit.MINUTES))
    }


    /**
     *  Returns a copy of this date-time with the specified hour-of-day added.
     *
     *  This is equivalent to `plus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result, from 0 to 23
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested hour, not null
     */
    fun plusHours(hours: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(hours, ChronoUnit.HOURS))
    }

    /**
     *  Returns a copy of this date-time with the specified day-of-month added.
     *
     *  This is equivalent to `plus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result, from 1 to 31
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested day, not null
     */
    fun plusDays(days: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(days, ChronoUnit.DAYS))
    }

    /**
     *  Returns a copy of this date-time with the specified week-of-year added.
     *
     *  This is equivalent to `plus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result, from 1 to 52 or 53
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested week, not null
     */
    fun plusWeeks(weeks: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(weeks, ChronoUnit.WEEKS))
    }

    /**
     *  Returns a copy of this date-time with the specified month-of-year added.
     *
     *  This is equivalent to `plus(months, ChronoUnit.MONTHS)`
     *  @param months  the month-of-year to set in the result, from 1 to 12
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested month, not null
     */
    fun plusMonths(months: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(months, ChronoUnit.MONTHS))
    }

    /**
     *  Returns a copy of this date-time with the specified year added.
     *
     *  This is equivalent to `plus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested year, not null
     */
    fun plusYears(years: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(years, ChronoUnit.YEARS))
    }

    /**
     *  Returns a copy of this date-time with the specified nano-of-second subtracted.
     *
     *  This is equivalent to `minus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result, from 0 to 999,999,999
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested nanosecond, not null
     */
    fun minusNanos(nanos: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.minus(nanos, ChronoUnit.NANOS))
    }

    /**
     *  Returns a copy of this date-time with the specified second-of-minute subtracted.
     *
     *  This is equivalent to `minus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result, from 0 to 59
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested second, not null
     */
    fun minusSeconds(seconds: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.minus(seconds, ChronoUnit.SECONDS))
    }

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour subtracted.
     *
     *  This is equivalent to `minus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result, from 0 to 59
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested minute, not null
     */
    fun minusMinutes(minutes: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.minus(minutes, ChronoUnit.MINUTES))
    }

    /**
     *  Returns a copy of this date-time with the specified hour-of-day subtracted.
     *
     *  This is equivalent to `minus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result, from 0 to 23
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested hour, not null
     */
    fun minusHours(hours: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.minus(hours, ChronoUnit.HOURS))
    }

    /**
     *  Returns a copy of this date-time with the specified day-of-month subtracted.
     *
     *  This is equivalent to `minus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result, from 1 to 31
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested day, not null
     */
    fun minusDays(days: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.minus(days, ChronoUnit.DAYS))
    }

    /**
     *  Returns a copy of this date-time with the specified week-of-year subtracted.
     *
     *  This is equivalent to `minus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result, from 1 to 52 or 53
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested week, not null
     */
    fun minusWeeks(weeks: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.minus(weeks, ChronoUnit.WEEKS))
    }


    /**
     * Returns a copy of this date-time with the specified month-of-year subtracted.
     *
     * This is equivalent to `minus(months, ChronoUnit.MONTHS)`
     * @param months the month-of-year to set in the result, from 1 to 12
     * @return a [ZonedHijrahDateTime] based on this date-time with the requested month, not null
     */
    fun minusMonths(months: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.minus(months, ChronoUnit.MONTHS))
    }

    /**
     *  Returns a copy of this date-time with the specified year subtracted.
     *
     *  This is equivalent to `minus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return a [ZonedHijrahDateTime] based on this date-time with the requested year, not null
     */
    fun minusYears(years: Long): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.plus(years, ChronoUnit.YEARS))
    }

    override fun toLocalDateTime(): HijrahDateTime {
        return HijrahDateTime(dateTime.toLocalDateTime())
    }

    override fun withEarlierOffsetAtOverlap(): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.withEarlierOffsetAtOverlap())
    }

    override fun withLaterOffsetAtOverlap(): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.withLaterOffsetAtOverlap())
    }

    override fun withZoneSameInstant(zone: ZoneId): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.withZoneSameInstant(zone))
    }

    override fun withZoneSameLocal(zone: ZoneId): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.withZoneSameLocal(zone))
    }
    
    override fun toString(): String {
        return dateTime.toString()
    }

    companion object {

        @JvmStatic
        fun now() = ofInstant(Instant.now(), ZoneId.systemDefault())
        @JvmStatic
        fun now(zoneId: ZoneId) = ofInstant(Instant.now(), zoneId)
        @JvmStatic
        fun now(clock: Clock) = now(clock.zone)

        @JvmStatic
        fun of(hijrahDateTime: HijrahDateTime, zoneId: ZoneId): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(hijrahDateTime.atZone(zoneId))
        }

        @JvmStatic
        fun of(date: HijrahDate, time: LocalTime, zoneId: ZoneId): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(date.atTime(time).atZone(zoneId))
        }

        @JvmStatic
        fun of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanoOfSecond: Int, zoneId: ZoneId): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(of(HijrahDate.of(year, month, dayOfMonth), LocalTime.of(hour, minute, second, nanoOfSecond), zoneId))
        }

        @JvmStatic
        fun from(temporalAccessor: TemporalAccessor): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(HijrahChronology.INSTANCE.zonedDateTime(temporalAccessor))
        }

        @JvmStatic
        fun ofInstant(instant: Instant, zoneId: ZoneId): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(HijrahChronology.INSTANCE.zonedDateTime(instant, zoneId))
        }

        @JvmStatic
        fun parse(text: CharSequence) = parse(text, HijrahDateTimeFormatters.HIJRAH_ZONED_DATE_TIME)

        @JvmStatic
        fun parse(text: CharSequence, formatter: DateTimeFormatter): ZonedHijrahDateTime {
            requireHijrahChronologyFormatter(formatter)
            return formatter.parse(text, Companion::from)
        }
    }

}