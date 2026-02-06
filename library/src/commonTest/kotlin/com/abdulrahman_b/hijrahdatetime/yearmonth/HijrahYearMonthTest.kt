package com.abdulrahman_b.hijrahdatetime.yearmonth

import com.abdulrahman_b.hijrahdatetime.DateTimeException
import com.abdulrahman_b.hijrahdatetime.HijrahDate
import com.abdulrahman_b.hijrahdatetime.HijrahMonth
import kotlinx.datetime.*
import kotlin.test.*

class HijrahYearMonthTest {

    @Test
    fun testConstructorAndProperties() {
        val ym = HijrahYearMonth(1445, 9)
        assertEquals(1445, ym.year)
        assertEquals(HijrahMonth.RAMADAN, ym.month)
        assertEquals(9, ym.month.value)
    }

    @Test
    fun testFirstLastDay() {
        val ym = HijrahYearMonth(1445, 9)
        assertEquals(HijrahDate(1445, 9, 1), ym.firstDay)
        assertEquals(30, ym.numberOfDays)
        assertEquals(HijrahDate(1445, 9, 30), ym.lastDay)
    }

    @Test
    fun testCompareTo() {
        val ym1 = HijrahYearMonth(1445, 9)
        val ym2 = HijrahYearMonth(1445, 10)
        val ym3 = HijrahYearMonth(1446, 1)

        assertTrue(ym1 < ym2)
        assertTrue(ym2 < ym3)
        assertEquals(0, ym1.compareTo(ym1))
    }

    @Test
    fun testPlusMinus() {
        val ym = HijrahYearMonth(1445, 9)
        
        assertEquals(HijrahYearMonth(1445, 10), ym.plusMonth(1))
        assertEquals(HijrahYearMonth(1445, 8), ym.minusMonth(1))
        assertEquals(HijrahYearMonth(1446, 9), ym.plusYear(1))
        assertEquals(HijrahYearMonth(1444, 9), ym.minusYear(1))
        
        // Edge of year
        assertEquals(HijrahYearMonth(1446, 1), HijrahYearMonth(1445, 12).plusMonth(1))
        assertEquals(HijrahYearMonth(1444, 12), HijrahYearMonth(1445, 1).minusMonth(1))
    }

    @Test
    fun testUntil() {
        val ym1 = HijrahYearMonth(1445, 9)
        val ym2 = HijrahYearMonth(1446, 3)
        
        // 1445-09 to 1446-03 is 3 + (12-9) = 6 months
        assertEquals(6, ym1.untilMonth(ym2))
        assertEquals(-6, ym2.untilMonth(ym1))
        
        assertEquals(1, ym1.untilYear(HijrahYearMonth(1446, 9)))
        assertEquals(0, ym1.untilYear(ym2))
    }

    @Test
    fun testToString() {
        assertEquals("1445-09", HijrahYearMonth(1445, 9).toString())
        assertEquals("1445-01", HijrahYearMonth(1445, 1).toString())
        assertEquals("1445-12", HijrahYearMonth(1445, 12).toString())
    }

    @Test
    fun testOnDay() {
        val ym = HijrahYearMonth(1445, 9)
        val date = ym.onDay(15)
        assertEquals(HijrahDate(1445, 9, 15), date)
        
        assertFailsWith<IllegalArgumentException> {
            ym.onDay(31).also(::println) // Ramadan 1445 has 30 days
        }
    }
}
