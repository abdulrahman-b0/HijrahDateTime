package com.abdulrahman_b.hijrahdatetime.yearmonth

import com.abdulrahman_b.hijrahdatetime.*
import kotlinx.datetime.DateTimeArithmeticException
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

/**
 * The year-month part of [HijrahDate], without a day-of-month.
 *
 * This class represents years and months without a reference to a particular time zone.
 * As such, these objects may denote different time intervals in different time zones: for someone in Berlin,
 * `1447-09` started and ended at different moments from those for someone in Tokyo.
 *
 * ### Arithmetic operations
 *
 * The arithmetic on [HijrahYearMonth] values is defined independently of the time zone (so `1447-09` plus one month
 * is `1447-10` everywhere).
 *
 * Operations with [DateTimeUnit.MonthBased] are provided for [HijrahYearMonth]:
 * - [HijrahYearMonth.plus] and [HijrahYearMonth.minus] allow expressing concepts like "two months later".
 * - [HijrahYearMonth.until] and its shortcuts [HijrahYearMonth.m] and [HijrahYearMonth.yearsUntil]
 *   can be used to find the number of months or years between two dates.
 *  */
class HijrahYearMonth(val year: Int, val month: HijrahMonth) : Comparable<HijrahYearMonth> {

    constructor(year: Int, month: Int) : this(
        year, HijrahMonth.entries.getOrElse(month - 1) { throw IllegalArgumentException("Invalid HijrahYearMonth: $year-$month") }
    )

    private val date = HijrahDate(year, month.number, 1)

    /**
     * Returns the first day of the year-month.
     */
    val firstDay get() = date

    /**
     * Returns the last day of the year-month.
     */
    val lastDay: HijrahDate = date.withLastDayOfMonth()


    /**
     * Returns the range of days in the year-month.
     */
    val days: HijrahDateRange = HijrahDateRange(firstDay, lastDay)

    /**
     * Returns the number of days in the year-month.
     */
    val numberOfDays: Int = lastDay.day

    val prolepticMonth: Int get() = year * 12 + (month.number - 1)


    fun plus(value: Int, unit: DateTimeUnit.MonthBased): HijrahYearMonth = date.plus(value, unit).yearMonth

    /**
     * Returns a [YearMonth] that results from subtracting the [value] number of the specified [unit] from this year-month.
     *
     * If the [value] is positive, the returned year-month is earlier than this year-month.
     * If the [value] is negative, the returned year-month is later than this year-month.
     *
     * @throws DateTimeArithmeticException if the result exceeds the boundaries of [YearMonth].
     */
    fun minus(value: Int, unit: DateTimeUnit.MonthBased): HijrahYearMonth =
        if (value != Int.MIN_VALUE) plus(-value, unit) else plus(Int.MAX_VALUE, unit).plus(1, unit)

    fun plusMonth(value: Int) = plus(value, DateTimeUnit.MONTH)
    fun minusMonth(value: Int) = minus(value, DateTimeUnit.MONTH)
    fun plusYear(value: Int) = plus(value, DateTimeUnit.YEAR)
    fun minusYear(value: Int) = minus(value, DateTimeUnit.YEAR)

    private fun until(other: HijrahYearMonth, unit: DateTimeUnit): Long {
        return when (unit) {
            DateTimeUnit.MONTH -> (other.year.toLong() - year) * 12 + (other.month.number - month.number)
            DateTimeUnit.YEAR -> (other.year.toLong() - year).let { years ->
                if (years > 0 && other.month.number < month.number) years - 1
                else if (years < 0 && other.month.number > month.number) years + 1
                else years
            }

            else -> throw IllegalArgumentException("Unsupported unit: $unit")
        }
    }

    fun untilMonth(other: HijrahYearMonth) = until(other, DateTimeUnit.MONTH)
    fun untilYear(other: HijrahYearMonth) = until(other, DateTimeUnit.YEAR)

    infix fun downTo(other: HijrahYearMonth) = HijrahYearMonthProgression(this, other, -1)

    fun format(format: HijrahDateTimeFormat): String = date.format(format)

    override fun compareTo(other: HijrahYearMonth): Int = date.compareTo(other.date)


    /**
     * Combines this year-month with the specified day-of-month into a [LocalDate] value.
     *
     * @throw IllegalArgumentException if the [day] is out of range for this year-month.
     */
    fun onDay(day: Int): HijrahDate {
        return runCatching {
            HijrahDate(year, month.number, day)
        }.onFailure { throw IllegalArgumentException("Invalid day of month: $day") }.getOrThrow()
    }


    override fun equals(other: Any?): Boolean {
        return this.date == (other as? HijrahYearMonth)?.date
    }

    override fun hashCode() = date.hashCode()

    override fun toString() = "$year-${month.number.toString().padStart(2, '0')}"


    companion object {

        internal val Format by lazy {
            HijrahDateTimeFormatBuilder().apply {
                year(); char('-'); monthNumber()
            }.build()
        }

        internal fun fromProlepticMonth(prolepticMonth: Int): HijrahYearMonth {
            val year = prolepticMonth.floorDiv(12)
            require(year in HijrahDate.MIN.year..HijrahDate.MAX.year) {
                "Year $year is out of range: ${HijrahDate.MIN.year}..${HijrahDate.MAX.year}"
            }
            val month = prolepticMonth.mod(12) + 1
            return HijrahYearMonth(year, month)
        }

        val MAX by lazy { HijrahDate.MAX.yearMonth }
        val MIN by lazy { HijrahDate.MIN.yearMonth }
    }
}

expect fun HijrahYearMonth.Companion.parse(text: String): HijrahYearMonth

expect fun HijrahYearMonth.Companion.parseOrNull(text: String): HijrahYearMonth?



