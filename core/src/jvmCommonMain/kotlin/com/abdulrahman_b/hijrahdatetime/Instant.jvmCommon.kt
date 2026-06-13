package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.format.javaFormatter
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toJavaZoneOffset
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import java.time.format.SignStyle
import java.time.temporal.ChronoField
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant


@OptIn(ExperimentalTime::class)
actual fun Instant.Companion.parseHijriOrNull(value: String): Instant? {
    return try {
        parseHijri(value)
    } catch (e: IllegalArgumentException) {
        return null
    }
}

actual fun Instant.Companion.parseHijri(value: String): Instant {
    try {
        val zonedDateTime =
            HijrahChronology.INSTANCE.zonedDateTime(HijrahOffsetDateTimeFormatter.parse(value))
        return zonedDateTime.toInstant().toKotlinInstant()
    } catch (e: DateTimeParseException) {
        throw IllegalArgumentException("Invalid Hijri date format: ${e.message}")
    }
}

actual fun Instant.format(
    format: HijrahDateTimeFormat,
    offset: UtcOffset
): String {
    val javaInstant = toJavaInstant()
    val zonedDateTime = HijrahChronology.INSTANCE.zonedDateTime(javaInstant, offset.toJavaZoneOffset())
    return format.javaFormatter.format(zonedDateTime)
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
