@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters
import com.abdulrahman_b.hijrahDateTime.serializers.HijrahDateTimeSerializer
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.atStartOfDay
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronology
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
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalQuery
import java.time.temporal.TemporalUnit
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
class HijrahDateTime internal constructor(
    private val dateTime: ChronoLocalDateTime<HijrahDate>
) : DateTimeTemporal<ChronoLocalDateTime<HijrahDate>, HijrahDateTime>(dateTime), Comparable<HijrahDateTime>, TemporalAdjuster, java.io.Serializable {

    val chronology get() = dateTime.chronology as HijrahChronology

    @Serial
    private val serialVersionUid = 1L

    override fun adjustInto(temporal: Temporal): Temporal {
        requireHijrahChronology(temporal)
        return dateTime.adjustInto(temporal)
    }

    override fun truncatedTo(unit: TemporalUnit): HijrahDateTime {
        return of(dateTime.toLocalDate(), dateTime.toLocalTime().truncatedTo(unit))
    }

    override fun isEqual(other: HijrahDateTime) = dateTime.isEqual(other.dateTime)

    override fun isBefore(other: HijrahDateTime) = dateTime.isBefore(other.dateTime)

    override fun isAfter(other: HijrahDateTime) = dateTime.isAfter(other.dateTime)

    override fun toLocalTime(): LocalTime = dateTime.toLocalTime()

    override fun toHijrahDate(): HijrahDate = dateTime.toLocalDate()


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
    fun atZone(zone: ZoneId): ZonedHijrahDateTime {
        return ZonedHijrahDateTime(dateTime.atZone(zone))
    }

    /**
     * Combines this date-time with an offset to create an [OffsetHijrahDateTime].
     *
     * @param offset  the offset to create the date-time with, not null
     * @return the offset date-time, not null
     */
    fun atOffset(offset: ZoneOffset): OffsetHijrahDateTime {
        return OffsetHijrahDateTime.of(this, offset)
    }


    /**
     * Converts this date-time to an [Instant].
     *
     * This combines this local date-time and the specified offset to form
     * an [Instant].
     *
     * @param offset  the offset to use for the conversion, not null
     * @return an [Instant] representing the same instant, not null
     */
    fun toInstant(offset: ZoneOffset): Instant {
        return dateTime.toInstant(offset)
    }

    /**
     * Converts this date-time to the number of seconds from the epoch
     *
     * This combines this local date-time and the specified offset to calculate the
     * epoch-second value.
     * Instants on the time-line after the epoch are positive, earlier are negative.
     *
     * @param offset  the offset to use for the conversion, not null
     * @return the number of seconds from the epoch
     */
    fun toEpochSecond(offset: ZoneOffset): Long {
        return dateTime.toEpochSecond(offset)
    }

    override fun factory(temporal: ChronoLocalDateTime<HijrahDate>) = HijrahDateTime(temporal)

    override fun equals(other: Any?): Boolean {
        if (other !is HijrahDateTime) return false
        return dateTime == other.dateTime
    }

    override fun hashCode() = dateTime.hashCode()

    override fun toString() = dateTime.toString()

    override fun compareTo(other: HijrahDateTime): Int = dateTime.compareTo(other.dateTime)

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
         *
         * @return the hijrah date-time, not null
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
         * @param year  the year to represent, from [HijrahDates.MIN_YEAR] to [HijrahDates.MAX_YEAR]
         * @param month  the month-of-year to represent, from 1 to 12, not null
         * @param dayOfMonth  the day-of-month to represent, from 1 to 29-30, not null
         * @param hour  the hour-of-day to represent, from 0 to 23, not null
         * @param minute  the minute-of-hour to represent, from 0 to 59, not null
         * @param second  the second-of-minute to represent, from 0 to 59, not null
         * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999, not null
         * @return the hijrah date-time, not null
         * @throws DateTimeException if the value of any field is out of range,
         *  or if the day-of-month is invalid for the month-year
         */
        @JvmStatic
        @JvmOverloads
        fun of(
            year: Int,
            month: Int,
            dayOfMonth: Int,
            hour: Int,
            minute: Int,
            second: Int = 0,
            nanoOfSecond: Int = 0
        ): HijrahDateTime {
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
         * @return the hijrah date-time, not null
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
         * @return the hijrah date-time, not null
         * @throws DateTimeException if the result exceeds the supported range
         */
        @JvmStatic
        fun ofInstant(instant: Instant, zoneId: ZoneId): HijrahDateTime {
            return ZonedHijrahDateTime.ofInstant(instant, zoneId).toHijrahDateTime()
        }

        /**
         * Obtains an instance of [HijrahDateTime] from a [Long] representing seconds, [Int] representing nanoseconds and [ZoneOffset].
         *
         * @param  epochSecond the number of seconds from the epoch, not null
         * @param  nanoOfSecond the nanosecond within the second, from 0 to 999,999,999
         * @param  zoneOffset the zone offset, not null
         *
         * @return the hijrah date-time, not null
         * @throws DateTimeException if the result exceeds the supported range
         */
        @JvmStatic
        fun ofEpochSecond(
            epochSecond: Long,
            nanoOfSecond: Int,
            zoneOffset: ZoneOffset
        ): HijrahDateTime {
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
        fun parse(
            text: CharSequence,
            formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME
        ): HijrahDateTime {
            requireHijrahChronologyFormatter(formatter)
            return formatter.parse(text, Companion::from)
        }

        /** The minimum supported [HijrahDateTime] */
        val MIN = HijrahDateTime(HijrahDates.MIN.atTime(LocalTime.MIN))

        /** The maximum supported [HijrahDateTime] */
        val MAX = HijrahDateTime(HijrahDates.MAX.atTime(LocalTime.MAX))

        /** The epoch in [HijrahDateTime] */
        val EPOCH = HijrahDates.EPOCH.atStartOfDay()

    }

}
