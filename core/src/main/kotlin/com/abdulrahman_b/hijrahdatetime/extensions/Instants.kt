@file:JvmName("InstantUtils")
@file:Suppress("unused")

package com.abdulrahman_b.hijrahdatetime.extensions

import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * A set of extension and utility functions for converting from [Instant] to Hijrah date-time types.
 */
object Instants {
    /** Converts this instant to a [ZonedHijrahDateTime] at the given [zoneId]. */
    @JvmStatic
    fun Instant.atHijrahZone(zoneId: ZoneId) = ZonedHijrahDateTime.ofInstant(this, zoneId)


    /** Converts this instant to a [OffsetHijrahDateTime] at the given [offset]. */
    @JvmStatic
    fun Instant.atHijrahOffset(offset: ZoneOffset) = OffsetHijrahDateTime.ofInstant(this, offset)

    /** A string representation of this instant using Hijrah date-time representation.*/
    @JvmStatic
    fun Instant.toHijrahString(): String =
        ZonedDateTime.parse(this.toString())
            .format(HijrahFormatters.HIJRAH_OFFSET_DATE_TIME)
}
