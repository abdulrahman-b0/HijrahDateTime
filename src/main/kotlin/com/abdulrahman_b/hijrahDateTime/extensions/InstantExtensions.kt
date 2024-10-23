@file:JvmName("InstantUtils")

package com.abdulrahman_b.hijrahDateTime.extensions

import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters
import com.abdulrahman_b.hijrahDateTime.time.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.ZonedHijrahDateTime
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/** Converts this instant to a [ZonedHijrahDateTime] at the given [zoneId]. */
fun Instant.atHijrahZone(zoneId: ZoneId) = ZonedHijrahDateTime.ofInstant(this, zoneId)

/** Converts this instant to a [OffsetHijrahDateTime] at the given [offset]. */
fun Instant.atHijrahOffset(offset: ZoneId) = OffsetHijrahDateTime.ofInstant(this, offset)

/** A string representation of this instant using Hijrah date-time representation.*/
fun Instant.toHijrahString(): String =
    ZonedDateTime.parse(this.toString()).format(HijrahDateTimeFormatters.HIJRAH_OFFSET_DATE_TIME)
