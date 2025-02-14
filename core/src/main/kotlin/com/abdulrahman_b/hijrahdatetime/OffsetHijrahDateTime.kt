@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates
import com.abdulrahman_b.hijrahdatetime.extensions.HijrahTemporalQueries
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_DATE_TIME
import com.abdulrahman_b.hijrahdatetime.utils.requireHijrahChronology
import java.io.Serial
import java.io.Serializable
import java.time.Clock
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalTime
import java.time.OffsetTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalField
import java.time.temporal.TemporalQueries
import java.time.temporal.TemporalQuery
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange

/**
 * A date-time with an offset from UTC/Greenwich in the Hijrah calendar system,
 * such as `1446-12-03T10:15:30+01:00`.
 *
 * [OffsetHijrahDateTime] is an immutable representation of a date-time with an offset.
 * This class stores all date and time fields, to a precision of nanoseconds,
 * as well as the offset from UTC/Greenwich. For example, the value
 * "2nd Shawwal 1445 at 13:45.30.123456789 +02:00" can be stored in an [OffsetHijrahDateTime].

 * This class is immutable and thread-safe.
 *
 **/
class OffsetHijrahDateTime private constructor(
    private val dateTime: HijrahDateTime,
    val offset: ZoneOffset
) : AbstractHijrahDateTime<OffsetHijrahDateTime>(dateTime), TemporalAdjuster, Serializable {



    private val serialVersionUid: Long = 1L

    override fun truncatedTo(unit: TemporalUnit): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.truncatedTo(unit), offset)
    }

    override fun isEqual(other: OffsetHijrahDateTime): Boolean {
        return toEpochSecond() == other.toEpochSecond() &&
                toLocalTime().nano == other.toLocalTime().nano
    }

    override fun isBefore(other: OffsetHijrahDateTime): Boolean {
        val thisEpochSec = toEpochSecond()
        val otherEpochSec = other.toEpochSecond()
        return thisEpochSec < otherEpochSec ||
                (thisEpochSec == otherEpochSec && toLocalTime().nano < other.toLocalTime().nano)
    }

    override fun isAfter(other: OffsetHijrahDateTime): Boolean {
        val thisEpochSec = toEpochSecond()
        val otherEpochSec = other.toEpochSecond()
        return thisEpochSec > otherEpochSec ||
                (thisEpochSec == otherEpochSec && toLocalTime().nano > other.toLocalTime().nano)
    }


    override fun isSupported(field: TemporalField): Boolean {
        return dateTime.isSupported(field) || offset.isSupported(field)
    }


    override fun get(field: TemporalField): Int {
        return when {
            dateTime.isSupported(field) -> dateTime.get(field)
            offset.isSupported(field) -> offset.get(field)
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }


    override fun getLong(field: TemporalField): Long {
        return when {
            dateTime.isSupported(field) -> dateTime.getLong(field)
            offset.isSupported(field) -> offset.getLong(field)
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    override fun range(field: TemporalField): ValueRange {
        if (field is ChronoField) {
            if (field === ChronoField.OFFSET_SECONDS) {
                return offset.range(field)
            }
            return dateTime.range(field)
        }
        return field.rangeRefinedBy(this)
    }

    override fun until(endExclusive: Temporal, unit: TemporalUnit): Long {
        return dateTime.until(endExclusive, unit)
    }


    override fun with(field: TemporalField, newValue: Long): OffsetHijrahDateTime {
        if (field == ChronoField.OFFSET_SECONDS)
            return withOffsetSameLocal(ZoneOffset.ofTotalSeconds(newValue.toInt()))
        return OffsetHijrahDateTime(dateTime.with(field, newValue), offset)
    }

    /**
     * Converts this date-time to the number of seconds from the epoch
     * @return the number of seconds from the epoch.
     */
    fun toEpochSecond(): Long = dateTime.toEpochSecond(offset)

    override fun adjustInto(temporal: Temporal): Temporal {
        requireHijrahChronology(temporal)
        return temporal
            .with(ChronoField.EPOCH_DAY, dateTime.toHijrahDate().toEpochDay())
            .with(ChronoField.NANO_OF_DAY, dateTime.toLocalTime().toNanoOfDay())
            .with(ChronoField.OFFSET_SECONDS, offset.totalSeconds.toLong())
    }


    override fun format(formatter: DateTimeFormatter): String {
        requireHijrahChronology(formatter)
        return formatter.format(this)
    }

    override fun compareTo(other: OffsetHijrahDateTime): Int {
        var cmp = compareInstant(this, other)
        if (cmp == 0) {
            cmp = dateTime.compareTo(other.dateTime)
        }
        return cmp
    }

    /** Returns the time part of this date-time. */
    fun toOffsetTime(): OffsetTime {
        return dateTime.toLocalTime().atOffset(offset)
    }

    override fun toLocalTime(): LocalTime = dateTime.toLocalTime()

    fun toHijrahDateTime(): HijrahDateTime = dateTime

    fun toOffsetDate(): OffsetHijrahDate = OffsetHijrahDate.of(dateTime.toHijrahDate(), offset)

    override fun toHijrahDate(): HijrahDate = dateTime.toHijrahDate()

    /**
     * Returns a copy of this [OffsetHijrahDateTime] with the specified offset ensuring
     * that the result is at the same instant.
     *
     * This method returns an object with the specified [ZoneOffset] and a [HijrahDateTime]
     * adjusted by the difference between the two offsets.
     * This will result in the old and new objects representing the same instant.
     * This is useful for finding the local time in a different offset.
     * For example, if this time represents `1446-12-03T10:30+02:00` and the offset specified is
     * `+03:00`, then this method returns `1446-12-03T11:30+03:00`.
     *
     * To change the offset without adjusting the local time use [withOffsetSameLocal].
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param offset  the zone offset to change to, not null
     * @return an [OffsetHijrahDateTime] based on this date-time with the requested offset, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun withOffsetSameInstant(offset: ZoneOffset): OffsetHijrahDateTime {
        if (offset == this.offset) {
            return this
        }
        val difference = offset.totalSeconds - this.offset.totalSeconds
        val adjusted = dateTime.plusSeconds(difference.toLong())
        return OffsetHijrahDateTime(adjusted, offset)
    }

    /**
     * Returns a copy of this [OffsetHijrahDateTime] with the specified offset ensuring
     * that the result has the same local date-time.
     *
     * This method returns an object with the same [HijrahDateTime] and the specified [ZoneOffset].
     * No calculation is needed or performed.
     * For example, if this time represents `1446-12-03T10:30+02:00` and the offset specified is
     * `+03:00`, then this method return `1446-12-03T10:30+03:00`.
     *
     * To take into account the difference between the offsets, and adjust the time fields,
     * use [withOffsetSameInstant].
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param offset  the zone offset to change to, not null
     * @return an [OffsetHijrahDateTime] based on this date-time with the requested offset, not null
     */
    fun withOffsetSameLocal(offset: ZoneOffset): OffsetHijrahDateTime {
        return if (offset == this.offset) {
            this
        } else OffsetHijrahDateTime(dateTime, offset)
    }

    /** Returns a copy of this date-time with the specified zoned id. */
    fun toZonedHijrahDateTime(zone: ZoneId): ZonedHijrahDateTime {
        return ZonedHijrahDateTime.of(dateTime, zone)
    }

    /**
     * Converts this date-time to an [Instant].
     *
     * This returns an [Instant] representing the same point on the
     * time-line as this date-time.
     *
     * @return an [Instant] representing the same instant, not null
     */
    fun toInstant(): Instant = dateTime.toInstant(offset)


    @Suppress("UNCHECKED_CAST")
    override fun <R : Any?> query(query: TemporalQuery<R>): R {
        return if (query === TemporalQueries.offset() || query === TemporalQueries.zone()) {
            offset as R
        } else {
            dateTime.query(query)
        }
    }


    override fun toString(): String {
        return "Hijrah-umalqura AH ${dateTime.format(HIJRAH_DATE_TIME)}$offset"
    }

    override fun factory(temporal: Temporal): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(temporal as HijrahDateTime, offset)
    }


    @JvmSynthetic operator fun component1() = toHijrahDate()
    @JvmSynthetic operator fun component2() = toLocalTime()
    @JvmSynthetic operator fun component3() = offset

    override fun equals(other: Any?): Boolean {
        if (other !is OffsetHijrahDateTime) return false

        if (dateTime != other.dateTime) return false
        if (offset != other.offset) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dateTime.hashCode()
        result = 31 * result + offset.hashCode()
        return result
    }

    companion object {

        @JvmStatic
        fun now() = now(Clock.systemDefaultZone())

        /**
         * Obtains the current date-time from the system clock in the specified offset.
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
         * @param offset  the zone ID to use, not null
         * @return the current date-time using the system clock, not null
         */
        @JvmStatic
        fun now(offset: ZoneOffset): OffsetHijrahDateTime {
            return now(Clock.system(offset))
        }

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
        fun now(clock: Clock): OffsetHijrahDateTime {
            val now = clock.instant() // called once
            return ofInstant(now, clock.zone.rules.getOffset(now))
        }


        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of [OffsetHijrahDateTime] from a date, time and offset.
         *
         * This creates an offset date-time with the specified hijrah date, time and offset.
         *
         * @param date  the local date, not null
         * @param time  the local time, not null
         * @param offset  the zone offset, not null
         * @return the offset date-time, not null
         */
        @JvmStatic
        fun of(date: HijrahDate, time: LocalTime, offset: ZoneOffset): OffsetHijrahDateTime {
            val dt = HijrahDateTime.of(date, time)
            return dt.atOffset(offset)
        }

        /**
         * Obtains an instance of [OffsetHijrahDateTime] from a date-time and offset.
         *
         * This creates an offset date-time with the specified hijrah date-time and offset.
         *
         * @param dateTime  the local date-time, not null
         * @param offset  the zone offset, not null
         * @return the offset date-time, not null
         */
        @JvmStatic
        fun of(dateTime: HijrahDateTime, offset: ZoneOffset): OffsetHijrahDateTime {
            return OffsetHijrahDateTime(dateTime, offset)
        }

        /**
         * Obtains an instance of [OffsetHijrahDateTime] from a year, month, day,
         * hour, minute, second, nanosecond and offset.
         *
         *
         * This creates an offset date-time with the seven specified fields.
         *
         *
         * This method exists primarily for writing test cases.
         * Non test-code will typically use other methods to create an offset time.
         * [OffsetHijrahDateTime] has five additional convenience variants of the
         * equivalent factory method taking fewer arguments.
         * They are not provided here to reduce the footprint of the API.
         *
         * @param year  the year to represent, from [HijrahDates.MIN_YEAR] to [HijrahDates.MAX_YEAR]
         * @param month  the month-of-year to represent, from 1 to 12
         * @param dayOfMonth  the day-of-month to represent, from 1 to 29-30
         * @param hour  the hour-of-day to represent, from 0 to 23
         * @param minute  the minute-of-hour to represent, from 0 to 59
         * @param second  the second-of-minute to represent, from 0 to 59
         * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
         * @param offset  the zone offset, not null
         * @return the offset date-time, not null
         * @throws DateTimeException if the value of any field is out of range, or
         * if the day-of-month is invalid for the month-year
         */
        @JvmStatic
        @JvmOverloads
        fun of(
            year: Int, month: Int, dayOfMonth: Int,
            hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, offset: ZoneOffset
        ): OffsetHijrahDateTime {
            val dt = HijrahDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond)
            return of(dt, offset)
        }


        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of [OffsetHijrahDateTime] from an [Instant] and zone ID.
         *
         *
         * This creates an offset date-time with the same instant as that specified.
         * Finding the offset from UTC/Greenwich is simple as there is only one valid
         * offset for each instant.
         *
         * @param instant  the instant to create the date-time from, not null
         * @param offset  the time-zone, which may be an offset, not null
         * @return the offset date-time, not null
         * @throws DateTimeException if the result exceeds the supported range
         */
        @JvmStatic
        fun ofInstant(instant: Instant, offset: ZoneOffset): OffsetHijrahDateTime {
            return OffsetHijrahDateTime(HijrahDateTime.ofInstant(instant, offset), offset)
        }


        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of [OffsetHijrahDateTime] from a temporal object.
         *
         * This obtains an offset date-time based on the specified temporal.
         * A [TemporalAccessor] represents an arbitrary set of date and time information,
         * which this factory converts to an instance of [OffsetHijrahDateTime].
         *
         * The conversion will first obtain a [ZoneOffset] from the temporal object.
         * It will then try to obtain a [OffsetHijrahDateTime], falling back to an [Instant] if necessary.
         * The result will be the combination of [ZoneOffset] with either
         * with [OffsetHijrahDateTime] or [Instant].

         * This method matches the signature of the functional interface [TemporalQuery]
         * allowing it to be used as a query via method reference, [OffsetHijrahDateTime.from].
         *
         * @param temporal  the temporal object to convert, not null
         * @return the offset date-time, not null
         * @throws DateTimeException if unable to convert to an [OffsetHijrahDateTime]
         */
        @JvmStatic
        fun from(temporal: TemporalAccessor): OffsetHijrahDateTime {
            if (temporal is OffsetHijrahDateTime) {
                return temporal
            }
            try {
                val offset = ZoneOffset.from(temporal)
                val date = temporal.query(HijrahTemporalQueries.hijrahDate())
                val time = temporal.query(TemporalQueries.localTime())
                if (date != null && time != null) {
                    return of(date, time, offset)
                } else {
                    val instant = Instant.from(temporal)
                    return ofInstant(instant, offset)
                }
            } catch (ex: DateTimeException) {
                throw DateTimeException(
                    "Unable to obtain OffsetHijrahDateTime from TemporalAccessor: $temporal of type ${temporal.javaClass.name}", ex
                )
            }
        }


        //-----------------------------------------------------------------------

        /**
         * Obtains an instance of [OffsetHijrahDateTime] from a text string using a specific formatter.
         *
         *
         * The text is parsed using the formatter, returning a date-time.
         *
         * @param text  the text to parse, not null
         * @param formatter  the formatter to use, not null
         * @return the parsed offset date-time, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        @JvmOverloads
        @JvmStatic
        fun parse(text: CharSequence, formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME): OffsetHijrahDateTime {
            requireHijrahChronology(formatter)
            return formatter.parse(text, Companion::from)
        }


        /**
         * Compares this [OffsetHijrahDateTime] to another date-time.
         * The comparison is based on the instant.
         *
         * @param datetime1  the first date-time to compare, not null
         * @param datetime2  the other date-time to compare to, not null
         * @return the comparator value, negative if less, positive if greater
         */
        private fun compareInstant(
            datetime1: OffsetHijrahDateTime,
            datetime2: OffsetHijrahDateTime
        ): Int {
            if (datetime1.offset == datetime2.offset) {
                return datetime1.dateTime.compareTo(datetime2.dateTime)
            }
            var cmp = datetime1.toEpochSecond().compareTo(datetime2.toEpochSecond())
            if (cmp == 0) {
                cmp = datetime1.dateTime.toLocalTime().nano - datetime2.dateTime.toLocalTime().nano
            }
            return cmp
        }
    }
}