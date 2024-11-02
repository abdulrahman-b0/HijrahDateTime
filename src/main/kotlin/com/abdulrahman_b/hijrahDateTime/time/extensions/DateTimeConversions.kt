package com.abdulrahman_b.hijrahDateTime.time.extensions

import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.ZonedHijrahDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate

/**
 * A set of extension functions for converting from ISO date-time types to Hijrah date-time types.
 */
object DateTimeConversions {

    /** Converts this local date to a [HijrahDate]. */
    @JvmStatic
    fun LocalDate.toHijrahDate(): HijrahDate = HijrahDate.from(this)

    /** Converts this local date to a [HijrahDateTime] at the start of the day. */
    @JvmStatic
    fun LocalDateTime.toHijrahDateTime() = HijrahDateTime.from(this)

    /** Converts this local date to a [ZonedHijrahDateTime] at the start of the day. */
    @JvmStatic
    fun ZonedDateTime.toZonedHijrahDateTime() = ZonedHijrahDateTime.from(this)

    /** Converts this local date to an [OffsetHijrahDateTime] at the start of the day. */
    @JvmStatic
    fun OffsetDateTime.toOffsetHijrahDateTime() = OffsetHijrahDateTime.from(this)

}