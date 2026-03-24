package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateTimeComponentsSerializer
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Suppress("unused")
@Serializable(with = HijrahDateTimeComponentsSerializer::class)
expect class HijrahDateTime(
    year: Int,
    month: Int,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    second: Int,
    nanosecond: Int,
) : Comparable<HijrahDateTime> {

    val date: HijrahDate
    val time: LocalTime

    val year: Int
    val month: HijrahMonth
    val day: Int
    val dayOfWeek: DayOfWeek
    val dayOfYear: Int
    val hour: Int
    val minute: Int
    val second: Int
    val nanosecond: Int

    override fun compareTo(other: HijrahDateTime): Int

    fun format(format: HijrahDateTimeFormat): String

    @OptIn(ExperimentalTime::class)
    fun toInstant(timeZone: FixedOffsetTimeZone): Instant

    fun toLocalDateTime(): LocalDateTime

    companion object {
        fun parse(string: String, format: HijrahDateTimeFormat): HijrahDateTime

        fun parseOrNull(string: String, format: HijrahDateTimeFormat): HijrahDateTime?
    }
}

@OptIn(ExperimentalTime::class)
fun Instant.toHijrahDateTime(timeZone: TimeZone): HijrahDateTime {
    return toLocalDateTime(timeZone).toHijrahDateTime()
}

expect fun LocalDateTime.toHijrahDateTime(): HijrahDateTime

fun HijrahDateTime.Companion.of(date: HijrahDate, time: LocalTime) =
    HijrahDateTime(date.year, date.month.number, date.day, time.hour, time.minute, time.second, time.nanosecond)


