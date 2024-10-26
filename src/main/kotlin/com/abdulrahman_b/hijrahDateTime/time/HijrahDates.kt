@file:Suppress("MemberVisibilityCanBePrivate", "unused")
package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronologyFormatter
import java.time.Instant
import java.time.LocalTime
import java.time.OffsetTime
import java.time.ZoneId
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

object HijrahDates {


    inline val HijrahDate.year: Int get() = get(ChronoField.YEAR)

    inline val HijrahDate.monthValue: Int get() = get(ChronoField.MONTH_OF_YEAR)

    inline val HijrahDate.month: HijrahMonth get() = HijrahMonth.of(monthValue)

    inline val HijrahDate.dayOfYear: Int get() = get(ChronoField.DAY_OF_YEAR)

    inline val HijrahDate.dayOfMonth: Int get() = get(ChronoField.DAY_OF_MONTH)

    inline val HijrahDate.dayOfWeek: Int get() = get(ChronoField.DAY_OF_WEEK)

    fun HijrahDate.plusDays(days: Long): HijrahDate = plus(days, ChronoUnit.DAYS)

    fun HijrahDate.plusMonths(months: Long): HijrahDate = plus(months, ChronoUnit.MONTHS)

    fun HijrahDate.plusYears(years: Long): HijrahDate = plus(years, ChronoUnit.YEARS)

    fun HijrahDate.minusDays(days: Long): HijrahDate = minus(days, ChronoUnit.DAYS)

    fun HijrahDate.minusMonths(months: Long): HijrahDate = minus(months, ChronoUnit.MONTHS)

    fun HijrahDate.minusYears(years: Long): HijrahDate = minus(years, ChronoUnit.YEARS)

    fun HijrahDate.withDayOfMonth(dayOfMonth: Int): HijrahDate =
        with(ChronoField.DAY_OF_MONTH, dayOfMonth.toLong())

    fun HijrahDate.withDayOfYear(dayOfYear: Int): HijrahDate =
        with(ChronoField.DAY_OF_YEAR, dayOfYear.toLong())

    fun HijrahDate.withMonth(month: HijrahMonth): HijrahDate =
        with(ChronoField.MONTH_OF_YEAR, month.value.toLong())

    fun HijrahDate.withYear(year: Int): HijrahDate = with(ChronoField.YEAR, year.toLong())

    fun HijrahDate.atStartOfDay() = HijrahDateTime.of(this, LocalTime.MIDNIGHT)

    fun HijrahDate.atStartOfDay(zone: ZoneId): ZonedHijrahDateTime {
        return ZonedHijrahDateTime.of(HijrahDateTime.of(this, LocalTime.MIDNIGHT), zone)
    }

    fun HijrahDate.datesUntil(endExclusive: HijrahDate): Sequence<HijrahDate> {
        val end = endExclusive.toEpochDay()
        val start = toEpochDay()
        require(end >= start) { "$endExclusive < $this" }
        return generateSequence(start) { it + 1 }
            .takeWhile { it < end }
            .map(HijrahDates::ofEpochDay)
    }

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
    fun HijrahDate.atTime(offsetTime: OffsetTime): OffsetHijrahDateTime {
        val datetime = HijrahDateTime.of(this, offsetTime.toLocalTime())
        return OffsetHijrahDateTime.of(datetime, offsetTime.offset)
    }


    /**
     * Creates an instance of [HijrahDate] from the specified [epochDay].
     *
     * @param epochDay the epoch-day to use, not null
     * @return the created [HijrahDate], not null
     */
    fun ofEpochDay(epochDay: Long): HijrahDate {
        return HijrahChronology.INSTANCE.dateEpochDay(epochDay)
    }

    /**
     * Creates an instance of [HijrahDate] from the specified [year] and [dayOfYear].
     *
     * @param year the year to use, not null
     * @param dayOfYear the day-of-year to use, not null
     */
    fun ofYearDay(year: Int, dayOfYear: Int): HijrahDate {
        return HijrahChronology.INSTANCE.dateYearDay(year, dayOfYear)
    }

    /**
     * Creates an instance of [HijrahDate] from the specified [Instant] and [ZoneId].
     *
     * @param instant  the instant to use, not null
     * @param zone  the zone to use, not null
     *
     * @return the created [HijrahDate], not null
     */
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
    fun parse(text: CharSequence) = parse(text, HijrahFormatters.HIJRAH_DATE)

    /**
     * Parses a string to obtain an instance of [HijrahDate].
     * This parser uses the specified formatter.
     *
     * @param text  the text to parse, not null
     * @param formatter  the formatter to use, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    fun parse(text: CharSequence, formatter: DateTimeFormatter): HijrahDate {
        requireHijrahChronologyFormatter(formatter)
        return formatter.parse(text, HijrahDate::from)
    }

    private const val SECONDS_PER_DAY = 86400


}