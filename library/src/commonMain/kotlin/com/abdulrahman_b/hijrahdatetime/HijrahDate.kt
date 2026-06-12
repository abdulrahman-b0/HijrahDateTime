@file:Suppress("unused")

package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateComponentsSerializer
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Represents a date in the Hijrah (Islamic) calendar system.
 *
 * This class is [Serializable] and uses [HijrahDateComponentsSerializer] for serialization.
 *
 * @param year the Hijrah year.
 * @param month the Hijrah month of year, from 1 to 12.
 * @param dayOfMonth the day of month, from 1 to 30 (depending on the month).
 * @throws IllegalArgumentException if the date is invalid.
 */
@Serializable(with = HijrahDateComponentsSerializer::class)
expect class HijrahDate(year: Int, month: Int, dayOfMonth: Int) : Comparable<HijrahDate> {

    /** The Hijrah year. */
    val year: Int
    /** The Hijrah month. */
    val month: HijrahMonth
    /** The day of month. */
    val day: Int
    /** The day of week. */
    val dayOfWeek: DayOfWeek
    /** The day of year. */
    val dayOfYear: Int


    override fun compareTo(other: HijrahDate): Int

    /**
     * Returns a copy of this date with the specified [period] added.
     * @throws kotlinx.datetime.DateTimeArithmeticException if the result is out of supported range.
     */
    operator fun plus(period: DatePeriod): HijrahDate

    /**
     * Returns a copy of this date with the specified [value] of [unit] added.
     * @throws kotlinx.datetime.DateTimeArithmeticException if the result is out of supported range.
     */
    fun plus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate

    /**
     * Returns a copy of this date with the specified [period] subtracted.
     * @throws kotlinx.datetime.DateTimeArithmeticException if the result is out of supported range.
     */
    operator fun minus(period: DatePeriod): HijrahDate

    /**
     * Returns a copy of this date with the specified [value] of [unit] subtracted.
     * @throws kotlinx.datetime.DateTimeArithmeticException if the result is out of supported range.
     */
    fun minus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate

    /**
     * Formats this date using the specified [format].
     */
    fun format(format: HijrahDateTimeFormat): String

    /**
     * Returns the number of days since the epoch (1970-01-01 ISO).
     */
    fun toEpochDays(): Long

    /**
     * Returns the range of valid values for the specified [unit] in this date.
     * For example, the range of days in the current month.
     */
    fun range(unit: DateTimeUnit.DateBased): ValueRange


    companion object {

        /**
         * Creates a [HijrahDate] from the number of days since the epoch (1970-01-01 ISO).
         */
        fun fromEpochDays(epochDay: Long): HijrahDate

        /**
         * Parses a [HijrahDate] from a string using the specified [format].
         * @throws IllegalArgumentException if the string cannot be parsed.
         */
        fun parse(string: String, format: HijrahDateTimeFormat): HijrahDate

        /**
         * Parses a [HijrahDate] from a string using the specified [format], or returns null if parsing fails.
         */
        fun parseOrNull(string: String, format: HijrahDateTimeFormat): HijrahDate?

        /** The minimum supported [HijrahDate]. */
        val MIN: HijrahDate
        /** The maximum supported [HijrahDate]. */
        val MAX: HijrahDate

    }

}


/** Converts this [LocalDate] to a [HijrahDate]. */
fun LocalDate.toHijrahDate(): HijrahDate = HijrahDate.fromEpochDays(toEpochDays())
/** Converts this [HijrahDate] to a [LocalDate]. */
fun HijrahDate.toLocalDate(): LocalDate = LocalDate.fromEpochDays(toEpochDays())


/** Returns a copy of this date with the specified number of days added. */
infix fun HijrahDate.plusDays(value: Int) = plus(value, DateTimeUnit.DAY)
/** Returns a copy of this date with the specified number of months added. */
infix fun HijrahDate.plusMonths(value: Int) = plus(value, DateTimeUnit.MONTH)
/** Returns a copy of this date with the specified number of weeks added. */
infix fun HijrahDate.plusWeeks(value: Int) = plus(value, DateTimeUnit.WEEK)
/** Returns a copy of this date with the specified number of years added. */
infix fun HijrahDate.plusYears(value: Int) = plus(value, DateTimeUnit.YEAR)
/** Returns a copy of this date with the specified number of days subtracted. */
infix fun HijrahDate.minusDays(value: Int) = minus(value, DateTimeUnit.DAY)
/** Returns a copy of this date with the specified number of weeks subtracted. */
infix fun HijrahDate.minusWeeks(value: Int) = minus(value, DateTimeUnit.WEEK)
/** Returns a copy of this date with the specified number of months subtracted. */
infix fun HijrahDate.minusMonths(value: Int) = minus(value, DateTimeUnit.MONTH)
/** Returns a copy of this date with the specified number of years subtracted. */
infix fun HijrahDate.minusYears(value: Int) = minus(value, DateTimeUnit.YEAR)

/** Combines this date with a [time] to create a [HijrahDateTime]. */
fun HijrahDate.atTime(time: LocalTime): HijrahDateTime =
    HijrahDateTime.of(this, time)

/** Returns the [Instant] at the start of this date in the specified [timeZone]. */
fun HijrahDate.atStartOfDay(timeZone: TimeZone): Instant = toLocalDate().atStartOfDayIn(timeZone)

/**
 * Returns a copy of this date with the specified [value] of [unit] replaced.
 */
fun HijrahDate.with(value: Int, unit: DateTimeUnit.DateBased): HijrahDate {
    return HijrahDate(
        year = if (unit == DateTimeUnit.YEAR) value else year,
        month = if (unit == DateTimeUnit.MONTH) value else month.number,
        dayOfMonth = if (unit == DateTimeUnit.DAY) value else day
    )
}

/** Returns a copy of this date with the specified [dayOfMonth] replaced. */
fun HijrahDate.withDayOfMonth(value: Int): HijrahDate = with(value, DateTimeUnit.DAY)
/** Returns a copy of this date with the specified [month] number replaced. */
fun HijrahDate.withMonth(value: Int): HijrahDate = with(value, DateTimeUnit.MONTH)
/** Returns a copy of this date with the specified [month] replaced. */
fun HijrahDate.withMonth(month: HijrahMonth): HijrahDate = with(month.number, DateTimeUnit.MONTH)
/** Returns a copy of this date with the specified [year] replaced. */
fun HijrahDate.withYear(value: Int): HijrahDate = with(value, DateTimeUnit.YEAR)

/** Returns a copy of this date adjusted to the previous occurrence of the specified [target] day of week. */
fun HijrahDate.withPreviousDayOfWeek(target: DayOfWeek): HijrahDate {
    val diff = dayOfWeek.isoDayNumber - target.isoDayNumber
    // If diff is 0, we want 7 days ago. If diff is positive (e.g., today is Wed (3), target Mon (1)), 3-1 = 2 days ago.
    // If diff is negative (e.g., today is Mon (1), target Wed (3)), 1-3 = -2. -2 + 7 = 5 days ago.
    val factor = if (diff <= 0) diff + 7 else diff
    return minusDays(factor)
}

/** Returns a copy of this date adjusted to the next occurrence of the specified [target] day of week. */
fun HijrahDate.withNextDayOfWeek(target: DayOfWeek): HijrahDate {
    val diff = target.isoDayNumber - dayOfWeek.isoDayNumber
    // If diff is 0, we want 7 days from now. If diff is positive, that's the number.
    // If diff is negative, add 7.
    val factor = if (diff <= 0) diff + 7 else diff
    return plusDays(factor)
}

/** Returns a copy of this date adjusted to the same or previous occurrence of the specified [target] day of week. */
fun HijrahDate.withSameOrPreviousDayOfWeek(target: DayOfWeek): HijrahDate {
    return if (dayOfWeek == target) this else withPreviousDayOfWeek(target)
}

/** Returns a copy of this date adjusted to the same or next occurrence of the specified [target] day of week. */
fun HijrahDate.withSameOrNextDayOfWeek(target: DayOfWeek): HijrahDate {
    return if (dayOfWeek == target) this else withNextDayOfWeek(target)
}

/** Returns a copy of this date adjusted to the last day of the month. */
fun HijrahDate.withLastDayOfMonth(): HijrahDate {
    val lastDay = range(DateTimeUnit.DAY).maximum.toInt()
    return withDayOfMonth(lastDay)
}


/** Returns a range from this date to the specified [other] date. */
operator fun HijrahDate.rangeTo(other: HijrahDate): HijrahDateRange = HijrahDateRange(this, other)

/** Returns an open-ended range from this date to the specified [other] date. */
operator fun HijrahDate.rangeUntil(other: HijrahDate): HijrahDateRange =
    HijrahDateRange(this, other.minusDays(1))

/** Returns a progression from this date down to the specified [other] date. */
infix fun HijrahDate.downTo(other: HijrahDate): HijrahDateProgression {
    return HijrahDateProgression(this, other, step = -1)
}

