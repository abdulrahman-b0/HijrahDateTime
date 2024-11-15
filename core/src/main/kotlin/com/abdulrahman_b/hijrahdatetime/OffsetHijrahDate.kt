@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates
import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates.toInstant
import com.abdulrahman_b.hijrahdatetime.extensions.HijrahTemporalQueries
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_OFFSET_DATE
import com.abdulrahman_b.hijrahdatetime.utils.requireHijrahChronology
import java.io.Serial
import java.io.Serializable
import java.time.Clock
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalTime
import java.time.OffsetTime
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
 * A date with an offset from UTC/Greenwich in the Hijrah calendar system,
 * such as `1446-12-03+01:00`.
 *
 * [OffsetHijrahDate] is an immutable representation of a date with an offset.
 * This class stores all date and time fields, to a precision of nanoseconds,
 * as well as the offset from UTC/Greenwich. For example, the value
 * "2nd Shawwal 1445 with offset +02:00" can be stored in an [OffsetHijrahDate].

 * This class is immutable and thread-safe.
 *
 **/
class OffsetHijrahDate private constructor(
    private val date: HijrahDate,
    val offset: ZoneOffset
) : AbstractHijrahDate<OffsetHijrahDate>(date), TemporalAdjuster, Serializable {


    @Serial
    private val serialVersionUid: Long = 1L

    override fun isEqual(other: OffsetHijrahDate): Boolean {
        return toEpochSecond() == other.toEpochSecond()
    }

    override fun isBefore(other: OffsetHijrahDate): Boolean {
        val thisEpochSec = toEpochSecond()
        val otherEpochSec = other.toEpochSecond()
        return thisEpochSec < otherEpochSec
    }

    override fun isAfter(other: OffsetHijrahDate): Boolean {
        val thisEpochSec = toEpochSecond()
        val otherEpochSec = other.toEpochSecond()
        return thisEpochSec > otherEpochSec
    }


    override fun isSupported(field: TemporalField): Boolean {
        return date.isSupported(field) || offset.isSupported(field)
    }


    override fun get(field: TemporalField): Int {
        return when {
            date.isSupported(field) -> date.get(field)
            offset.isSupported(field) -> offset.get(field)
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }


    override fun getLong(field: TemporalField): Long {
        return when {
            date.isSupported(field) -> date.getLong(field)
            offset.isSupported(field) -> offset.getLong(field)
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    override fun range(field: TemporalField): ValueRange {
        if (field is ChronoField) {
            if (field === ChronoField.OFFSET_SECONDS) {
                return offset.range(field)
            }
            return date.range(field)
        }
        return field.rangeRefinedBy(this)
    }

    override fun until(endExclusive: Temporal, unit: TemporalUnit): Long {
        return date.until(endExclusive, unit)
    }


    override fun with(field: TemporalField, newValue: Long): OffsetHijrahDate {
        if (field == ChronoField.OFFSET_SECONDS)
            return withOffsetSameLocal(ZoneOffset.ofTotalSeconds(newValue.toInt()))
        return OffsetHijrahDate(date.with(field, newValue), offset)
    }

    /**
     * Converts this date to the number of seconds from the epoch
     * @return the number of seconds from the epoch.
     */
    fun toEpochSecond(): Long = toInstant().epochSecond

    /**
     * Obtains an [OffsetHijrahDateTime] with the specified [OffsetTime].
     * @param localTime the offset time to use, not null
     */
    fun atTime(localTime: LocalTime): OffsetHijrahDateTime {
        return OffsetHijrahDateTime.of(date, localTime, offset)
    }

    /**
     * Combines this date with the time of midnight to create a [OffsetHijrahDateTime]
     * at the start of this date.
     *
     * This returns a [OffsetHijrahDateTime] formed from this date at the time of midnight, 00:00, at the start of this date.
     *
     * @return the local date-time of midnight at the start of this date, not null
     */
    fun atStartOfDay(): OffsetHijrahDateTime {
        return OffsetHijrahDateTime.of(date, LocalTime.MIDNIGHT, offset)
    }

    fun toHijrahDate(): HijrahDate = date

    override fun adjustInto(temporal: Temporal): Temporal {
        requireHijrahChronology(temporal)
        return temporal
            .with(ChronoField.EPOCH_DAY, date.toEpochDay())
            .with(ChronoField.OFFSET_SECONDS, offset.totalSeconds.toLong())
    }


    override fun format(formatter: DateTimeFormatter): String {
        requireHijrahChronology(formatter)
        return formatter.format(this)
    }

    override fun compareTo(other: OffsetHijrahDate): Int {
        var cmp = compareInstant(this, other)
        if (cmp == 0) {
            cmp = date.compareTo(other.date)
        }
        return cmp
    }


    /**
     * Returns a copy of this [OffsetHijrahDate] with the specified offset ensuring
     * that the result has the same local date.
     *
     * This method returns an object with the same [HijrahDate] and the specified [ZoneOffset].
     * No calculation is needed or performed.
     * For example, if this time represents `1446-12-03+02:00` and the offset specified is
     * `+03:00`, then this method return `1446-12-03+03:00`.
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param offset  the zone offset to change to, not null
     * @return an [OffsetHijrahDate] based on this date with the requested offset, not null
     */
    fun withOffsetSameLocal(offset: ZoneOffset): OffsetHijrahDate {
        return if (offset == this.offset) {
            this
        } else OffsetHijrahDate(date, offset)
    }

    /**
     * Converts this date to an [Instant].
     *
     * This returns an [Instant] representing the same point on the
     * time-line as this date.
     *
     * @return an [Instant] representing the same instant, not null
     */
    fun toInstant(): Instant = date.toInstant(offset)


    @Suppress("UNCHECKED_CAST")
    override fun <R : Any?> query(query: TemporalQuery<R>): R {
        return if (query === TemporalQueries.offset() || query === TemporalQueries.zone()) {
            offset as R
        } else {
            date.query(query)
        }
    }


    override fun toString(): String {
        return "Hijrah-umalqura AH ${this.format(HIJRAH_OFFSET_DATE)}"
    }

    override fun factory(temporal: Temporal): OffsetHijrahDate {
        return OffsetHijrahDate(HijrahDate.from(temporal), offset)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is OffsetHijrahDate) return false

        if (date != other.date) return false
        if (offset != other.offset) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + offset.hashCode()
        return result
    }

    @JvmSynthetic operator fun component1() = toHijrahDate()
    @JvmSynthetic operator fun component2() = offset

    companion object {

        /**
         * Obtains the current date from the system clock in the default time-zone.
         * @return the current date using the system clock, not null
         */
        @JvmStatic
        fun now() = now(Clock.systemDefaultZone())

        /**
         * Obtains the current date from the system clock in the specified offset.
         *
         *
         * This will query the system clock [Clock.system] to obtain the current date.
         * Specifying the time-zone avoids dependence on the default time-zone.
         * The offset will be calculated from the specified time-zone.
         *
         *
         * Using this method will prevent the ability to use an alternate clock for testing
         * because the clock is hard-coded.
         *
         * @param offset  the zone ID to use, not null
         * @return the current date using the system clock, not null
         */
        @JvmStatic
        fun now(offset: ZoneOffset): OffsetHijrahDate {
            return now(Clock.system(offset))
        }

        /**
         * Obtains the current date from the specified clock.
         *
         *
         * This will query the specified clock to obtain the current date.
         * The offset will be calculated from the time-zone in the clock.
         *
         *
         * Using this method allows the use of an alternate clock for testing.
         * The alternate clock may be introduced using [dependency injection][Clock].
         *
         * @param clock  the clock to use, not null
         * @return the current date, not null
         */
        @JvmStatic
        fun now(clock: Clock): OffsetHijrahDate {
            val now = clock.instant() // called once
            return ofInstant(now, clock.zone.rules.getOffset(now))
        }


        //-----------------------------------------------------------------------

        /**
         * Obtains an instance of [OffsetHijrahDate] from a date and offset.
         *
         * This creates an offset date with the specified hijrah date and offset.
         *
         * @param dateTime  the local date, not null
         * @param offset  the zone offset, not null
         * @return the offset date, not null
         */
        @JvmStatic
        fun of(dateTime: HijrahDate, offset: ZoneOffset): OffsetHijrahDate {
            return OffsetHijrahDate(dateTime, offset)
        }

        /**
         * Obtains an instance of [OffsetHijrahDate] from a year, month, day,and  offset.
         *
         *
         * This creates an offset date with the seven specified fields.
         *
         *
         * This method exists primarily for writing test cases.
         * Non test-code will typically use other methods to create an offset time.
         * [OffsetHijrahDate] has five additional convenience variants of the
         * equivalent factory method taking fewer arguments.
         * They are not provided here to reduce the footprint of the API.
         *
         * @param year  the year to represent, from [HijrahDates.MIN_YEAR] to [HijrahDates.MAX_YEAR]
         * @param month  the month-of-year to represent, from 1 to 12
         * @param dayOfMonth  the day-of-month to represent, from 1 to 29-30
         * @param offset  the zone offset, not null
         * @return the offset date, not null
         * @throws DateTimeException if the value of any field is out of range, or
         * if the day-of-month is invalid for the month-year
         */
        @JvmStatic
        fun of(
            year: Int, month: Int, dayOfMonth: Int, offset: ZoneOffset
        ): OffsetHijrahDate {
            val dt = HijrahDate.of(year, month, dayOfMonth)
            return of(dt, offset)
        }


        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of [OffsetHijrahDate] from an [Instant] and zone ID.
         *
         *
         * This creates an offset date with the same instant as that specified.
         * Finding the offset from UTC/Greenwich is simple as there is only one valid
         * offset for each instant.
         *
         * @param instant  the instant to create the date from, not null
         * @param offset  the time-zone, which may be an offset, not null
         * @return the offset date, not null
         * @throws DateTimeException if the result exceeds the supported range
         */
        @JvmStatic
        fun ofInstant(instant: Instant, offset: ZoneOffset): OffsetHijrahDate {
            return OffsetHijrahDate(HijrahDates.ofInstant(instant, offset), offset)
        }


        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of [OffsetHijrahDate] from a temporal object.
         *
         * This obtains an offset date based on the specified temporal.
         * A [TemporalAccessor] represents an arbitrary set of date and time information,
         * which this factory converts to an instance of [OffsetHijrahDate].
         *
         * The conversion will first obtain a [ZoneOffset] from the temporal object.
         * It will then try to obtain a [OffsetHijrahDate], falling back to an [Instant] if necessary.
         * The result will be the combination of [ZoneOffset] with either
         * with [OffsetHijrahDate] or [Instant].

         * This method matches the signature of the functional interface [TemporalQuery]
         * allowing it to be used as a query via method reference, [OffsetHijrahDate.from].
         *
         * @param temporal  the temporal object to convert, not null
         * @return the offset date, not null
         * @throws DateTimeException if unable to convert to an [OffsetHijrahDate]
         */
        @JvmStatic
        fun from(temporal: TemporalAccessor): OffsetHijrahDate {
            if (temporal is OffsetHijrahDate) {
                return temporal
            }
            try {
                val offset = ZoneOffset.from(temporal)
                val date = temporal.query(HijrahTemporalQueries.hijrahDate())
                if (date != null) {
                    return of(date, offset)
                } else {
                    val instant = Instant.from(temporal)
                    return ofInstant(instant, offset)
                }
            } catch (ex: DateTimeException) {
                throw DateTimeException(
                    "Unable to obtain OffsetHijrahDate from TemporalAccessor: $temporal of type ${temporal.javaClass.name}", ex
                )
            }
        }


        //-----------------------------------------------------------------------

        /**
         * Obtains an instance of [OffsetHijrahDate] from a text string using a specific formatter.
         *
         *
         * The text is parsed using the formatter, returning a date.
         *
         * @param text  the text to parse, not null
         * @param formatter  the formatter to use, not null
         * @return the parsed offset date, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        @JvmOverloads
        @JvmStatic
        fun parse(text: CharSequence, formatter: DateTimeFormatter = HIJRAH_OFFSET_DATE): OffsetHijrahDate {
            requireHijrahChronology(formatter)
            return formatter.parse(text, Companion::from)
        }


        /**
         * Compares this [OffsetHijrahDate] to another date.
         * The comparison is based on the instant.
         *
         * @param date1  the first date to compare, not null
         * @param date2  the other date to compare to, not null
         * @return the comparator value, negative if less, positive if greater
         */
        private fun compareInstant(
            date1: OffsetHijrahDate,
            date2: OffsetHijrahDate
        ): Int {
            if (date1.offset == date2.offset) {
                return date1.date.compareTo(date2.date)
            }
            val cmp = date1.toEpochSecond().compareTo(date2.toEpochSecond())
            return cmp
        }

        private const val SECONDS_PER_DAY: Long = 86400
    }
}