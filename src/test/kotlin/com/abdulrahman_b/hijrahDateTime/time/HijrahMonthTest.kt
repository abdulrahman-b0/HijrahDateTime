package com.abdulrahman_b.hijrahDateTime.time

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.time.temporal.UnsupportedTemporalTypeException

class HijrahMonthTest {

    @Test
    @DisplayName("HijrahMonth is resolved by its value properly")
    fun hijrahMonthIsResolvedByItsValueProperly() {
        assertEquals(HijrahMonth.SAFAR, HijrahMonth.of(2))
        assertEquals(HijrahMonth.JUMADA_AL_AWWAL, HijrahMonth.of(5))
        assertEquals(HijrahMonth.DHU_AL_QIDAH, HijrahMonth.of(11))
    }

    @Test
    @DisplayName("Month chrono field is supported")
    fun monthChronoFieldIsSupported() {
        assertTrue(HijrahMonth.SAFAR.isSupported(ChronoField.MONTH_OF_YEAR))
    }

    @Test
    @DisplayName("Day of month chrono field is not supported")
    fun monthChronoFieldIsNotSupported() {
        assertFalse(HijrahMonth.SAFAR.isSupported(ChronoField.DAY_OF_MONTH))
    }

    @Test
    @DisplayName("HijrahMonth is ranged properly")
    fun range() {

        assertEquals(1, HijrahMonth.SAFAR.range(ChronoField.MONTH_OF_YEAR).minimum)
        assertEquals(12, HijrahMonth.SAFAR.range(ChronoField.MONTH_OF_YEAR).maximum)

    }

    @Test
    @DisplayName("HijrahMonth is got properly")
    fun get() {
        assertEquals(2, HijrahMonth.SAFAR.get(ChronoField.MONTH_OF_YEAR))
        assertEquals(5, HijrahMonth.JUMADA_AL_AWWAL.get(ChronoField.MONTH_OF_YEAR))
        assertEquals(11, HijrahMonth.DHU_AL_QIDAH.get(ChronoField.MONTH_OF_YEAR))
        assertThrows<UnsupportedTemporalTypeException> {
            HijrahMonth.SAFAR.get(ChronoField.DAY_OF_WEEK)
        }
    }

    @Test
    fun getLong() {
        assertEquals(2, HijrahMonth.SAFAR.getLong(ChronoField.MONTH_OF_YEAR))
        assertEquals(5, HijrahMonth.JUMADA_AL_AWWAL.getLong(ChronoField.MONTH_OF_YEAR))
        assertEquals(11, HijrahMonth.DHU_AL_QIDAH.getLong(ChronoField.MONTH_OF_YEAR))
        assertThrows<UnsupportedTemporalTypeException> {
            HijrahMonth.SAFAR.getLong(ChronoField.YEAR)
        }
    }

    @Test
    @DisplayName("HijrahMonth is queried properly")
    fun query() {
        assertEquals(HijrahMonth.SAFAR, HijrahMonth.SAFAR.query(HijrahMonth::from))
        assertEquals(HijrahMonth.JUMADA_AL_AWWAL, HijrahDate.of(1443, 5, 15).query(HijrahMonth::from))
    }

    @Test
    @DisplayName("HijrahMonth is adjusted into the specified HijrahDate properly")
    fun adjustInto() {

        val date = HijrahDate.of(1443, 5, 15)
        val hijrahDateAdjusted = HijrahMonth.SAFAR.adjustInto(date)

        assertEquals(HijrahMonth.SAFAR.value, hijrahDateAdjusted.get(ChronoField.MONTH_OF_YEAR))
    }
}