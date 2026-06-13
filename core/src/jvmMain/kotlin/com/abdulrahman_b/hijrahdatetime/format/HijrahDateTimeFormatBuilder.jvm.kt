package com.abdulrahman_b.hijrahdatetime.format

import com.abdulrahman_b.hijrahdatetime.toJavaDecimalStyle

actual fun HijrahDateTimeFormatBuilder.build(): HijrahDateTimeFormat {
    return HijrahDateTimeFormat(
        builder
            .toFormatter(locale)
            .withDecimalStyle(decimalStyle.toJavaDecimalStyle())
    )
}