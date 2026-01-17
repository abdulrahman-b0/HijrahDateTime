package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahChronology.INSTANCE
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalAdjusters.lastDayOfMonth
import java.time.chrono.HijrahDate as JavaHijrahDate

@Serializable(with = HijrahDateSerializer::class)
actual class HijrahDate internal constructor(private val javaDate: JavaHijrahDate) : Comparable<HijrahDate> {

    actual val year get() = javaDate.get(ChronoField.YEAR)
    actual val month get() = javaDate.get(ChronoField.MONTH_OF_YEAR)
    actual val dayOfMonth get() = javaDate.get(ChronoField.DAY_OF_MONTH)
    actual val dayOfWeek get() = DayOfWeek.entries[javaDate.get(ChronoField.DAY_OF_WEEK) - 1]
    actual val dayOfYear get() = javaDate.get(ChronoField.DAY_OF_YEAR)

    actual constructor(year: Int, month: Int, dayOfMonth: Int) : this(JavaHijrahDate.of(year, month, dayOfMonth))

    actual override operator fun compareTo(other: com.abdulrahman_b.hijrahdatetime.HijrahDate): Int =
        javaDate.compareTo(other.javaDate)

    actual operator fun plus(period: DatePeriod): com.abdulrahman_b.hijrahdatetime.HijrahDate = HijrahDate(
        javaDate.plus(period.years.toLong(), ChronoUnit.YEARS)
            .plus(period.months.toLong(), ChronoUnit.MONTHS)
            .plus(period.days.toLong(), ChronoUnit.DAYS)
    )

    actual operator fun minus(period: DatePeriod): com.abdulrahman_b.hijrahdatetime.HijrahDate = HijrahDate(
        javaDate.minus(period.years.toLong(), ChronoUnit.YEARS)
            .minus(period.months.toLong(), ChronoUnit.MONTHS)
            .minus(period.days.toLong(), ChronoUnit.DAYS)
    )


    actual fun plus(value: Int, unit: DateTimeUnit.DateBased) =
        HijrahDate(javaDate.plus(value.toLong(), unit.asJavaTemporalUnit()))

    actual fun minus(value: Int, unit: DateTimeUnit.DateBased) =
        HijrahDate(javaDate.minus(value.toLong(), unit.asJavaTemporalUnit()))

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

        actual fun parse(string: String, format: HijrahDateTimeFormat): HijrahDate = format.javaFormatter
            .withChronology(HijrahChronology.INSTANCE)
            .parse(string, JavaHijrahDate::from)
            .let(::HijrahDate)


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
                ).with(lastDayOfMonth())
            )
        }
        actual val MAX = HijrahDate(
            JavaHijrahDate.of(
                /* prolepticYear = */ INSTANCE.range(ChronoField.YEAR).maximum.toInt(),
                /* month = */ 12,
                /* dayOfMonth = */ 29
            ).with(TemporalAdjusters.lastDayOfMonth())
        )
    }


    override fun equals(other: Any?): Boolean =
        this === other || (other is HijrahDate && javaDate == other.javaDate)

    override fun hashCode(): Int = javaDate.hashCode()

    override fun toString(): String = javaDate.toString()
}

fun JavaHijrahDate.toKotlinHijrahDate(): HijrahDate = HijrahDate(this)
