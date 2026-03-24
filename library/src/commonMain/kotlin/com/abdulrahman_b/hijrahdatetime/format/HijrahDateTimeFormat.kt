@file:Suppress("unused")
package com.abdulrahman_b.hijrahdatetime.format

import com.abdulrahman_b.hijrahdatetime.FormatLocale
import com.abdulrahman_b.hijrahdatetime.FormatLocales

expect class HijrahDateTimeFormat {


    companion object {
        /**
         * Creates an instance of `HijrahDateTimeFormat` using the specified pattern and locale.
         *
         * @param pattern The format pattern to use, defining how the date and time should be represented.
         * @param locale The locale to use for formatting, defaulting to `FormatLocales.English`.
         * @return A `HijrahDateTimeFormat` instance configured with the specified pattern and locale.
         */
        fun ofPattern(
            pattern: String,
            locale: FormatLocale = FormatLocales.English
        ): HijrahDateTimeFormat
        
        /**
         * Creates a `HijrahDateTimeFormat` instance using the best matching localized pattern for the specified locale.
         *
         * @param pattern the format pattern string used to define the date and time formatting.
         * @param locale the locale to customize the format; defaults to `FormatLocales.English` if not specified.
         * @return a `HijrahDateTimeFormat` instance configured with the provided pattern and locale.
         */
        fun ofBestPattern(
            pattern: String,
            locale: FormatLocale = FormatLocales.English
        ): HijrahDateTimeFormat

    }

}
