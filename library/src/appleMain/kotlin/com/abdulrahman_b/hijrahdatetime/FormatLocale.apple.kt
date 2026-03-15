package com.abdulrahman_b.hijrahdatetime

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual typealias FormatLocale = NSLocale

actual object FormatLocales {
    actual fun getDefault() = NSLocale.currentLocale
    actual val Arabic: FormatLocale = NSLocale("ar_SA")
    actual val English: FormatLocale = NSLocale("en_US")
}