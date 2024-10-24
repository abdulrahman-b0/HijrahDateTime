package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters
import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters.getRecommendedFormatter
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronologyFormatter
import java.io.Serial
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor

abstract class HijrahTemporal <in T, out R> internal constructor(
    private val temporal: Temporal,
): java.io.Serializable where T: Temporal, R: HijrahTemporal<T, R> {

    @Serial
    private val serialVersionUid: Long = 1L

    val year get() = temporal.get(ChronoField.YEAR)
    val monthValue get() = temporal.get(ChronoField.MONTH_OF_YEAR)
    val month get() = HijrahMonth.of(monthValue)
    val dayOfYear get() = temporal.get(ChronoField.DAY_OF_YEAR)
    val dayOfMonth get() = temporal.get(ChronoField.DAY_OF_MONTH)
    val dayOfWeek get() = temporal.get(ChronoField.DAY_OF_WEEK)
    val hour get() = temporal.get(ChronoField.HOUR_OF_DAY)
    val minuteOfHour get() = temporal.get(ChronoField.MINUTE_OF_HOUR)
    val secondOfMinute get() = temporal.get(ChronoField.SECOND_OF_MINUTE)
    val nanoOfSecond get() = temporal.get(ChronoField.NANO_OF_SECOND)
    val nanoOfDay get() = temporal.getLong(ChronoField.NANO_OF_DAY)


    /**
     *  Returns a copy of this date-time with the specified nano-of-second added.
     *
     *  This is equivalent to `plus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result, from 0 to 999,999,999
     *  @return a [R] based on this date-time with the requested nanosecond, not null
     */
    fun plusNanos(nanos: Long): R {
        return typedFactory(temporal.plus(nanos, ChronoUnit.NANOS))
    }


    /**
     *  Returns a copy of this date-time with the specified second-of-minute added.
     *
     *  This is equivalent to `plus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result, from 0 to 59
     *  @return a [R] based on this date-time with the requested second, not null
     */
    fun plusSeconds(seconds: Long): R {
        return typedFactory(temporal.plus(seconds, ChronoUnit.SECONDS))
    }

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour added.
     *
     *  This is equivalent to `plus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result, from 0 to 59
     *  @return a [R] based on this date-time with the requested minute, not null
     */
    fun plusMinutes(minutes: Long): R {
        return typedFactory(temporal.plus(minutes, ChronoUnit.MINUTES))
    }


    /**
     *  Returns a copy of this date-time with the specified hour-of-day added.
     *
     *  This is equivalent to `plus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result, from 0 to 23
     *  @return a [R] based on this date-time with the requested hour, not null
     */
    fun plusHours(hours: Long): R {
        return typedFactory(temporal.plus(hours, ChronoUnit.HOURS))
    }

    /**
     *  Returns a copy of this date-time with the specified day-of-month added.
     *
     *  This is equivalent to `plus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result, from 1 to 31
     *  @return a [R] based on this date-time with the requested day, not null
     */
    fun plusDays(days: Long): R {
        return typedFactory(temporal.plus(days, ChronoUnit.DAYS))
    }

    /**
     *  Returns a copy of this date-time with the specified week-of-year added.
     *
     *  This is equivalent to `plus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result, from 1 to 52 or 53
     *  @return a [R] based on this date-time with the requested week, not null
     */
    fun plusWeeks(weeks: Long): R {
        return typedFactory(temporal.plus(weeks, ChronoUnit.WEEKS))
    }

    /**
     *  Returns a copy of this date-time with the specified month-of-year added.
     *
     *  This is equivalent to `plus(months, ChronoUnit.MONTHS)`
     *  @param months  the month-of-year to set in the result, from 1 to 12
     *  @return a [R] based on this date-time with the requested month, not null
     */
    fun plusMonths(months: Long): R {
        return typedFactory(temporal.plus(months, ChronoUnit.MONTHS))
    }

    /**
     *  Returns a copy of this date-time with the specified year added.
     *
     *  This is equivalent to `plus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return a [R] based on this date-time with the requested year, not null
     */
    fun plusYears(years: Long): R {
        return typedFactory(temporal.plus(years, ChronoUnit.YEARS))
    }

    /**
     *  Returns a copy of this date-time with the specified nano-of-second subtracted.
     *
     *  This is equivalent to `minus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result, from 0 to 999,999,999
     *  @return a [R] based on this date-time with the requested nanosecond, not null
     */
    fun minusNanos(nanos: Long): R {
        return typedFactory(temporal.minus(nanos, ChronoUnit.NANOS))
    }

    /**
     *  Returns a copy of this date-time with the specified second-of-minute subtracted.
     *
     *  This is equivalent to `minus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result, from 0 to 59
     *  @return a [R] based on this date-time with the requested second, not null
     */
    fun minusSeconds(seconds: Long): R {
        return typedFactory(temporal.minus(seconds, ChronoUnit.SECONDS))
    }

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour subtracted.
     *
     *  This is equivalent to `minus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result, from 0 to 59
     *  @return a [R] based on this date-time with the requested minute, not null
     */
    fun minusMinutes(minutes: Long): R {
        return typedFactory(temporal.minus(minutes, ChronoUnit.MINUTES))
    }

    /**
     *  Returns a copy of this date-time with the specified hour-of-day subtracted.
     *
     *  This is equivalent to `minus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result, from 0 to 23
     *  @return a [R] based on this date-time with the requested hour, not null
     */
    fun minusHours(hours: Long): R {
        return typedFactory(temporal.minus(hours, ChronoUnit.HOURS))
    }

    /**
     *  Returns a copy of this date-time with the specified day-of-month subtracted.
     *
     *  This is equivalent to `minus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result, from 1 to 31
     *  @return a [R] based on this date-time with the requested day, not null
     */
    fun minusDays(days: Long): R {
        return typedFactory(temporal.minus(days, ChronoUnit.DAYS))
    }

    /**
     *  Returns a copy of this date-time with the specified week-of-year subtracted.
     *
     *  This is equivalent to `minus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result, from 1 to 52 or 53
     *  @return a [R] based on this date-time with the requested week, not null
     */
    fun minusWeeks(weeks: Long): R {
        return typedFactory(temporal.minus(weeks, ChronoUnit.WEEKS))
    }


    /**
     * Returns a copy of this date-time with the specified month-of-year subtracted.
     *
     * This is equivalent to `minus(months, ChronoUnit.MONTHS)`
     * @param months the month-of-year to set in the result, from 1 to 12
     * @return a [R] based on this date-time with the requested month, not null
     */
    fun minusMonths(months: Long): R {
        return typedFactory(temporal.minus(months, ChronoUnit.MONTHS))
    }

    /**
     *  Returns a copy of this date-time with the specified year subtracted.
     *
     *  This is equivalent to `minus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return a [R] based on this date-time with the requested year, not null
     */
    fun minusYears(years: Long): R {
        return typedFactory(temporal.plus(years, ChronoUnit.YEARS))
    }



    @Suppress("UNCHECKED_CAST")
    private fun typedFactory(temporal: Temporal): R {
        return factory(temporal as T)
    }
    
    protected abstract fun factory(temporal: T): R
}