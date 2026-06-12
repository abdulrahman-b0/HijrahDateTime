@file:Suppress("unused")

package com.abdulrahman_b.hijrahdatetime.format

/**
 * Represents a locale used for formatting date-time values.
 */
expect class FormatLocale

/**
 * Provides access to common [FormatLocale] instances.
 */
expect object FormatLocales {

    /** The Arabic locale. */
    val Arabic: FormatLocale
    /** The English locale. */
    val English: FormatLocale

    /** Returns the default system locale. */
    fun getDefault(): FormatLocale

}