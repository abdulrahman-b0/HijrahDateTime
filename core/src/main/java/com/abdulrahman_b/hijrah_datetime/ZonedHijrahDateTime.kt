@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package com.abdulrahman_b.hijrah_datetime

import com.abdulrahman_b.hijrah_datetime.extensions.HijrahDates
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import com.abdulrahman_b.hijrah_datetime.utils.requireHijrahChronology
import java.io.Serial
import java.io.Serializable
import java.time.Clock
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.ChronoZonedDateTime
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalField
import java.time.temporal.TemporalQuery
import java.time.temporal.TemporalUnit
import java.time.temporal.ValueRange
import java.time.zone.ZoneRules

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

open class ZonedHijrahDateTime internal constructor(
    private val dateTime: ChronoZonedDateTime<HijrahDate>
): HijrahTemporal<ZonedHijrahDateTime>(dateTime), Serializable {

    val zone: ZoneId get() = dateTime.zone
    val offset: ZoneOffset get() = dateTime.offset

    @Serial
    private val serialVersionUid = 1L

    override fun truncatedTo(unit: TemporalUnit) = of(toHijrahDateTime().truncatedTo(unit), zone)
    override fun toHijrahDate(): HijrahDate = dateTime.toLocalDate()
    override fun toLocalTime(): LocalTime = dateTime.toLocalTime()
    override fun isEqual(other: ZonedHijrahDateTime) = dateTime.isEqual(other.dateTime)
    override fun isBefore(other: ZonedHijrahDateTime) = dateTime.isBefore(other.dateTime)
    override fun isAfter(other: ZonedHijrahDateTime) = dateTime.isAfter(other.dateTime)
    override fun range(field: TemporalField): ValueRange = dateTime.range(field)
    override fun compareTo(other: ZonedHijrahDateTime) = dateTime.compareTo(other.dateTime)
    fun toHijrahDateTime() =
        HijrahDateTime(dateTime.toLocalDateTime())

    /**
     * Returns a copy of `this` date-time changing the zone offset to the earlier of the two valid offsets at a local time-line overlap.
     *
     * This method only has any effect when the local time-line overlaps, such as at an autumn daylight savings cutover.
     * In this scenario, there are two valid offsets for the local date-time. Calling this method will return a zoned date-time with the earlier of the two selected.
     * If this method is called when it is not an overlap, this is returned.
     * This instance is immutable and unaffected by this method call.
     * @return a [ZonedHijrahDateTime] based on this date-time with the earlier offset, not null
     * @throws DateTimeException – if no rules are valid for this date-time
     */
    fun withEarlierOffsetAtOverlap() =
        ZonedHijrahDateTime(dateTime.withEarlierOffsetAtOverlap())

    /**
     * Returns a copy of this date-time changing the zone offset to theclater of the two valid offsets at a local time-line overlap.
     *
     * This method only has any effect when the local time-line overlaps, such as
     * at an autumn daylight savings cutover. In this scenario, there are two
     * valid offsets for the local date-time. Calling this method will return
     * a zoned date-time with the later of the two selected.
     *
     * If this method is called when it is not an overlap, `this` is returned.
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @return a [ChronoZonedDateTime] based on this date-time with the later offset, not null
     * @throws DateTimeException if no rules can be found for the zone
     * @throws DateTimeException if no rules are valid for this date-time
     */
    fun withLaterOffsetAtOverlap() =
        ZonedHijrahDateTime(dateTime.withLaterOffsetAtOverlap())

    /**
     * Returns a copy of this date-time with a different time-zone,
     * retaining the instant.
     *
     * This method changes the time-zone and retains the instant.
     * This normally results in a change to the local date-time.
     *
     * This method is based on retaining the same instant, thus gaps and overlaps
     * in the local time-line have no effect on the result.
     *
     * To change the offset while keeping the local time,
     * use [withZoneSameLocal].
     *
     * @param zone  the time-zone to change to, not null
     * @return a [ChronoZonedDateTime] based on this date-time with the requested zone, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun withZoneSameInstant(zone: ZoneId) =
        ZonedHijrahDateTime(
            dateTime.withZoneSameInstant(zone)
        )

    /**
     * Converts this date-time to an [Instant].
     *
     * This returns an [Instant] representing the same point on the
     * time-line as this date-time.
     *
     * @return an [Instant] representing the same instant, not null
     */
    fun toInstant(): Instant = dateTime.toInstant()

    /**
     * Converts this date-time to the number of seconds from the epoch
     * @return the number of seconds from the epoch.
     */
    fun toEpochSecond(): Long = dateTime.toEpochSecond()

    /**
     * Returns a copy of this date-time with a different time-zone,
     * retaining the local date-time if possible.
     *
     * This method changes the time-zone and retains the local date-time.
     * The local date-time is only changed if it is invalid for the new zone.
     *
     * To change the zone and adjust the local date-time,
     * use [withZoneSameInstant].
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param zone  the time-zone to change to, not null
     * @return a {@code ChronoZonedDateTime} based on this date-time with the requested zone, not null
     */
    fun withZoneSameLocal(zone: ZoneId) =
        ZonedHijrahDateTime(
            dateTime.withZoneSameLocal(zone)
        )

    /**
     * Obtains an instance of [OffsetHijrahDateTime] from this date-time.
     * This creates an [OffsetHijrahDateTime] with the same local date-time and offset.
     *
     * @return an OffsetHijrahDateTime representing the same local date-time and offset
     */
    fun toOffsetHijrahDateTime() =
        OffsetHijrahDateTime.of(
            toHijrahDateTime(),
            offset
        )

    override fun toString() = dateTime.toString()

    @Suppress("UNCHECKED_CAST")
    override fun factory(temporal: Temporal) =
        ZonedHijrahDateTime(temporal as ChronoZonedDateTime<HijrahDate>)

    override fun equals(other: Any?): Boolean {
        if (other !is ZonedHijrahDateTime) return false
        return dateTime == other.dateTime
    }

    override fun hashCode() = dateTime.hashCode()

    companion object {

        @JvmStatic
        fun now() =
            ofInstant(
                Instant.now(),
                ZoneId.systemDefault()
            )

        /**
         * Obtains the current date-time from the system clock in the specified time-zone.
         *
         *
         * This will query the system clock [Clock.system] to obtain the current date-time.
         * Specifying the time-zone avoids dependence on the default time-zone.
         * The offset will be calculated from the specified time-zone.
         *
         *
         * Using this method will prevent the ability to use an alternate clock for testing
         * because the clock is hard-coded.
         *
         * @param zoneId  the zone ID to use, not null
         * @return the current date-time using the system clock, not null
         */
        @JvmStatic
        fun now(zoneId: ZoneId) =
            ofInstant(
                Instant.now(),
                zoneId
            )

        /**
         * Obtains the current date-time from the specified clock.
         *
         *
         * This will query the specified clock to obtain the current date-time.
         * The offset will be calculated from the time-zone in the clock.
         *
         *
         * Using this method allows the use of an alternate clock for testing.
         * The alternate clock may be introduced using [dependency injection][Clock].
         *
         * @param clock  the clock to use, not null
         * @return the current date-time, not null
         */
        @JvmStatic
        fun now(clock: Clock) =
            now(
                clock.zone
            )

        /**
         * Obtains an instance of [ZonedHijrahDateTime] from a date-time and zone id.
         *
         * This creates a zoned date-time with the specified local date-time and zone id.
         *
         * @param hijrahDateTime  the local date-time, not null
         * @param zoneId  the zone offset, not null
         * @return the zoned date-time, not null
         */
        @JvmStatic
        fun of(hijrahDateTime: HijrahDateTime, zoneId: ZoneId): ZonedHijrahDateTime {
            return hijrahDateTime.atZone(zoneId)
        }

        /**
         * Obtains an instance of [ZonedHijrahDateTime] from a date, time and offset.
         *
         * This creates an offset date-time with the specified local date, time and offset.
         *
         * @param date  the hijrah date, not null
         * @param time  the local time, not null
         * @param zoneId  the zone id, not null
         * @return the zoned date-time, not null
         */
        @JvmStatic
        fun of(date: HijrahDate, time: LocalTime, zoneId: ZoneId): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(
                date.atTime(
                    time
                ).atZone(zoneId)
            )
        }

        /**
         * Obtains an instance of [ZonedHijrahDateTime] from a year, month, day,
         * hour, minute, second, nanosecond and offset.
         *
         *
         * This creates a zoned date-time with the seven specified fields.
         *
         *
         * This method exists primarily for writing test cases.
         * Non test-code will typically use other methods to create an offset time.
         * [ZonedHijrahDateTime] has five additional convenience variants of the
         * equivalent factory method taking fewer arguments.
         * They are not provided here to reduce the footprint of the API.
         *
         * @param year  the year to represent, from [HijrahDates.MIN_YEAR] to [HijrahDates.MAX_YEAR]
         * @param month  the month-of-year to represent, from 1 to 12, not null
         * @param dayOfMonth  the day-of-month to represent, from 1 to 29-30, not null
         * @param hour  the hour-of-day to represent, from 0 to 23, not null
         * @param minute  the minute-of-hour to represent, from 0 to 59, not null
         * @param second  the second-of-minute to represent, from 0 to 59, not null
         * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999, not null
         * @param zoneId  the zone id, not null
         * @return the offset date-time, not null
         * @throws DateTimeException if the value of any field is out of range, or
         * if the day-of-month is invalid for the month-year
         */
        @JvmStatic
        @JvmOverloads
        fun of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zoneId: ZoneId) : ZonedHijrahDateTime {
            return of(
                HijrahDate.of(year, month, dayOfMonth),
                LocalTime.of(hour, minute, second, nanoOfSecond),
                zoneId
            )
        }

        /**
         * Obtains an instance of [ZonedHijrahDateTime] from a temporal object.
         *
         * This obtains an offset date-time based on the specified temporal.
         * A [TemporalAccessor] represents an arbitrary set of date and time information,
         * which this factory converts to an instance of [ZonedHijrahDateTime].
         *
         * The conversion will first obtain a [ZoneOffset] from the temporal object.
         * It will then try to obtain a [ZonedHijrahDateTime], falling back to an [Instant] if necessary.
         * The result will be the combination of [ZoneOffset] with either
         * with [ZonedHijrahDateTime] or [Instant].

         * This method matches the signature of the functional interface [TemporalQuery]
         * allowing it to be used as a query via method reference, [ZonedHijrahDateTime.from].
         *
         * @param temporalAccessor  the temporal object to convert, not null
         * @return the offset date-time, not null
         * @throws DateTimeException if unable to convert to an [ZonedHijrahDateTime]
         */
        @JvmStatic
        fun from(temporalAccessor: TemporalAccessor): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(
                HijrahChronology.INSTANCE.zonedDateTime(temporalAccessor)
            )
        }

        /**
         * Obtains an instance of [ZonedHijrahDateTime] from an [Instant] and zone ID.
         *
         *
         * This creates an offset date-time with the same instant as that specified.
         * Finding the offset from UTC/Greenwich is simple as there is only one valid
         * offset for each instant.
         *
         * @param instant  the instant to create the date-time from, not null
         * @param zoneId  the time-zone, which may be an offset, not null
         * @return the offset date-time, not null
         * @throws DateTimeException if the result exceeds the supported range
         */
        @JvmStatic
        fun ofInstant(instant: Instant, zoneId: ZoneId): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(
                HijrahChronology.INSTANCE.zonedDateTime(instant, zoneId)
            )
        }

        /**
         * Obtains an instance of [ZonedHijrahDateTime] from a text string using a specific formatter.
         *
         *
         * The text is parsed using the formatter, returning a date-time.
         *
         * @param text  the text to parse, not null
         * @param formatter  the formatter to use, not null
         * @return the parsed offset date-time, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        @JvmStatic
        @JvmOverloads
        fun parse(text: CharSequence, formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME): ZonedHijrahDateTime {
            requireHijrahChronology(formatter)
            return formatter.parse(text, ::from)
        }
    }

}