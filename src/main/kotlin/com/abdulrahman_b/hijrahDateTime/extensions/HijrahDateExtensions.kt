@file:Suppress("MemberVisibilityCanBePrivate", "unused")
@file:JvmName("HijrahDateUtils")

package com.abdulrahman_b.hijrahDateTime.extensions

import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters
import com.abdulrahman_b.hijrahDateTime.time.HijrahMonth
import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.OffsetHijrahDateTime
import java.time.Instant
import java.time.OffsetTime
import java.time.ZoneId
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField


val HijrahDate.year: Int get() = get(ChronoField.YEAR)

val HijrahDate.monthValue: Int get() = get(ChronoField.MONTH_OF_YEAR)

val HijrahDate.month: HijrahMonth get() = HijrahMonth.of(monthValue)

val HijrahDate.dayOfYear: Int get() = get(ChronoField.DAY_OF_YEAR)

val HijrahDate.dayOfMonth: Int get() = get(ChronoField.DAY_OF_MONTH)

val HijrahDate.dayOfWeek: Int get() = get(ChronoField.DAY_OF_WEEK)

fun HijrahDate.atTime(offsetTime: OffsetTime): OffsetHijrahDateTime {
    val datetime = HijrahDateTime.of(this, offsetTime.toLocalTime())
    return OffsetHijrahDateTime.of(datetime, offsetTime.offset)
}


object HijrahDateFactory {

    fun ofEpochDay(epochDay: Long): HijrahDate {
        return HijrahChronology.INSTANCE.dateEpochDay(epochDay)
    }

    fun ofYearDay(year: Int, dayOfYear: Int): HijrahDate {
        return HijrahChronology.INSTANCE.dateYearDay(year, dayOfYear)
    }

    fun ofInstant(instant: Instant, zone: ZoneId): HijrahDate {
        val offset = zone.rules.getOffset(instant)
        val localSecond = instant.epochSecond + offset.totalSeconds
        val localEpochDay = Math.floorDiv(localSecond, SECONDS_PER_DAY)
        return ofEpochDay(localEpochDay)
    }

    fun parse(text: CharSequence) = parse(text, HijrahDateTimeFormatters.HIJRAH_LOCAL_DATE)

    fun parse(text: CharSequence, formatter: DateTimeFormatter): HijrahDate {
        return formatter.parse(text, HijrahDate::from)
    }

    private const val SECONDS_PER_DAY = 86400

}