package com.abdulrahman_b.hijrahdatetime.internal

import com.abdulrahman_b.hijrahdatetime.HijrahDate
import com.abdulrahman_b.hijrahdatetime.range
import com.abdulrahman_b.hijrahdatetime.toEpochDays
import kotlinx.datetime.DateTimeArithmeticException
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek

internal inline fun runArithmeticCatching(block: () -> HijrahDate): HijrahDate {
    try {
        return block()
    } catch (e: IllegalArgumentException) {
        throw DateTimeArithmeticException("Hijrah date out of range", cause = e)
    }
}


internal fun HijrahDate.validateYearAndDay() {
    val yearRange = range(DateTimeUnit.YEAR)
    require(year in yearRange) { "Hijrah date out of range, year must be between $yearRange" }

    val dayRange = range(DateTimeUnit.DAY)
    require(day in dayRange) { "Hijrah date out of range, day must be between $dayRange" }

}

internal fun HijrahDate.getDayOfYear(): Int {
    return (toEpochDays() - HijrahDate(year, 1, 1).toEpochDays()).toInt() + 1
}

internal fun HijrahDate.getDayOfWeek(): DayOfWeek {
    //Epoch days historically were Thursday, it's the 3rd-indexed day in the iso calendar
    val dow0 = (toEpochDays() + DayOfWeek.THURSDAY.ordinal) % DAYS_OF_WEEK
    val positiveDow0 = if (dow0 < 0) dow0 + DAYS_OF_WEEK else dow0
    return DayOfWeek(positiveDow0.toInt() + 1) //Plus 1 because currently dow0 reference index and need the actual number of day
}

internal fun compareDates(date1: HijrahDate, date2: HijrahDate): Int {
    val y = date1.year.compareTo(date2.year)
    if (y != 0) {
        return y
    }
    val m = date1.month.number.compareTo(date2.month.number)
    if (m != 0) {
        return m
    }
    return date1.day.compareTo(date2.day)
}

internal fun getMinHijrahDate(): HijrahDate = HijrahDate(UmmAlQuraData.BASE_HIJRI_YEAR, 1, 1)
internal fun getMaxHijrahDate(): HijrahDate = HijrahDate(
    year = UmmAlQuraData.MAX_HIJRI_YEAR,
    month = MONTHS_OF_YEAR,
    day = getLengthOfMonth(UmmAlQuraData.MAX_HIJRI_YEAR, MONTHS_OF_YEAR)
)