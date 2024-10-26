package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.time.HijrahDates.dayOfMonth
import com.abdulrahman_b.hijrahDateTime.time.HijrahDates.dayOfWeek
import com.abdulrahman_b.hijrahDateTime.time.HijrahDates.dayOfYear
import com.abdulrahman_b.hijrahDateTime.time.HijrahDates.month
import com.abdulrahman_b.hijrahDateTime.time.HijrahDates.monthValue
import com.abdulrahman_b.hijrahDateTime.time.HijrahDates.year
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoUnit

class HijrahDateExtensionsKtTest {

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


}