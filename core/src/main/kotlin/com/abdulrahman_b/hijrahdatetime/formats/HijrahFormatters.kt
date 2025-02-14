@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.abdulrahman_b.hijrahdatetime.formats

import com.abdulrahman_b.hijrahdatetime.EarlyHijrahDate
import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDate
import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_DATE
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_DATE_TIME
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.buildHijrahDateFormatter
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.buildHijrahDateTimeFormatter
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.buildOffsetHijrahDateTimeFormatter
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
import java.time.temporal.TemporalAccessor
import java.time.temporal.UnsupportedTemporalTypeException
import java.util.*

/**
 * A set of predefined standard Hijrah date-time formatters.
 *
 * This class provides instances of pre-built formatters for the most common.
 */
object HijrahFormatters {

    @JvmField
    val LOCAL_TIME_12_HOURS: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendValue(ChronoField.CLOCK_HOUR_OF_AMPM, 1, 2, SignStyle.NOT_NEGATIVE)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .optionalStart()
        .appendLiteral(':')
        .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .optionalEnd()
        .optionalEnd()
        .appendPattern(" a")
        .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)

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
    val HIJRAH_DATE: DateTimeFormatter = buildHijrahDateFormatter(separator = "-")


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
    val HIJRAH_OFFSET_DATE: DateTimeFormatter = buildOffsetHijrahDateFormatter()

    /**
     * The Hijrah date-time formatter that formats or parses a date-time without
     * an offset, such as '1446-04-18T15:15:30'.
     *
     * This returns an immutable formatter capable of formatting and parsing
     * the hijrah date-time format.
     * The format consists of:
     * * The [HIJRAH_DATE] formatter
     * * The letter 'T'. Parsing is case-insensitive.
     * * The [ISO_LOCAL_TIME]
     * The returned formatter has a chronology of Hijrah set to ensure dates in
     * other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    @JvmField
    val HIJRAH_DATE_TIME: DateTimeFormatter = buildHijrahDateTimeFormatter()


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
     *  Parsing is case-insensitive.
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in
     * other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    @JvmField
    val HIJRAH_OFFSET_DATE_TIME: DateTimeFormatter =
        buildOffsetHijrahDateTimeFormatter(HIJRAH_DATE_TIME)

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
     *  Parsing is case-sensitive.
     * * A close square bracket ']'.
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in
     * other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    @JvmField
    val HIJRAH_ZONED_DATE_TIME: DateTimeFormatter = buildZonedHijrahDateTimeFormatter()

    private fun DateTimeFormatterBuilder.toFormatter(resolverStyle: ResolverStyle, chronology: HijrahChronology, locale: Locale = Locale.getDefault()): DateTimeFormatter {
        return this.toFormatter().withChronology(chronology).withResolverStyle(resolverStyle).withLocale(
            locale
        )
    }

    /**
     * Returns the recommended formatter for the specified temporal object.
     *
     * @param temporal the temporal object to get the recommended formatter for.
     * @return the recommended formatter for the specified temporal object.
     */
    @Throws(UnsupportedTemporalTypeException::class)
    @JvmStatic
    fun getRecommendedFormatter(temporal: TemporalAccessor): DateTimeFormatter {
        return when (temporal) {
            is HijrahDate -> HIJRAH_DATE
            is EarlyHijrahDate -> HIJRAH_DATE
            is HijrahDateTime -> HIJRAH_DATE_TIME
            is OffsetHijrahDate -> HIJRAH_OFFSET_DATE
            is ZonedHijrahDateTime -> HIJRAH_ZONED_DATE_TIME
            is OffsetHijrahDateTime -> HIJRAH_OFFSET_DATE_TIME
            else -> throw UnsupportedTemporalTypeException("Unsupported temporal type: $temporal")
        }
    }

    /**
     * A builder for creating Hijrah date formatters.
     *
     * This builder allows for the simple creation of Hijrah date formatters, without rewriting the pattern each time.
     *
     * This returns an immutable formatter capable of formatting and parsing
     * the Hijrah local date format.
     * The format consists of:
     *
     * * Four digits or more for the [ChronoField.YEAR] year.
     * Years in the range [HijrahDates.MIN_YEAR] to [HijrahDates.MAX_YEAR] that is pre-padded by zero to ensure four digits.
     * Years outside that range will have a prefixed positive or negative symbol.
     * * A separator literal defined by the caller in the [separator] parameter.
     * * Two digits for the [ChronoField.MONTH_OF_YEAR] month-of-year.
     *  This is pre-padded by zero to ensure two digits.
     * * A separator literal defined by the caller in the [separator] parameter.
     * * Two digits for the [ChronoField.DAY_OF_MONTH] day-of-month.
     *  This is pre-padded by zero to ensure two digits.
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in
     * other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     *
     * @param separator the separator to use between the date fields.
     */
    @JvmStatic
    fun buildHijrahDateFormatter(separator: String): DateTimeFormatter {
        return DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 1, 4, SignStyle.NOT_NEGATIVE)
            .appendLiteral(separator)
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral(separator)
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)
    }

    /**
     * A builder for creating Hijrah offset date formatters.
     *
     * This builder allows for the simple creation of Hijrah date formatters, without rewriting the pattern each time.
     *
     * This returns an immutable formatter capable of formatting and parsing the Hijrah extended offset date format.
     * The format consists of:
     * * The [hijrahDateFormatter] formatter, which is obtained by using the [buildHijrahDateFormatter] method or using the [HIJRAH_DATE] formatter constant.
     * * The [ZoneOffset.getId]. If the offset has seconds then they will be handled. Optional.
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     *
     * @param hijrahDateFormatter the separator to use between the date fields. This is used to build the `hijrahDateFormatter` formatter internally.
     */
    @JvmStatic
    @JvmOverloads
    fun buildOffsetHijrahDateFormatter(hijrahDateFormatter: DateTimeFormatter = HIJRAH_DATE): DateTimeFormatter {
        return DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(hijrahDateFormatter)
            .optionalStart()
            .appendOffsetId()
            .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)
    }

    /**
     * A builder for creating local hijrah date-time formatters.
     *
     * This builder allows for the simple creation of Hijrah date-time formatters, without rewriting the pattern each time.
     *
     * This returns an immutable formatter capable of formatting and parsing the hijrah date-time format.
     * The format consists of:
     * * The [hijrahDateFormatter] formatter
     * * The separator literal defined by the caller in the [datetimeSeparator] parameter. Parsing is case-insensitive.
     * * The [timeFormatter] formatter
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    @JvmStatic
    @JvmOverloads
    fun buildHijrahDateTimeFormatter(
        hijrahDateFormatter: DateTimeFormatter = HIJRAH_DATE,
        datetimeSeparator: String = "T",
        timeFormatter: DateTimeFormatter = ISO_LOCAL_TIME
    ): DateTimeFormatter {
        return DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(hijrahDateFormatter)
            .appendLiteral(datetimeSeparator)
            .append(timeFormatter)
            .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)
    }

    /**
     * A builder for creating offset hijrah date-time formatters.
     *
     * This builder allows for the simple creation of Hijrah date-time formatters, without rewriting the pattern each time.
     *
     * This returns an immutable formatter capable of formatting and parsing the hijrah date-time format.
     * The format consists of:
     * * The [hijrahDateTimeFormatter] formatter, which you can build using the [buildHijrahDateTimeFormatter] method or use the [HIJRAH_DATE_TIME] formatter constant.
     * * The [ZoneOffset.getId]. If the offset has seconds then they will be handled.
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     */
    @JvmStatic
    fun buildOffsetHijrahDateTimeFormatter(
        hijrahDateTimeFormatter: DateTimeFormatter,
    ): DateTimeFormatter {
        return DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(hijrahDateTimeFormatter)
            .parseLenient()
            .appendOffsetId()
            .parseStrict()
            .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)
    }

    /**
     * A builder for creating zoned Hijrah date-time formatters.
     *
     * This builder allows for the simple creation of Hijrah date-time formatters, without rewriting the pattern each time.
     *
     * This returns an immutable formatter capable of formatting and parsing the hijrah date-time format.
     * The format consists of:
     * * The [offsetHijrahDateTimeFormatter] formatter, which you can build using the [buildOffsetHijrahDateTimeFormatter] method or use the [HIJRAH_OFFSET_DATE_TIME] formatter constant.
     * * The beginning zone id encloser literal defined by the caller in the [beginZoneIdEncloser] parameter For example, '['. Empty string is allowed. Parsing is case-sensitive.
     * * The [ZoneId.getId]. Parsing is case-sensitive.
     * * The end zone id encloser literal defined by the caller in the [endZoneIdEncloser] parameter. For example, ']'. Empty string is allowed. Parsing is case-sensitive.
     *
     * The returned formatter has a chronology of Hijrah set to ensure dates in other calendar systems are correctly converted.
     * It has no override zone and uses the [ResolverStyle.STRICT] resolver style.
     *
     */
    @JvmStatic
    @JvmOverloads
    fun buildZonedHijrahDateTimeFormatter(
        offsetHijrahDateTimeFormatter: DateTimeFormatter = HIJRAH_OFFSET_DATE_TIME,
        beginZoneIdEncloser: String = "[",
        endZoneIdEncloser: String = "]"
    ): DateTimeFormatter {
        return DateTimeFormatterBuilder()
            .append(offsetHijrahDateTimeFormatter)
            .optionalStart()
            .appendLiteral(beginZoneIdEncloser)
            .parseCaseSensitive()
            .appendZoneRegionId()
            .appendLiteral(endZoneIdEncloser)
            .toFormatter(ResolverStyle.STRICT, HijrahChronology.INSTANCE)
    }
}