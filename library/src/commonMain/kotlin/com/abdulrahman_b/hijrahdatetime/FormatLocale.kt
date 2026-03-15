package com.abdulrahman_b.hijrahdatetime

expect class FormatLocale

expect object FormatLocales {

    val Arabic: FormatLocale
    val English: FormatLocale

    fun getDefault(): FormatLocale

}