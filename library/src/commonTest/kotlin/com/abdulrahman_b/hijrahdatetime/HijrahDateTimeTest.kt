package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormats
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.ints.shouldBeInRange
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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
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
    fun `test pure date quality with local timezone`() {
        var dt = HijrahDateTime(1445, 9, 1, 10, 0, 0, 0)
        var d = HijrahDate(1445, 9, 1)

        println(dt.date.toEpochDays())
        println(d.toEpochDays())
        dt.date shouldBeEqual d

        dt = HijrahDateTime(1445, 9, 2, 0, 0, 0, 0)
        d = HijrahDate(1445, 9, 2)
        dt.date shouldBeEqual d
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test date property is equal to the individual values`() {
        var dt = Clock.System.now().toHijrahDateTime(TimeZone.UTC)
        var d = dt.date
        assertEquals(dt.year, d.year)
        assertEquals(dt.month.number, d.month.number)
        assertEquals(dt.day, d.day)

        dt = Clock.System.now().toHijrahDateTime(TimeZone.of("Asia/Riyadh"))
        d = dt.date
        assertEquals(dt.year, d.year)
        assertEquals(dt.month.number, d.month.number)
        assertEquals(dt.day, d.day)

        dt = Clock.System.now().toHijrahDateTime(TimeZone.of("America/New_York"))
        d = dt.date
        assertEquals(dt.year, d.year)
        assertEquals(dt.month.number, d.month.number)
        assertEquals(dt.day, d.day)
    }

    @OptIn(ExperimentalTime::class)
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

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test instant conversion`() {
        val instant = Instant.fromEpochSeconds(1710115200)

        //Time zone is UTC
        val dt = instant.toHijrahDateTime(TimeZone.UTC)
        assertEquals(1445, dt.year)
        assertEquals(9, dt.month.number)
        assertEquals(1, dt.day)
        assertEquals(0, dt.hour)

        //Test with a positive offset time zone
        val saDt = instant.toHijrahDateTime(TimeZone.of("Asia/Riyadh"))
        assertEquals(1445, saDt.year)
        assertEquals(9, saDt.month.number)
        assertEquals(1, dt.day)
        assertEquals(3, saDt.hour)

        //Test with a negative offset time zone (UTC-5 / UTC-4)
        val nyDt = instant.toHijrahDateTime(TimeZone.of("America/New_York"))
        assertEquals(1445, nyDt.year)
        assertEquals(8, nyDt.month.number)
        assertEquals(29, nyDt.day)
        nyDt.hour shouldBeInRange  19..20

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

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test instant round trip with fixed offset`() {
        val tz = TimeZone.of("UTC+03:00") as FixedOffsetTimeZone
        val instant = Instant.fromEpochSeconds(1710115200) // 2024-03-11T00:00:00Z
        val dt = instant.toHijrahDateTime(tz)
        val roundTrip = dt.toInstant(tz)
        assertEquals(instant.epochSeconds, roundTrip.epochSeconds)
    }
}
