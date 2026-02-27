package com.abdulrahman_b.hijrahdatetime.yearmonth

import com.abdulrahman_b.hijrahdatetime.*
import kotlin.test.*

class HijrahYearMonthTest {

    @Test
    fun `test construction`() {
        val ym = HijrahYearMonth(1445, 9)
        assertEquals(1445, ym.year)
        assertEquals(HijrahMonth.RAMADAN, ym.month)
        assertEquals(9, ym.month.number)

        val ym2 = HijrahYearMonth(1445, HijrahMonth.SHAWWAL)
        assertEquals(10, ym2.month.number)
    }

    @Test
    fun `test arithmetic`() {
        val ym = HijrahYearMonth(1445, 12)
        val next = ym.plusMonth(1)
        assertEquals(1446, next.year)
        assertEquals(1, next.month.number)

        val prev = next.minusMonth(1)
        assertEquals(1445, prev.year)
        assertEquals(12, prev.month.number)

        val nextYear = ym.plusYear(1)
        assertEquals(1446, nextYear.year)
        assertEquals(12, nextYear.month.number)
    }

    @Test
    fun `test until`() {
        val start = HijrahYearMonth(1445, 1)
        val end = HijrahYearMonth(1446, 1)
        assertEquals(12, start.untilMonth(end))
        assertEquals(1, start.untilYear(end))
    }

    @Test
    fun `test onDay and boundaries`() {
        val ym = HijrahYearMonth(1445, 9)
        val date = ym.onDay(1)
        assertEquals(1445, date.year)
        assertEquals(9, date.month.number)
        assertEquals(1, date.day)

        val firstDay = ym.firstDay
        assertEquals(1, firstDay.day)

        val lastDay = ym.lastDay
        // Ramadan 1445 had 30 days
        assertEquals(30, lastDay.day)
        assertEquals(30, ym.numberOfDays)
    }

    @Test
    fun `test progression`() {
        val start = HijrahYearMonth(1445, 1)
        val end = HijrahYearMonth(1445, 3)
        val progression = HijrahYearMonthProgression(start, end, 1)
        
        assertEquals(3, progression.size)
        val list = progression.toList()
        assertEquals(HijrahYearMonth(1445, 1), list[0])
        assertEquals(HijrahYearMonth(1445, 2), list[1])
        assertEquals(HijrahYearMonth(1445, 3), list[2])
        
        assertTrue(progression.contains(HijrahYearMonth(1445, 2)))
    }
}
