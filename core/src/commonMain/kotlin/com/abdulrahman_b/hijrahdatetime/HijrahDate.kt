@file:Suppress("unused")

package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.internal.BASE_GREGORIAN_EPOCH_DAY
import com.abdulrahman_b.hijrahdatetime.internal.DAYS_OF_WEEK
import com.abdulrahman_b.hijrahdatetime.internal.LEAP_YEAR_LENGTH
import com.abdulrahman_b.hijrahdatetime.internal.MONTHS_OF_YEAR
import com.abdulrahman_b.hijrahdatetime.internal.QUARTERS_OF_YEAR
import com.abdulrahman_b.hijrahdatetime.internal.UmmAlQuraData
import com.abdulrahman_b.hijrahdatetime.internal.WEEKS_OF_MONTH
import com.abdulrahman_b.hijrahdatetime.internal.getLengthOfMonth
import com.abdulrahman_b.hijrahdatetime.internal.getLengthOfYear
import com.abdulrahman_b.hijrahdatetime.internal.runArithmeticCatching
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
 * @param day the day of month, from 1 to 30 (depending on the month).
 * @throws IllegalArgumentException if the date is invalid.
 */
@Serializable(with = HijrahDateComponentsSerializer::class)
expect class HijrahDate(year: Int, month: Int, day: Int) : Comparable<HijrahDate> {

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
     * Formats this date using the specified [format].
     */
    fun format(format: HijrahDateTimeFormat): String


    companion object {

        /**
         * Parses a [HijrahDate] from a string using the specified [format].
         * @throws IllegalArgumentException if the string cannot be parsed.
         */
        fun parse(string: String, format: HijrahDateTimeFormat): HijrahDate

        /**
         * Parses a [HijrahDate] from a string using the specified [format], or returns null if parsing fails.
         */
        fun parseOrNull(string: String, format: HijrahDateTimeFormat): HijrahDate?

        val MIN: HijrahDate

        val MAX: HijrahDate

    }

}

/**
 * Indicates whether the current Hijrah year is a leap year.
 *
 * In the Hijrah calendar system, a leap year has an additional day.
 * This property checks the length of the current year to determine whether
 * it corresponds to a leap year.
 *
 * @return `true` if the current year is a leap year; `false` otherwise.
 */
val HijrahDate.isLeapYear: Boolean
    get() = getLengthOfYear(year) == LEAP_YEAR_LENGTH

fun LocalDate.toHijrahDate(): HijrahDate = HijrahDate.fromEpochDays(toEpochDays())


fun HijrahDate.toLocalDate(): LocalDate = LocalDate.fromEpochDays(toEpochDays())


/** Returns a copy of this date with the specified number of days added. */
infix fun HijrahDate.plusDays(value: Int): HijrahDate {
    return runArithmeticCatching {
        val newDay = day + value
        if (newDay in 1..getLengthOfMonth(year, month.number))
            return HijrahDate(year, month.number, newDay)
        return HijrahDate.fromEpochDays(toEpochDays() + value.toLong())
    }
}

/** Returns a copy of this date with the specified number of months added. */
infix fun HijrahDate.plusMonths(value: Int): HijrahDate = runArithmeticCatching {
    // 1. Calculate absolute month index
    val totalMonths = (this.year * 12 + (this.month.number - 1)) + value
    val newYear = totalMonths / 12
    val newMonth = (totalMonths % 12) + 1

    // 2. Clamp the day: If the new month has 29 days, but we are on day 30,
    // we must cap it at 29.
    val maxDaysInNewMonth = getLengthOfMonth(newYear, newMonth)
    val newDay = minOf(this.day, maxDaysInNewMonth)

    return@runArithmeticCatching HijrahDate(newYear, newMonth, newDay)
}

/** Returns a copy of this date with the specified number of weeks added. */
infix fun HijrahDate.plusWeeks(value: Int) = plusDays(value * DAYS_OF_WEEK)

/** Returns a copy of this date with the specified number of years added. */
infix fun HijrahDate.plusYears(value: Int): HijrahDate = runArithmeticCatching {
    val newYear = year + value

    // 1. Anchor: Keep the same month and day initially
    // 2. Clamp: If the day exceeds the month's length in the target year,
    //    reduce it to the last day of that month.
    val maxDaysInMonth = getLengthOfMonth(newYear, month.number)
    val newDay = minOf(day, maxDaysInMonth)

    return@runArithmeticCatching HijrahDate(newYear, month.number, newDay)
}

/** Returns a copy of this date with the specified number of days subtracted. */
infix fun HijrahDate.minusDays(value: Int): HijrahDate {
    return runArithmeticCatching {
        return HijrahDate.fromEpochDays(toEpochDays() - value)
    }
}

/** Returns a copy of this date with the specified number of weeks subtracted. */
infix fun HijrahDate.minusWeeks(value: Int) = plusWeeks(-value)

/** Returns a copy of this date with the specified number of months subtracted. */
infix fun HijrahDate.minusMonths(value: Int): HijrahDate = plusMonths(-value)

/** Returns a copy of this date with the specified number of years subtracted. */
infix fun HijrahDate.minusYears(value: Int) = plusYears(-value)

/** Combines this date with a [time] to create a [HijrahDateTime]. */
fun HijrahDate.atTime(time: LocalTime): HijrahDateTime =
    HijrahDateTime.of(this, time)

/** Returns the [Instant] at the start of this date in the specified [timeZone]. */
fun HijrahDate.atStartOfDay(timeZone: TimeZone): Instant = toLocalDate().atStartOfDayIn(timeZone)

/**
 * Returns a copy of this date with the specified [value] of [unit] replaced.
 */
fun HijrahDate.with(value: Int, unit: DateTimeUnit.DateBased): HijrahDate {
    when (unit) {
        DateTimeUnit.DAY -> require(value in 1..range(unit).last) { "Hijrah date out of range: day must be in range 1..${range(unit).last}" }
        DateTimeUnit.MONTH -> require(value in 1..range(unit).last) { "Hijrah date out of range: month must be in range 1..${range(unit).last}" }
        DateTimeUnit.YEAR -> require(value in range(unit)) { "Hijrah date out of range: year must be in range ${range(unit).first}..${range(unit).last}" }
        else -> throw IllegalArgumentException("Invalid unit: $unit")
    }
    return HijrahDate(
        year = if (unit == DateTimeUnit.YEAR) value else year,
        month = if (unit == DateTimeUnit.MONTH) value else month.number,
        day = if (unit == DateTimeUnit.DAY) value else day
    )
}

/** Returns a copy of this date with the specified [value] replaced. */
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
    val lastDay =
        range(DateTimeUnit.DAY).let {
            ValueRange(
                it.start.toLong(),
                it.endInclusive.toLong()
            )
        }.maximum.toInt()
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

fun HijrahDate.toEpochDays(): Long {
    val daysFromYears =
        (UmmAlQuraData.BASE_HIJRI_YEAR until year).sumOf { getLengthOfYear(it).toLong() }
    val daysFromMonths = (1 until month.number).sumOf { getLengthOfMonth(year, it) }
    val days = day - 1
    return daysFromYears + daysFromMonths + days + BASE_GREGORIAN_EPOCH_DAY
}

/**
 * Creates a [HijrahDate] from the number of days since the epoch (1970-01-01 ISO).
 */
fun HijrahDate.Companion.fromEpochDays(epoch: Long): HijrahDate {
    val relativeEpoch = epoch - BASE_GREGORIAN_EPOCH_DAY
    var remainingDays = relativeEpoch

    var year = UmmAlQuraData.BASE_HIJRI_YEAR
    while (true) {
        val daysInYear = getLengthOfYear(year)
        if (remainingDays < daysInYear) break
        remainingDays -= daysInYear
        year++
    }

    var month = 1
    while (true) {
        val daysInMonth = getLengthOfMonth(year, month)
        if (remainingDays < daysInMonth) break
        remainingDays -= daysInMonth
        month++
    }

    val day = (remainingDays + 1).toInt()


    return HijrahDate(year, month, day)
}
/**
 * Returns a copy of this date with the specified [period] added.
 * @throws kotlinx.datetime.DateTimeArithmeticException if the result is out of supported range.
 */
fun HijrahDate.plus(period: DatePeriod): HijrahDate {
    var newDate = this
    if (period.years > 0)
        newDate = newDate.plusYears(period.years)
    if (period.months > 0)
        newDate = newDate.plusMonths(period.months)
    if (period.days > 0)
        newDate = newDate.plusDays(period.days)
    return newDate
}

/**
 * Returns a copy of this date with the specified [period] subtracted.
 * @throws kotlinx.datetime.DateTimeArithmeticException if the result is out of supported range.
 */
fun HijrahDate.minus(period: DatePeriod): HijrahDate {
    var newDate = this
    if (period.years > 0)
        newDate = newDate.minusYears(period.years)
    if (period.months > 0)
        newDate = newDate.minusMonths(period.months)
    if (period.days > 0)
        newDate = newDate.minusDays(period.days)
    return newDate
}

/**
 * Returns a copy of this date with the specified [value] of [unit] added.
 * @throws kotlinx.datetime.DateTimeArithmeticException if the result is out of supported range.
 */
fun HijrahDate.plus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate {

    val value = when (unit) {
        DateTimeUnit.DAY -> plusDays(value)
        DateTimeUnit.WEEK -> plusWeeks(value)
        DateTimeUnit.MONTH -> plusMonths(value)
        DateTimeUnit.QUARTER -> plusMonths(value * 3)
        DateTimeUnit.YEAR -> plusYears(value)
        DateTimeUnit.CENTURY -> plusYears(value * 100)
        else -> throw IllegalArgumentException("Invalid unit: $unit")
    }

    return value
}

/**
 * Returns a copy of this date with the specified [value] of [unit] subtracted.
 * @throws kotlinx.datetime.DateTimeArithmeticException if the result is out of supported range.
 */
fun HijrahDate.minus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate {
    val value = when (unit) {
        DateTimeUnit.DAY -> minusDays(value)
        DateTimeUnit.WEEK -> minusWeeks(value)
        DateTimeUnit.MONTH -> minusMonths(value)
        DateTimeUnit.QUARTER -> minusMonths(value * 3)
        DateTimeUnit.YEAR -> minusYears(value)
        DateTimeUnit.CENTURY -> minusYears(value * 100)
        else -> throw IllegalArgumentException("Invalid unit: $unit")
    }

    return value
}

/**
 * Returns the range of valid values for the specified [unit] in this date.
 * For example, the range of days in the current month.
 */
fun HijrahDate.range(unit: DateTimeUnit.DateBased): IntRange {
    return when (unit) {
        DateTimeUnit.DAY -> IntRange(1, getLengthOfMonth(year, month.number))
        DateTimeUnit.WEEK -> IntRange(1, WEEKS_OF_MONTH)
        DateTimeUnit.MONTH -> IntRange(1, MONTHS_OF_YEAR)
        DateTimeUnit.QUARTER -> IntRange(1, QUARTERS_OF_YEAR)
        DateTimeUnit.YEAR -> IntRange(UmmAlQuraData.BASE_HIJRI_YEAR, UmmAlQuraData.MAX_HIJRI_YEAR)
        DateTimeUnit.CENTURY -> IntRange(UmmAlQuraData.BASE_HIJRI_YEAR / 100, UmmAlQuraData.MAX_HIJRI_YEAR / 100)
        else -> throw IllegalArgumentException("Invalid unit: $unit")
    }
}

