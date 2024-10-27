package com.abdulrahman_b.hijrahDateTime.time.extensions

import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.ZonedHijrahDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate

object DateTimeConversions {

    /** Converts this local date to a [HijrahDate]. */
    fun LocalDate.toHijrahDate(): HijrahDate = HijrahDate.from(this)

    /** Converts this local date to a [HijrahDateTime] at the start of the day. */
    fun LocalDateTime.toHijrahDateTime() = HijrahDateTime.from(this)

    /** Converts this local date to a [ZonedHijrahDateTime] at the start of the day. */
    fun ZonedDateTime.toZonedHijrahDateTime() = ZonedHijrahDateTime.from(this)

    /** Converts this local date to an [OffsetHijrahDateTime] at the start of the day. */
    fun OffsetDateTime.toOffsetHijrahDateTime() = OffsetHijrahDateTime.from(this)

}