package com.abdulrahman_b.hijrahdatetime.internal

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import java.time.Period
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

internal fun DateTimeUnit.asJavaChronoUnit(): ChronoUnit {
    return when (this) {
        DateTimeUnit.NANOSECOND -> ChronoUnit.NANOS
        DateTimeUnit.MICROSECOND -> ChronoUnit.MICROS
        DateTimeUnit.MILLISECOND -> ChronoUnit.MILLIS
        DateTimeUnit.SECOND -> ChronoUnit.SECONDS
        DateTimeUnit.MINUTE -> ChronoUnit.MINUTES
        DateTimeUnit.HOUR -> ChronoUnit.HOURS
        DateTimeUnit.DAY -> ChronoUnit.DAYS
        DateTimeUnit.WEEK -> ChronoUnit.WEEKS
        DateTimeUnit.MONTH -> ChronoUnit.MONTHS
        DateTimeUnit.YEAR -> ChronoUnit.YEARS
        DateTimeUnit.CENTURY -> ChronoUnit.CENTURIES
        DateTimeUnit.DateBased -> throw UnsupportedOperationException("DateTimeUnit.QUARTER is not supported in JVM target")
        else -> throw IllegalArgumentException("Unknown DateTimeUnit: $this")
    }
}

internal fun DateTimeUnit.asJavaChronoField(): ChronoField {
    return when (this) {
        DateTimeUnit.NANOSECOND -> ChronoField.NANO_OF_SECOND
        DateTimeUnit.MICROSECOND -> ChronoField.MICRO_OF_SECOND
        DateTimeUnit.MILLISECOND -> ChronoField.MILLI_OF_SECOND
        DateTimeUnit.SECOND -> ChronoField.SECOND_OF_MINUTE
        DateTimeUnit.MINUTE -> ChronoField.MINUTE_OF_HOUR
        DateTimeUnit.HOUR -> ChronoField.HOUR_OF_DAY
        DateTimeUnit.DAY -> ChronoField.DAY_OF_MONTH
        DateTimeUnit.WEEK -> ChronoField.ALIGNED_WEEK_OF_MONTH
        DateTimeUnit.MONTH -> ChronoField.MONTH_OF_YEAR
        DateTimeUnit.YEAR -> ChronoField.YEAR
        DateTimeUnit.CENTURY -> throw IllegalArgumentException("DateTimeUnit.CENTURY cannot be mapped to java.time.ChronoField")
        else -> throw IllegalArgumentException("Unknown DateTimeUnit: $this")
    }
}

internal fun java.time.DayOfWeek.asKotlinDayOfWeek(): DayOfWeek {
    return DayOfWeek.entries[ordinal]
}

internal fun DateTimePeriod.asJavaPeriod(): Period = Period.of(years, months, days)

