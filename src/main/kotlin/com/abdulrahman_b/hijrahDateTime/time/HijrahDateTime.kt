@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters
import com.abdulrahman_b.hijrahDateTime.serializers.HijrahDateTimeSerializer
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahTemporal
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronologyFormatter
import kotlinx.serialization.Serializable
import java.io.Serial
import java.time.Clock
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalField
import java.time.temporal.TemporalQuery
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange
import java.time.zone.ZoneRules

/**
 * A date-time without a time-zone in the Hijrah calendar system,
 * such as `1446-04-18T17:31:30`.
 *
 * [HijrahDateTime] is an immutable date-time object that represents a date-time,
 * often viewed as year-month-day-hour-minute-second. Other date and time fields,
 * such as day-of-year, day-of-week and week-of-year, can also be accessed.
 * Time is represented to nanosecond precision.
 * For example, the value "2nd Shawwal 1445 at 13:45.30.123456789" can be
 * stored in a [HijrahDateTime].
 *
 * This class does not store or represent a time-zone.
 * Instead, it is a description of the date, combined with
 * the local time as seen on a wall clock.
 * It cannot represent an instant on the time-line without additional information
 * such as an offset or time-zone.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 */

@Serializable(with = HijrahDateTimeSerializer::class)
class HijrahDateTime internal constructor(private val dateTime: ChronoLocalDateTime<HijrahDate>) :
    ChronoLocalDateTime<HijrahDate> by dateTime,
    java.io.Serializable,
    HijrahTemporal<ChronoLocalDateTime<HijrahDate>, HijrahDateTime>(dateTime) {

    @Serial
    private val serialVersionUid = 1L


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
    override fun plus(amountToAdd: Long, unit: TemporalUnit): HijrahDateTime {
        return HijrahDateTime(dateTime.plus(amountToAdd, unit))
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
    override fun minus(amountToSubtract: Long, unit: TemporalUnit): HijrahDateTime {
        return HijrahDateTime(dateTime.minus(amountToSubtract, unit))
    }


    override fun plus(amount: TemporalAmount): HijrahDateTime {
        return HijrahDateTime(dateTime.plus(amount))
    }

    override fun minus(amount: TemporalAmount): HijrahDateTime {
        return HijrahDateTime(dateTime.minus(amount))
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
    override fun with(adjuster: TemporalAdjuster): HijrahDateTime {
        return HijrahDateTime(dateTime.with(adjuster))
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
    override fun with(field: TemporalField, newValue: Long): HijrahDateTime {
        return HijrahDateTime(dateTime.with(field, newValue))
    }

    override fun adjustInto(temporal: Temporal): Temporal {
        requireHijrahTemporal(temporal)
        return dateTime.adjustInto(temporal)
    }

    override fun format(formatter: DateTimeFormatter): String {
        requireHijrahChronologyFormatter(formatter)
        return dateTime.format(formatter)
    }

    /**
     * Combines this time with a time-zone to create a [ZonedHijrahDateTime].
     *
     * This returns a [ZonedHijrahDateTime] formed from this date-time at the
     * specified time-zone. The result will match this date-time as closely as possible.
     * Time-zone rules, such as daylight savings, mean that not every local date-time
     * is valid for the specified zone, thus the local date-time may be adjusted.
     *
     * The local date-time is resolved to a single instant on the time-line.
     * This is achieved by finding a valid offset from UTC/Greenwich for the local
     * date-time as defined by the [ZoneRules] of the zone ID.
     *
     * In most cases, there is only one valid offset for a local hijrah date-time.
     * In the case of an overlap, where clocks are set back, there are two valid offsets.
     * This method uses the earlier offset typically corresponding to "summer".
     *
     * In the case of a gap, where clocks jump forward, there is no valid offset.
     * Instead, the local date-time is adjusted to be later by the length of the gap.
     * For a typical one hour daylight savings change, the local date-time will be
     * moved one hour later into the offset typically corresponding to "summer".
     *
     * To obtain the later offset during an overlap, call
     * [ZonedHijrahDateTime.withLaterOffsetAtOverlap] on the result of this method.
     *
     * @param zone  the time-zone to use, not null
     * @return the zoned date-time formed from this date-time, not null
     */
    override fun atZone(zone: ZoneId): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.atZone(zone))
    }

    override fun factory(temporal: ChronoLocalDateTime<HijrahDate>) = HijrahDateTime(temporal)

    override fun equals(other: Any?): Boolean {
        if (other !is HijrahDateTime) return false
        return dateTime == other
    }

    override fun hashCode(): Int {
        return dateTime.hashCode()
    }

    override fun toString(): String {
        return dateTime.toString()
    }


    companion object {

        /**
         * Obtains the current date-time from the system clock in the default time-zone.
         */
        @JvmStatic
        fun now() = HijrahDateTime(HijrahDate.now().atTime(LocalTime.now()))

        /**
         * Obtains the current date-time from the system clock in the specified time-zone.
         *
         * @param zoneId  the time-zone, not null
         * @return the current date-time, not null
         */
        @JvmStatic
        fun now(zoneId: ZoneId): HijrahDateTime = ofInstant(Instant.now(), zoneId)

        /**
         * Obtains the current date-time from the system clock in the specified time-zone.
         *
         * @param clock  the clock to use, not null
         * @return the current date-time, not null
         */
        @JvmStatic
        fun now(clock: Clock): HijrahDateTime = now(clock.zone)

        /**
         * Obtains an instance of [HijrahDateTime] from a [HijrahDate] and a [LocalTime].
         *
         * This returns a [HijrahDateTime] with the specified date and time.
         * The day must be valid for the year and month, otherwise an exception will be thrown.
         *
         * @param date  the Hijrah date, not null
         * @param time  the local time, not null
         */
        @JvmStatic
        fun of(date: HijrahDate, time: LocalTime): HijrahDateTime {
            return HijrahDateTime(date.atTime(time))
        }

        /**
         * Obtains an instance of [HijrahDateTime] from year, month,
         * day, hour, minute, second and nanosecond.
         *
         * This returns a [HijrahDateTime] with the specified year, month,
         * day-of-month, hour, minute, second and nanosecond.
         * The day must be valid for the year and month, otherwise an exception will be thrown.
         *
         * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
         * @param month  the month-of-year to represent, not null
         * @param dayOfMonth  the day-of-month to represent, from 1 to 31
         * @param hour  the hour-of-day to represent, from 0 to 23
         * @param minute  the minute-of-hour to represent, from 0 to 59
         * @param second  the second-of-minute to represent, from 0 to 59
         * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
         * @return the local date-time, not null
         * @throws DateTimeException if the value of any field is out of range,
         *  or if the day-of-month is invalid for the month-year
         */
        @JvmStatic
        @JvmOverloads
        fun of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0): HijrahDateTime {
            val hijrahDate = HijrahDate.of(year, month, dayOfMonth)
            val localTime = LocalTime.of(hour, minute, second, nanoOfSecond)
            return of(hijrahDate, localTime)
        }

        /**
         * Obtains an instance of [HijrahDateTime] from a temporal object.
         *
         * This obtains a local date-time based on the specified temporal.
         * A [TemporalAccessor] represents an arbitrary set of date and time information,
         * which this factory converts to an instance of [HijrahDateTime].
         *
         * The conversion extracts and combines the [HijrahDate] and the
         * [LocalTime] from the temporal object.
         *
         * This method matches the signature of the functional interface [TemporalQuery]
         * allowing it to be used as a query via method reference, [HijrahDateTime.from].
         *
         * @param temporal  the temporal object to convert, not null
         * @return the local date-time, not null
         * @throws DateTimeException if unable to convert to a [HijrahDateTime]
         */
        @JvmStatic
        fun from(temporal: TemporalAccessor): HijrahDateTime {
            return HijrahDateTime(HijrahChronology.INSTANCE.localDateTime(temporal))
        }

        /**
         * Obtains an instance of [HijrahDateTime] from an [Instant] and [ZoneId].
         *
         * This creates a local date-time based on the specified instant.
         * First, the offset from UTC/Greenwich is obtained using the zone ID and instant,
         * Then, the instant and offset are used to calculate the local date-time.
         *
         * @param instant  the instant to create the date-time from, not null
         * @param zoneId  the time-zone, which may be an offset, not null
         * @return the local date-time, not null
         * @throws DateTimeException if the result exceeds the supported range
         */
        @JvmStatic
        fun ofInstant(instant: Instant, zoneId: ZoneId): HijrahDateTime {
            return ZonedHijrahDateTime.ofInstant(instant, zoneId).toLocalDateTime()
        }

        /**
         * Obtains an instance of [HijrahDateTime] from a [Long] representing seconds, [Int] representing nanoseconds and [ZoneOffset].
         *
         * @param  epochSecond the number of seconds from the epoch, not null
         * @param  nanoOfSecond the nanosecond within the second, from 0 to 999,999,999
         * @param  zoneOffset the zone offset, not null
         *
         * @return the local date-time, not null
         * @throws DateTimeException if the result exceeds the supported range
         */
        @JvmStatic
        fun ofEpochSecond(epochSecond: Long, nanoOfSecond: Int, zoneOffset: ZoneOffset): HijrahDateTime {
            return ofInstant(Instant.ofEpochSecond(epochSecond, nanoOfSecond.toLong()), zoneOffset)
        }

        /**
         * Parses a string to obtain an instance of [HijrahDateTime].
         * This parser uses the specified formatter.
         *
         * @param text  the text to parse, not null
         * @param formatter  the formatter to use, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        @JvmStatic
        @JvmOverloads
        fun parse(text: CharSequence, formatter: DateTimeFormatter = HijrahDateTimeFormatters.HIJRAH_LOCAL_DATE_TIME): HijrahDateTime {
            requireHijrahChronologyFormatter(formatter)
            return formatter.parse(text, Companion::from)
        }

    }

}
