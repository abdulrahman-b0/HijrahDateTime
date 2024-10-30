package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.DateTimeException
import java.time.Duration
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

class OffsetHijrahDateTimeTest {

    private var offsetHijrahDateTime = OffsetHijrahDateTime.now(ZoneOffset.of("+03:00")) //Cannot use lateinit here, because its inline class, and null is not appropriate value for it

    @BeforeEach
    fun setUp() {
        offsetHijrahDateTime = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.of("+03:00"))
    }

    @Nested
    @DisplayName("Factory Methods")
    inner class FactoryTests {

        @Test
        @DisplayName("OffsetHijrahDateTime is obtained from TemporalAccessor properly")
        fun hijrahDateTimeIsObtainedFromTemporalAccessorProperly() {
            val temporalAccessor = OffsetDateTime.from(offsetHijrahDateTime)
            val obtainedOffsetHijrahDateTime = OffsetHijrahDateTime.from(temporalAccessor)

            assertEquals(offsetHijrahDateTime, obtainedOffsetHijrahDateTime)
        }


        @Test
        @DisplayName("OffsetHijrahDateTime.of HijrahDate and LocalTime is obtained properly")
        fun ofHijrahDateAndLocalTime() {
            val hijrahDate = HijrahDate.of(1446, 2, 5)
            val localTime = LocalTime.of(12, 43, 18, 0)

            val actual = OffsetHijrahDateTime.of(hijrahDate, localTime, ZoneOffset.of("+03:00"))

            assertEquals(offsetHijrahDateTime, actual)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.of individual fields is obtained properly")
        fun ofIndividualFields() {
            val actual = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.of("+03:00"))

            assertEquals(offsetHijrahDateTime, actual)
        }


        @Test
        @DisplayName("OffsetHijrahDateTime.ofInstant is obtained properly")
        fun ofInstant() {
            val instant = offsetHijrahDateTime.toInstant()

            val actual = OffsetHijrahDateTime.ofInstant(instant, ZoneOffset.of("+03:00"))

            assertEquals(offsetHijrahDateTime, actual)
        }

    }

    @Nested
    @DisplayName("Formatting and Parsing")
    inner class FormattingAndParsingTest {

        @Test
        @DisplayName("OffsetHijrahDateTime is formatted properly")
        fun hijrahDateTimeIsFormattedProperly() {
            val expected = "1446-02-05T12:43:18+03:00"
            val actual = offsetHijrahDateTime.format(HijrahFormatters.HIJRAH_OFFSET_DATE_TIME)
            assertEquals(expected, actual)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime is parsed properly")
        fun hijrahDateTimeIsParsedProperly() {
            val text = "1446-02-05T12:43:18+03:00"
            val parsedHijrahDateTime = OffsetHijrahDateTime.parse(text, HijrahFormatters.HIJRAH_OFFSET_DATE_TIME)

            assertEquals(offsetHijrahDateTime, parsedHijrahDateTime)
        }


    }

    @Nested
    @DisplayName("Fields Retrieval")
    inner class FieldsRetrievalTest {

        @Test
        @DisplayName("OffsetHijrahDateTime.year returns correct value")
        fun getYearReturnsCorrectValue() {
            assertEquals(1446, offsetHijrahDateTime.year)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.monthValue returns correct value")
        fun getMonthValueReturnsCorrectValue() {
            assertEquals(2, offsetHijrahDateTime.monthValue)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.month returns correct value")
        fun getMonth() {
            assertEquals(HijrahMonth.SAFAR, offsetHijrahDateTime.month)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.dayOfYear returns correct value")
        fun getDayOfYear() {
            val expected1 = 29 + offsetHijrahDateTime.dayOfMonth
            val expected2 = 30 + offsetHijrahDateTime.dayOfMonth

            assertTrue(offsetHijrahDateTime.dayOfYear == expected1 || offsetHijrahDateTime.dayOfYear == expected2) {
                "Expected either $expected1 or $expected2, but got ${offsetHijrahDateTime.dayOfYear}"
            }
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.dayOfMonth returns correct value")
        fun getDayOfMonth() {
            assertEquals(5, offsetHijrahDateTime.dayOfMonth)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.dayOfWeek returns correct value")
        fun getDayOfWeek() {
            assertEquals(5, offsetHijrahDateTime.dayOfWeekValue)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.hour returns correct value")
        fun getHour() {
            assertEquals(12, offsetHijrahDateTime.hour)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.minuteOfHour returns correct value")
        fun getMinuteOfHour() {
            assertEquals(43, offsetHijrahDateTime.minuteOfHour)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.secondOfMinute returns correct value")
        fun getSecondOfMinute() {
            assertEquals(18, offsetHijrahDateTime.secondOfMinute)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.nanoOfSecond returns correct value")
        fun getNanoOfSecond() {
            assertEquals(0, offsetHijrahDateTime.nanoOfSecond)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.nanoOfDay returns correct value")
        fun getNanoOfDay() {
            assertDoesNotThrow {
                offsetHijrahDateTime.nanoOfDay
            }
        }

    }

    @Nested
    @DisplayName("Arithmetic Operations")
    inner class ArithmeticTest {

        @Test
        @DisplayName("OffsetHijrahDateTime.plus adds the specified amount properly")
        fun plus() {

            var newHijrahDateTime = offsetHijrahDateTime.plus(Duration.ofMinutes(5))
            assertEquals(48, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = offsetHijrahDateTime.plus(Duration.ofHours(1))
            assertEquals(13, newHijrahDateTime.hour)

            newHijrahDateTime = offsetHijrahDateTime.plus(Duration.ofDays(1))
            assertEquals(6, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = offsetHijrahDateTime.plus(1, ChronoUnit.MONTHS)
            assertEquals(3, newHijrahDateTime.monthValue)

            newHijrahDateTime = offsetHijrahDateTime.plus(1, ChronoUnit.YEARS)
            assertEquals(1447, newHijrahDateTime.year)

            val combinedNewDateTime = offsetHijrahDateTime.plus(5, ChronoUnit.MINUTES)
                .plus(1, ChronoUnit.HOURS)
                .plus(1, ChronoUnit.DAYS)
                .plus(1, ChronoUnit.MONTHS)
                .plus(1, ChronoUnit.YEARS)

            val expectedDateTime = OffsetHijrahDateTime.of(1447, 3, 6, 13, 48, 18, 0, ZoneOffset.of("+03:00"))

            assertEquals(expectedDateTime, combinedNewDateTime)


        }

        @Test
        @DisplayName("OffsetHijrahDateTime.minus subtracts the specified amount properly")
        fun minus() {

            var newHijrahDateTime = offsetHijrahDateTime.minus(5, ChronoUnit.MINUTES)
            assertEquals(38, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = offsetHijrahDateTime.minus(1, ChronoUnit.HOURS)
            assertEquals(11, newHijrahDateTime.hour)

            newHijrahDateTime = offsetHijrahDateTime.minus(1, ChronoUnit.DAYS)
            assertEquals(4, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = offsetHijrahDateTime.minus(1, ChronoUnit.MONTHS)
            assertEquals(1, newHijrahDateTime.monthValue)

            newHijrahDateTime = offsetHijrahDateTime.minus(1, ChronoUnit.YEARS)
            assertEquals(1445, newHijrahDateTime.year)

            val combinedNewDateTime = offsetHijrahDateTime.minus(5, ChronoUnit.MINUTES)
                .minus(1, ChronoUnit.HOURS)
                .minus(1, ChronoUnit.DAYS)
                .minus(1, ChronoUnit.MONTHS)
                .minus(1, ChronoUnit.YEARS)

            val expectedDateTime = OffsetHijrahDateTime.of(1445, 1, 4, 11, 38, 18, 0, ZoneOffset.of("+03:00"))

            assertEquals(expectedDateTime, combinedNewDateTime)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.with adjusts the specified field properly")
        fun with() {
            var newHijrahDateTime = offsetHijrahDateTime.with(ChronoField.MINUTE_OF_HOUR, 55)
            assertEquals(55, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = offsetHijrahDateTime.with(ChronoField.HOUR_OF_DAY, 15)
            assertEquals(15, newHijrahDateTime.hour)

            newHijrahDateTime = offsetHijrahDateTime.with(ChronoField.DAY_OF_MONTH, 10)
            assertEquals(10, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = offsetHijrahDateTime.with(ChronoField.MONTH_OF_YEAR, 3)
            assertEquals(3, newHijrahDateTime.monthValue)

            newHijrahDateTime = offsetHijrahDateTime.with(ChronoField.YEAR, 1447)
            assertEquals(1447, newHijrahDateTime.year)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.range returns the correct range for the specified field")
        fun range() {
            var range = offsetHijrahDateTime.range(ChronoField.MONTH_OF_YEAR)
            assertEquals(1, range.minimum)
            assertEquals(12, range.maximum)

            range = offsetHijrahDateTime.range(ChronoField.DAY_OF_MONTH)
            assertEquals(1, range.minimum)
            assertEquals(30, range.maximum)

            range = offsetHijrahDateTime.range(ChronoField.DAY_OF_WEEK)
            assertEquals(1, range.minimum)
            assertEquals(7, range.maximum)

            range = offsetHijrahDateTime.range(ChronoField.HOUR_OF_DAY)
            assertEquals(0, range.minimum)
            assertEquals(23, range.maximum)

            range = offsetHijrahDateTime.range(ChronoField.MINUTE_OF_HOUR)
            assertEquals(0, range.minimum)
            assertEquals(59, range.maximum)

            range = offsetHijrahDateTime.range(ChronoField.SECOND_OF_MINUTE)
            assertEquals(0, range.minimum)
            assertEquals(59, range.maximum)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.until returns the correct duration between two OffsetHijrahDateTime instances")
        fun until() {
            var hijrahDateTime2 = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 23, 0, ZoneOffset.of("+03:00"))
            var duration = offsetHijrahDateTime.until(hijrahDateTime2, ChronoUnit.SECONDS)
            assertEquals(5, duration)

            hijrahDateTime2 = OffsetHijrahDateTime.of(1446, 2, 5, 12, 44, 18, 0, ZoneOffset.of("+03:00"))
            duration = offsetHijrahDateTime.until(hijrahDateTime2, ChronoUnit.MINUTES)
            assertEquals(1, duration)

            hijrahDateTime2 = OffsetHijrahDateTime.of(1446, 2, 5, 13, 43, 18, 0, ZoneOffset.of("+03:00"))
            duration = offsetHijrahDateTime.until(hijrahDateTime2, ChronoUnit.HOURS)
            assertEquals(1, duration)


            hijrahDateTime2 = OffsetHijrahDateTime.of(1446, 2, 6, 12, 43, 18, 0, ZoneOffset.of("+03:00"))
            duration = offsetHijrahDateTime.until(hijrahDateTime2, ChronoUnit.DAYS)
            assertEquals(1, duration)

            hijrahDateTime2 = OffsetHijrahDateTime.of(1447, 2, 5, 12, 43, 18, 0, ZoneOffset.of("+03:00"))
            duration = offsetHijrahDateTime.until(hijrahDateTime2, ChronoUnit.MONTHS)
            assertEquals(12, duration)

            hijrahDateTime2 = OffsetHijrahDateTime.of(1447, 2, 5, 12, 43, 18, 0, ZoneOffset.of("+03:00"))
            duration = offsetHijrahDateTime.until(hijrahDateTime2, ChronoUnit.YEARS)
            assertEquals(1, duration)

        }


    }




    @Nested
    @DisplayName("Field Support")
    inner class FieldSupportTest {
        @Test
        @DisplayName("ChronoField.YEAR is supported")
        fun yearFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.YEAR))

        @Test
        @DisplayName("ChronoField.MONTH_OF_YEAR is supported")
        fun monthFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.MONTH_OF_YEAR))

        @Test
        @DisplayName("ChronoField.DAY_OF_YEAR is supported")
        fun dayOfYearFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.DAY_OF_YEAR))

        @Test
        @DisplayName("ChronoField.DAY_OF_MONTH is supported")
        fun dayOfMonthFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.DAY_OF_MONTH))

        @Test
        @DisplayName("ChronoField.DAY_OF_WEEK is supported")
        fun dayOfWeekFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.DAY_OF_WEEK))

        @Test
        @DisplayName("ChronoField.HOUR_OF_DAY is supported")
        fun hourFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.HOUR_OF_DAY))

        @Test
        @DisplayName("ChronoField.MINUTE_OF_HOUR is supported")
        fun minuteFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.MINUTE_OF_HOUR))

        @Test
        @DisplayName("ChronoField.SECOND_OF_MINUTE is supported")
        fun secondFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.SECOND_OF_MINUTE))

        @Test
        @DisplayName("ChronoField.NANO_OF_SECOND is supported")
        fun nanoSecondFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.NANO_OF_SECOND))

        @Test
        @DisplayName("ChronoField.SECOND_OF_DAY is supported")
        fun secondOfDayFieldIsSupported() = assertTrue(offsetHijrahDateTime.isSupported(ChronoField.SECOND_OF_DAY))

    }


    @Nested
    @DisplayName("Zone Conversion")
    inner class ZoneConversionTest {

        @Test
        @DisplayName("OffsetHijrahDateTime.withZoneSameInstant converts the zone properly")
        fun withZoneSameInstant() {
            val newZone = ZoneOffset.of("+08:00")
            val newZonedHijrahDateTime = offsetHijrahDateTime.withOffsetSameInstant(newZone)

            val expected = OffsetHijrahDateTime.of(1446, 2, 5, 17, 43, 18, 0, newZone)

            assertEquals(expected, newZonedHijrahDateTime)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.withZoneSameLocal converts the zone properly")
        fun withZoneSameLocal() {
            val newZone = ZoneOffset.of("+08:00")
            val newZonedHijrahDateTime = offsetHijrahDateTime.withOffsetSameLocal(newZone)

            val expected = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, newZone)

            assertEquals(expected, newZonedHijrahDateTime)
        }

        @Test
        @DisplayName("OffsetHijrahDateTime.toLocalDateTime returns the correct HijrahDateTime instance")
        fun toLocalDateTime() {
            val hijrahDateTime = offsetHijrahDateTime.dateTime

            val expected = HijrahDateTime.of(1446, 2, 5, 12, 43, 18)

            assertTrue(hijrahDateTime.isEqual(expected)) {
                "Expected: $expected\nActual: $hijrahDateTime"
            }
        }
    }

    @Nested
    @DisplayName("Comparison")
    inner class ComparisonTest {

        @Test
        @DisplayName("Later instance is after the earlier instance")
        fun isAfter() {
            val hijrahDateTime2 = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 17, 0, ZoneOffset.of("+03:00"))
            assertTrue(offsetHijrahDateTime.isAfter(hijrahDateTime2))
        }

        @Test
        @DisplayName("Earlier instance is before the later instance")
        fun isBefore() {
            val hijrahDateTime2 = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 19, 0, ZoneOffset.of("+03:00"))
            assertTrue(offsetHijrahDateTime.isBefore(hijrahDateTime2))
        }

        @Test
        @DisplayName("Equal instances are equal")
        fun isEqual() {
            val hijrahDateTime2 = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.of("+03:00"))
            assertTrue(offsetHijrahDateTime.isEqual(hijrahDateTime2))
        }

        @Test
        @DisplayName("Earlier instance with different offset is before the later instance")
        fun isBeforeWithDifferentOffset() {
            val hijrahDateTime2 = offsetHijrahDateTime.withOffsetSameInstant(ZoneOffset.of("+04:00")).plusNanos(1)
            assertTrue(offsetHijrahDateTime.isBefore(hijrahDateTime2))
        }

        @Test
        @DisplayName("Later instance with different offset is after the earlier instance")
        fun isAfterWithDifferentOffset() {
            val hijrahDateTime2 = offsetHijrahDateTime.withOffsetSameInstant(ZoneOffset.of("+02:00")).minusNanos(1)
            assertTrue(offsetHijrahDateTime.isAfter(hijrahDateTime2))
        }

        @Test
        @DisplayName("Equal instances with different offset are equal")
        fun isEqualWithDifferentOffset() {
            val hijrahDateTime2 = offsetHijrahDateTime.withOffsetSameInstant(ZoneOffset.of("+02:00"))
            assertTrue(offsetHijrahDateTime.isEqual(hijrahDateTime2))
        }
    }

    @Nested
    @DisplayName("Invalid Values")
    inner class InvalidValues {

        @Test
        @DisplayName("Invalid zone offset throws exception")
        fun invalidZoneOffsetThrowsException() {
            assertThrows<DateTimeException> {
                OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.of("03:00"))
            }
        }

    }
}