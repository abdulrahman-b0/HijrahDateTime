package com.abdulrahman_b.hijrahdatetime.yearmonth

import com.abdulrahman_b.hijrahdatetime.HijrahDateTimeFormatBuilder
import com.abdulrahman_b.hijrahdatetime.HijrahMonth
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

        assertFailsWith<IllegalArgumentException> {
            HijrahYearMonth(1445, 13)
        }
        assertFailsWith<IllegalArgumentException> {
            HijrahYearMonth(1299, 12)
        }
        assertFailsWith<IllegalArgumentException> {
            HijrahYearMonth(1601, 1)
        }
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

        assertEquals(HijrahYearMonth(1447, 12), ym.plusYear(2))
        assertEquals(HijrahYearMonth(1443, 12), ym.minusYear(2))
        assertEquals(HijrahYearMonth(1446, 2), ym.plusMonth(2))
        assertEquals(HijrahYearMonth(1445, 10), ym.minusMonth(2))
    }

    @Test
    fun `test until`() {
        val start = HijrahYearMonth(1445, 1)
        val end = HijrahYearMonth(1446, 1)
        assertEquals(12, start.untilMonth(end))
        assertEquals(1, start.untilYear(end))

        val end2 = HijrahYearMonth(1446, 7)
        assertEquals(18, start.untilMonth(end2))
        assertEquals(1, start.untilYear(end2))

        val end3 = HijrahYearMonth(1444, 1)
        assertEquals(-12, start.untilMonth(end3))
        assertEquals(-1, start.untilYear(end3))

        val start2 = HijrahYearMonth(1445, 10)
        val end4 = HijrahYearMonth(1446, 5)
        assertEquals(7, start2.untilMonth(end4))
        assertEquals(0, start2.untilYear(end4))
    }

    @Test
    fun `test prolepticMonth and comparison`() {
        val ym1 = HijrahYearMonth(1445, 1)
        val ym2 = HijrahYearMonth(1445, 2)
        val ym3 = HijrahYearMonth(1446, 1)

        assertTrue(ym1 < ym2)
        assertTrue(ym2 < ym3)
        assertTrue(ym1 < ym3)

        assertEquals(ym1.prolepticMonth + 1, ym2.prolepticMonth)
        assertEquals(ym1.prolepticMonth + 12, ym3.prolepticMonth)
        
        assertEquals(ym1, HijrahYearMonth.fromProlepticMonth(ym1.prolepticMonth))
    }

    @Test
    fun `test toString and formatting`() {
        val ym = HijrahYearMonth(1445, 9)
        assertEquals("1445-09", ym.toString())
        
        // HijrahDateTimeFormat for YearMonth
        val format = HijrahDateTimeFormatBuilder().apply {
            year(); char('/'); monthNumber()
        }.build()
        assertEquals("1445/09", ym.format(format))
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
    fun `test ranges and contains`() {
        val start = HijrahYearMonth(1445, 1)
        val end = HijrahYearMonth(1445, 12)
        val range = start..end
        assertTrue(HijrahYearMonth(1445, 6) in range)
        assertTrue(start in range)
        assertTrue(end in range)
        assertFalse(HijrahYearMonth(1444, 12) in range)
        assertFalse(HijrahYearMonth(1446, 1) in range)
        
        val emptyRange = end..start
        assertTrue(emptyRange.isEmpty())
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
        assertTrue(progression.containsAll(listOf(HijrahYearMonth(1445, 1), HijrahYearMonth(1445, 3))))

        val downProgression = HijrahYearMonth(1445, 3) downTo HijrahYearMonth(1445, 1)
        assertEquals(3, downProgression.size)
        assertEquals(HijrahYearMonth(1445, 3), downProgression.first)
        assertEquals(HijrahYearMonth(1445, 1), downProgression.last)
    }
}
