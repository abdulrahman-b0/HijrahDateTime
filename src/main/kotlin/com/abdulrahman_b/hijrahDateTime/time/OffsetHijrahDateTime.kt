@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters
import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters.HIJRAH_LOCAL_DATE_TIME
import com.abdulrahman_b.hijrahDateTime.serializers.OffsetHijrahDateTimeSerializer
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronologyFormatter
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahTemporal
import kotlinx.serialization.Serializable
import java.io.Serial
import java.time.Clock
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalField
import java.time.temporal.TemporalQueries
import java.time.temporal.TemporalQuery
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange

@Serializable(with = OffsetHijrahDateTimeSerializer::class)
class OffsetHijrahDateTime internal constructor(
    val dateTime: HijrahDateTime,
    val offset: ZoneOffset
) : Temporal, TemporalAdjuster, Comparable<OffsetHijrahDateTime>, java.io.Serializable {

    @Serial
    private val serialVersionUid: Long = 1L

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

    /**
     * Checks if the specified field is supported.
     *
     * This checks if the specified field can be queried on this date-time.
     * If false, methods throw an exception.
     *
     * @param field  the field to check, null returns false
     * @return true if this date-time can be queried for the field, false if not
     */
    override fun isSupported(field: TemporalField): Boolean {
        return dateTime.isSupported(field) && field is ChronoField
    }


    /**
     * Checks if the specified unit is supported.
     *
     * This checks if the specified unit can be added to or subtracted from this date-time.
     * If false, then methods throw an exception.
     *
     * @param unit  the unit to check, null returns false
     * @return true if the unit can be added/subtracted, false if not
     */
    override fun isSupported(unit: TemporalUnit): Boolean {
        return dateTime.isSupported(unit) && unit is ChronoUnit
    }

    /**
     * Gets the value of the specified field as a [Int].
     *
     * This queries the date-time for the value of the specified field.
     * The returned value may be outside the valid range of values for the field.
     * If the date-time cannot return the value, because the field is unsupported or for
     * some other reason, an exception is thrown.

     * @param field  the field to get, not null
     * @return the value for the field
     * @throws DateTimeException if a value for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun get(field: TemporalField): Int {
        return when {
            dateTime.isSupported(field) -> dateTime.get(field)
            offset.isSupported(field) -> offset.get(field)
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    /**
     * Gets the value of the specified field as a [Long].
     *
     * This queries the date-time for the value of the specified field.
     * The returned value may be outside the valid range of values for the field.
     * If the date-time cannot return the value, because the field is unsupported or for
     * some other reason, an exception is thrown.

     * @param field  the field to get, not null
     * @return the value for the field
     * @throws DateTimeException if a value for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun getLong(field: TemporalField): Long {
        return when {
            dateTime.isSupported(field) -> dateTime.getLong(field)
            offset.isSupported(field) -> offset.getLong(field)
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    override fun range(field: TemporalField): ValueRange {
        if (field is ChronoField) {
            if (field === ChronoField.INSTANT_SECONDS || field === ChronoField.OFFSET_SECONDS) {
                return field.range()
            }
            return dateTime.range(field)
        }
        return field.rangeRefinedBy(this)
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
    override fun plus(amountToAdd: Long, unit: TemporalUnit): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(amountToAdd, unit), offset)
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
    override fun minus(amountToSubtract: Long, unit: TemporalUnit): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.minus(amountToSubtract, unit), offset)
    }

    override fun until(endExclusive: Temporal, unit: TemporalUnit): Long {
        return dateTime.until(endExclusive, unit)
    }


    override fun plus(amount: TemporalAmount): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(amount), offset)
    }

    override fun minus(amount: TemporalAmount): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.minus(amount), offset)
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
    override fun with(adjuster: TemporalAdjuster): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.with(adjuster), offset)
    }

    /**
     * Returns an object of the same type as this object with the specified field altered.
     *
     * This returns a new object based on this one with the value for the specified field changed.
     * For example, on a [HijrahDate], this could be used to set the year, month or day-of-month.
     * The returned object will have the same observable type as this object.

     * If the field is not a [ChronoField], then the result of this method
     * is obtained by invoking [TemporalField.adjustInto]
     * passing `this` as the first argument.

     * @param field  the field to set in the result, not null
     * @param newValue  the new value of the field in the result
     * @return an object of the same type with the specified field set, not null
     * @throws DateTimeException if the field cannot be set
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun with(field: TemporalField, newValue: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.with(field, newValue), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified nano-of-second added.
     *
     *  This is equivalent to `plus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result, from 0 to 999,999,999
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested nanosecond, not null
     */
    fun plusNanos(nanos: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(nanos, ChronoUnit.NANOS), offset)
    }


    /**
     *  Returns a copy of this date-time with the specified second-of-minute added.
     *
     *  This is equivalent to `plus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result, from 0 to 59
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested second, not null
     */
    fun plusSeconds(seconds: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(seconds, ChronoUnit.SECONDS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour added.
     *
     *  This is equivalent to `plus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result, from 0 to 59
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested minute, not null
     */
    fun plusMinutes(minutes: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(minutes, ChronoUnit.MINUTES), offset)
    }


    /**
     *  Returns a copy of this date-time with the specified hour-of-day added.
     *
     *  This is equivalent to `plus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result, from 0 to 23
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested hour, not null
     */
    fun plusHours(hours: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(hours, ChronoUnit.HOURS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified day-of-month added.
     *
     *  This is equivalent to `plus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result, from 1 to 31
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested day, not null
     */
    fun plusDays(days: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(days, ChronoUnit.DAYS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified week-of-year added.
     *
     *  This is equivalent to `plus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result, from 1 to 52 or 53
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested week, not null
     */
    fun plusWeeks(weeks: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(weeks, ChronoUnit.WEEKS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified month-of-year added.
     *
     *  This is equivalent to `plus(months, ChronoUnit.MONTHS)`
     *  @param months  the month-of-year to set in the result, from 1 to 12
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested month, not null
     */
    fun plusMonths(months: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(months, ChronoUnit.MONTHS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified year added.
     *
     *  This is equivalent to `plus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested year, not null
     */
    fun plusYears(years: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(years, ChronoUnit.YEARS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified nano-of-second subtracted.
     *
     *  This is equivalent to `minus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result, from 0 to 999,999,999
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested nanosecond, not null
     */
    fun minusNanos(nanos: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.minus(nanos, ChronoUnit.NANOS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified second-of-minute subtracted.
     *
     *  This is equivalent to `minus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result, from 0 to 59
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested second, not null
     */
    fun minusSeconds(seconds: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.minus(seconds, ChronoUnit.SECONDS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour subtracted.
     *
     *  This is equivalent to `minus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result, from 0 to 59
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested minute, not null
     */
    fun minusMinutes(minutes: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.minus(minutes, ChronoUnit.MINUTES), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified hour-of-day subtracted.
     *
     *  This is equivalent to `minus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result, from 0 to 23
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested hour, not null
     */
    fun minusHours(hours: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.minus(hours, ChronoUnit.HOURS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified day-of-month subtracted.
     *
     *  This is equivalent to `minus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result, from 1 to 31
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested day, not null
     */
    fun minusDays(days: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.minus(days, ChronoUnit.DAYS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified week-of-year subtracted.
     *
     *  This is equivalent to `minus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result, from 1 to 52 or 53
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested week, not null
     */
    fun minusWeeks(weeks: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.minus(weeks, ChronoUnit.WEEKS), offset)
    }


    /**
     * Returns a copy of this date-time with the specified month-of-year subtracted.
     *
     * This is equivalent to `minus(months, ChronoUnit.MONTHS)`
     * @param months the month-of-year to set in the result, from 1 to 12
     * @return a [OffsetHijrahDateTime] based on this date-time with the requested month, not null
     */
    fun minusMonths(months: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.minus(months, ChronoUnit.MONTHS), offset)
    }

    /**
     *  Returns a copy of this date-time with the specified year subtracted.
     *
     *  This is equivalent to `minus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return a [OffsetHijrahDateTime] based on this date-time with the requested year, not null
     */
    fun minusYears(years: Long): OffsetHijrahDateTime {
        return OffsetHijrahDateTime(dateTime.plus(years, ChronoUnit.YEARS), offset)
    }


    fun toEpochSecond(): Long = dateTime.toEpochSecond(offset)

    override fun adjustInto(temporal: Temporal): Temporal {
        requireHijrahTemporal(temporal)
        return temporal
            .with(ChronoField.EPOCH_DAY, dateTime.toLocalDate().toEpochDay())
            .with(ChronoField.NANO_OF_DAY, dateTime.toLocalTime().toNanoOfDay())
            .with(ChronoField.OFFSET_SECONDS, offset.totalSeconds.toLong())
    }

    override fun compareTo(other: OffsetHijrahDateTime): Int {
        var cmp = compareInstant(this, other)
        if (cmp == 0) {
            cmp = dateTime.compareTo(other.dateTime)
        }
        return cmp
    }

    fun format(formatter: DateTimeFormatter): String {
        requireHijrahChronologyFormatter(formatter)
        return formatter.format(this)
    }

    fun withOffsetSameInstant(offset: ZoneOffset): OffsetHijrahDateTime {
        if (offset == this.offset) {
            return this
        }
        val difference = offset.totalSeconds - this.offset.totalSeconds
        val adjusted = dateTime.plusSeconds(difference.toLong())
        return OffsetHijrahDateTime(adjusted, offset)
    }

    fun withOffsetSameLocal(offset: ZoneOffset): OffsetHijrahDateTime {
        return if (offset == this.offset) {
            this
        } else OffsetHijrahDateTime(dateTime, offset)
    }

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
        return "Hijrah-umalqura AH ${dateTime.format(HIJRAH_LOCAL_DATE_TIME)}$offset"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
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

        fun now() = now(Clock.systemDefaultZone())

        /**
         * Obtains the current date-time from the system clock in the specified time-zone.
         *
         *
         * This will query the [system clock][Clock.system] to obtain the current date-time.
         * Specifying the time-zone avoids dependence on the default time-zone.
         * The offset will be calculated from the specified time-zone.
         *
         *
         * Using this method will prevent the ability to use an alternate clock for testing
         * because the clock is hard-coded.
         *
         * @param zone  the zone ID to use, not null
         * @return the current date-time using the system clock, not null
         */
        fun now(zone: ZoneId): OffsetHijrahDateTime {
            return now(Clock.system(zone))
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
        fun now(clock: Clock): OffsetHijrahDateTime {
            val now = clock.instant() // called once
            return ofInstant(now, clock.zone.rules.getOffset(now))
        }


        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `OffsetHijrahDateTime` from a date, time and offset.
         *
         * This creates an offset date-time with the specified local date, time and offset.
         *
         * @param date  the local date, not null
         * @param time  the local time, not null
         * @param offset  the zone offset, not null
         * @return the offset date-time, not null
         */
        fun of(date: HijrahDate, time: LocalTime, offset: ZoneOffset): OffsetHijrahDateTime {
            val dt = HijrahDateTime.of(date, time)
            return OffsetHijrahDateTime(dt, offset)
        }

        /**
         * Obtains an instance of `OffsetHijrahDateTime` from a date-time and offset.
         *
         * This creates an offset date-time with the specified local date-time and offset.
         *
         * @param dateTime  the local date-time, not null
         * @param offset  the zone offset, not null
         * @return the offset date-time, not null
         */
        fun of(dateTime: HijrahDateTime, offset: ZoneOffset): OffsetHijrahDateTime {
            return OffsetHijrahDateTime(dateTime, offset)
        }

        /**
         * Obtains an instance of `OffsetHijrahDateTime` from a year, month, day,
         * hour, minute, second, nanosecond and offset.
         *
         *
         * This creates an offset date-time with the seven specified fields.
         *
         *
         * This method exists primarily for writing test cases.
         * Non test-code will typically use other methods to create an offset time.
         * `OffsetHijrahDateTime` has five additional convenience variants of the
         * equivalent factory method taking fewer arguments.
         * They are not provided here to reduce the footprint of the API.
         *
         * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
         * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
         * @param dayOfMonth  the day-of-month to represent, from 1 to 31
         * @param hour  the hour-of-day to represent, from 0 to 23
         * @param minute  the minute-of-hour to represent, from 0 to 59
         * @param second  the second-of-minute to represent, from 0 to 59
         * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
         * @param offset  the zone offset, not null
         * @return the offset date-time, not null
         * @throws DateTimeException if the value of any field is out of range, or
         * if the day-of-month is invalid for the month-year
         */
        fun of(
            year: Int, month: Int, dayOfMonth: Int,
            hour: Int, minute: Int, second: Int, nanoOfSecond: Int, offset: ZoneOffset
        ): OffsetHijrahDateTime {
            val dt = HijrahDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond)
            return OffsetHijrahDateTime(dt, offset)
        }


        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `OffsetHijrahDateTime` from an `Instant` and zone ID.
         *
         *
         * This creates an offset date-time with the same instant as that specified.
         * Finding the offset from UTC/Greenwich is simple as there is only one valid
         * offset for each instant.
         *
         * @param instant  the instant to create the date-time from, not null
         * @param zone  the time-zone, which may be an offset, not null
         * @return the offset date-time, not null
         * @throws DateTimeException if the result exceeds the supported range
         */
        fun ofInstant(instant: Instant, zone: ZoneId): OffsetHijrahDateTime {
            val rules = zone.rules
            val offset = rules.getOffset(instant)
            val hdt = HijrahDateTime.ofEpochSecond(instant.epochSecond, instant.nano, offset)
            return OffsetHijrahDateTime(hdt, offset)
        }


        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `OffsetHijrahDateTime` from a temporal object.
         *
         * This obtains an offset date-time based on the specified temporal.
         * A `TemporalAccessor` represents an arbitrary set of date and time information,
         * which this factory converts to an instance of `OffsetHijrahDateTime`.
         *
         * The conversion will first obtain a `ZoneOffset` from the temporal object.
         * It will then try to obtain a `OffsetHijrahDateTime`, falling back to an `Instant` if necessary.
         * The result will be the combination of `ZoneOffset` with either
         * with `OffsetHijrahDateTime` or `Instant`.

         * This method matches the signature of the functional interface [TemporalQuery]
         * allowing it to be used as a query via method reference, `OffsetHijrahDateTime::from`.
         *
         * @param temporal  the temporal object to convert, not null
         * @return the offset date-time, not null
         * @throws DateTimeException if unable to convert to an `OffsetHijrahDateTime`
         */
        fun from(temporal: TemporalAccessor): OffsetHijrahDateTime {
            if (temporal is OffsetHijrahDateTime) {
                return temporal
            }
            try {
                val offset = ZoneOffset.from(temporal)
                val date = temporal.query(HijrahChronology.INSTANCE::date)
                val time = temporal.query(TemporalQueries.localTime())
                if (date != null && time != null) {
                    return of(date, time, offset)
                } else {
                    val instant = Instant.from(temporal)
                    return ofInstant(instant, offset)
                }
            } catch (ex: DateTimeException) {
                throw DateTimeException(
                    "Unable to obtain OffsetHijrahDateTime from TemporalAccessor: " +
                            temporal + " of type " + temporal.javaClass.name, ex
                )
            }
        }


        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `OffsetHijrahDateTime` from a text string
         * such as `1446-04-19T10:15:30+01:00`.
         *
         *
         * The string must represent a valid date-time and is parsed using
         * [HijrahDateTimeFormatters.HIJRAH_OFFSET_DATE_TIME]
         *
         * @param text  the text to parse such as "1446-04-19T10:15:30+01:00", not null
         * @return the parsed offset date-time, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        fun parse(text: CharSequence): OffsetHijrahDateTime {
            return parse(text, HijrahDateTimeFormatters.HIJRAH_OFFSET_DATE_TIME)
        }

        /**
         * Obtains an instance of `OffsetHijrahDateTime` from a text string using a specific formatter.
         *
         *
         * The text is parsed using the formatter, returning a date-time.
         *
         * @param text  the text to parse, not null
         * @param formatter  the formatter to use, not null
         * @return the parsed offset date-time, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        fun parse(text: CharSequence, formatter: DateTimeFormatter): OffsetHijrahDateTime {
            requireHijrahChronologyFormatter(formatter)
            return formatter.parse(text, Companion::from)
        }


        /**
         * Compares this `HijrahOffsetHijrahDateTime` to another date-time.
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