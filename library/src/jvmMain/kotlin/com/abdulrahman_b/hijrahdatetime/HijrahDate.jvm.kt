package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateComponentsSerializer
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeArithmeticException
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.time.DateTimeException
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahChronology.INSTANCE
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalAdjusters.lastDayOfMonth
import java.time.chrono.HijrahDate as JavaHijrahDate

@Serializable(with = HijrahDateComponentsSerializer::class)
actual class HijrahDate internal constructor(private val javaDate: JavaHijrahDate) : Comparable<HijrahDate> {

    actual val year get() = javaDate.get(ChronoField.YEAR)
    actual val month get() = javaDate.get(ChronoField.MONTH_OF_YEAR)
    actual val dayOfMonth get() = javaDate.get(ChronoField.DAY_OF_MONTH)
    actual val dayOfWeek get() = DayOfWeek.entries[javaDate.get(ChronoField.DAY_OF_WEEK) - 1]
    actual val dayOfYear get() = javaDate.get(ChronoField.DAY_OF_YEAR)

    actual constructor(year: Int, month: Int, dayOfMonth: Int) : this(
        try {
            JavaHijrahDate.of(year, month, dayOfMonth)
        } catch (e: DateTimeException) {
            throw IllegalArgumentException("Invalid date: $year-$month-$dayOfMonth", e)
        }
    )

    actual override operator fun compareTo(other: com.abdulrahman_b.hijrahdatetime.HijrahDate): Int =
        javaDate.compareTo(other.javaDate)

    actual operator fun plus(period: DatePeriod): com.abdulrahman_b.hijrahdatetime.HijrahDate {
        return try {
            HijrahDate(
                javaDate.plus(period.years.toLong(), ChronoUnit.YEARS)
                    .plus(period.months.toLong(), ChronoUnit.MONTHS)
                    .plus(period.days.toLong(), ChronoUnit.DAYS)
            )
        } catch (e: DateTimeException) {
            throw DateTimeArithmeticException(e.message.toString(), e)
        }
    }

    actual operator fun minus(period: DatePeriod): com.abdulrahman_b.hijrahdatetime.HijrahDate {
        return try {
            HijrahDate(
                javaDate.minus(period.years.toLong(), ChronoUnit.YEARS)
                    .minus(period.months.toLong(), ChronoUnit.MONTHS)
                    .minus(period.days.toLong(), ChronoUnit.DAYS)
            )
        } catch (e: DateTimeException) {
            throw DateTimeArithmeticException(e.message.toString(), e)
        }
    }


    actual fun plus(value: Int, unit: DateTimeUnit.DateBased) = try {
        HijrahDate(javaDate.plus(value.toLong(), unit.asJavaTemporalUnit()))
    } catch (e: DateTimeException) {
        throw DateTimeArithmeticException(e.message.toString(), e)
    }

    actual fun minus(value: Int, unit: DateTimeUnit.DateBased) = try {
        HijrahDate(javaDate.minus(value.toLong(), unit.asJavaTemporalUnit()))
    } catch (e: DateTimeException) {
        throw DateTimeArithmeticException(e.message.toString(), e)
    }

    actual fun toEpochDays(): Long = javaDate.toEpochDay()

    fun toJavaHijrahDate(): JavaHijrahDate = javaDate

    actual fun format(format: HijrahDateTimeFormat): String =
        format.javaFormatter.withChronology(javaDate.chronology).format(javaDate)


    actual fun range(unit: DateTimeUnit.DateBased): ValueRange {
        val field = unit.asJavaTemporalField()
        val range = javaDate.range(field)
        return ValueRange(range.minimum, range.maximum)
    }


    actual companion object {

        actual fun parse(string: String, format: HijrahDateTimeFormat): HijrahDate = try {
            format.javaFormatter
                .withChronology(HijrahChronology.INSTANCE).parse(string, JavaHijrahDate::from).let(::HijrahDate)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException(e.message)
        }


        actual fun parseOrNull(string: String, format: HijrahDateTimeFormat): HijrahDate? =
            runCatching { parse(string, format) }.getOrNull()

        actual fun fromEpochDays(epochDay: Long): HijrahDate =
            HijrahDate(HijrahChronology.INSTANCE.dateEpochDay(epochDay))

        actual val MIN by lazy {
            HijrahDate(
                JavaHijrahDate.of(
                    /* prolepticYear = */ INSTANCE.range(ChronoField.YEAR).minimum.toInt(),
                    /* month = */1,
                    /* dayOfMonth = */1
                )
            )
        }
        actual val MAX by lazy {
            HijrahDate(
                JavaHijrahDate.of(
                    /* prolepticYear = */ INSTANCE.range(ChronoField.YEAR).maximum.toInt(),
                    /* month = */ 12,
                    /* dayOfMonth = */ 29
                ).with(TemporalAdjusters.lastDayOfMonth())
            )
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HijrahDate) return false
        return toEpochDays() == other.toEpochDays()
    }

    override fun hashCode(): Int = javaDate.hashCode()

    override fun toString(): String = javaDate.toString()
}

fun JavaHijrahDate.toKotlinHijrahDate(): HijrahDate = HijrahDate(this)
