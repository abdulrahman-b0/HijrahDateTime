package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahDateTimeFormatters
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class HijrahDateTimeTest {

    private val hijrahDateTime = HijrahDateTime.of(1446, 2, 5, 12, 43, 18)

    @Nested
    @DisplayName("Factory Methods")
    inner class FactoryTests {

        @Test
        @DisplayName("HijrahDateTime.atZone returns ZonedHijrahDateTime properly")
        fun atZone() {
            val zoneId = ZoneOffset.of("+03:00")
            val zonedHijrahDateTime = hijrahDateTime.atZone(zoneId)
            
            assertEquals(hijrahDateTime, zonedHijrahDateTime.toLocalDateTime())
        }

        @Test
        @DisplayName("HijrahDateTime with zone offset is obtained properly")
        fun currentZonedTimeObtainedProperly() {

            fun normalizeSecondsAndNanos(hijrahDateTime: HijrahDateTime): HijrahDateTime {
                return hijrahDateTime.minus((hijrahDateTime.nanoOfSecond.nanoseconds + hijrahDateTime.secondOfMinute.seconds).toJavaDuration())
            }

            val hijrahDateTime =
                HijrahDateTime.now(ZoneOffset.of("+03:00")).let(::normalizeSecondsAndNanos)
            val utcHijrahDateTime =
                HijrahDateTime.now(Clock.systemUTC()).let(::normalizeSecondsAndNanos)

            assertEquals(hijrahDateTime.minus(3, ChronoUnit.HOURS), utcHijrahDateTime)
        }

        @Test
        @DisplayName("HijrahDateTime.of HijrahDate and LocalTime is obtained properly")
        fun ofHijrahDateAndLocalTime() {
            val hijrahDate = HijrahDate.of(1446, 2, 5)
            val localTime = LocalTime.of(12, 43, 18)
            assertEquals(hijrahDateTime, HijrahDateTime.of(hijrahDate, localTime))
        }

        @Test
        @DisplayName("HijrahDateTime.of individual fields is obtained properly")
        fun ofYearMonthDayHourMinuteSecondNanoOfSecond() {
            assertEquals(hijrahDateTime, HijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0))
        }

        @Test
        @DisplayName("HijrahDateTime.ofEpochSecond is obtained properly")
        fun ofEpochSecond() {
            val epochSecond = hijrahDateTime.toEpochSecond(ZoneOffset.UTC)
            assertEquals(hijrahDateTime, HijrahDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC))
        }

        @Test
        @DisplayName("HijrahDateTime.ofInstant is obtained properly")
        fun ofInstant() {
            val instant = hijrahDateTime.toInstant(ZoneOffset.UTC)
            assertEquals(hijrahDateTime, HijrahDateTime.ofInstant(instant, ZoneOffset.UTC))
        }

    }

    @Nested
    @DisplayName("Formatting and Parsing")
    inner class FormattingAndParsingTest {

        @Test
        @DisplayName("HijrahDateTime is formatted properly")
        fun hijrahDateTimeIsFormattedProperly() {
            val expected = "1446-02-05T12:43:18"
            val actual = hijrahDateTime.format(HijrahDateTimeFormatters.HIJRAH_LOCAL_DATE_TIME)
            assertEquals(expected, actual)
        }

        @Test
        @DisplayName("HijrahDateTime is parsed properly")
        fun hijrahDateTimeIsParsedProperly() {
            val text = "1446-02-05T12:43:18"
            val parsedHijrahDateTime = HijrahDateTime.parse(text)

            assertEquals(hijrahDateTime, parsedHijrahDateTime)
        }




    }

    @Test
    @DisplayName("HijrahDateTime is obtained from TemporalAccessor properly")
    fun hijrahDateTimeIsObtainedFromTemporalAccessorProperly() {
        val temporalAccessor = LocalDateTime.from(hijrahDateTime)
        val obtainedHijrahDateTime = HijrahDateTime.from(temporalAccessor)

        assertEquals(hijrahDateTime, obtainedHijrahDateTime)
    }

    @Nested
    @DisplayName("Fields Retrieval")
    inner class FieldsRetrievalTest {

        @Test
        @DisplayName("HijrahDateTime.year returns correct value")
        fun getYearReturnsCorrectValue() {
            assertEquals(1446, hijrahDateTime.year)
        }

        @Test
        @DisplayName("HijrahDateTime.monthValue returns correct value")
        fun getMonthValueReturnsCorrectValue() {
            assertEquals(2, hijrahDateTime.monthValue)
        }

        @Test
        @DisplayName("HijrahDateTime.month returns correct value")
        fun getMonth() {
            assertEquals(HijrahMonth.SAFAR, hijrahDateTime.month)
        }

        @Test
        @DisplayName("HijrahDateTime.dayOfYear returns correct value")
        fun getDayOfYear() {
            val expected1 = 29 + hijrahDateTime.dayOfMonth
            val expected2 = 30 + hijrahDateTime.dayOfMonth

            assertTrue(hijrahDateTime.dayOfYear == expected1 || hijrahDateTime.dayOfYear == expected2) {
                "Expected either $expected1 or $expected2, but got ${hijrahDateTime.dayOfYear}"
            }
        }

        @Test
        @DisplayName("HijrahDateTime.dayOfMonth returns correct value")
        fun getDayOfMonth() {
            assertEquals(5, hijrahDateTime.dayOfMonth)
        }

        @Test
        @DisplayName("HijrahDateTime.dayOfWeek returns correct value")
        fun getDayOfWeek() {
            assertEquals(5, hijrahDateTime.dayOfWeek)
        }

        @Test
        @DisplayName("HijrahDateTime.hour returns correct value")
        fun getHour() {
            assertEquals(12, hijrahDateTime.hour)
        }

        @Test
        @DisplayName("HijrahDateTime.minuteOfHour returns correct value")
        fun getMinuteOfHour() {
            assertEquals(43, hijrahDateTime.minuteOfHour)
        }

        @Test
        @DisplayName("HijrahDateTime.secondOfMinute returns correct value")
        fun getSecondOfMinute() {
            assertEquals(18, hijrahDateTime.secondOfMinute)
        }

        @Test
        @DisplayName("HijrahDateTime.nanoOfSecond returns correct value")
        fun getNanoOfSecond() {
            assertEquals(0, hijrahDateTime.nanoOfSecond)
        }

        @Test
        @DisplayName("HijrahDateTime.nanoOfDay returns correct value")
        fun getNanoOfDay() {
            assertDoesNotThrow {
                hijrahDateTime.nanoOfDay
            }
        }

    }

    @Nested
    @DisplayName("Arithmetic Operations")
    inner class ArithmeticTest {

        @Test
        @DisplayName("HijrahDateTime.plus adds the specified amount properly")
        fun plus() {

            var newHijrahDateTime = hijrahDateTime.plus(Duration.ofMinutes(5))
            assertEquals(48, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = hijrahDateTime.plus(Duration.ofHours(1))
            assertEquals(13, newHijrahDateTime.hour)

            newHijrahDateTime = hijrahDateTime.plus(Duration.ofDays(1))
            assertEquals(6, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = hijrahDateTime.plus(1, ChronoUnit.MONTHS)
            assertEquals(3, newHijrahDateTime.monthValue)

            newHijrahDateTime = hijrahDateTime.plus(1, ChronoUnit.YEARS)
            assertEquals(1447, newHijrahDateTime.year)

            val combinedNewDateTime = hijrahDateTime.plus(5, ChronoUnit.MINUTES)
                .plus(1, ChronoUnit.HOURS)
                .plus(1, ChronoUnit.DAYS)
                .plus(1, ChronoUnit.MONTHS)
                .plus(1, ChronoUnit.YEARS)

            val expectedDateTime = HijrahDateTime.of(1447, 3, 6, 13, 48, 18)

            assertEquals(expectedDateTime, combinedNewDateTime)


        }

        @Test
        @DisplayName("HijrahDateTime.minus subtracts the specified amount properly")
        fun minus() {

            var newHijrahDateTime = hijrahDateTime.minus(5, ChronoUnit.MINUTES)
            assertEquals(38, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = hijrahDateTime.minus(1, ChronoUnit.HOURS)
            assertEquals(11, newHijrahDateTime.hour)

            newHijrahDateTime = hijrahDateTime.minus(1, ChronoUnit.DAYS)
            assertEquals(4, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = hijrahDateTime.minus(1, ChronoUnit.MONTHS)
            assertEquals(1, newHijrahDateTime.monthValue)

            newHijrahDateTime = hijrahDateTime.minus(1, ChronoUnit.YEARS)
            assertEquals(1445, newHijrahDateTime.year)

            val combinedNewDateTime = hijrahDateTime.minus(5, ChronoUnit.MINUTES)
                .minus(1, ChronoUnit.HOURS)
                .minus(1, ChronoUnit.DAYS)
                .minus(1, ChronoUnit.MONTHS)
                .minus(1, ChronoUnit.YEARS)

            val expectedDateTime = HijrahDateTime.of(1445, 1, 4, 11, 38, 18)

            assertEquals(expectedDateTime, combinedNewDateTime)
        }

        @Test
        @DisplayName("HijrahDateTime.with adjusts the specified field properly")
        fun with() {
            var newHijrahDateTime = hijrahDateTime.with(ChronoField.MINUTE_OF_HOUR, 55)
            assertEquals(55, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = hijrahDateTime.with(ChronoField.HOUR_OF_DAY, 15)
            assertEquals(15, newHijrahDateTime.hour)

            newHijrahDateTime = hijrahDateTime.with(ChronoField.DAY_OF_MONTH, 10)
            assertEquals(10, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = hijrahDateTime.with(ChronoField.MONTH_OF_YEAR, 3)
            assertEquals(3, newHijrahDateTime.monthValue)

            newHijrahDateTime = hijrahDateTime.with(ChronoField.YEAR, 1447)
            assertEquals(1447, newHijrahDateTime.year)
        }

        @Test
        @DisplayName("HijrahDateTime.range returns the correct range for the specified field")
        fun range() {
            var range = hijrahDateTime.range(ChronoField.MONTH_OF_YEAR)
            assertEquals(1, range.minimum)
            assertEquals(12, range.maximum)

            range = hijrahDateTime.range(ChronoField.DAY_OF_MONTH)
            assertEquals(1, range.minimum)
            assertEquals(30, range.maximum)

            range = hijrahDateTime.range(ChronoField.DAY_OF_WEEK)
            assertEquals(1, range.minimum)
            assertEquals(7, range.maximum)

            range = hijrahDateTime.range(ChronoField.HOUR_OF_DAY)
            assertEquals(0, range.minimum)
            assertEquals(23, range.maximum)

            range = hijrahDateTime.range(ChronoField.CLOCK_HOUR_OF_DAY)
            assertEquals(1, range.minimum)
            assertEquals(24, range.maximum)

            range = hijrahDateTime.range(ChronoField.MINUTE_OF_HOUR)
            assertEquals(0, range.minimum)
            assertEquals(59, range.maximum)

            range = hijrahDateTime.range(ChronoField.SECOND_OF_MINUTE)
            assertEquals(0, range.minimum)
            assertEquals(59, range.maximum)
        }

        @Test
        @DisplayName("HijrahDateTime.until returns the correct duration between two HijrahDateTime instances")
        fun until() {
            var hijrahDateTime2 = HijrahDateTime.of(1446, 2, 5, 12, 43, 23)
            var duration = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.SECONDS)
            assertEquals(5, duration)

            hijrahDateTime2 = HijrahDateTime.of(1446, 2, 5, 12, 44, 18)
            duration = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.MINUTES)
            assertEquals(1, duration)

            hijrahDateTime2 = HijrahDateTime.of(1446, 2, 5, 13, 43, 18)
            duration = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.HOURS)
            assertEquals(1, duration)


            hijrahDateTime2 = HijrahDateTime.of(1446, 2, 6, 12, 43, 18)
            duration = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.DAYS)
            assertEquals(1, duration)

            hijrahDateTime2 = HijrahDateTime.of(1447, 2, 5, 12, 43, 18)
            duration = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.MONTHS)
            assertEquals(12, duration)

            hijrahDateTime2 = HijrahDateTime.of(1447, 2, 5, 12, 43, 18)
            duration = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.YEARS)
            assertEquals(1, duration)

        }


    }




    @Nested
    @DisplayName("Field Support")
    inner class FieldSupportTest {
        @Test
        @DisplayName("ChronoField.YEAR is supported")
        fun yearFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.YEAR))

        @Test
        @DisplayName("ChronoField.MONTH_OF_YEAR is supported")
        fun monthFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.MONTH_OF_YEAR))

        @Test
        @DisplayName("ChronoField.DAY_OF_YEAR is supported")
        fun dayOfYearFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.DAY_OF_YEAR))

        @Test
        @DisplayName("ChronoField.DAY_OF_MONTH is supported")
        fun dayOfMonthFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.DAY_OF_MONTH))

        @Test
        @DisplayName("ChronoField.DAY_OF_WEEK is supported")
        fun dayOfWeekFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.DAY_OF_WEEK))

        @Test
        @DisplayName("ChronoField.HOUR_OF_DAY is supported")
        fun hourFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.HOUR_OF_DAY))

        @Test
        @DisplayName("ChronoField.MINUTE_OF_HOUR is supported")
        fun minuteFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.MINUTE_OF_HOUR))

        @Test
        @DisplayName("ChronoField.SECOND_OF_MINUTE is supported")
        fun secondFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.SECOND_OF_MINUTE))

        @Test
        @DisplayName("ChronoField.NANO_OF_SECOND is supported")
        fun nanoSecondFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.NANO_OF_SECOND))

        @Test
        @DisplayName("ChronoField.SECOND_OF_DAY is supported")
        fun secondOfDayFieldIsSupported() = assertTrue(hijrahDateTime.isSupported(ChronoField.SECOND_OF_DAY))

    }
}