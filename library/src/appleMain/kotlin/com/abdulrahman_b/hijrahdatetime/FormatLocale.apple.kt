package com.abdulrahman_b.hijrahdatetime

import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageIdentifier

actual class FormatLocale(val nsLocale: NSLocale) {

    actual val languageCode: String = nsLocale.languageIdentifier
    actual val countryCode: String? = nsLocale.countryCode


    actual constructor(languageCode: String, countryCode: String?) : this(nsLocaleFactory(languageCode, countryCode))

    actual companion object {
        actual fun getDefault() = FormatLocale(NSLocale.currentLocale)
        actual val English: FormatLocale = FormatLocale(NSLocale("en_US"))

        private fun nsLocaleFactory(languageCode: String, countryCode: String?): NSLocale =
            NSLocale(if (countryCode == null) languageCode else "${languageCode}_$countryCode")

    }

}