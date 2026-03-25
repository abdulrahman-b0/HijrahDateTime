package com.abdulrahman_b.hijrahdatetime.format

import java.util.Locale

actual typealias FormatLocale = Locale

actual object FormatLocales {
    actual val English: FormatLocale = Locale.ENGLISH
    actual val Arabic: FormatLocale = Locale.Builder().setLanguage("ar").build()
    actual fun getDefault(): Locale = Locale.getDefault()
}