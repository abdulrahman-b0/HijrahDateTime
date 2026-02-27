package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateTimeComponentsSerializer
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import java.time.DateTimeException
import java.time.LocalTime
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant
import java.time.chrono.HijrahDate as JavaHijrahDate

@Serializable(with = HijrahDateTimeComponentsSerializer::class)
actual class HijrahDateTime(
    private val javaDatetime: ChronoLocalDateTime<JavaHijrahDate>
) : Comparable<HijrahDateTime> {

    actual val date: HijrahDate = (javaDatetime.toLocalDate() as JavaHijrahDate).toKotlinHijrahDate()
    actual val time = javaDatetime.toLocalTime().toKotlinLocalTime()

    actual val year get() = javaDatetime.get(ChronoField.YEAR)
    actual val month get() = HijrahMonth.of(javaDatetime.get(ChronoField.MONTH_OF_YEAR))
    actual val day get() = javaDatetime.get(ChronoField.DAY_OF_MONTH)
    actual val dayOfWeek get() = DayOfWeek.entries[javaDatetime.get(ChronoField.DAY_OF_WEEK) - 1]
    actual val dayOfYear get() = javaDatetime.get(ChronoField.DAY_OF_YEAR)
    actual val hour get() = javaDatetime.get(ChronoField.HOUR_OF_DAY)
    actual val minute get() = javaDatetime.get(ChronoField.MINUTE_OF_HOUR)
    actual val second get() = javaDatetime.get(ChronoField.SECOND_OF_MINUTE)
    actual val nanosecond get() = javaDatetime.get(ChronoField.NANO_OF_SECOND)

    actual constructor(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hour: Int,
        minute: Int,
        second: Int,
        nanosecond: Int,
    ) : this(
        try {
            JavaHijrahDate.of(year, month, dayOfMonth).atTime(LocalTime.of(hour, minute, second, nanosecond))
        } catch (e: DateTimeException) {
            throw IllegalArgumentException("Invalid datetime: $year-$month-$dayOfMonth $hour:$minute:$second", e)
        })

    actual override operator fun compareTo(other: HijrahDateTime): Int = javaDatetime.compareTo(other.javaDatetime)


    actual fun format(format: HijrahDateTimeFormat): String =
        format.javaFormatter.withChronology(HijrahChronology.INSTANCE).format(javaDatetime)

    actual fun toInstant(timeZone: FixedOffsetTimeZone): Instant {
        return javaDatetime.toInstant(timeZone.toJavaZoneOffset()).toKotlinInstant()
    }

    actual companion object {

        @Suppress("UNCHECKED_CAST")
        actual fun parse(
            string: String,
            format: HijrahDateTimeFormat,
        ): HijrahDateTime {
            try {
                val accessor = format.javaFormatter.parse(string)
                val javaDate = try {
                    JavaHijrahDate.from(accessor)
                } catch (e: Exception) {
                    try {
                        val ld = java.time.LocalDate.from(accessor)
                        HijrahChronology.INSTANCE.date(ld)
                    } catch (e2: Exception) {
                        val y = accessor.get(ChronoField.YEAR)
                        val m = accessor.get(ChronoField.MONTH_OF_YEAR)
                        val d = accessor.get(ChronoField.DAY_OF_MONTH)
                        JavaHijrahDate.of(y, m, d)
                    }
                }
                val javaTime = LocalTime.from(accessor)
                return HijrahDateTime(javaDate.atTime(javaTime))
            } catch (e: DateTimeParseException) {
                throw IllegalArgumentException(e.message)
            } catch (e: DateTimeException) {
                throw IllegalArgumentException(e.message)
            }
        }

        actual fun parseOrNull(
            string: String,
            format: HijrahDateTimeFormat,
        ): HijrahDateTime? = runCatching { parse(string, format) }.getOrNull()
    }

    actual fun toLocalDateTime(): LocalDateTime {
        return java.time.LocalDateTime.from(javaDatetime).toKotlinLocalDateTime()
    }

    override fun equals(other: Any?): Boolean =
        this === other || (other is HijrahDateTime && javaDatetime == other.javaDatetime)

    override fun hashCode(): Int = javaDatetime.hashCode()

    override fun toString(): String = javaDatetime.toString()

}

actual fun Instant.toHijrahDateTime(timeZone: TimeZone): HijrahDateTime {
    val ldt = toJavaInstant().atZone(timeZone.toJavaZoneId()).toLocalDateTime()
    return HijrahDateTime(JavaHijrahDate.from(ldt).atTime(ldt.toLocalTime()))
}

actual fun LocalDateTime.toHijrahDateTime(): HijrahDateTime {
    return HijrahDateTime(HijrahChronology.INSTANCE.localDateTime(toJavaLocalDateTime()))
}