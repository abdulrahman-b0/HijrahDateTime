package com.abdulrahman_b.hijrahdatetime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HijrahMonthTest {

    @Test
    fun testOf() {
        assertEquals(HijrahMonth.MUHARRAM, HijrahMonth.of(1))
        assertEquals(HijrahMonth.SAFAR, HijrahMonth.of(2))
        assertEquals(HijrahMonth.RABI_AL_AWWAL, HijrahMonth.of(3))
        assertEquals(HijrahMonth.RABI_AL_AKHIR, HijrahMonth.of(4))
        assertEquals(HijrahMonth.JUMADA_AL_ULA, HijrahMonth.of(5))
        assertEquals(HijrahMonth.JUMADA_AL_AKHIRAH, HijrahMonth.of(6))
        assertEquals(HijrahMonth.RAJAB, HijrahMonth.of(7))
        assertEquals(HijrahMonth.SHAABAN, HijrahMonth.of(8))
        assertEquals(HijrahMonth.RAMADAN, HijrahMonth.of(9))
        assertEquals(HijrahMonth.SHAWWAL, HijrahMonth.of(10))
        assertEquals(HijrahMonth.THUL_QIDAH, HijrahMonth.of(11))
        assertEquals(HijrahMonth.THUL_HIJJAH, HijrahMonth.of(12))

        assertFailsWith<IllegalArgumentException> { HijrahMonth.of(0) }
        assertFailsWith<IllegalArgumentException> { HijrahMonth.of(13) }
    }

    @Test
    fun testNumber() {
        assertEquals(1, HijrahMonth.MUHARRAM.number)
        assertEquals(12, HijrahMonth.THUL_HIJJAH.number)
    }

    @Test
    fun testYearMonthExtension() {
        val date = HijrahDate(1445, 9, 1)
        val ym = date.yearMonth
        assertEquals(1445, ym.year)
        assertEquals(HijrahMonth.RAMADAN, ym.month)
    }
}
