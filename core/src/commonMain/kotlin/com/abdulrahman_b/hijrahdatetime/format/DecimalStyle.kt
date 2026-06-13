package com.abdulrahman_b.hijrahdatetime.format

/**
 * Defines the decimal style used for formatting.
 */
sealed class DecimalStyle {
    /** Uses standard decimal digits (0-9). */
    object Standard: DecimalStyle()
    /** Uses decimal digits specific to the specified [locale]. */
    data class OfLocale(val locale: FormatLocale) : DecimalStyle()
}