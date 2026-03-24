package com.abdulrahman_b.hijrahdatetime.format

import com.abdulrahman_b.hijrahdatetime.DecimalStyle
import com.abdulrahman_b.hijrahdatetime.FormatLocale
import com.abdulrahman_b.hijrahdatetime.FormatLocales
import com.abdulrahman_b.hijrahdatetime.toJavaTextStyle
import kotlinx.datetime.format.Padding
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField


@Suppress("unused")
actual class HijrahDateTimeFormatBuilder {

    internal val builder = DateTimeFormatterBuilder()

    actual var locale: FormatLocale = FormatLocales.English

    actual var decimalStyle: DecimalStyle = DecimalStyle.Standard

    actual fun format(format: HijrahDateTimeFormat) {
        builder.append(format.javaFormatter)
    }

    actual fun year(padding: Padding) {
        builder.appendValue(ChronoField.YEAR, 4, 4, SignStyle.NORMAL)
    }

    actual fun monthNumber(padding: Padding) {
        builder.appendValue(ChronoField.MONTH_OF_YEAR, 2, 2, SignStyle.NORMAL)
    }

    actual fun monthName(style: NameStyle) {
        builder.appendText(ChronoField.MONTH_OF_YEAR, style.toJavaTextStyle())
    }

    actual fun dayOfWeek(style: NameStyle) {
        builder.appendText(ChronoField.DAY_OF_WEEK, style.toJavaTextStyle())
    }

    actual fun dayOfMonth(padding: Padding) {
        builder.appendValue(ChronoField.DAY_OF_MONTH, 2, 2, SignStyle.NORMAL)
    }

    actual fun char(char: Char) {
        builder.appendLiteral(char)
    }

    actual fun chars(chars: String) {
        builder.appendLiteral(chars)
    }

    actual fun byUnicodePattern(pattern: String) {
        builder.appendPattern(pattern)
    }


    actual fun zoneOffset() {
        builder.appendOffsetId()
    }

    actual fun hour(padding: Padding) {
        val minWidth = if (padding == Padding.ZERO) 2 else 1
        builder.appendValue(ChronoField.HOUR_OF_DAY, minWidth, 2, SignStyle.NORMAL)
    }

    actual fun amPmHour(padding: Padding) {
        val minWidth = if (padding == Padding.ZERO) 2 else 1
        builder.appendValue(ChronoField.HOUR_OF_AMPM, minWidth, 2, SignStyle.NORMAL)
    }

    actual fun minute() {
        builder.appendValue(ChronoField.MINUTE_OF_HOUR, 2, 2, SignStyle.NORMAL)
    }

    actual fun second() {
        builder.appendValue(ChronoField.SECOND_OF_MINUTE, 2, 2, SignStyle.NORMAL)
    }

    actual fun secondFraction(minLength: Int, maxLength: Int) {
        builder.appendFraction(ChronoField.NANO_OF_SECOND, minLength, maxLength, true)
    }

    actual fun amPm() {
        builder.appendValue(ChronoField.AMPM_OF_DAY, 2, 2, SignStyle.NORMAL)
    }

}