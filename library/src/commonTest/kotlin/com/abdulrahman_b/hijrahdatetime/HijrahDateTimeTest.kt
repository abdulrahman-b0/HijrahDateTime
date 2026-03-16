package com.abdulrahman_b.hijrahdatetime

import io.kotest.assertions.throwables.shouldThrow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

class HijrahDateTimeTest {

    @Test
    fun `test Hijri creation`() {
        val dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        assertEquals(1445, dt.year)
        assertEquals(9, dt.month.number)
        assertEquals(1, dt.day)
        assertEquals(10, dt.hour)
        assertEquals(30, dt.minute)
        assertEquals(0, dt.second)
        assertEquals(0, dt.nanosecond)
        assertEquals(DayOfWeek.MONDAY, dt.dayOfWeek)

        shouldThrow<IllegalArgumentException> {
            HijrahDateTime(1445, 9, 31, 10, 30, 0, 0) //Invalid day of month
        }

        shouldThrow<IllegalArgumentException> {
            HijrahDateTime(1445, 13, 1, 10, 30, 0, 0)
        }

        shouldThrow<IllegalArgumentException> {
            HijrahDateTime(1445, 9, 1, 24, 0, 0, 0)
        }

        shouldThrow<IllegalArgumentException> {
            HijrahDateTime(1445, 9, 1, 10, 60, 0, 0)
        }
    }

    @Test
    fun `test comparison`() {
        val dt1 = HijrahDateTime(1445, 9, 1, 10, 0, 0, 0)
        val dt2 = HijrahDateTime(1445, 9, 1, 11, 0, 0, 0)
        val dt3 = HijrahDateTime(1445, 9, 2, 10, 0, 0, 0)

        assertTrue(dt1 < dt2)
        assertTrue(dt2 < dt3)
        assertEquals(0, dt1.compareTo(HijrahDateTime(1445, 9, 1, 10, 0, 0, 0)))
    }

    @Test
    fun `test to instant`() {
        // 2024-03-11T00:00:00Z is 1445-09-01
        val dt = HijrahDateTime(1445, 9, 1, 0, 0, 0, 0)
        val instant = dt.toInstant(TimeZone.UTC)
        assertEquals(1710115200, instant.epochSeconds)
    }

    @Test
    fun `test to LocalDateTime`() {
        val dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        val localDt = dt.toLocalDateTime()
        assertEquals(2024, localDt.year)
        assertEquals(Month.MARCH, localDt.month)
        assertEquals(11, localDt.day)
        assertEquals(10, localDt.hour)
        assertEquals(30, localDt.minute)
    }

    @Test
    fun `test instant conversion`() {
        val instant = Instant.fromEpochSeconds(1710115200)
        val dt = instant.toHijrahDateTime(TimeZone.UTC)
        assertEquals(1445, dt.year)
        assertEquals(9, dt.month.number)
        assertEquals(1, dt.day)
        assertEquals(0, dt.hour)
    }

    @Test
    fun `test of date and time`() {
        val date = HijrahDate(1445, 9, 1)
        val time = LocalTime(10, 30, 0, 0)
        val dt = HijrahDateTime.of(date, time)
        assertEquals(1445, dt.year)
        assertEquals(10, dt.hour)
        assertEquals(date, dt.date)
        assertEquals(time, dt.time)
    }
    
    @Test
    fun `test LocalDateTime conversion to HijrahDateTime with time of day`() {
        val ldt = LocalDateTime(2026, 3, 16, 10, 30)
        val dt = ldt.toHijrahDateTime()
        assertEquals(1447, dt.year)
        assertEquals(9, dt.month.number)
        assertEquals(27, dt.day)
        assertEquals(10, dt.hour)
        assertEquals(30, dt.minute)
    }

    @Test
    fun `test LocalDateTime conversion to HijrahDateTime with 0 time of day`() {
        val ldt = LocalDateTime(2026, 3, 16, 0, 0)
        val dt = ldt.toHijrahDateTime()
        assertEquals(1447, dt.year)
        assertEquals(9, dt.month.number)
        assertEquals(27, dt.day)
        assertEquals(0, dt.hour)
        assertEquals(0, dt.minute)
    }


    @Test
    fun `test parsing`() {
        val dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        val format = HijrahDateTimeFormats.DATETIME_ISO
        val string = dt.format(format)
        
        val parsed = HijrahDateTime.parse(string, format)
        assertEquals(dt.year, parsed.year)
        assertEquals(dt.month, parsed.month)
        assertEquals(dt.day, parsed.day)
        assertEquals(dt.hour, parsed.hour)
        assertEquals(dt.minute, parsed.minute)
        
        val parsedOrNull = HijrahDateTime.parseOrNull(string, format)
        assertEquals(dt.year, parsedOrNull?.year)
        
        assertNull(HijrahDateTime.parseOrNull("invalid", format))
    }

    @Test
    fun `test instant round trip with fixed offset`() {
        val tz = TimeZone.of("UTC+03:00") as FixedOffsetTimeZone
        val instant = Instant.fromEpochSeconds(1710115200) // 2024-03-11T00:00:00Z
        val dt = instant.toHijrahDateTime(tz)
        val roundTrip = dt.toInstant(tz)
        assertEquals(instant.epochSeconds, roundTrip.epochSeconds)
    }
}
