package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.*
import kotlin.test.*

class HijrahDateTimeTest {

    @Test
    fun testConstructorAndProperties() {
        val dateTime = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        assertEquals(1445, dateTime.year)
        assertEquals(9, dateTime.month)
        assertEquals(1, dateTime.dayOfMonth)
        assertEquals(10, dateTime.hour)
        assertEquals(30, dateTime.minute)
        assertEquals(0, dateTime.second)
        assertEquals(0, dateTime.nanosecond)
        
        assertEquals(HijrahDate(1445, 9, 1), dateTime.date)
        assertEquals(LocalTime(10, 30, 0, 0), dateTime.time)
    }

    @Test
    fun testCompareTo() {
        val dt1 = HijrahDateTime(1445, 9, 1, 10, 0, 0, 0)
        val dt2 = HijrahDateTime(1445, 9, 1, 11, 0, 0, 0)
        val dt3 = HijrahDateTime(1445, 9, 2, 10, 0, 0, 0)

        assertTrue(dt1 < dt2)
        assertTrue(dt2 < dt3)
        assertEquals(0, dt1.compareTo(dt1))
    }

    @Test
    fun testToInstant() {
        val dateTime = HijrahDateTime(1445, 9, 1, 0, 0, 0, 0)
        val timeZone = TimeZone.UTC
        val instant = dateTime.toInstant(timeZone)
        
        // 1445-09-01 00:00:00 UTC is 2024-03-11 00:00:00 UTC
        assertEquals(Instant.fromEpochSeconds(1710115200), instant)
        
        val back = instant.toHijrahDateTime(timeZone)
        assertEquals(dateTime.year, back.year)
        assertEquals(dateTime.month, back.month)
        assertEquals(dateTime.dayOfMonth, back.dayOfMonth)
    }

    @Test
    fun testToLocalDateTime() {
        val dateTime = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        val localDateTime = dateTime.toLocalDateTime()
        assertEquals(LocalDateTime(2024, 3, 11, 10, 30, 0, 0), localDateTime)
        
        // Use UTC for roundtrip to avoid system timezone issues
        val back = localDateTime.toInstant(TimeZone.UTC).toHijrahDateTime(TimeZone.UTC)
        assertEquals(dateTime, back)
    }

    @Test
    fun testOfAndAtTime() {
        val date = HijrahDate(1445, 9, 1)
        val time = LocalTime(10, 30)
        
        val dt1 = HijrahDateTime.of(date, time)
        val dt2 = date.atTime(time)
        val dt3 = date.atStartOfDay()
        
        assertEquals(dt1, dt2)
        assertEquals(10, dt2.hour)
        assertEquals(0, dt3.hour)
        assertEquals(0, dt3.minute)
    }
}
