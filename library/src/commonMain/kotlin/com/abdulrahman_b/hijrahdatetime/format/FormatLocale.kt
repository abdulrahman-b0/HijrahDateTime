@file:Suppress("unused")

package com.abdulrahman_b.hijrahdatetime.format

expect class FormatLocale

expect object FormatLocales {

    val Arabic: FormatLocale
    val English: FormatLocale

    fun getDefault(): FormatLocale

}