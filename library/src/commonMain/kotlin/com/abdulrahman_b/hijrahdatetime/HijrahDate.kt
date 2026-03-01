@file:Suppress("unused")

package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateComponentsSerializer
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable(with = HijrahDateComponentsSerializer::class)
expect class HijrahDate(year: Int, month: Int, dayOfMonth: Int) : Comparable<HijrahDate> {


    val year: Int
    val month: HijrahMonth
    val day: Int
    val dayOfWeek: DayOfWeek
    val dayOfYear: Int


    override fun compareTo(other: HijrahDate): Int

    operator fun plus(period: DatePeriod): HijrahDate

    fun plus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate

    operator fun minus(period: DatePeriod): HijrahDate

    fun minus(value: Int, unit: DateTimeUnit.DateBased): HijrahDate

    fun format(format: HijrahDateTimeFormat): String

    fun toEpochDays(): Long

    fun range(unit: DateTimeUnit.DateBased): ValueRange


    companion object {

        fun fromEpochDays(epochDay: Long): HijrahDate

        fun parse(string: String, format: HijrahDateTimeFormat): HijrahDate

        fun parseOrNull(string: String, format: HijrahDateTimeFormat): HijrahDate?

        val MIN: HijrahDate
        val MAX: HijrahDate

    }

}

fun Instant.toHijrahDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): HijrahDate =
    toLocalDateTime(timeZone).toHijrahDateTime().date


fun LocalDate.toHijrahDate(): HijrahDate = HijrahDate.fromEpochDays(toEpochDays())
fun HijrahDate.toLocalDate(): LocalDate = LocalDate.fromEpochDays(toEpochDays())


infix fun HijrahDate.plusDays(value: Int) = plus(value, DateTimeUnit.DAY)
infix fun HijrahDate.plusMonths(value: Int) = plus(value, DateTimeUnit.MONTH)
infix fun HijrahDate.plusWeeks(value: Int) = plus(value, DateTimeUnit.WEEK)
infix fun HijrahDate.plusYears(value: Int) = plus(value, DateTimeUnit.YEAR)
infix fun HijrahDate.minusDays(value: Int) = minus(value, DateTimeUnit.DAY)
infix fun HijrahDate.minusWeeks(value: Int) = minus(value, DateTimeUnit.WEEK)
infix fun HijrahDate.minusMonths(value: Int) = minus(value, DateTimeUnit.MONTH)
infix fun HijrahDate.minusYears(value: Int) = minus(value, DateTimeUnit.YEAR)

fun HijrahDate.atTime(time: LocalTime): HijrahDateTime =
    HijrahDateTime.of(this, time)

fun HijrahDate.atStartOfDay(timeZone: TimeZone): Instant = toLocalDate().atStartOfDayIn(timeZone)

fun HijrahDate.with(value: Int, unit: DateTimeUnit.DateBased): HijrahDate {
    return HijrahDate(
        year = if (unit == DateTimeUnit.YEAR) value else year,
        month = if (unit == DateTimeUnit.MONTH) value else month.number,
        dayOfMonth = if (unit == DateTimeUnit.DAY) value else day
    )
}

fun HijrahDate.withDayOfMonth(value: Int): HijrahDate = with(value, DateTimeUnit.DAY)
fun HijrahDate.withMonth(value: Int): HijrahDate = with(value, DateTimeUnit.MONTH)
fun HijrahDate.withMonth(month: HijrahMonth): HijrahDate = with(month.number, DateTimeUnit.MONTH)
fun HijrahDate.withYear(value: Int): HijrahDate = with(value, DateTimeUnit.YEAR)

fun HijrahDate.withPreviousDayOfWeek(target: DayOfWeek): HijrahDate {
    val diff = dayOfWeek.isoDayNumber - target.isoDayNumber
    // If diff is 0, we want 7 days ago. If diff is positive (e.g., today is Wed (3), target Mon (1)), 3-1 = 2 days ago.
    // If diff is negative (e.g., today is Mon (1), target Wed (3)), 1-3 = -2. -2 + 7 = 5 days ago.
    val factor = if (diff <= 0) diff + 7 else diff
    return minusDays(factor)
}

fun HijrahDate.withNextDayOfWeek(target: DayOfWeek): HijrahDate {
    val diff = target.isoDayNumber - dayOfWeek.isoDayNumber
    // If diff is 0, we want 7 days from now. If diff is positive, that's the number.
    // If diff is negative, add 7.
    val factor = if (diff <= 0) diff + 7 else diff
    return plusDays(factor)
}

fun HijrahDate.withSameOrPreviousDayOfWeek(target: DayOfWeek): HijrahDate {
    return if (dayOfWeek == target) this else withPreviousDayOfWeek(target)
}

fun HijrahDate.withSameOrNextDayOfWeek(target: DayOfWeek): HijrahDate {
    return if (dayOfWeek == target) this else withNextDayOfWeek(target)
}

fun HijrahDate.withLastDayOfMonth(): HijrahDate {
    val lastDay = range(DateTimeUnit.DAY).maximum.toInt()
    return withDayOfMonth(lastDay)
}


operator fun HijrahDate.rangeTo(other: HijrahDate): HijrahDateRange = HijrahDateRange(this, other)

operator fun HijrahDate.rangeUntil(other: HijrahDate): HijrahDateRange = HijrahDateRange(this, other.minusDays(1))

infix fun HijrahDate.downTo(other: HijrahDate): HijrahDateProgression {
    require(this <= other) { "$this should be less than or equal to $other" }
    return HijrahDateProgression(this, other, step = 1)
}

