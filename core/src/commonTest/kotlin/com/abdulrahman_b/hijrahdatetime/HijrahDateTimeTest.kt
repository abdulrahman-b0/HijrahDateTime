package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormats
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
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
    fun `test instant conversion with fixed epoch seconds`() {
        val instant = Instant.fromEpochSeconds(1774396800)

        var dt = instant.toHijrahDateTime(TimeZone.UTC)
        assertEquals(dt.year, 1447)
        assertEquals(dt.date.year, 1447)
        assertEquals(dt.month.number, 10)
        assertEquals(dt.date.month.number, 10)
        assertEquals(dt.date.day, 6)
        assertEquals(dt.day, 6)

        dt = instant.toHijrahDateTime(TimeZone.of("Asia/Riyadh"))
        assertEquals(dt.year, 1447)
        assertEquals(dt.date.year, 1447)
        assertEquals(dt.month.number, 10)
        assertEquals(dt.date.month.number, 10)
        assertEquals(dt.date.day, 6)
        assertEquals(dt.day, 6)

        dt = instant.toHijrahDateTime(TimeZone.of("America/New_York"))
        assertEquals(dt.year, 1447)
        assertEquals(dt.date.year, 1447)
        assertEquals(dt.month.number, 10)
        assertEquals(dt.date.month.number, 10)
        assertEquals(dt.date.day, 5)
        assertEquals(dt.day, 5)

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test instant conversion with variable epoch seconds`() {
        val instant = HijrahDate(1447, 10, 6).atStartOfDay(TimeZone.UTC)
        var dt = instant.toHijrahDateTime(TimeZone.UTC)
        assertEquals(dt.year, 1447)
        assertEquals(dt.date.year, 1447)
        assertEquals(dt.month.number, 10)
        assertEquals(dt.date.month.number, 10)
        assertEquals(dt.date.day, 6)
        assertEquals(dt.day, 6)

        dt = instant.toHijrahDateTime(TimeZone.of("Asia/Riyadh"))
        assertEquals(dt.year, 1447)
        assertEquals(dt.date.year, 1447)
        assertEquals(dt.month.number, 10)
        assertEquals(dt.date.month.number, 10)
        assertEquals(dt.date.day, 6)
        assertEquals(dt.day, 6)

        dt = instant.toHijrahDateTime(TimeZone.of("America/New_York"))
        assertEquals(dt.year, 1447)
        assertEquals(dt.date.year, 1447)
        assertEquals(dt.month.number, 10)
        assertEquals(dt.date.month.number, 10)
        assertEquals(dt.date.day, 5)
        assertEquals(dt.day, 5)
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

    @Test
    fun `test toInstant before 1970`() {
        // Arrange
        val dtBefore = HijrahDateTime(1358, 2, 11, 0, 0, 0, 0)
        
        // Act
        val instantBefore = dtBefore.toInstant(TimeZone.UTC)
        
        // Assert
        instantBefore.epochSeconds shouldBe -970531200L
    }

    @Test
    fun `test toInstant start of Umm Al-Qura`() {
        // Arrange
        val dtStart = HijrahDateTime(1300, 1, 1, 0, 0, 0, 0)
        
        // Act
        val instantStart = dtStart.toInstant(TimeZone.UTC)
        
        // Assert
        instantStart.epochSeconds shouldBe -2749766400L // 1882-11-12
    }

    @Test
    fun `test toInstant end of Umm Al-Qura`() {
        // Arrange
        val dtEnd = HijrahDateTime(1600, 12, 30, 23, 59, 59, 0)
        
        // Act
        val instantEnd = dtEnd.toInstant(TimeZone.UTC)
        
        // Assert
        instantEnd.epochSeconds shouldBe 6466089599L
    }

    @Test
    fun `test toLocalDateTime start of Umm Al-Qura`() {
        // Arrange
        val dtStart = HijrahDateTime(1300, 1, 1, 10, 30, 0, 0)
        
        // Act
        val ldtStart = dtStart.toLocalDateTime()
        
        // Assert
        ldtStart.year shouldBe 1882
        ldtStart.month shouldBe Month.NOVEMBER
        ldtStart.day shouldBe 12
        ldtStart.hour shouldBe 10
        ldtStart.minute shouldBe 30
    }

    @Test
    fun `test toLocalDateTime end of Umm Al-Qura`() {
        // Arrange
        val dtEnd = HijrahDateTime(1600, 12, 30, 23, 59, 59, 0)
        
        // Act
        val ldtEnd = dtEnd.toLocalDateTime()
        
        // Assert
        ldtEnd.year shouldBe 2174
        ldtEnd.month shouldBe Month.NOVEMBER
        ldtEnd.day shouldBe 25
        ldtEnd.hour shouldBe 23
        ldtEnd.minute shouldBe 59
    }
}
