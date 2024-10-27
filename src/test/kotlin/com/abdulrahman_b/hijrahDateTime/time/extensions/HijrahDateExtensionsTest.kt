package com.abdulrahman_b.hijrahDateTime.time.extensions

import com.abdulrahman_b.hijrahDateTime.time.HijrahMonth
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.datesUntil
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.dayOfMonth
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.dayOfWeek
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.dayOfYear
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.minusDays
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.month
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.monthValue
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.plusDays
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.toInstant
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.year
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoUnit

class HijrahDateExtensionsTest {

    private val hijrahDate = HijrahDate.of(1446, 2, 5)

    @Nested
    @DisplayName("Fields Retrieval")
    inner class FieldsRetrievalTest {
        @Test
        @DisplayName("Year is retrieved properly")
        fun year() {
            assertEquals(1446, hijrahDate.year)
        }

        @Test
        @DisplayName("Month is retrieved properly")
        fun month() {
            assertEquals(HijrahMonth.SAFAR, hijrahDate.month)
        }

        @Test
        @DisplayName("Month value is retrieved properly")
        fun monthValue() {
            assertEquals(2, hijrahDate.monthValue)
        }

        @Test
        @DisplayName("Day of year is retrieved properly")
        fun dayOfYear() {
            val expected1 = 29 + hijrahDate.dayOfMonth
            val expected2 = 30 + hijrahDate.dayOfMonth

            assertTrue(hijrahDate.dayOfYear == expected1 || hijrahDate.dayOfYear == expected2) {
                "Expected $expected1 or $expected2 but got ${hijrahDate.dayOfYear}"
            }
        }

        @Test
        @DisplayName("Day of month is retrieved properly")
        fun dayOfMonth() {
            assertEquals(5, hijrahDate.dayOfMonth)
        }

        @Test
        @DisplayName("Day of week is retrieved properly")
        fun dayOfWeek() {
            assertEquals(5, hijrahDate.dayOfWeek)
        }
    }

    @Nested
    @DisplayName("HijrahDateFactory")
    inner class HijrahDateFactoryTest {
        @Test
        @DisplayName("HijrahDate is obtained from epoch day properly")
        fun ofEpochDay() {
            val epochDay = hijrahDate.toEpochDay()
            val obtainedHijrahDate = HijrahDates.ofEpochDay(epochDay)

            assertEquals(hijrahDate, obtainedHijrahDate)
        }

        @Test
        @DisplayName("HijrahDate is obtained from year and day of year properly")
        fun ofYearDay() {
            val year = hijrahDate.year
            val dayOfYear = hijrahDate.dayOfYear
            val obtainedHijrahDate = HijrahDates.ofYearDay(year, dayOfYear)

            assertEquals(hijrahDate, obtainedHijrahDate)
        }

        @Test
        @DisplayName("HijrahDate is obtained from Instant properly")
        fun ofInstant() {
            val instant = hijrahDate.atTime(LocalTime.of(0, 0,)).toInstant(ZoneOffset.UTC)

            var obtainedHijrahDate = HijrahDates.ofInstant(instant, ZoneOffset.UTC)
            assertEquals(hijrahDate, obtainedHijrahDate)

            obtainedHijrahDate = HijrahDates.ofInstant(instant, ZoneOffset.of("-03:00"))
            assertEquals(hijrahDate.minus(1, ChronoUnit.DAYS), obtainedHijrahDate)

        }

        @Test
        @DisplayName("HijrahDate is parsed properly")
        fun parse() {
            val text = "1446-02-05"
            val parsedHijrahDate = HijrahDates.parse(text)

            assertEquals(hijrahDate, parsedHijrahDate)
        }
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    inner class ArithmeticOperationsTest {
        @Test
        @DisplayName("HijrahDate is added properly")
        fun plus() {
            val expectedHijrahDate = hijrahDate.plusDays(1)
            val obtainedHijrahDate = HijrahDate.of(1446, 2, 6)

            assertEquals(expectedHijrahDate, obtainedHijrahDate)
        }

        @Test
        @DisplayName("HijrahDate is subtracted properly")
        fun minus() {
            val expectedHijrahDate = hijrahDate.minusDays(1)
            val obtainedHijrahDate = HijrahDate.of(1446, 2, 4)

            assertEquals(expectedHijrahDate, obtainedHijrahDate)
        }

        @Test
        @DisplayName("Difference between two HijrahDates is calculated properly")
        fun until() {
            val otherHijrahDate = hijrahDate.plus(1, ChronoUnit.DAYS)
            val expected = 1L
            val obtained = hijrahDate.until(otherHijrahDate, ChronoUnit.DAYS)

            assertEquals(expected, obtained)
        }

        @Test
        @DisplayName("Difference between two HijrahDates is calculated properly")
        fun until2() {
            val otherHijrahDate = hijrahDate.plus(1, ChronoUnit.DAYS)
            val expected = 1L
            val obtained = hijrahDate.until(otherHijrahDate, ChronoUnit.DAYS)

            assertEquals(expected, obtained)
        }

        @Test
        @DisplayName("Dates sequence from HijrahDate to another HijrahDate is generated properly")
        fun datesUntilWithoutStep() {
            val otherHijrahDate = hijrahDate.plus(3, ChronoUnit.DAYS)
            val expected = listOf(hijrahDate, hijrahDate.plusDays(1), hijrahDate.plusDays(2))
            val obtained = hijrahDate.datesUntil(otherHijrahDate).toList()

            assertEquals(expected, obtained.toList())
        }

        @Test
        @DisplayName("Stepped dates sequence from date to another is generated properly")
        fun datesUntilWithStep() {
            val otherHijrahDate = hijrahDate.plus(3, ChronoUnit.DAYS)
            val expected = listOf(hijrahDate, hijrahDate.plusDays(2))
            val obtained = hijrahDate.datesUntil(otherHijrahDate, 2).toList()

            assertEquals(expected, obtained.toList())
        }
    }

    @Test
    @DisplayName("HijrahDate is converted to Instant properly")
    fun toInstant() {
        val instant = hijrahDate.toInstant(ZoneOffset.UTC)
        val obtainedHijrahDate = HijrahDates.ofInstant(instant, ZoneOffset.UTC)

        assertEquals(hijrahDate, obtainedHijrahDate)
    }


}