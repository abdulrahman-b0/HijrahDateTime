package com.abdulrahman_b.hijrahdatetime.format

import android.text.format.DateFormat
import java.time.format.DateTimeFormatter

actual class HijrahDateTimeFormat(val javaFormatter: DateTimeFormatter) {

    actual companion object {

        actual fun ofPattern(
            pattern: String,
            locale: FormatLocale
        ): HijrahDateTimeFormat = HijrahDateTimeFormat(DateTimeFormatter.ofPattern(pattern, locale))

        actual fun ofBestPattern(pattern: String, locale: FormatLocale): HijrahDateTimeFormat {
            val bestPattern = DateFormat.getBestDateTimePattern(locale, pattern)
            return ofPattern(bestPattern, locale)
        }
    }
}

actual val HijrahDateTimeFormat.javaFormatter: DateTimeFormatter get() = this.javaFormatter