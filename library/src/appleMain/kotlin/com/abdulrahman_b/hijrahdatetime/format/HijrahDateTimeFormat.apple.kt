package com.abdulrahman_b.hijrahdatetime.format

import com.abdulrahman_b.hijrahdatetime.FormatLocale
import com.abdulrahman_b.hijrahdatetime.FormatLocales
import platform.Foundation.NSDateFormatter

actual class HijrahDateTimeFormat(val nsFormatter: NSDateFormatter) {
    actual companion object {
        actual fun ofPattern(
            pattern: String,
            locale: FormatLocale
        ): HijrahDateTimeFormat {
            val formatter = NSDateFormatter().apply {
                this.dateFormat = pattern
                this.locale = locale
            }
            return HijrahDateTimeFormat(formatter)
        }

        actual fun ofBestPattern(pattern: String, locale: FormatLocale): HijrahDateTimeFormat {
            return when (locale) {
                FormatLocales.Arabic -> ofPattern(pattern.replace(",", "،"), locale)
                else -> ofPattern(pattern, FormatLocales.English)
            }
        }
    }
}
