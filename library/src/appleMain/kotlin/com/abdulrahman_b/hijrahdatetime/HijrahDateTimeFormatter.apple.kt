package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.format.Padding
import platform.Foundation.NSDateFormatter

actual class HijrahDateTimeFormat(val nsFormatter: NSDateFormatter) {
    actual companion object {
        actual fun ofPattern(
            pattern: String,
            locale: FormatLocale
        ): HijrahDateTimeFormat {
            val formatter = NSDateFormatter().apply {
                this.dateFormat = pattern
                this.locale = locale
            }
            return HijrahDateTimeFormat(formatter)
        }
    }
}

actual class HijrahDateTimeFormatBuilder {
    private val pattern = StringBuilder()

    actual var locale: FormatLocale = FormatLocales.English
    actual var decimalStyle: DecimalStyle = DecimalStyle.Standard

    actual fun format(format: HijrahDateTimeFormat) {
        pattern.append(format.nsFormatter.dateFormat)
    }

    actual fun year(padding: Padding) {
        pattern.append("yyyy")
    }

    actual fun monthNumber(padding: Padding) {
        pattern.append(if (padding == Padding.ZERO) "MM" else "M")
    }

    actual fun monthName(style: NameStyle) {
        pattern.append(if (style == NameStyle.FULL) "MMMM" else "MMM")
    }

    actual fun dayOfWeek(style: NameStyle) {
        pattern.append(if (style == NameStyle.FULL) "EEEE" else "EEE")
    }

    actual fun dayOfMonth(padding: Padding) {
        pattern.append(if (padding == Padding.ZERO) "dd" else "d")
    }

    actual fun char(char: Char) {
        pattern.append("'$char'")
    }

    actual fun chars(chars: String) {
        pattern.append("'$chars'")
    }

    actual fun byUnicodePattern(pattern: String) {
        this.pattern.append(pattern)
    }

    actual fun build(): HijrahDateTimeFormat {
        val formatter = NSDateFormatter().apply {
            this.dateFormat = pattern.toString()
            this.locale = this@HijrahDateTimeFormatBuilder.locale
            // NSDateFormatter uses the locale's numbering system by default.
            // If you need to force a specific DecimalStyle, you'd configure the formatter.numberFormatter here.
        }
        return HijrahDateTimeFormat(formatter)
    }

    actual fun zoneOffset() {
        this.pattern.append("ZZZZZ")
    }

    actual fun hour(padding: Padding) {
        val hourFormat = if (padding == Padding.ZERO) "HH" else "H"
        pattern.append(hourFormat)
    }

    actual fun amPmHour(padding: Padding) {
        val amPmFormat = if (padding == Padding.ZERO) "hh a" else "h a"
        pattern.append(amPmFormat)
    }

    actual fun minute() {
        pattern.append("mm")
    }

    actual fun second() {
        pattern.append("ss")
    }

    actual fun secondFraction(minLength: Int, maxLength: Int) {
        pattern.append('.')
        repeat(minLength) { pattern.append('S') }
    }

    actual fun amPm() {
        pattern.append("a")
    }
}