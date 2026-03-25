@file:Suppress("unused")

package com.abdulrahman_b.hijrahdatetime.format

object HijrahDateTimeFormats {

    val DATE_ISO = buildDateFormat(FormatLocales.English, DecimalStyle.Standard)
    val TIME_ISO = buildTimeFormat(FormatLocales.English, DecimalStyle.Standard)
    val DATETIME_ISO = buildDateTimeFormatter(FormatLocales.English, DecimalStyle.Standard)
    val OFFSET_DATE_TIME_ISO = buildDateTimeFormat {
        format(DATETIME_ISO)
        zoneOffset()
    }

    fun buildDateFormat(
        locale: FormatLocale,
        decimalStyle: DecimalStyle,
        separator: Char = '-',
    ) = buildDateTimeFormat {
        this.locale = locale
        this.decimalStyle = decimalStyle

        year()
        char(separator)
        monthNumber()
        char(separator)
        dayOfMonth()
    }

    fun buildTimeFormat(
        locale: FormatLocale,
        decimalStyle: DecimalStyle,
    ) = buildDateTimeFormat {
        this.locale = locale
        this.decimalStyle = decimalStyle

        hour()
        char(':')
        minute()
        char(':')
        second()
    }
    fun buildDateTimeFormatter(
        locale: FormatLocale,
        decimalStyle: DecimalStyle,
        separator: Char = 'T',
    ) = buildDateTimeFormat {
        this.locale = locale
        this.decimalStyle = decimalStyle

        format(buildDateFormat(locale, decimalStyle))
        char(separator)
        format(buildTimeFormat(locale, decimalStyle))
    }



}