@file:Suppress("unused")

package com.abdulrahman_b.hijrahdatetime.format

import kotlinx.datetime.format.Padding

/**
 * A builder for creating [HijrahDateTimeFormat] instances.
 */
expect class HijrahDateTimeFormatBuilder() {

    /** The [FormatLocale] to use for formatting. */
    var locale: FormatLocale

    /** The [DecimalStyle] to use for formatting digits. */
    var decimalStyle: DecimalStyle

    /** Appends the specified [format] to this builder. */
    fun format(format: HijrahDateTimeFormat)
    /** Appends the year with the specified [padding]. */
    fun year(padding: Padding = Padding.ZERO)
    /** Appends the month number with the specified [padding]. */
    fun monthNumber(padding: Padding = Padding.ZERO)
    /** Appends the month name with the specified [style]. */
    fun monthName(style: NameStyle = NameStyle.FULL)
    /** Appends the day of week name with the specified [style]. */
    fun dayOfWeek(style: NameStyle = NameStyle.FULL)
    /** Appends the day of month with the specified [padding]. */
    fun dayOfMonth(padding: Padding = Padding.ZERO)
    /** Appends the hour (0-23) with the specified [padding]. */
    fun hour(padding: Padding = Padding.ZERO)
    /** Appends the hour (1-12) with the specified [padding]. */
    fun amPmHour(padding: Padding = Padding.ZERO)
    /** Appends the AM/PM marker. */
    fun amPm()
    /** Appends the minute. */
    fun minute()
    /** Appends the second. */
    fun second()
    /** Appends the second fraction with the specified [minLength] and [maxLength]. */
    fun secondFraction(minLength: Int = 1, maxLength: Int = 9)
    /** Appends a single character. */
    fun char(char: Char)
    /** Appends a string of characters. */
    fun chars(chars: String)
    /** Appends a pattern defined by a Unicode pattern string. */
    fun byUnicodePattern(pattern: String)
    /** Appends the zone offset. */
    fun zoneOffset()

}

/** Builds a [HijrahDateTimeFormat] from this builder. */
expect fun HijrahDateTimeFormatBuilder.build(): HijrahDateTimeFormat

/**
 * Creates a [HijrahDateTimeFormat] using the specified [block].
 */
fun buildDateTimeFormat(block: HijrahDateTimeFormatBuilder.() -> Unit): HijrahDateTimeFormat {
    return HijrahDateTimeFormatBuilder().apply(block).build()
}

