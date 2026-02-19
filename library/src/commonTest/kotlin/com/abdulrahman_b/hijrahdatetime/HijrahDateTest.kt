package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.*
import kotlin.test.*

class HijrahDateTest {

    @Test
    fun `test Hijri creation`() {
        val date = HijrahDate(1445, 9, 1)
        assertEquals(1445, date.year)
        assertEquals(9, date.month)
        assertEquals(1, date.dayOfMonth)
        assertEquals(DayOfWeek.MONDAY, date.dayOfWeek)
    }

    @Test
    fun `test arithmetic days`() {
        // 1445-09-29 + 1 day should be 1445-09-30 (Ramadan 1445 has 30 days)
        val date = HijrahDate(1445, 9, 29)
        val nextDay = date plusDays 1
        
        assertEquals(30, nextDay.dayOfMonth)
        assertEquals(9, nextDay.month)
        
        val nextMonthFirst = nextDay plusDays 1
        assertEquals(10, nextMonthFirst.month)
        assertEquals(1, nextMonthFirst.dayOfMonth)

        val prevDay = nextMonthFirst minusDays 1
        assertEquals(30, prevDay.dayOfMonth)
        assertEquals(9, prevDay.month)
    }

    @Test
    fun `test arithmetic months`() {
        val date = HijrahDate(1446, 1, 29)
        val nextMonth = date plusMonths 1
        assertEquals(2, nextMonth.month)
        assertEquals(29, nextMonth.dayOfMonth)

        val nextYear = date plusYears 1
        assertEquals(1447, nextYear.year)
        assertEquals(1, nextYear.month)
        assertEquals(29, nextYear.dayOfMonth)
        
        val minusMonth = nextMonth minusMonths 1
        assertEquals(1, minusMonth.month)
        assertEquals(29, minusMonth.dayOfMonth)
    }

    @Test
    fun `test comparison`() {
        val date1 = HijrahDate(1446, 3, 11)
        val date2 = HijrahDate(1446, 3, 12)
        val date3 = HijrahDate(1446, 2, 11)

        assertTrue(date1 < date2)
        assertTrue(date1 > date3)
        assertEquals(0, date1.compareTo(HijrahDate(1446, 3, 11)))
        assertEquals(date1, HijrahDate(1446, 3, 11))
    }

    @Test
    fun `test to epoch days`() {
        // 1446-01-01 AH is 2024-07-07 ISO. 
        // Epoch days for 2024-07-07 is 19911
        val date = HijrahDate(1446, 1, 1)
        assertEquals(19911, date.toEpochDays())
        
        val dateFromEpoch = HijrahDate.fromEpochDays(19911)
        assertEquals(1446, dateFromEpoch.year)
        assertEquals(1, dateFromEpoch.month)
        assertEquals(1, dateFromEpoch.dayOfMonth)
    }

    @Test
    fun `test to LocalDate`() {
        val date = HijrahDate(1445, 9, 1) // 2024-03-11
        val localDate = date.toLocalDate()
        assertEquals(2024, localDate.year)
        assertEquals(3, localDate.monthNumber)
        assertEquals(11, localDate.dayOfMonth)
    }

    @Test
    fun `test instant conversion`() {
        // 2024-03-11T00:00:00Z is 1445-09-01
        val instant = Instant.fromEpochSeconds(1710115200)
        val date = instant.toHijrahDate(TimeZone.UTC)
        assertEquals(1445, date.year)
        assertEquals(9, date.month)
        assertEquals(1, date.dayOfMonth)
    }

    @Test
    fun `test withNextDayOfWeek`() {
        // 1445-09-01 is Monday
        val monday = HijrahDate(1445, 9, 1)
        assertEquals(DayOfWeek.MONDAY, monday.dayOfWeek)

        // Next Wednesday should be 1445-09-03
        val nextWednesday = monday.withNextDayOfWeek(DayOfWeek.WEDNESDAY)
        assertEquals(3, nextWednesday.dayOfMonth)
        assertEquals(DayOfWeek.WEDNESDAY, nextWednesday.dayOfWeek)

        // Next Monday should be 1445-09-08
        val nextMonday = monday.withNextDayOfWeek(DayOfWeek.MONDAY)
        assertEquals(8, nextMonday.dayOfMonth)
        assertEquals(DayOfWeek.MONDAY, nextMonday.dayOfWeek)
    }

    @Test
    fun `test withPreviousDayOfWeek`() {
        // 1445-09-01 is Monday
        val monday = HijrahDate(1445, 9, 1)

        // Previous Friday should be 1445-08-27
        // 2024-03-08 is 1445-08-27
        val prevFriday = monday.withPreviousDayOfWeek(DayOfWeek.FRIDAY)
        assertEquals(27, prevFriday.dayOfMonth)
        assertEquals(8, prevFriday.month)
        assertEquals(DayOfWeek.FRIDAY, prevFriday.dayOfWeek)

        // Previous Monday should be 1445-08-23
        val prevMonday = monday.withPreviousDayOfWeek(DayOfWeek.MONDAY)
        assertEquals(23, prevMonday.dayOfMonth)
        assertEquals(8, prevMonday.month)
        assertEquals(DayOfWeek.MONDAY, prevMonday.dayOfWeek)
    }

    @Test
    fun `test withSameOrNextDayOfWeek`() {
        val monday = HijrahDate(1445, 9, 1)

        // Same or next Monday should be Today
        val sameMonday = monday.withSameOrNextDayOfWeek(DayOfWeek.MONDAY)
        assertEquals(1, sameMonday.dayOfMonth)

        // Same or next Tuesday should be Tomorrow
        val nextTuesday = monday.withSameOrNextDayOfWeek(DayOfWeek.TUESDAY)
        assertEquals(2, nextTuesday.dayOfMonth)
    }

    @Test
    fun `test withSameOrPreviousDayOfWeek`() {
        val monday = HijrahDate(1445, 9, 1)

        // Same or previous Monday should be Today
        val sameMonday = monday.withSameOrPreviousDayOfWeek(DayOfWeek.MONDAY)
        assertEquals(1, sameMonday.dayOfMonth)

        // Same or previous Sunday should be Yesterday
        val prevSunday = monday.withSameOrPreviousDayOfWeek(DayOfWeek.SUNDAY)
        assertEquals(DayOfWeek.SUNDAY, prevSunday.dayOfWeek)
        assertTrue(prevSunday < monday)
    }

    @Test
    fun `test withLastDayOfMonth`() {
        val date = HijrahDate(1445, 9, 1)
        val lastDay = date.withLastDayOfMonth()
        
        // Ramadan 1445 had 30 days
        assertEquals(30, lastDay.dayOfMonth)
        assertEquals(9, lastDay.month)
        
        val date2 = HijrahDate(1445, 10, 1)
        val lastDay2 = date2.withLastDayOfMonth()
        // Shawwal 1445 had 29 days
        assertEquals(29, lastDay2.dayOfMonth)
    }

    @Test
    fun `test with components`() {
        val date = HijrahDate(1445, 9, 1)
        assertEquals(1446, date.withYear(1446).year)
        assertEquals(10, date.withMonth(10).month)
        assertEquals(15, date.withDayOfMonth(15).dayOfMonth)
    }

    @Test
    fun `test ranges`() {
        val start = HijrahDate(1445, 9, 1)
        val end = HijrahDate(1445, 9, 10)
        val range: ClosedRange<HijrahDate> = start..end
        
        assertEquals(start, range.start)
        assertEquals(end, range.endInclusive)
        assertTrue(range.contains(HijrahDate(1445, 9, 5)))
        assertFalse(range.contains(HijrahDate(1445, 9, 11)))
        
        val untilRange: ClosedRange<HijrahDate> = start..<end
        assertEquals(start, untilRange.start)
        assertEquals(HijrahDate(1445, 9, 9), untilRange.endInclusive)
    }

    @Test
    fun `test value range`() {
        val date = HijrahDate(1445, 9, 1)
        val dayRange = date.range(DateTimeUnit.DAY)
        assertEquals(1, dayRange.minimum)
        assertEquals(30, dayRange.maximum)
        
        val monthRange = date.range(DateTimeUnit.MONTH)
        assertEquals(1, monthRange.minimum)
        assertEquals(12, monthRange.maximum)
    }

    @Test
    fun `test atTime and atStartOfDay`() {
        val date = HijrahDate(1445, 9, 1)
        val time = LocalTime(10, 30)
        val dateTime = date.atTime(time)
        
        assertEquals(date, dateTime.date)
        assertEquals(time, dateTime.time)
        
        val startOfDay = date.atStartOfDay()
        assertEquals(0, startOfDay.hour)
        assertEquals(0, startOfDay.minute)
    }
}
