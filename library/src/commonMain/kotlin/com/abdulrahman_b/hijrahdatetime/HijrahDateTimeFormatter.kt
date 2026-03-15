@file:Suppress("unused")
package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.format.Padding

expect class HijrahDateTimeFormat {


    companion object {
        fun ofPattern(
            pattern: String,
            locale: FormatLocale = FormatLocales.English
        ): HijrahDateTimeFormat

    }

}

expect class HijrahDateTimeFormatBuilder() {

    var locale: FormatLocale

    var decimalStyle: DecimalStyle

    fun format(format: HijrahDateTimeFormat)
    fun year(padding: Padding = Padding.ZERO)
    fun monthNumber(padding: Padding = Padding.ZERO)
    fun monthName(style: NameStyle = NameStyle.FULL)
    fun dayOfWeek(style: NameStyle = NameStyle.FULL)
    fun dayOfMonth(padding: Padding = Padding.ZERO)
    fun hour(padding: Padding = Padding.ZERO)
    fun amPmHour(padding: Padding = Padding.ZERO)
    fun amPm()
    fun minute()
    fun second()
    fun secondFraction(minLength: Int = 1, maxLength: Int = 9)
    fun char(char: Char)
    fun chars(chars: String)
    fun byUnicodePattern(pattern: String)
    fun zoneOffset()
    fun build(): HijrahDateTimeFormat

}

fun buildDateTimeFormat(block: HijrahDateTimeFormatBuilder.() -> Unit): HijrahDateTimeFormat {
    return HijrahDateTimeFormatBuilder().apply(block).build()
}

enum class NameStyle {
    FULL, ABBREVIATED
}
