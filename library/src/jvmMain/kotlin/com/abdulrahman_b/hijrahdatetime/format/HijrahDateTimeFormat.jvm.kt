@file:Suppress("unused")
package com.abdulrahman_b.hijrahdatetime.format

import com.abdulrahman_b.hijrahdatetime.FormatLocale
import com.abdulrahman_b.hijrahdatetime.FormatLocales
import java.time.format.DateTimeFormatter

actual class HijrahDateTimeFormat(val javaFormatter: DateTimeFormatter) {


    actual companion object {

        actual fun ofPattern(
            pattern: String,
            locale: FormatLocale
        ): HijrahDateTimeFormat = HijrahDateTimeFormat(DateTimeFormatter.ofPattern(pattern, locale))

        actual fun ofBestPattern(pattern: String, locale: FormatLocale): HijrahDateTimeFormat =
            when (locale) {
                FormatLocales.Arabic -> ofPattern(pattern.replace(",", "،"), locale)
                else -> ofPattern(pattern, FormatLocales.English)
            }
    }
}

actual val HijrahDateTimeFormat.javaFormatter: DateTimeFormatter
    get() = javaFormatter

