package com.abdulrahman_b.hijrah_datetime.extensions

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.ZonedHijrahDateTime
import java.time.DateTimeException
import java.time.chrono.HijrahChronology
import java.time.temporal.TemporalQuery
import java.time.chrono.HijrahDate

/**
 * A set of temporal queries for obtaining Hijrah date-time objects.
 */
object HijrahTemporalQueries {

    /**
     * A query that provides access to the [HijrahDate] from a temporal object.
     * The query returns `null` if the temporal object does not have a Hijrah date.
     *
     * @return the [HijrahDate] from the temporal object, or `null` if not available.
     * @throws DateTimeException if unable to query.
     * @throws ArithmeticException if numeric calculations are required and result overflows.
     */
    @JvmStatic
    fun hijrahDate(): TemporalQuery<HijrahDate> = TemporalQuery { temporal ->

        when (temporal) {
            is HijrahDate -> temporal
            is HijrahDateTime -> temporal.toHijrahDate()
            is ZonedHijrahDateTime -> temporal.toHijrahDate()
            is OffsetHijrahDateTime -> temporal.toHijrahDate()
            else -> temporal.query(HijrahChronology.INSTANCE::date)
        }
    }



}