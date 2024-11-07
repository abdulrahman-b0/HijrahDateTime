@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.abdulrahman_b.hijrah_datetime.formats

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.extensions.HijrahDates
import java.time.ZoneId
import java.time.ZoneOffset
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

/**
 * A set of predefined standard Hijrah date-time formatters.
 *
 * This class provides instances of pre-built formatters for the most common.
 */
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
     * Years in the range [HijrahDates.MIN_YEAR] to [HijrahDates.MAX_YEAR] that is pre-padded by zero to ensure four digits.
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

    @JvmField
    val HIJRAH_DATE: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendValue(ChronoField.YEAR, 4, 4, SignStyle.EXCEEDS_PAD)
        .appendLiteral('-')
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendLiteral('-')
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)



    /**
     * The Hijrah date formatter that formats or parses a date with an
     * offset, such as '1446-04-18+03:00'.
     *
     * This returns an immutable formatter capable of formatting and parsing
     * the Hijrah extended offset date format.
     * The format consists of:
     *
     * * The [HIJRAH_DATE]
     * * The [ZoneOffset.getId]. If the offset has seconds then they will be handled. Optional.
     *
     * The returned formatter has a chronology of [HijrahChronology] set to ensure dates in
     * other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    @JvmField
    val HIJRAH_OFFSET_DATE: DateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(HIJRAH_DATE)
        .optionalStart()
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
    @JvmField
    val HIJRAH_DATE_TIME: DateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(HIJRAH_DATE)
        .appendLiteral('T')
        .append(ISO_LOCAL_TIME)
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)


    /**
     * The Hijrah date-time formatter that formats or parses a date-time with an
     * offset, such as '1446-12-18T10:15:30+03:00'.
     *
     * This returns an immutable formatter capable of formatting and parsing
     * the Hijrah extended offset date-time format.
     * The format consists of:
     *
     * * The [HIJRAH_DATE_TIME] formatter.
     * * The [ZoneOffset.getId]. If the offset has seconds then they will be handled.
     *  The offset parsing is lenient, which allows the minutes and seconds to be optional.
     *  Parsing is case insensitive.
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in
     * other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    @JvmField
    val HIJRAH_OFFSET_DATE_TIME: DateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(HIJRAH_DATE_TIME)
        .parseLenient()
        .appendOffsetId()
        .parseStrict()
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)

    /**
     * The Hijrah date-time formatter that formats or parses a date-time with
     * offset and zone, such as '1446-04-18T10:15:30+03:00[Asia/Riyadh]'.
     *
     * This returns an immutable formatter capable of formatting and parsing
     * a format that extends the Hijrah offset date-time format
     * to add the time-zone.
     * The format consists of:
     *
     * * The [HIJRAH_OFFSET_DATE_TIME] formatter.
     * * If the zone ID is not available or is a [ZoneOffset] then the format is done.
     * * An open square bracket '['.
     * * The [ZoneId.getId].
     *  Parsing is case sensitive.
     * * A close square bracket ']'.
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in
     * other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    @JvmField
    val HIJRAH_ZONED_DATE_TIME: DateTimeFormatter = DateTimeFormatterBuilder()
        .append(HIJRAH_OFFSET_DATE_TIME)
        .optionalStart()
        .appendLiteral('[')
        .parseCaseSensitive()
        .appendZoneRegionId()
        .appendLiteral(']')
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)

    private fun DateTimeFormatterBuilder.toFormatter(resolverStyle: ResolverStyle, chronology: HijrahChronology, locale: Locale = Locale.getDefault()): DateTimeFormatter {
        return this.toFormatter().withChronology(chronology).withResolverStyle(resolverStyle).withLocale(locale)
    }

    /**
     * Returns the recommended formatter for the specified temporal object.
     *
     * @param temporal the temporal object to get the recommended formatter for.
     * @return the recommended formatter for the specified temporal object.
     */
    @Throws(UnsupportedTemporalTypeException::class)
    @JvmStatic
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