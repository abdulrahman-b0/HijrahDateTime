package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat

actual fun HijrahDate.format(format: HijrahDateTimeFormat): String {
    return format.javaFormatter.withChronology(javaDate.chronology).format(javaDate)
}