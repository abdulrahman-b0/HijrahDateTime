package com.abdulrahman_b.hijrahdatetime

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.DateTimeException
import java.time.temporal.*

class EarlyHijrahDateTest {

    val date = EarlyHijrahDate.of(700, 1, 30)

    @Test
    @DisplayName("Out of range values throw exceptions")
    fun outOfRange() {
        assertThrows<DateTimeException> {
            EarlyHijrahDate.of(1446, 12, 5)
        }
        assertThrows<DateTimeException> {
            EarlyHijrahDate.of(700, 12, 32)
        }
    }

    @Test
    @DisplayName("EarlyHijrahDate is formatted properly")
    fun format() {
        assertEquals("700-01-30", date.format())
    }

    @Test
    @DisplayName("Supported ChronoFields are Year, Month and Day")
    fun isSupported() {
        assertTrue(date.isSupported(ChronoField.YEAR))
        assertTrue(date.isSupported(ChronoField.MONTH_OF_YEAR))
        assertTrue(date.isSupported(ChronoField.DAY_OF_MONTH))
        assertFalse(date.isSupported(ChronoField.DAY_OF_WEEK))
        assertFalse(date.isSupported(ChronoField.HOUR_OF_DAY))
    }

    @Test
    fun get() {
        assertEquals(700, date.get(ChronoField.YEAR))
        assertEquals(700, date.get(ChronoField.YEAR_OF_ERA))
        assertEquals(1, date.get(ChronoField.MONTH_OF_YEAR))
        assertEquals(30, date.get(ChronoField.DAY_OF_MONTH))
        assertThrows(UnsupportedTemporalTypeException::class.java) {
            date.get(ChronoField.DAY_OF_WEEK)
        }
    }

    @Test
    fun range() {
        assertEquals(ValueRange.of(EarlyHijrahDate.MIN_YEAR.toLong(), EarlyHijrahDate.MAX_YEAR.toLong()), date.range(ChronoField.YEAR))
        assertEquals(ValueRange.of(1, 12), date.range(ChronoField.MONTH_OF_YEAR))
        assertEquals(ValueRange.of(1, 30), date.range(ChronoField.DAY_OF_MONTH))
        assertThrows(UnsupportedTemporalTypeException::class.java) {
            date.range(ChronoField.DAY_OF_WEEK)
        }
    }

    @Test
    @DisplayName("Query works properly")
    fun query() {
        assertNull(date.query(TemporalQueries.chronology()))
        assertEquals(date.query(TemporalQueries.precision()), ChronoUnit.DAYS)
        assertEquals(date.query { accessor -> accessor.get(ChronoField.YEAR) }, date.year)
        assertNull(date.query(TemporalQueries.localDate()))
        assertNull(date.query(TemporalQueries.zone()))
    }

    @Test
    fun getLong() {
        assertEquals(700L, date.getLong(ChronoField.YEAR))
        assertEquals(700L, date.getLong(ChronoField.YEAR_OF_ERA))
        assertEquals(1L, date.getLong(ChronoField.MONTH_OF_YEAR))
        assertEquals(30L, date.getLong(ChronoField.DAY_OF_MONTH))
        assertThrows(UnsupportedTemporalTypeException::class.java) {
            date.getLong(ChronoField.DAY_OF_WEEK)
        }
    }
    
    @Test
    fun with() {
        val newYearDate = date.with(ChronoField.YEAR, 701)
        assertEquals("701-01-30", newYearDate.format())
    
        val newMonthDate = date.with(ChronoField.MONTH_OF_YEAR, 2)
        assertEquals("700-02-30", newMonthDate.format())
    
        val newDayDate = date.with(ChronoField.DAY_OF_MONTH, 15)
        assertEquals("700-01-15", newDayDate.format())
    
        assertThrows(UnsupportedTemporalTypeException::class.java) {
            date.with(ChronoField.DAY_OF_WEEK, 5)
        }
    }

    @Test
    fun withYear() {
        val newDate = date.withYear(701)
        assertEquals("701-01-30", newDate.format())
    }

    @Test
    fun withMonth() {
        val newDate = date.withMonth(HijrahMonth.SAFAR)
        assertEquals("700-02-30", newDate.format())
    }

    @Test
    fun withDayOfMonth() {
        val newDate = date.withDayOfMonth(15)
        assertEquals("700-01-15", newDate.format())
    }

    @Test
    fun toStringTest() {
        assertEquals("AH 700-01-30", date.toString())
    }

    @Test
    fun equalsTest() {
        val date2 = EarlyHijrahDate.of(700, 1, 30)
        assertEquals(date, date2)
    }

    @Test
    fun compareTo() {
        var date2 = EarlyHijrahDate.of(700, 1, 30)
        assertEquals(0, date.compareTo(date2))

        date2 = EarlyHijrahDate.of(700, 2, 1)
        assertTrue(date < date2)

        date2 = EarlyHijrahDate.of(700, 1, 1)
        assertTrue(date > date2)

    }

    @Test
    fun hashCodeTest() {
        val date2 = EarlyHijrahDate.of(700, 1, 30)
        assertEquals(date.hashCode(), date2.hashCode())
    }

}