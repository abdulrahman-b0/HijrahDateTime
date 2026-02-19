@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates
import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates.atLocalTime
import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates.atStartOfDay
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_LOCAL_DATE_TIME
import com.abdulrahman_b.hijrahdatetime.utils.requireHijrahChronology
import java.io.Serializable
import java.time.*
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.Chronology
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.*
import java.time.zone.ZoneRules

/**
 * A date-time without a time-zone in the Hijrah calendar system,
 * such as `1446-04-18T17:31:30`.
 *
 * [ExpirementalHijrahDateTime] is an immutable date-time object that represents a date-time,
 * often viewed as year-month-day-hour-minute-second. Other date and time fields,
 * such as day-of-year, day-of-week and week-of-year, can also be accessed.
 * Time is represented to nanosecond precision.
 * For example, the value "2nd Shawwal 1445 at 13:45.30.123456789" can be
 * stored in a [ExpirementalHijrahDateTime].
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

@Suppress("UNCHECKED_CAST")
interface ArithmeticDateHelper<T: Temporal> {
    
    internal val temporal: T
    
    val year get() = temporal.get(ChronoField.YEAR)
    val monthValue get() = temporal.get(ChronoField.MONTH_OF_YEAR)
    val month get() = HijrahMonth.of(monthValue)
    val dayOfYear get() = temporal.get(ChronoField.DAY_OF_YEAR)
    val dayOfMonth get() = temporal.get(ChronoField.DAY_OF_MONTH)
    val dayOfWeekValue get() = temporal.get(ChronoField.DAY_OF_WEEK)
    val dayOfWeek: DayOfWeek get() = DayOfWeek.of(dayOfWeekValue)

    /**
     *  Returns a copy of this date-time with the specified day-of-month added.
     *
     *  This is equivalent to `plus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result
     *  @return the same type of this object based on this date-time plus the requested day, not null
     */
    fun plusDays(days: Long): T = temporal.plus(days, ChronoUnit.DAYS) as T

    /**
     *  Returns a copy of this date-time with the specified week-of-year added.
     *
     *  This is equivalent to `plus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result
     *  @return the same type of this object based on this date-time plus the requested week, not null
     */
    fun plusWeeks(weeks: Long): T = temporal.plus(weeks, ChronoUnit.WEEKS) as T

    /**
     *  Returns a copy of this date-time with the specified month-of-year added.
     *
     *  This is equivalent to `plus(months, ChronoUnit.MONTHS)`
     *  @param months  the month-of-year to set in the result
     *  @return the same type of this object based on this date-time plus the requested month, not null
     */
    fun plusMonths(months: Long): T = temporal.plus(months, ChronoUnit.MONTHS) as T

    /**
     *  Returns a copy of this date-time with the specified year added.
     *
     *  This is equivalent to `plus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return the same type of this object based on this date-time plus the requested year, not null
     */
    fun plusYears(years: Long): T = temporal.plus(years, ChronoUnit.YEARS) as T

    /**
     *  Returns a copy of this date-time with the specified day-of-month subtracted.
     *
     *  This is equivalent to `minus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result
     *  @return the same type of this object based on this date-time minus the requested day, not null
     */
    fun minusDays(days: Long): T = temporal.minus(days, ChronoUnit.DAYS) as T

    /**
     *  Returns a copy of this date-time with the specified week-of-year subtracted.
     *
     *  This is equivalent to `minus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result.
     *  @return the same type of this object based on this date-time minus the requested week, not null
     */
    fun minusWeeks(weeks: Long): T = temporal.minus(weeks, ChronoUnit.WEEKS) as T


    /**
     * Returns a copy of this date-time with the specified month-of-year subtracted.
     *
     * This is equivalent to `minus(months, ChronoUnit.MONTHS)`
     * @param months the month-of-year to set in the result
     * @return the same type of this object based on this date-time minus the requested month, not null
     */
    fun minusMonths(months: Long): T = temporal.minus(months, ChronoUnit.MONTHS) as T

    /**
     *  Returns a copy of this date-time with the specified year subtracted.
     *
     *  This is equivalent to `minus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return the same type of this object based on this date-time minus the requested year, not null
     */
    fun minusYears(years: Long): T = temporal.minus(years, ChronoUnit.YEARS) as T


    /**
     * Returns a copy of this date-time with the specified day-of-month set.
     *
     * This is equivalent to `with(ChronoField.DAY_OF_MONTH, dayOfMonth)`
     * @param dayOfMonth the day-of-month to set in the result, from 1 to 30 in Hijrah calendar
     */
    fun withDayOfMonth(dayOfMonth: Int): T = temporal.with(ChronoField.DAY_OF_MONTH, dayOfMonth.toLong()) as T

    /**
     * Returns a copy of this date-time with the specified day-of-year set.
     *
     * This is equivalent to `with(ChronoField.DAY_OF_YEAR, dayOfYear)`
     * @param dayOfYear the day-of-year to set in the result, from 1 to 354 or 355 in Hijrah calendar
     */
    fun withDayOfYear(dayOfYear: Int): T = temporal.with(ChronoField.DAY_OF_YEAR, dayOfYear.toLong()) as T

    /**
     * Returns a copy of this date-time with the specified day-of-week set.
     *
     * This is equivalent to `with(ChronoField.MONTH, month.value)`
     * @param month the month-of-year to set in the result.
     */
    fun withMonth(month: HijrahMonth): T = withMonth(month.value)

    /**
     * Returns a copy of this date-time with the specified day-of-week set.
     *
     * This is equivalent to `with(ChronoField.MONTH, month)`
     * @param month the month-of-year to set in the result, from 1 to 12
     */
    fun withMonth(month: Int): T = temporal.with(ChronoField.MONTH_OF_YEAR, month.toLong()) as T

    /**
     * Returns a copy of this date-time with the specified month-of-year set.
     *
     * This is equivalent to `with(ChronoField.YEAR, month)`
     * @param year the year to set in the result, ranges from 1300 to 1600
     */
    fun withYear(year: Int): T = temporal.with(ChronoField.YEAR, year.toLong()) as T
}

class ExpirementalHijrahDateTime(
    private val dateTime: ChronoLocalDateTime<HijrahDate>
) : TemporalAdjuster, Serializable, ChronoLocalDateTime<HijrahDate> by dateTime, ArithmeticDateHelper<ExpirementalHijrahDateTime> {


    private val serialVersionUid = 1L

    override fun adjustInto(temporal: Temporal): Temporal {
        requireHijrahChronology(temporal)
        return dateTime.adjustInto(temporal)
    }

    override fun format(formatter: DateTimeFormatter?): String? {
        return dateTime.format(formatter)
    }

    fun truncatedTo(unit: TemporalUnit): ExpirementalHijrahDateTime {
        return of(
            dateTime.toLocalDate(),
            dateTime.toLocalTime().truncatedTo(unit)
        )
    }

    fun isEqual(other: ExpirementalHijrahDateTime) = dateTime.isEqual(other.dateTime)

    fun isBefore(other: ExpirementalHijrahDateTime) = dateTime.isBefore(other.dateTime)

    fun isAfter(other: ExpirementalHijrahDateTime) = dateTime.isAfter(other.dateTime)
    override fun getChronology(): Chronology? {
        return dateTime.getChronology()
    }

    override fun toLocalTime(): LocalTime = dateTime.toLocalTime()
    override fun isSupported(unit: TemporalUnit?): Boolean {
        return dateTime.isSupported(unit)
    }

    override fun <R : Any?> query(query: TemporalQuery<R?>?): R? {
        return dateTime.query(query)
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
     * For a typical one-hour daylight savings change, the local date-time will be
     * moved one hour later into the offset typically corresponding to "summer".
     *
     * To obtain the later offset during an overlap, call
     * [ZonedHijrahDateTime.withLaterOffsetAtOverlap] on the result of this method.
     *
     * @param zone  the time-zone to use, not null
     * @return the zoned date-time formed from this date-time, not null
     */
    override fun atZone(zone: ZoneId): ZonedHijrahDateTime {
        return ZonedHijrahDateTime.of(this, zone)
    }

    /**
     * Combines this date-time with an offset to create an [OffsetHijrahDateTime].
     *
     * @param offset  the offset to create the date-time with, not null
     * @return the offset date-time, not null
     */
    fun atOffset(offset: ZoneOffset): OffsetHijrahDateTime {
        return OffsetHijrahDateTime.of(
            this,
            offset
        )
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
    override fun toInstant(offset: ZoneOffset): Instant {
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
    override fun toEpochSecond(offset: ZoneOffset): Long {
        return dateTime.toEpochSecond(offset)
    }

    override fun compareTo(other: ChronoLocalDateTime<*>?): Int {
        return dateTime.compareTo(other)
    }

    override fun isAfter(other: ChronoLocalDateTime<*>?): Boolean {
        return dateTime.isAfter(other)
    }

    override fun isBefore(other: ChronoLocalDateTime<*>?): Boolean {
        return dateTime.isBefore(other)
    }

    override fun isEqual(other: ChronoLocalDateTime<*>?): Boolean {
        return dateTime.isEqual(other)
    }



    override fun equals(other: Any?): Boolean {
        if (other !is ExpirementalHijrahDateTime) return false
        return dateTime == other.dateTime
    }

    override fun hashCode() = dateTime.hashCode()

    override fun toString() = dateTime.toString()


    @JvmSynthetic operator fun component1() = toLocalDate()
    @JvmSynthetic operator fun component2() = toLocalTime()
    override fun range(field: TemporalField?): ValueRange? {
        return dateTime.range(field)
    }

    override fun get(field: TemporalField?): Int {
        return dateTime.get(field)
    }

    override val temporal: ExpirementalHijrahDateTime
        get() = TODO("Not yet implemented")

    companion object {

        /**
         * Obtains the current date-time from the system clock in the default time-zone.
         */
        @JvmStatic
        fun now() = of(HijrahDate.now(), LocalTime.now())

        /**
         * Obtains the current date-time from the system clock in the specified time-zone.
         *
         * @param zoneId  the time-zone, not null
         * @return the current date-time, not null
         */
        @JvmStatic
        fun now(zoneId: ZoneId): ExpirementalHijrahDateTime = ofInstant(Instant.now(), zoneId)

        /**
         * Obtains the current date-time from the system clock in the specified time-zone.
         *
         * @param clock  the clock to use, not null
         * @return the current date-time, not null
         */
        @JvmStatic
        fun now(clock: Clock): ExpirementalHijrahDateTime = now(clock.zone)

        /**
         * Obtains an instance of [ExpirementalHijrahDateTime] from a [HijrahDate] and a [LocalTime].
         *
         * This returns a [ExpirementalHijrahDateTime] with the specified date and time.
         * The day must be valid for the year and month, otherwise an exception will be thrown.
         *
         * @param date  the Hijrah date, not null
         * @param time  the local time, not null
         *
         * @return the hijrah date-time, not null
         */
        @JvmStatic
        fun of(date: HijrahDate, time: LocalTime): ExpirementalHijrahDateTime = ExpirementalHijrahDateTime(date.atTime(time))

        /**
         * Obtains an instance of [ExpirementalHijrahDateTime] from year, month,
         * day, hour, minute, second and nanosecond.
         *
         * This returns a [ExpirementalHijrahDateTime] with the specified year, month,
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
        ): ExpirementalHijrahDateTime {
            val hijrahDate = HijrahDate.of(year, month, dayOfMonth)
            val localTime = LocalTime.of(hour, minute, second, nanoOfSecond)
            return of(hijrahDate, localTime)
        }

        /**
         * Obtains an instance of [ExpirementalHijrahDateTime] from a temporal object.
         *
         * This obtains a local date-time based on the specified temporal.
         * A [TemporalAccessor] represents an arbitrary set of date and time information,
         * which this factory converts to an instance of [ExpirementalHijrahDateTime].
         *
         * The conversion extracts and combines the [HijrahDate] and the
         * [LocalTime] from the temporal object.
         *
         * This method matches the signature of the functional interface [TemporalQuery]
         * allowing it to be used as a query via method reference, [ExpirementalHijrahDateTime.from].
         *
         * @param temporal  the temporal object to convert, not null
         * @return the hijrah date-time, not null
         * @throws DateTimeException if unable to convert to a [ExpirementalHijrahDateTime]
         */
        @JvmStatic
        fun from(temporal: TemporalAccessor): ExpirementalHijrahDateTime {
            return ExpirementalHijrahDateTime(HijrahChronology.INSTANCE.localDateTime(temporal))
        }

        /**
         * Obtains an instance of [ExpirementalHijrahDateTime] from an [Instant] and [ZoneId].
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
        fun ofInstant(instant: Instant, zoneId: ZoneId): ExpirementalHijrahDateTime {
            return ZonedHijrahDateTime.ofInstant(instant, zoneId).toHijrahDateTime()
        }

        /**
         * Obtains an instance of [ExpirementalHijrahDateTime] from a [Long] representing seconds, [Int] representing nanoseconds and [ZoneOffset].
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
        ): ExpirementalHijrahDateTime = ofInstant(
            Instant.ofEpochSecond(epochSecond, nanoOfSecond.toLong()),
            zoneOffset
        )

        /**
         * Parses a string to obtain an instance of [ExpirementalHijrahDateTime].
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
            formatter: DateTimeFormatter = HIJRAH_LOCAL_DATE_TIME
        ): ExpirementalHijrahDateTime = requireHijrahChronology(formatter).parse(text, Companion::from)

        /** The minimum supported [ExpirementalHijrahDateTime] */
        @JvmField
        val MIN = HijrahDates.MIN.atLocalTime(LocalTime.MIN)

        /** The maximum supported [ExpirementalHijrahDateTime] */
        @JvmField
        val MAX: ExpirementalHijrahDateTime = HijrahDates.MAX.atLocalTime(LocalTime.MAX)

        /** The epoch in [ExpirementalHijrahDateTime] */
        @JvmField
        val EPOCH = HijrahDates.EPOCH.atStartOfDay()

    }

}
