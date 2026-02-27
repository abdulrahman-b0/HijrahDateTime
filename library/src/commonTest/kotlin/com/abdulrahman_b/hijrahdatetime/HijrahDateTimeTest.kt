package com.abdulrahman_b.hijrahdatetime

import io.kotest.assertions.throwables.shouldThrow
import kotlinx.datetime.*
import kotlin.test.*
import kotlin.time.Instant

class HijrahDateTimeTest {

    @Test
    fun `test Hijri creation`() {
        var dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
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
        val instant = dt.toInstant(TimeZone.UTC as FixedOffsetTimeZone)
        assertEquals(1710115200, instant.epochSeconds)
    }

    @Test
    fun `test to LocalDateTime`() {
        val dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        val localDt = dt.toLocalDateTime()
        assertEquals(2024, localDt.year)
        assertEquals(Month.MARCH, localDt.month)
        assertEquals(11, localDt.dayOfMonth)
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
    fun `test formatting`() {
        val dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        val format = HijrahDateTimeFormats.DATETIME_ISO
        val formatted = dt.format(format)
        // Expected format: YYYY-MM-DDTHH:MM:SS
        assertEquals("1445-09-01T10:30:00", formatted)
    }
}
