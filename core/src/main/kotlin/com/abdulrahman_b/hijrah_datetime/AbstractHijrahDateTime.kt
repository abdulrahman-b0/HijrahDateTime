package com.abdulrahman_b.hijrah_datetime

import java.time.DateTimeException
import java.time.LocalTime
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException

sealed class AbstractHijrahDateTime<T: AbstractHijrahDateTime<T>>(
    temporal: Temporal
): AbstractHijrahDate<T>(temporal) {

    val hour get() = get(ChronoField.HOUR_OF_DAY)
    val minuteOfHour get() = get(ChronoField.MINUTE_OF_HOUR)
    val secondOfMinute get() = get(ChronoField.SECOND_OF_MINUTE)
    val nanoOfSecond get() = get(ChronoField.NANO_OF_SECOND)
    val nanoOfDay get() = getLong(ChronoField.NANO_OF_DAY)

    /**
     *  Returns a copy of this date-time with the specified nano-of-second added.
     *
     *  This is equivalent to `plus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result
     *  @return the same type of this object based on this date-time with the requested nanosecond, not null
     */
    fun plusNanos(nanos: Long): T = plus(nanos, ChronoUnit.NANOS)


    /**
     *  Returns a copy of this date-time with the specified second-of-minute added.
     *
     *  This is equivalent to `plus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result
     *  @return the same type of this object based on this date-time plus the requested second, not null
     */
    fun plusSeconds(seconds: Long): T = plus(seconds, ChronoUnit.SECONDS)

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour added.
     *
     *  This is equivalent to `plus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result
     *  @return the same type of this object based on this date-time plus the requested minute, not null
     */
    fun plusMinutes(minutes: Long): T = plus(minutes, ChronoUnit.MINUTES)


    /**
     *  Returns a copy of this date-time with the specified hour-of-day added.
     *
     *  This is equivalent to `plus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result
     *  @return the same type of this object based on this date-time plus the requested hour, not null
     */
    fun plusHours(hours: Long): T = plus(hours, ChronoUnit.HOURS)

    /**
     *  Returns a copy of this date-time with the specified nano-of-second subtracted.
     *
     *  This is equivalent to `minus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result
     *  @return the same type of this object based on this date-time minus the requested nanosecond, not null
     */
    fun minusNanos(nanos: Long): T = minus(nanos, ChronoUnit.NANOS)

    /**
     *  Returns a copy of this date-time with the specified second-of-minute subtracted.
     *
     *  This is equivalent to `minus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result
     *  @return the same type of this object based on this date-time minus the requested second, not null
     */
    fun minusSeconds(seconds: Long): T = minus(seconds, ChronoUnit.SECONDS)

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour subtracted.
     *
     *  This is equivalent to `minus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result
     *  @return the same type of this object based on this date-time minus the requested minute, not null
     */
    fun minusMinutes(minutes: Long): T = minus(minutes, ChronoUnit.MINUTES)

    /**
     *  Returns a copy of this date-time with the specified hour-of-day subtracted.
     *
     *  This is equivalent to `minus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result
     *  @return the same type of this object based on this date-time minus the requested hour, not null
     */
    fun minusHours(hours: Long): T = minus(hours, ChronoUnit.HOURS)


    /**
     *  Returns a copy of this date-time with the specified nano-of-second set.
     *
     *  This is equivalent to `with(ChronoField.NANO_OF_SECOND, nanoOfSecond)`
     *  @param nanoOfSecond  the nano-of-second to set in the result, from 0 to 999,999,999
     *  @return the same type of this object based on this date-time with the requested nanosecond, not null
     */
    fun withNano(nanoOfSecond: Int): T = with(ChronoField.NANO_OF_SECOND, nanoOfSecond.toLong())


    /**
     * Returns a copy of this date-time with the specified second-of-minute set.
     *
     * This is equivalent to `with(ChronoField.SECOND_OF_MINUTE, secondOfMinute)`
     * @param secondOfMinute the second-of-minute to set in the result, from 0 to 59
     * @return the same type of this object based on this date-time with the requested second, not null
     */
    fun withSecond(secondOfMinute: Int): T =
        with(ChronoField.SECOND_OF_MINUTE, secondOfMinute.toLong())

    /**
     * Returns a copy of this date-time with the specified minute-of-hour set.
     *
     * This is equivalent to `with(ChronoField.MINUTE_OF_HOUR, minuteOfHour)`
     * @param minuteOfHour the minute-of-hour to set in the result, from 0 to 59
     * @return the same type of this object based on this date-time with the requested minute, not null
     */
    fun withMinute(minuteOfHour: Int): T =
        with(ChronoField.MINUTE_OF_HOUR, minuteOfHour.toLong())

    /**
     * Returns a copy of this date-time with the specified hour-of-day set.
     *
     * This is equivalent to `with(ChronoField.HOUR_OF_DAY, hour)`
     * @param hour the hour-of-day to set in the result, from 0 to 23
     * @return the same type of this object based on this date-time with the requested hour, not null
     */
    fun withHour(hour: Int): T = with(ChronoField.HOUR_OF_DAY, hour.toLong())


    /**
     * Returns a copy of this of the implementation class with the time truncated.
     *
     * Truncation returns a copy of the original date-time with fields
     * smaller than the specified unit set to zero.
     * For example, truncating with the [ChronoUnit.MINUTES] unit
     * will set the second-of-minute and nano-of-second field to zero.
     *
     * The unit must have a [TemporalUnit.getDuration] that divides into the length of a standard day without remainder.
     * This includes all supplied time units on [ChronoUnit] and
     * [ChronoUnit.DAYS]. Other units throw an exception.
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param unit  the unit to truncate to, not null
     * @return a {@code LocalDateTime} based on this date-time with the time truncated, not null
     * @throws DateTimeException if unable to truncate
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     */
    abstract infix fun truncatedTo(unit: TemporalUnit): T

    /**
     * Gets the hijrah date part of this date-time.
     *
     * This returns a local date with the same year, month and day
     * as this date-time.
     *
     * @return the date part of this date-time, not null
     */
    abstract fun toHijrahDate(): HijrahDate

    /**
     * Gets the local time part of this date-time.
     *
     * This returns a local time with the same hour, minute, second and
     * nanosecond as this date-time.
     *
     * @return the time part of this date-time, not null
     */
    abstract fun toLocalTime(): LocalTime
}