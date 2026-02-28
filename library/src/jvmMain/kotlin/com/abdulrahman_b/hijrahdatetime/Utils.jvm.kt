package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.*
import java.time.chrono.HijrahChronology
import java.time.format.*
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalField
import java.time.temporal.TemporalUnit
import kotlin.time.Instant
import kotlin.time.toKotlinInstant


fun NameStyle.toJavaTextStyle(): TextStyle = when (this) {
    NameStyle.FULL -> TextStyle.FULL
    NameStyle.ABBREVIATED -> TextStyle.SHORT
}

fun DecimalStyle.toJavaDecimalStyle(): java.time.format.DecimalStyle = when (this) {
    is DecimalStyle.OfLocale -> java.time.format.DecimalStyle.of(locale.locale)
    DecimalStyle.Standard -> java.time.format.DecimalStyle.STANDARD
}

internal fun DateTimeUnit.asJavaTemporalUnit(): TemporalUnit {
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
        else -> throw IllegalArgumentException("Unsupported DateTimeUnit: The unit $this cannot be mapped to java.time.temporal.TemporalUnit")
    }
}

internal fun DateTimeUnit.asJavaTemporalField(): TemporalField {
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
        else -> throw IllegalArgumentException("Unsupported DateTimeUnit: The unit $this cannot be mapped to java.time.temporal.TemporalField")
    }
}

actual fun LocalTime.format(format: HijrahDateTimeFormat): String = format.javaFormatter.format(toJavaLocalTime())

actual fun DayOfWeek.getDisplayName(
    nameStyle: NameStyle,
    locale: FormatLocale
): String = toJavaDayOfWeek().getDisplayName(nameStyle.toJavaTextStyle(), locale.locale)

actual fun Instant.Companion.parseHijriOrNull(value: String): Instant? {
    return try {
        parseHijri(value)
    } catch (e: DateTimeParseException) {
        return null
    }
}


private val HijrahOffsetDateTimeFormatter by lazy {
    DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendValue(ChronoField.YEAR, 1, 4, SignStyle.NOT_NEGATIVE)
        .appendLiteral('-')
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendLiteral('-')
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral('T')
        .append(DateTimeFormatter.ISO_LOCAL_TIME)
        .parseLenient()
        .appendOffsetId()
        .parseStrict()
        .toFormatter()
        .withResolverStyle(ResolverStyle.STRICT)
        .withChronology(HijrahChronology.INSTANCE)
}

actual fun Instant.Companion.parseHijri(value: String): Instant {
    val zonedDateTime = HijrahChronology.INSTANCE.zonedDateTime(HijrahOffsetDateTimeFormatter.parse(value))
    return zonedDateTime.toInstant().toKotlinInstant()
}