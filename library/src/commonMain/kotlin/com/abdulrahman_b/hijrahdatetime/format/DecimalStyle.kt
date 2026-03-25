package com.abdulrahman_b.hijrahdatetime.format

sealed class DecimalStyle {
    object Standard: DecimalStyle()
    data class OfLocale(val locale: FormatLocale) : DecimalStyle()
}