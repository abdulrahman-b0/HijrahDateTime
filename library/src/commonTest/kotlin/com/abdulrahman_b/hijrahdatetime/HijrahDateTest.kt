package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.*
import kotlin.test.*

class HijrahDateTest {

    @Test
    fun testConstructorAndProperties() {
        val date = HijrahDate(1445, 9, 1) // Ramadan 1, 1445
        assertEquals(1445, date.year)
        assertEquals(9, date.month)
        assertEquals(1, date.dayOfMonth)
        // 1445-09-01 is roughly 2024-03-11, which was a Monday
        assertEquals(DayOfWeek.MONDAY, date.dayOfWeek)
    }

    @Test
    fun testCompareTo() {
        val date1 = HijrahDate(1445, 9, 1)
        val date2 = HijrahDate(1445, 9, 2)
        val date3 = HijrahDate(1445, 10, 1)
        val date4 = HijrahDate(1446, 1, 1)

        assertTrue(date1 < date2)
        assertTrue(date2 < date3)
        assertTrue(date3 < date4)
        assertEquals(0, date1.compareTo(date1))
    }

    @Test
    fun testPlusMinusPeriod() {
        val date = HijrahDate(1445, 9, 1)
        
        val plusMonth = date + DatePeriod(months = 1)
        assertEquals(1445, plusMonth.year)
        assertEquals(10, plusMonth.month)
        assertEquals(1, plusMonth.dayOfMonth)

        val minusMonth = date - DatePeriod(months = 1)
        assertEquals(1445, minusMonth.year)
        assertEquals(8, minusMonth.month)
        assertEquals(1, minusMonth.dayOfMonth)
        
        val plusYear = date + DatePeriod(years = 1)
        assertEquals(1446, plusYear.year)
        assertEquals(9, plusYear.month)
        assertEquals(1, plusYear.dayOfMonth)
    }

    @Test
    fun testPlusMinusUnit() {
        val date = HijrahDate(1445, 9, 1)
        
        assertEquals(2, date.plus(1, DateTimeUnit.DAY).dayOfMonth)
        assertEquals(10, date.plus(1, DateTimeUnit.MONTH).month)
        assertEquals(1446, date.plus(1, DateTimeUnit.YEAR).year)
        
        assertEquals(1444, date.minus(1, DateTimeUnit.YEAR).year)
    }
    
    @Test
    fun testPlusMinusUnitFixed() {
        val date = HijrahDate(1445, 9, 1)
        assertEquals(1444, date.minus(1, DateTimeUnit.YEAR).year)
        assertEquals(8, date.minus(1, DateTimeUnit.MONTH).month)
    }

    @Test
    fun testEpochDays() {
        val date = HijrahDate(1445, 9, 1)
        val epochDays = date.toEpochDays()
        val fromEpoch = HijrahDate.fromEpochDays(epochDays)
        assertEquals(date, fromEpoch)
    }

    @Test
    fun testToLocalDate() {
        val hijrahDate = HijrahDate(1445, 9, 1)
        val localDate = hijrahDate.toLocalDate()
        // 1445-09-01 Hijri is 2024-03-11 ISO
        assertEquals(LocalDate(2024, 3, 11), localDate)
        assertEquals(hijrahDate, localDate.toHijrahDate())
    }

    @Test
    fun testExtensions() {
        val date = HijrahDate(1445, 9, 1)
        
        assertEquals(2, (date plusDays 1).dayOfMonth)
        assertEquals(10, (date plusMonths 1).month)
        assertEquals(1446, (date plusYears 1).year)
        
        assertEquals(15, date.withDayOfMonth(15).dayOfMonth)
        assertEquals(10, date.withMonth(10).month)
        assertEquals(1446, date.withYear(1446).year)
    }

    @Test
    fun testWithDayOfWeek() {
        val date = HijrahDate(1445, 9, 1) // Monday
        
        val nextFriday = date.withNextDayOfWeek(DayOfWeek.FRIDAY)
        assertEquals(DayOfWeek.FRIDAY, nextFriday.dayOfWeek)
        assertEquals(HijrahDate(1445, 9, 5), nextFriday)

        val prevFriday = date.withPreviousDayOfWeek(DayOfWeek.FRIDAY)
        assertEquals(DayOfWeek.FRIDAY, prevFriday.dayOfWeek)
        assertTrue(prevFriday < date)
        assertEquals(HijrahDate(1445, 8, 27), prevFriday)


        val sameOrNextMonday = date.withSameOrNextDayOfWeek(DayOfWeek.MONDAY)
        assertEquals(date, sameOrNextMonday)
        
        val sameOrPrevMonday = date.withSameOrPreviousDayOfWeek(DayOfWeek.MONDAY)
        assertEquals(date, sameOrPrevMonday)
    }

    @Test
    fun testLastDayOfMonth() {
        // Ramadan 1445 has 30 days
        var date = HijrahDate(1445, 9, 1)
        var lastDay = date.withLastDayOfMonth()
        assertEquals(30, lastDay.dayOfMonth)
        assertEquals(9, lastDay.month)

        //Ramadan 1444 has 29 days
        date = HijrahDate(1444, 9, 1)
        lastDay = date.withLastDayOfMonth()
        assertEquals(29, lastDay.dayOfMonth)
        assertEquals(9, lastDay.month)
    }
    
    @Test
    fun testMinMax() {
        val min = HijrahDate.MIN
        val max = HijrahDate.MAX
        println("HijrahDate.Companion.MIN = $min")
        println("HijrahDate.Companion.MAX = $max")
        assertTrue(min < max)
        
        // Ensure they don't throw when accessing properties
        assertNotNull(min.year)
        assertNotNull(max.year)
    }
}
