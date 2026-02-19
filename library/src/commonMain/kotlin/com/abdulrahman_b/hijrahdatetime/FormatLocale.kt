package com.abdulrahman_b.hijrahdatetime

expect class FormatLocale(
    languageCode: String,
    countryCode: String? = null
) {

    val countryCode: String?

    val languageCode: String

    companion object {

        val English: FormatLocale
        fun getDefault(): FormatLocale
    }

}