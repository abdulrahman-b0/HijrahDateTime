package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.format.javaFormatter
import com.abdulrahman_b.hijrahdatetime.internal.compareDates
import com.abdulrahman_b.hijrahdatetime.internal.getDayOfWeek
import com.abdulrahman_b.hijrahdatetime.internal.getDayOfYear
import com.abdulrahman_b.hijrahdatetime.internal.validateYearAndDay
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateComponentsSerializer
import kotlinx.serialization.Serializable
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.chrono.HijrahDate as JavaHijrahDate

@Serializable(with = HijrahDateComponentsSerializer::class)
actual class HijrahDate private constructor(
    actual val year: Int,
    actual val month: HijrahMonth,
    actual val day: Int
) : Comparable<HijrahDate> {

    actual constructor(year: Int, month: Int, day: Int) : this(year, HijrahMonth.of(month), day) {
        validateYearAndDay()
    }

    actual val dayOfWeek get() = getDayOfWeek()
    actual val dayOfYear get() = getDayOfYear()


    actual override operator fun compareTo(other: HijrahDate): Int = compareDates(this, other)


    /** Returns the underlying Java [java.time.chrono.HijrahDate]. */
    fun toJavaHijrahDate(): JavaHijrahDate = JavaHijrahDate.of(year, month.number, day)

    actual fun format(format: HijrahDateTimeFormat): String {
        return format.javaFormatter.format(toJavaHijrahDate())
    }


    actual companion object {

        actual fun parse(string: String, format: HijrahDateTimeFormat): HijrahDate = try {
            format.javaFormatter
                .withChronology(HijrahChronology.INSTANCE).parse(string, JavaHijrahDate::from).toKotlinHijrahDate()
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException(e.message)
        }


        actual fun parseOrNull(string: String, format: HijrahDateTimeFormat): HijrahDate? =
            runCatching { parse(string, format) }.getOrNull()

    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HijrahDate) return false
        return toEpochDays() == other.toEpochDays()
    }

    override fun hashCode(): Int = toJavaHijrahDate().hashCode()

    override fun toString(): String = toJavaHijrahDate().toString()
}

/** Converts this Java [java.time.chrono.HijrahDate] to a Kotlin [HijrahDate]. */
fun JavaHijrahDate.toKotlinHijrahDate(): HijrahDate = HijrahDate(get(ChronoField.YEAR), get(ChronoField.MONTH_OF_YEAR), get(ChronoField.DAY_OF_MONTH))
