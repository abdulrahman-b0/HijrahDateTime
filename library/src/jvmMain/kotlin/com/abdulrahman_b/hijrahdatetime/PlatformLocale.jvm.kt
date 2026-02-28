package com.abdulrahman_b.hijrahdatetime

import java.util.*

actual class FormatLocale(val locale: Locale) {

    actual val languageCode: String = locale.language
    actual val countryCode: String? = locale.country.takeIf { it.isNotEmpty() }

    actual constructor(languageCode: String, countryCode: String?) : this(
        Locale.Builder().setLanguage(languageCode).setRegion(countryCode).build()
    )


    actual companion object {
        actual val English: FormatLocale = FormatLocale(Locale.ENGLISH)
        actual fun getDefault() = FormatLocale(Locale.getDefault())
    }



}