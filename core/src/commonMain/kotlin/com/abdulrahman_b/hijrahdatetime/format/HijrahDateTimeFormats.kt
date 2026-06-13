@file:Suppress("unused")

package com.abdulrahman_b.hijrahdatetime.format

/**
 * Provides predefined [HijrahDateTimeFormat] instances.
 */
object HijrahDateTimeFormats {

    /** ISO-like date format (e.g., 1445-01-01). */
    val DATE_ISO = buildDateFormat(FormatLocales.English, DecimalStyle.Standard)
    /** ISO-like time format (e.g., 12:30:45). */
    val TIME_ISO = buildTimeFormat(FormatLocales.English, DecimalStyle.Standard)
    /** ISO-like date-time format (e.g., 1445-01-01T12:30:45). */
    val DATETIME_ISO = buildDateTimeFormatter(FormatLocales.English, DecimalStyle.Standard)
    /** ISO-like offset date-time format (e.g., 1445-01-01T12:30:45Z). */
    val OFFSET_DATE_TIME_ISO = buildDateTimeFormat {
        format(DATETIME_ISO)
        zoneOffset()
    }

    /**
     * Builds a date format using the specified [locale], [decimalStyle], and [separator].
     */
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

    /**
     * Builds a time format using the specified [locale] and [decimalStyle].
     */
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

    /**
     * Builds a date-time format using the specified [locale], [decimalStyle], and [separator].
     */
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