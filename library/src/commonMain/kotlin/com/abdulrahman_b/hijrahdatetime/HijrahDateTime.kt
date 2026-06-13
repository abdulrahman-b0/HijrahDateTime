package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.internal.SECONDS_OF_DAY
import com.abdulrahman_b.hijrahdatetime.internal.SECONDS_OF_HOUR
import com.abdulrahman_b.hijrahdatetime.internal.SECONDS_OF_MINUTE
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateTimeComponentsSerializer
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Represents a date and time in the Hijrah (Islamic) calendar system.
 *
 * This class is [Serializable] and uses [HijrahDateTimeComponentsSerializer] for serialization.
 */
@Suppress("unused")
@Serializable(with = HijrahDateTimeComponentsSerializer::class)
expect class HijrahDateTime(
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int,
    second: Int,
    nanosecond: Int,
) : Comparable<HijrahDateTime> {

    /** The [HijrahDate] part of this date-time. */
    val date: HijrahDate
    /** The [LocalTime] part of this date-time. */
    val time: LocalTime

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
    /** The hour of day, from 0 to 23. */
    val hour: Int
    /** The minute of hour, from 0 to 59. */
    val minute: Int
    /** The second of minute, from 0 to 59. */
    val second: Int
    /** The nanosecond of second, from 0 to 999,999,999. */
    val nanosecond: Int

    override fun compareTo(other: HijrahDateTime): Int

    /** Formats this date-time using the specified [format]. */
    fun format(format: HijrahDateTimeFormat): String

    companion object {
        /**
         * Parses a [HijrahDateTime] from a string using the specified [format].
         * @throws IllegalArgumentException if the string cannot be parsed.
         */
        fun parse(string: String, format: HijrahDateTimeFormat): HijrahDateTime

        /**
         * Parses a [HijrahDateTime] from a string using the specified [format], or returns null if parsing fails.
         */
        fun parseOrNull(string: String, format: HijrahDateTimeFormat): HijrahDateTime?
    }
}


/** Converts this [LocalDateTime] to a [HijrahDateTime]. */
fun LocalDateTime.toHijrahDateTime(): HijrahDateTime {
    val epochDay = this.date.toEpochDays()
    val hijrahDate = HijrahDate.fromEpochDays(epochDay)
    return hijrahDate.atTime(this.time)
}

/** Converts this [HijrahDateTime] to a [LocalDateTime]. */
fun HijrahDateTime.toLocalDateTime(): LocalDateTime {
    val hijrahDate = HijrahDate(year, month.number, day)
    val epochDay = hijrahDate.toEpochDays()
    return LocalDateTime(LocalDate.fromEpochDays(epochDay), time)
}

/** Creates a [HijrahDateTime] from the specified [date] and [time]. */
fun HijrahDateTime.Companion.of(date: HijrahDate, time: LocalTime) =
    HijrahDateTime(date.year, date.month.number, date.day, time.hour, time.minute, time.second, time.nanosecond)

/** Converts this date-time to an [Instant] in the specified [timeZone]. */
fun HijrahDateTime.toInstant(timeZone: FixedOffsetTimeZone): Instant {
    val localEpochSeconds = (date.toEpochDays() * SECONDS_OF_DAY) +
            (hour * SECONDS_OF_HOUR) +
            (minute * SECONDS_OF_MINUTE) +
            second

    val utcEpochSeconds = localEpochSeconds - timeZone.offset.totalSeconds

    // Instant handles negative values flawlessly here!
    return Instant.fromEpochSeconds(utcEpochSeconds, nanosecond)
}