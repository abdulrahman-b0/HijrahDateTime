@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.abdulrahman_b.hijrahDateTime.formats

import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.ZonedHijrahDateTime
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_TIME
import java.time.format.DateTimeFormatterBuilder
import java.time.format.ResolverStyle
import java.time.format.SignStyle
import java.time.temporal.ChronoField
import java.time.temporal.Temporal
import java.time.temporal.UnsupportedTemporalTypeException
import java.util.Locale

object HijrahFormatters {

    /**
     * The Hijrah date formatter that formats or parses a date without an
     * offset, such as '1446-04-18'.
     *
     * This returns an immutable formatter capable of formatting and parsing
     * the Hijrah local date format.
     * The format consists of:
     *
     * * Four digits or more for the [ChronoField.YEAR] year.
     * Years in the range 0000 to 9999 will be pre-padded by zero to ensure four digits.
     * Years outside that range will have a prefixed positive or negative symbol.
     * * A dash
     * * Two digits for the [ChronoField.MONTH_OF_YEAR] month-of-year.
     *  This is pre-padded by zero to ensure two digits.
     * * A dash
     * * Two digits for the [ChronoField.DAY_OF_MONTH] day-of-month.
     *  This is pre-padded by zero to ensure two digits.
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in
     * other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    val HIJRAH_DATE: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .appendLiteral('-')
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendLiteral('-')
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)


    val HIJRAH_ZONED_DATE: DateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(HIJRAH_DATE)
        .optionalStart()
        .appendOffsetId()
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)


    val HIJRAH_OFFSET_DATE: DateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(HIJRAH_DATE)
        .appendOffsetId()
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)


    /**
     * The Hijrah date-time formatter that formats or parses a date-time without
     * an offset, such as '1446-04-18T15:15:30'.
     *
     * This returns an immutable formatter capable of formatting and parsing
     * the hijrah date-time format.
     * The format consists of:
     * * The [HIJRAH_DATE] formatter
     * * The letter 'T'. Parsing is case insensitive.
     * * The [ISO_LOCAL_TIME]
     * The returned formatter has a chronology of Hijrah set to ensure dates in
     * other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    val HIJRAH_DATE_TIME: DateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(HIJRAH_DATE)
        .appendLiteral('T')
        .append(ISO_LOCAL_TIME)
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)

    val HIJRAH_ZONED_DATE_TIME: DateTimeFormatter = DateTimeFormatterBuilder()
        .append(HIJRAH_DATE_TIME)
        .optionalStart()
        .appendOffsetId()
        .optionalStart()
        .appendLiteral('[')
        .parseCaseSensitive()
        .appendZoneRegionId()
        .appendLiteral(']')
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)


    val HIJRAH_OFFSET_DATE_TIME: DateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(HIJRAH_DATE_TIME)
        .parseLenient()
        .appendOffsetId()
        .parseStrict()
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)

    fun DateTimeFormatterBuilder.toFormatter(resolverStyle: ResolverStyle, chronology: HijrahChronology, locale: Locale = Locale.getDefault()): DateTimeFormatter {
        return this.toFormatter().withChronology(chronology).withResolverStyle(resolverStyle).withLocale(locale)
    }

    @Throws(UnsupportedTemporalTypeException::class)
    fun getRecommendedFormatter(temporal: Temporal): DateTimeFormatter {
        return when (temporal) {
            is HijrahDate -> HIJRAH_DATE
            is HijrahDateTime -> HIJRAH_DATE_TIME
            is ZonedHijrahDateTime -> HIJRAH_ZONED_DATE_TIME
            is OffsetHijrahDateTime -> HIJRAH_OFFSET_DATE_TIME
            else -> throw UnsupportedTemporalTypeException("Unsupported temporal type: $temporal")
        }
    }

}