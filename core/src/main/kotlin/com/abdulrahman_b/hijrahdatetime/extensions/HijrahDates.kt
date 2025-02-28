@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.abdulrahman_b.hijrahdatetime.extensions

import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
import com.abdulrahman_b.hijrahdatetime.HijrahMonth
import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import com.abdulrahman_b.hijrahdatetime.utils.requireHijrahChronology
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime
import java.time.OffsetTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters.lastDayOfMonth

/**
 * A set of extensions and utility functions that provide the ability to work with [HijrahDate].
 */
object HijrahDates {

    /**
     * Represents the minimum year supported by the Hijrah calendar system.
     *
     * This value is derived from the range of years defined by the Hijrah chronology.
     * It indicates the lowest possible year that can be represented in the calendar system.
     *
     * This constant is primarily used for validation or formatting tasks where
     * limiting or verifying the year range is necessary within the Hijrah calendar.
     */
    @JvmField val MIN_YEAR = HijrahChronology.INSTANCE.range(ChronoField.YEAR).minimum.toInt()
    /**
     * Represents the maximum year supported by the Hijrah calendar system.
     *
     * This value is derived from the range of years defined by the Hijrah chronology.
     * It indicates the highest possible year that can be represented in the calendar system.
     *
     * This constant is primarily used for validation or formatting tasks where
     * limiting or verifying the year range is necessary within the Hijrah calendar.
     */
    @JvmField val MAX_YEAR = HijrahChronology.INSTANCE.range(ChronoField.YEAR).maximum.toInt()
    /**
     * Represents the minimum supported Hijrah date, defined as the first day of the minimum supported year.
     *
     * This constant marks the earliest point in the Hijrah calendar that can be represented.
     * It is useful in various temporal calculations, comparisons, or when defining bounds
     * for Hijrah date ranges.
     *
     * The value is statically defined as `HijrahDate.of(MIN_YEAR, 1, 1)`, where `MIN_YEAR` corresponds
     * to the earliest valid year in the Hijrah chronological system supported by the implementation.
     */
    @JvmField val MIN: HijrahDate = HijrahDate.of(MIN_YEAR, 1, 1)
    /**
     * The maximum valid `HijrahDate` supported by the system.
     *
     * This variable represents the maximum Hijrah date, specifically
     * the last day of the 12th month in the maximum supported Hijrah year.
     * It is computed by using the maximum year (`MAX_YEAR`), set to the last day
     * of that month, and applying necessary adjustments.
     *
     * This value is primarily used for validation of date operations and
     * ensuring calculations do not exceed the supported range for `HijrahDate`.
     */
    @JvmField val MAX: HijrahDate = HijrahDate.of(MAX_YEAR, 12, 29).with(lastDayOfMonth())
    /**
     * Represents the Hijrah date at the Unix epoch
     *
     * This constant holds a precomputed instance of the Hijrah date for the epoch day 0,
     * which aligns with the start of the Unix time-line. It is primarily used as a reference
     * point for calculations or conversions involving the Hijrah calendar system and the Unix epoch.
     */
    @JvmField val EPOCH: HijrahDate = ofEpochDay(0)

    /**
     * Represents the year in the Hijrah calendar system for the given [HijrahDate].
     *
     * This property retrieves the value of the `[ChronoField.YEAR]` field from the Hijrah calendar
     * representation of the date.
     *
     * @return the year value as an integer within the Hijrah calendar.
     */
    @get:JvmSynthetic inline val HijrahDate.year: Int get() = get(ChronoField.YEAR)
    /**
     * Returns the numeric value of the Hijrah month represented by this HijrahDate.
     *
     * The value will be an integer from 1 to 12, where 1 corresponds to Muharram,
     * the first month of the Hijrah calendar, and 12 corresponds to Dhu al-Hijjah, the last month.
     *
     * This property retrieves the value associated with the field [ChronoField.MONTH_OF_YEAR]
     * from the underlying temporal representation of the Hijrah date.
     *
     * @receiver The HijrahDate instance from which the month value is retrieved.
     * @return The integer value corresponding to the month in the Hijrah calendar.
     */
    @get:JvmSynthetic inline val HijrahDate.monthValue: Int get() = get(ChronoField.MONTH_OF_YEAR)
    /**
     * Retrieves the month-of-year field from the Hijrah date as an instance of [HijrahMonth].
     *
     * This property provides access to the specific [HijrahMonth] instance that corresponds to
     * the `monthValue` of the Hijrah date.
     *
     * @receiver The Hijrah date from which the month instance is derived.
     * @return The month in the Hijrah calendar as a [HijrahMonth] instance.
     */
    @get:JvmSynthetic inline val HijrahDate.month: HijrahMonth get() = HijrahMonth.of(monthValue)
    /**
     * Retrieves the day of the year for a Hijrah calendar date.
     * The value corresponds to the ordinal number of the day within the current year.
     * For example, the first day of the year will return `1`, the second `2`, and so on.
     *
     * @return The day of the year based on the Hijrah calendar chronology.
     */
    @get:JvmSynthetic inline val HijrahDate.dayOfYear: Int get() = get(ChronoField.DAY_OF_YEAR)
    /**
     * Represents the day of the month in the Hijrah calendar system.
     *
     * This property retrieves the value of the {@link ChronoField.DAY_OF_MONTH} field
     * from the current instance of the HijrahDate.
     *
     * The returned value is an integer representing the numerical day within
     * the current month of the Hijrah calendar.
     *
     * @return an [Int] representing the day of the month.
     */
    @get:JvmSynthetic inline val HijrahDate.dayOfMonth: Int get() = get(ChronoField.DAY_OF_MONTH)

    @get:JvmSynthetic inline val HijrahDate.dayOfWeekValue: Int get() = get(ChronoField.DAY_OF_WEEK)


    @get:JvmSynthetic inline val HijrahDate.dayOfWeek: DayOfWeek get() = DayOfWeek.of(dayOfWeekValue)

    /**
     * Returns a copy of this date with the specified number of days added.
     *
     * @param days the days to add, may be negative
     * @return a [HijrahDate] based on this date with the days added, not null
     */
    @JvmSynthetic
    fun HijrahDate.plusDays(days: Long): HijrahDate = plus(days, ChronoUnit.DAYS)

    /**
     * Returns a copy of this date with the specified number of weeks added.
     *
     * @param weeks the weeks to add, may be negative
     * @return a [HijrahDate] based on this date with the weeks added, not null
     */
    @JvmSynthetic
    fun HijrahDate.plusWeeks(weeks: Long): HijrahDate = plus(weeks, ChronoUnit.WEEKS)

    /**
     * Returns a copy of this date with the specified number of months added.
     *
     * @param months the months to add, may be negative
     * @return a [HijrahDate] based on this date with the months added, not null
     */
    @JvmSynthetic
    fun HijrahDate.plusMonths(months: Long): HijrahDate = plus(months, ChronoUnit.MONTHS)

    /**
     * Returns a copy of this date with the specified number of years added.
     *
     * @param years the years to add, may be negative
     * @return a [HijrahDate] based on this date with the years added, not null
     */
    @JvmSynthetic
    fun HijrahDate.plusYears(years: Long): HijrahDate = plus(years, ChronoUnit.YEARS)

    /**
     * Returns a copy of this date with the specified number of days subtracted.
     *
     * @param days the days to subtract, may be negative
     * @return a [HijrahDate] based on this date with the days subtracted, not null
     */
    @JvmSynthetic
    fun HijrahDate.minusDays(days: Long): HijrahDate = minus(days, ChronoUnit.DAYS)

    /**
     * Returns a copy of this date with the specified number of weeks subtracted.
     *
     * @param weeks the weeks to subtract, may be negative
     * @return a [HijrahDate] based on this date with the weeks subtracted, not null
     */
    @JvmSynthetic
    fun HijrahDate.minusWeeks(weeks: Long): HijrahDate = minus(weeks, ChronoUnit.WEEKS)

    /**
     * Returns a copy of this date with the specified number of months subtracted.
     *
     * @param months the months to subtract, may be negative
     * @return a [HijrahDate] based on this date with the months subtracted, not null
     */
    @JvmSynthetic
    fun HijrahDate.minusMonths(months: Long): HijrahDate = minus(months, ChronoUnit.MONTHS)

    /**
     * Returns a copy of this date with the specified number of years subtracted.
     *
     * @param years the years to subtract, may be negative
     * @return a [HijrahDate] based on this date with the years subtracted, not null
     * */
    @JvmSynthetic
    fun HijrahDate.minusYears(years: Long): HijrahDate = minus(years, ChronoUnit.YEARS)

    /**
     * Returns a copy of this date with the day-of-month altered.
     *
     * @param dayOfMonth the day-of-month to set in the returned date. must be between 1 and 29-30 depending on the month last day in the calendar.
     * @return a [HijrahDate] based on this date with the requested day-of-month, not null
     * @throws DateTimeException if the day-of-month is invalid for the month-year
     */
    @JvmSynthetic
    fun HijrahDate.withDayOfMonth(dayOfMonth: Int): HijrahDate =
        with(ChronoField.DAY_OF_MONTH, dayOfMonth.toLong())


    /**
     * Returns a copy of this date with the specified day-of-year.
     *
     * @param dayOfYear the day-of-year to set in the returned date.
     * @return a [HijrahDate] based on this date with the requested day-of-year, not null
     */
    @JvmSynthetic
    fun HijrahDate.withDayOfYear(dayOfYear: Int): HijrahDate =
        with(ChronoField.DAY_OF_YEAR, dayOfYear.toLong())


    /**
     * Returns a copy of this date with the specified month-of-year.
     *
     * @param month the month-of-year to set in the returned date.
     * @return a [HijrahDate] based on this date with the requested month, not null
     */
    @JvmSynthetic
    fun HijrahDate.withMonth(month: HijrahMonth): HijrahDate =
        with(ChronoField.MONTH_OF_YEAR, month.value.toLong())

    /**
     * Returns a copy of this date with the year altered.
     *
     * @param year the year to set in the returned date.
     * @return a [HijrahDate] based on this date with the requested year, not null
     */
    @JvmSynthetic
    fun HijrahDate.withYear(year: Int): HijrahDate = with(ChronoField.YEAR, year.toLong())

    /**
     * Combines this date with the time of midnight to create a [HijrahDateTime]
     * at the start of this date.
     *
     * This returns a [HijrahDateTime] formed from this date at the time of midnight, 00:00, at the start of this date.
     *
     * @return the local date-time of midnight at the start of this date, not null
     */
    @JvmStatic
    fun HijrahDate.atStartOfDay() = HijrahDateTime.of(this, LocalTime.MIDNIGHT)

    /**
     * Combines this date with the time of midnight to create a [ZonedHijrahDateTime]
     * at the start of this date in the specified [zone].
     *
     * This returns a [ZonedHijrahDateTime] formed from this date at the time of midnight, 00:00, at the start of this date.
     *
     * @param zone the time-zone to use, not null
     * @return the local date-time of midnight at the start of this date, not null
     */
    @JvmStatic
    fun HijrahDate.atStartOfDay(zone: ZoneId): ZonedHijrahDateTime {
        return ZonedHijrahDateTime.of(HijrahDateTime.of(this, LocalTime.MIDNIGHT), zone)
    }

    /**
     * Returns a sequence of dates starting from this date and ending before the [endExclusive] date.
     * The dates start from this date inclusive and ends at [endExclusive] date exclusive.
     *
     * The end date must be greater than or equal to this date. Otherwise, an exception will be thrown.
     *
     * @param endExclusive the exclusive end date
     * @throws IllegalArgumentException if the end date is before this date
     * @return the sequence of dates starting from this date and ending before the [endExclusive] date
     */
    @JvmStatic
    fun HijrahDate.datesUntil(endExclusive: HijrahDate): Sequence<HijrahDate> {
        val end = endExclusive.toEpochDay()
        val start = toEpochDay()
        require(end >= start) { "$endExclusive < $this" }
        return generateSequence(start) { it + 1 }
            .takeWhile { it < end }
            .map(HijrahDates::ofEpochDay)
    }

    /**
     * Returns a sequence of dates starting from this date and ending before the [endExclusive] date with the specified [step].
     *
     * The dates start from this date inclusive and ends at [endExclusive] date exclusive.
     *
     * The end date must be greater than or equal to this date. Otherwise, an exception will be thrown.
     *
     * @param endExclusive the exclusive end date
     * @param step the step between each date
     * @throws IllegalArgumentException if the end date is before this date
     * @throws IllegalArgumentException if the step is less than or equal to zero
     * @return the sequence of dates starting from this date and ending before the [endExclusive] date
     */
    @JvmStatic
    fun HijrahDate.datesUntil(endExclusive: HijrahDate, step: Long): Sequence<HijrahDate> {
        val end = endExclusive.toEpochDay()
        val start = toEpochDay()
        require(end >= start) { "$endExclusive < $this" }
        require(step > 0) { "step must be greater than zero, but was $step" }
        return generateSequence(start) { it + step }
            .takeWhile { it < end }
            .map(HijrahDates::ofEpochDay)
    }

    /**
     * Obtains an [OffsetHijrahDateTime] with the specified [OffsetTime].
     */
    @JvmStatic
    fun HijrahDate.atTime(offsetTime: OffsetTime): OffsetHijrahDateTime {
        val datetime = HijrahDateTime.of(this, offsetTime.toLocalTime())
        return OffsetHijrahDateTime.of(datetime, offsetTime.offset)
    }

    /**
     * Obtains an [HijrahDateTime] with the specified [LocalTime]. This function should be always used instead of member function [HijrahDate.atTime].
     */
    @JvmStatic
    fun HijrahDate.atLocalTime(localTime: LocalTime): HijrahDateTime =
        HijrahDateTime.of(this, localTime)

    /**
     * Converts this date to an [Instant].
     *
     * This combines this local date (At start of the day) and the specified offset to form
     * an [Instant].
     *
     * @param offset  the offset to use for the conversion, not null
     * @return an [Instant] representing the same instant, not null
     */
    @JvmStatic
    fun HijrahDate.toInstant(offset: ZoneOffset): Instant = atStartOfDay(offset).toInstant()


    /**
     * Creates an instance of [HijrahDate] from the specified [epochDay].
     *
     * @param epochDay the epoch-day to use, not null
     * @return the created [HijrahDate], not null
     */
    @JvmStatic
    @FactoryFunction
    fun ofEpochDay(epochDay: Long): HijrahDate =
        HijrahChronology.INSTANCE.dateEpochDay(epochDay)


    /**
     * Creates an instance of [HijrahDate] from the specified [year] and [dayOfYear].
     *
     * @param year the year to use, not null
     * @param dayOfYear the day-of-year to use, not null
     */
    @JvmStatic
    @FactoryFunction
    fun ofYearDay(year: Int, dayOfYear: Int): HijrahDate =
        HijrahChronology.INSTANCE.dateYearDay(year, dayOfYear)

    /**
     * Creates an instance of [HijrahDate] from the specified [Instant] and [ZoneId].
     *
     * @param instant  the instant to use, not null
     * @param zone  the zone to use, not null
     *
     * @return the created [HijrahDate], not null
     */
    @JvmStatic
    @FactoryFunction
    fun ofInstant(instant: Instant, zone: ZoneId): HijrahDate {
        val offset = zone.rules.getOffset(instant)
        val localSecond = instant.epochSecond + offset.totalSeconds
        val localEpochDay = Math.floorDiv(localSecond, SECONDS_PER_DAY)
        return ofEpochDay(localEpochDay)
    }

    /**
     * Parses a string to obtain an instance of [HijrahDate].
     * This parser uses the default formatter, [HijrahFormatters.HIJRAH_DATE].
     *
     * @param text  the text to parse, not null
     * @return the parsed local date-time, not null
     */
    @JvmStatic
    @FactoryFunction
    fun parse(text: CharSequence) = parse(text, HijrahFormatters.HIJRAH_DATE)

    /**
     * Parses a string to obtain an instance of [HijrahDate].
     * This parser uses the specified formatter.
     *
     * @param text  the text to parse, not null
     * @param formatter  the formatter to use, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    @JvmStatic
    @FactoryFunction
    fun parse(text: CharSequence, formatter: DateTimeFormatter): HijrahDate =
        requireHijrahChronology(formatter).parse(text, HijrahDate::from)

    private const val SECONDS_PER_DAY = 86400

    /** Factory function annotation, used to mark functions that create instances of [HijrahDate]. */
    private annotation class FactoryFunction

}