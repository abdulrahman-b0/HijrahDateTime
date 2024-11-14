package com.abdulrahman_b.hijrahdatetime

import java.time.DayOfWeek
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal

sealed class AbstractHijrahDate <T: AbstractHijrahDate<T>> (temporal: Temporal): AbstractHijrahTemporal<T>(temporal) {


    inline val year get() = get(ChronoField.YEAR)
    inline val monthValue get() = get(ChronoField.MONTH_OF_YEAR)
    inline val month get() = HijrahMonth.of(monthValue)
    inline val dayOfYear get() = get(ChronoField.DAY_OF_YEAR)
    inline val dayOfMonth get() = get(ChronoField.DAY_OF_MONTH)
    inline val dayOfWeekValue get() = get(ChronoField.DAY_OF_WEEK)
    inline val dayOfWeek: DayOfWeek get() = DayOfWeek.of(dayOfWeekValue)

    /**
     *  Returns a copy of this date-time with the specified day-of-month added.
     *
     *  This is equivalent to `plus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result
     *  @return the same type of this object based on this date-time plus the requested day, not null
     */
    fun plusDays(days: Long): T = plus(days, ChronoUnit.DAYS)

    /**
     *  Returns a copy of this date-time with the specified week-of-year added.
     *
     *  This is equivalent to `plus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result
     *  @return the same type of this object based on this date-time plus the requested week, not null
     */
    fun plusWeeks(weeks: Long): T = plus(weeks, ChronoUnit.WEEKS)

    /**
     *  Returns a copy of this date-time with the specified month-of-year added.
     *
     *  This is equivalent to `plus(months, ChronoUnit.MONTHS)`
     *  @param months  the month-of-year to set in the result
     *  @return the same type of this object based on this date-time plus the requested month, not null
     */
    fun plusMonths(months: Long): T = plus(months, ChronoUnit.MONTHS)

    /**
     *  Returns a copy of this date-time with the specified year added.
     *
     *  This is equivalent to `plus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return the same type of this object based on this date-time plus the requested year, not null
     */
    fun plusYears(years: Long): T = plus(years, ChronoUnit.YEARS)

    /**
     *  Returns a copy of this date-time with the specified day-of-month subtracted.
     *
     *  This is equivalent to `minus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result
     *  @return the same type of this object based on this date-time minus the requested day, not null
     */
    fun minusDays(days: Long): T = minus(days, ChronoUnit.DAYS)

    /**
     *  Returns a copy of this date-time with the specified week-of-year subtracted.
     *
     *  This is equivalent to `minus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result.
     *  @return the same type of this object based on this date-time minus the requested week, not null
     */
    fun minusWeeks(weeks: Long): T = minus(weeks, ChronoUnit.WEEKS)


    /**
     * Returns a copy of this date-time with the specified month-of-year subtracted.
     *
     * This is equivalent to `minus(months, ChronoUnit.MONTHS)`
     * @param months the month-of-year to set in the result
     * @return the same type of this object based on this date-time minus the requested month, not null
     */
    fun minusMonths(months: Long): T = minus(months, ChronoUnit.MONTHS)

    /**
     *  Returns a copy of this date-time with the specified year subtracted.
     *
     *  This is equivalent to `minus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return the same type of this object based on this date-time minus the requested year, not null
     */
    fun minusYears(years: Long): T = minus(years, ChronoUnit.YEARS)


    /**
     * Returns a copy of this date-time with the specified day-of-month set.
     *
     * This is equivalent to `with(ChronoField.DAY_OF_MONTH, dayOfMonth)`
     * @param dayOfMonth the day-of-month to set in the result, from 1 to 30 in Hijrah calendar
     */
    fun withDayOfMonth(dayOfMonth: Int): T = with(ChronoField.DAY_OF_MONTH, dayOfMonth.toLong())

    /**
     * Returns a copy of this date-time with the specified day-of-year set.
     *
     * This is equivalent to `with(ChronoField.DAY_OF_YEAR, dayOfYear)`
     * @param dayOfYear the day-of-year to set in the result, from 1 to 354 or 355 in Hijrah calendar
     */
    fun withDayOfYear(dayOfYear: Int): T = with(ChronoField.DAY_OF_YEAR, dayOfYear.toLong())

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
    fun withMonth(month: Int): T = with(ChronoField.MONTH_OF_YEAR, month.toLong())

    /**
     * Returns a copy of this date-time with the specified month-of-year set.
     *
     * This is equivalent to `with(ChronoField.YEAR, month)`
     * @param year the year to set in the result, ranges from 1300 to 1600
     */
    fun withYear(year: Int): T = with(ChronoField.YEAR, year.toLong())

}

