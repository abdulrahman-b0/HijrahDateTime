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
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

class ZonedHijrahDateTimeTest {

    private var zonedHijrahDateTime = ZonedHijrahDateTime.now() //Cannot use lateinit here, because its inline class, and null is not appropriate value for it

    @BeforeEach
    fun setUp() {
        zonedHijrahDateTime = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh"))
    }

    @Nested
    @DisplayName("Factory Methods")
    inner class FactoryTests {

        @Test
        @DisplayName("ZonedHijrahDateTime is obtained from TemporalAccessor properly")
        fun hijrahDateTimeIsObtainedFromTemporalAccessorProperly() {
            val temporalAccessor = ZonedDateTime.from(zonedHijrahDateTime)
            val obtainedHijrahDateTime = ZonedHijrahDateTime.from(temporalAccessor)

            assertTrue(zonedHijrahDateTime.isEqual(obtainedHijrahDateTime)) {
                "Expected: $zonedHijrahDateTime\nActual: $obtainedHijrahDateTime"
            }
        }
        

        @Test
        @DisplayName("ZonedHijrahDateTime.of HijrahDate and LocalTime is obtained properly")
        fun ofHijrahDateAndLocalTime() {
            val hijrahDate = HijrahDate.of(1446, 2, 5)
            val localTime = LocalTime.of(12, 43, 18, 0)

            val actual = ZonedHijrahDateTime.of(hijrahDate, localTime, ZoneId.of("Asia/Riyadh"))

            assertTrue(zonedHijrahDateTime.isEqual(actual)) {
                "Expected: $zonedHijrahDateTime\nActual: $actual"
            }
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.of individual fields is obtained properly")
        fun ofIndividualFields() {
            val actual = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh"))

            assertTrue(zonedHijrahDateTime.isEqual(actual)) {
                "Expected: $zonedHijrahDateTime\nActual: $actual"
            }
        }


        @Test
        @DisplayName("ZonedHijrahDateTime.ofInstant is obtained properly")
        fun ofInstant() {
            val instant = zonedHijrahDateTime.toInstant()

            val actual = ZonedHijrahDateTime.ofInstant(instant, ZoneId.of("Asia/Riyadh"))

            assertTrue(zonedHijrahDateTime.isEqual(actual)) {
                "Expected: $zonedHijrahDateTime\nActual: $actual"
            }
        }

    }

    @Nested
    @DisplayName("Formatting and Parsing")
    inner class FormattingAndParsingTest {

        @Test
        @DisplayName("ZonedHijrahDateTime is formatted properly")
        fun hijrahDateTimeIsFormattedProperly() {
            val expected = "1446-02-05T12:43:18+03:00[Asia/Riyadh]"
            val actual = zonedHijrahDateTime.format(HijrahFormatters.HIJRAH_ZONED_DATE_TIME)
            assertEquals(expected, actual)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime is parsed properly")
        fun hijrahDateTimeIsParsedProperly() {
            val text = "1446-02-05T12:43:18+03:00[Asia/Riyadh]"
            val parsedHijrahDateTime = ZonedHijrahDateTime.parse(text)

            assertTrue(zonedHijrahDateTime.isEqual(parsedHijrahDateTime)) {
                "Expected: $zonedHijrahDateTime\nActual: $parsedHijrahDateTime"
            }
        }


    }

    @Nested
    @DisplayName("Fields Retrieval")
    inner class FieldsRetrievalTest {

        @Test
        @DisplayName("ZonedHijrahDateTime.year returns correct value")
        fun getYearReturnsCorrectValue() {
            assertEquals(1446, zonedHijrahDateTime.year)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.monthValue returns correct value")
        fun getMonthValueReturnsCorrectValue() {
            assertEquals(2, zonedHijrahDateTime.monthValue)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.month returns correct value")
        fun getMonth() {
            assertEquals(HijrahMonth.SAFAR, zonedHijrahDateTime.month)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.dayOfYear returns correct value")
        fun getDayOfYear() {
            val expected1 = 29 + zonedHijrahDateTime.dayOfMonth
            val expected2 = 30 + zonedHijrahDateTime.dayOfMonth

            assertTrue(zonedHijrahDateTime.dayOfYear == expected1 || zonedHijrahDateTime.dayOfYear == expected2) {
                "Expected either $expected1 or $expected2, but got ${zonedHijrahDateTime.dayOfYear}"
            }
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.dayOfMonth returns correct value")
        fun getDayOfMonth() {
            assertEquals(5, zonedHijrahDateTime.dayOfMonth)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.dayOfWeek returns correct value")
        fun getDayOfWeek() {
            assertEquals(5, zonedHijrahDateTime.dayOfWeekValue)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.hour returns correct value")
        fun getHour() {
            assertEquals(12, zonedHijrahDateTime.hour)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.minuteOfHour returns correct value")
        fun getMinuteOfHour() {
            assertEquals(43, zonedHijrahDateTime.minuteOfHour)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.secondOfMinute returns correct value")
        fun getSecondOfMinute() {
            assertEquals(18, zonedHijrahDateTime.secondOfMinute)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.nanoOfSecond returns correct value")
        fun getNanoOfSecond() {
            assertEquals(0, zonedHijrahDateTime.nanoOfSecond)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.nanoOfDay returns correct value")
        fun getNanoOfDay() {
            assertDoesNotThrow {
                zonedHijrahDateTime.nanoOfDay
            }
        }

    }

    @Nested
    @DisplayName("Arithmetic Operations")
    inner class ArithmeticTest {

        @Test
        @DisplayName("ZonedHijrahDateTime.plus adds the specified amount properly")
        fun plus() {

            var newHijrahDateTime = zonedHijrahDateTime.plus(Duration.ofMinutes(5))
            assertEquals(48, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = zonedHijrahDateTime.plus(Duration.ofHours(1))
            assertEquals(13, newHijrahDateTime.hour)

            newHijrahDateTime = zonedHijrahDateTime.plus(Duration.ofDays(1))
            assertEquals(6, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = zonedHijrahDateTime.plus(1, ChronoUnit.MONTHS)
            assertEquals(3, newHijrahDateTime.monthValue)

            newHijrahDateTime = zonedHijrahDateTime.plus(1, ChronoUnit.YEARS)
            assertEquals(1447, newHijrahDateTime.year)

            val combinedNewDateTime = zonedHijrahDateTime.plus(5, ChronoUnit.MINUTES)
                .plus(1, ChronoUnit.HOURS)
                .plus(1, ChronoUnit.DAYS)
                .plus(1, ChronoUnit.MONTHS)
                .plus(1, ChronoUnit.YEARS)

            val expectedDateTime = ZonedHijrahDateTime.of(1447, 3, 6, 13, 48, 18, 0, ZoneId.of("Asia/Riyadh"))

            assertEquals(expectedDateTime, combinedNewDateTime)


        }

        @Test
        @DisplayName("ZonedHijrahDateTime.minus subtracts the specified amount properly")
        fun minus() {

            var newHijrahDateTime = zonedHijrahDateTime.minus(5, ChronoUnit.MINUTES)
            assertEquals(38, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = zonedHijrahDateTime.minus(1, ChronoUnit.HOURS)
            assertEquals(11, newHijrahDateTime.hour)

            newHijrahDateTime = zonedHijrahDateTime.minus(1, ChronoUnit.DAYS)
            assertEquals(4, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = zonedHijrahDateTime.minus(1, ChronoUnit.MONTHS)
            assertEquals(1, newHijrahDateTime.monthValue)

            newHijrahDateTime = zonedHijrahDateTime.minus(1, ChronoUnit.YEARS)
            assertEquals(1445, newHijrahDateTime.year)

            val combinedNewDateTime = zonedHijrahDateTime.minus(5, ChronoUnit.MINUTES)
                .minus(1, ChronoUnit.HOURS)
                .minus(1, ChronoUnit.DAYS)
                .minus(1, ChronoUnit.MONTHS)
                .minus(1, ChronoUnit.YEARS)

            val expectedDateTime = ZonedHijrahDateTime.of(1445, 1, 4, 11, 38, 18, 0, ZoneId.of("Asia/Riyadh"))

            assertEquals(expectedDateTime, combinedNewDateTime)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.with adjusts the specified field properly")
        fun with() {
            var newHijrahDateTime = zonedHijrahDateTime.with(ChronoField.MINUTE_OF_HOUR, 55)
            assertEquals(55, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = zonedHijrahDateTime.with(ChronoField.HOUR_OF_DAY, 15)
            assertEquals(15, newHijrahDateTime.hour)

            newHijrahDateTime = zonedHijrahDateTime.with(ChronoField.DAY_OF_MONTH, 10)
            assertEquals(10, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = zonedHijrahDateTime.with(ChronoField.MONTH_OF_YEAR, 3)
            assertEquals(3, newHijrahDateTime.monthValue)

            newHijrahDateTime = zonedHijrahDateTime.with(ChronoField.YEAR, 1447)
            assertEquals(1447, newHijrahDateTime.year)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.range returns the correct range for the specified field")
        fun range() {
            var range = zonedHijrahDateTime.range(ChronoField.MONTH_OF_YEAR)
            assertEquals(1, range.minimum)
            assertEquals(12, range.maximum)

            range = zonedHijrahDateTime.range(ChronoField.DAY_OF_MONTH)
            assertEquals(1, range.minimum)
            assertEquals(30, range.maximum)

            range = zonedHijrahDateTime.range(ChronoField.DAY_OF_WEEK)
            assertEquals(1, range.minimum)
            assertEquals(7, range.maximum)

            range = zonedHijrahDateTime.range(ChronoField.HOUR_OF_DAY)
            assertEquals(0, range.minimum)
            assertEquals(23, range.maximum)

            range = zonedHijrahDateTime.range(ChronoField.MINUTE_OF_HOUR)
            assertEquals(0, range.minimum)
            assertEquals(59, range.maximum)

            range = zonedHijrahDateTime.range(ChronoField.SECOND_OF_MINUTE)
            assertEquals(0, range.minimum)
            assertEquals(59, range.maximum)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.until returns the correct duration between two ZonedHijrahDateTime instances")
        fun until() {
            var hijrahDateTime2 = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 23, 0, ZoneId.of("Asia/Riyadh"))
            var duration = zonedHijrahDateTime.until(hijrahDateTime2, ChronoUnit.SECONDS)
            assertEquals(5, duration)

            hijrahDateTime2 = ZonedHijrahDateTime.of(1446, 2, 5, 12, 44, 18, 0, ZoneId.of("Asia/Riyadh"))
            duration = zonedHijrahDateTime.until(hijrahDateTime2, ChronoUnit.MINUTES)
            assertEquals(1, duration)

            hijrahDateTime2 = ZonedHijrahDateTime.of(1446, 2, 5, 13, 43, 18, 0, ZoneId.of("Asia/Riyadh"))
            duration = zonedHijrahDateTime.until(hijrahDateTime2, ChronoUnit.HOURS)
            assertEquals(1, duration)


            hijrahDateTime2 = ZonedHijrahDateTime.of(1446, 2, 6, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh"))
            duration = zonedHijrahDateTime.until(hijrahDateTime2, ChronoUnit.DAYS)
            assertEquals(1, duration)

            hijrahDateTime2 = ZonedHijrahDateTime.of(1447, 2, 5, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh"))
            duration = zonedHijrahDateTime.until(hijrahDateTime2, ChronoUnit.MONTHS)
            assertEquals(12, duration)

            hijrahDateTime2 = ZonedHijrahDateTime.of(1447, 2, 5, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh"))
            duration = zonedHijrahDateTime.until(hijrahDateTime2, ChronoUnit.YEARS)
            assertEquals(1, duration)

        }


    }




    @Nested
    @DisplayName("Field Support")
    inner class FieldSupportTest {
        @Test
        @DisplayName("ChronoField.YEAR is supported")
        fun yearFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.YEAR))

        @Test
        @DisplayName("ChronoField.MONTH_OF_YEAR is supported")
        fun monthFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.MONTH_OF_YEAR))

        @Test
        @DisplayName("ChronoField.DAY_OF_YEAR is supported")
        fun dayOfYearFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.DAY_OF_YEAR))

        @Test
        @DisplayName("ChronoField.DAY_OF_MONTH is supported")
        fun dayOfMonthFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.DAY_OF_MONTH))

        @Test
        @DisplayName("ChronoField.DAY_OF_WEEK is supported")
        fun dayOfWeekFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.DAY_OF_WEEK))

        @Test
        @DisplayName("ChronoField.HOUR_OF_DAY is supported")
        fun hourFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.HOUR_OF_DAY))

        @Test
        @DisplayName("ChronoField.MINUTE_OF_HOUR is supported")
        fun minuteFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.MINUTE_OF_HOUR))

        @Test
        @DisplayName("ChronoField.SECOND_OF_MINUTE is supported")
        fun secondFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.SECOND_OF_MINUTE))

        @Test
        @DisplayName("ChronoField.NANO_OF_SECOND is supported")
        fun nanoSecondFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.NANO_OF_SECOND))

        @Test
        @DisplayName("ChronoField.SECOND_OF_DAY is supported")
        fun secondOfDayFieldIsSupported() = assertTrue(zonedHijrahDateTime.isSupported(ChronoField.SECOND_OF_DAY))

    }


    @Nested
    @DisplayName("Zone Conversion")
    inner class ZoneConversionTest {

        @Test
        @DisplayName("ZonedHijrahDateTime.withZoneSameInstant converts the zone properly")
        fun withZoneSameInstant() {
            val newZone = ZoneId.of("Asia/Shanghai")
            val newZonedHijrahDateTime = zonedHijrahDateTime.withZoneSameInstant(newZone)

            val expected = ZonedHijrahDateTime.of(1446, 2, 5, 17, 43, 18, 0, newZone)

            assertEquals(expected, newZonedHijrahDateTime)
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.withZoneSameLocal converts the zone properly")
        fun withZoneSameLocal() {
            val newZone = ZoneId.of("Asia/Shanghai")
            val newZonedHijrahDateTime = zonedHijrahDateTime.withZoneSameLocal(newZone)

            val expected = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, newZone)

            assertTrue(expected.isEqual(newZonedHijrahDateTime)) {
                "Expected: $expected\nActual: $newZonedHijrahDateTime"
            }
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.withEarlierOffsetAtOverlap returns the same instance")
        fun withEarlierOffsetAtOverlap() {
            val newZonedHijrahDateTime = zonedHijrahDateTime.withEarlierOffsetAtOverlap()

            assertTrue(zonedHijrahDateTime.isEqual(newZonedHijrahDateTime) || zonedHijrahDateTime.isAfter(newZonedHijrahDateTime)) {
                "Expected: $zonedHijrahDateTime \nActual: $newZonedHijrahDateTime"
            }
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.withLaterOffsetAtOverlap returns correct instance")
        fun withLaterOffsetAtOverlap() {
            val newZonedHijrahDateTime = zonedHijrahDateTime.withLaterOffsetAtOverlap()

            assertTrue(zonedHijrahDateTime.isEqual(newZonedHijrahDateTime) || zonedHijrahDateTime.isBefore(newZonedHijrahDateTime)) {
                "Expected: $zonedHijrahDateTime\nActual: $newZonedHijrahDateTime"
            }
        }

        @Test
        @DisplayName("ZonedHijrahDateTime.toHijrahDateTime returns the correct HijrahDateTime instance")
        fun toLocalDateTime() {
            val hijrahDateTime = zonedHijrahDateTime.toHijrahDateTime()

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
        @DisplayName("Earlier instance is before the later one")
        fun earlierInstanceIsBeforeTheLaterOne() {
            val hijrahDateTime2 = zonedHijrahDateTime.plusNanos(1)

            assertTrue(zonedHijrahDateTime.isBefore(hijrahDateTime2))
        }

        @Test
        @DisplayName("Later instance is after the earlier one")
        fun laterInstanceIsAfterTheEarlierOne() {
            val hijrahDateTime2 = zonedHijrahDateTime.minusNanos(1)

            assertTrue(zonedHijrahDateTime.isAfter(hijrahDateTime2))
        }

        @Test
        @DisplayName("Same instances are actually equal")
        fun instancesAreEqual() {
            val hijrahDateTime2 = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh"))

            assertTrue(zonedHijrahDateTime.isEqual(hijrahDateTime2))
        }

        @Test
        @DisplayName("Earlier instance with different zone is before the later one")
        fun earlierInstanceWithDifferentZoneIsBeforeTheLaterOne() {
            val hijrahDateTime2 = zonedHijrahDateTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai")).plusNanos(1)
            assertTrue(zonedHijrahDateTime.isBefore(hijrahDateTime2))
        }

        @Test
        @DisplayName("Later instance with different zone is after the earlier one")
        fun laterInstanceWithDifferentZoneIsAfterTheEarlierOne() {
            val hijrahDateTime2 = zonedHijrahDateTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai")).minusNanos(1)

            assertTrue(zonedHijrahDateTime.isAfter(hijrahDateTime2))
        }

        @Test
        @DisplayName("Same instances with different zones are actually equal")
        fun equalInstancesWithDifferentZoneAreActuallyEqual() {
            val hijrahDateTime2 = zonedHijrahDateTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))

            assertTrue(zonedHijrahDateTime.isEqual(hijrahDateTime2))
        }

    }

    @Nested
    @DisplayName("Invalid Values")
    inner class InvalidValuesTest {

        @Test
        @DisplayName("Invalid zone ID throws DateTimeException")
        fun invalidZoneIdThrowsDateTimeException() {
            assertThrows<DateTimeException> {
                ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneId.of("Invalid/Zone"))
            }
        }

    }


}