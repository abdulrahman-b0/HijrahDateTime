package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.chrono.HijrahEra
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalQueries
import java.time.temporal.UnsupportedTemporalTypeException

class HijrahDateTimeTest {

    private lateinit var hijrahDateTime: HijrahDateTime

    @BeforeEach
    fun setUp() {
        hijrahDateTime = HijrahDateTime.of(1446, 2, 5, 12, 43, 18, 100)
    }

    @Nested
    @DisplayName("Factory Methods")
    inner class FactoryTests {

        @Test
        @DisplayName("HijrahDateTime.atZone returns ZonedHijrahDateTime properly")
        fun atZone() {
            val zoneId = ZoneOffset.of("+03:00")
            val zonedHijrahDateTime = hijrahDateTime.atZone(zoneId)

            assertEquals(hijrahDateTime, zonedHijrahDateTime.toHijrahDateTime())
        }

        @Test
        @DisplayName("HijrahDateTime with zone offset is obtained properly")
        fun currentZonedTimeObtainedProperly() {


            val hijrahDateTime =
                HijrahDateTime.now(ZoneOffset.of("+03:00")).truncatedTo(ChronoUnit.SECONDS)
            val utcHijrahDateTime =
                HijrahDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.SECONDS)

            assertEquals(hijrahDateTime.minus(3, ChronoUnit.HOURS), utcHijrahDateTime)
        }

        @Test
        @DisplayName("HijrahDateTime.of HijrahDate and LocalTime is obtained properly")
        fun ofHijrahDateAndLocalTime() {
            val hijrahDate = HijrahDate.of(1446, 2, 5)
            val localTime = LocalTime.of(12, 43, 18, 100)
            assertEquals(hijrahDateTime, HijrahDateTime.of(hijrahDate, localTime))
        }

        @Test
        @DisplayName("HijrahDateTime.of individual fields is obtained properly")
        fun ofYearMonthDayHourMinuteSecondNanoOfSecond() {
            assertEquals(hijrahDateTime, HijrahDateTime.of(1446, 2, 5, 12, 43, 18, 100))
        }

        @Test
        @DisplayName("HijrahDateTime.ofEpochSecond is obtained properly")
        fun ofEpochSecond() {
            val epochSecond = hijrahDateTime.toEpochSecond(ZoneOffset.UTC)
            assertEquals(
                hijrahDateTime,
                HijrahDateTime.ofEpochSecond(epochSecond, 100, ZoneOffset.UTC)
            )
        }

        @Test
        @DisplayName("HijrahDateTime.ofInstant is obtained properly")
        fun ofInstant() {
            val instant = hijrahDateTime.toInstant(ZoneOffset.UTC)
            assertEquals(hijrahDateTime, HijrahDateTime.ofInstant(instant, ZoneOffset.UTC))
        }

        @Test
        @DisplayName("Invalid day of month throws exception")
        fun invalidDayOfMonthThrowsException() {
            assertThrows<Exception> {
                HijrahDateTime.of(1446, 2, 31, 12, 43, 18, 100)
            }
        }

        @Test
        @DisplayName("Invalid month throws exception")
        fun invalidMonthThrowsException() {
            assertThrows<Exception> {
                HijrahDateTime.of(1446, 13, 5, 12, 43, 18, 100)
            }
        }

        @Test
        @DisplayName("Invalid or out of range throws exception")
        fun invalidOrOutOfRangeFactoryValuesThrows() {
            assertThrows<Exception> {
                HijrahDateTime.of(1446, 2, 5, 24, 43, 18, 100)
            }

            assertThrows<Exception> {
                HijrahDateTime.of(1446, 2, 5, 12, 60, 18, 100)
            }

            assertThrows<Exception> {
                HijrahDateTime.of(1446, 2, 5, 12, 43, 60, 100)
            }

            assertThrows<DateTimeException> {
                HijrahDateTime.of(1299, 2, 5, 12, 43, 18, 100)
            }

            assertThrows<DateTimeException> {
                HijrahDateTime.of(1601, 2, 5, 12, 43, 18, 100)
            }

        }

    }

    @Nested
    @DisplayName("Formatting and Parsing")
    inner class FormattingAndParsingTest {

        @Test
        @DisplayName("HijrahDateTime is formatted properly")
        fun hijrahDateTimeIsFormattedProperly() {
            val expected = "1446-02-05T12:43:18.0000001"
            var actual = hijrahDateTime.format(HijrahFormatters.HIJRAH_DATE_TIME)
            assertEquals(expected, actual)

            actual = hijrahDateTime.format()
            assertEquals(expected, actual)

            actual = hijrahDateTime.format(HijrahFormatters.HIJRAH_DATE)
            assertEquals("1446-02-05", actual)

            assertThrows<IllegalArgumentException> {
                hijrahDateTime.format(DateTimeFormatter.ISO_DATE_TIME)
            }

        }

        @Test
        @DisplayName("HijrahDateTime is parsed properly")
        fun hijrahDateTimeIsParsedProperly() {
            val text = "1446-02-05T12:43:18.0000001"
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
            assertEquals(5, hijrahDateTime.dayOfWeekValue)
            assertEquals(DayOfWeek.FRIDAY, hijrahDateTime.dayOfWeek)
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
            assertEquals(100, hijrahDateTime.nanoOfSecond)
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

            var newHijrahDateTime = hijrahDateTime.plusNanos(5)
            assertEquals(105, newHijrahDateTime.nanoOfSecond)

            newHijrahDateTime = hijrahDateTime.plusSeconds(5)
            assertEquals(23, newHijrahDateTime.secondOfMinute)

            newHijrahDateTime = hijrahDateTime.plus(Duration.ofMinutes(5))
            assertEquals(48, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = hijrahDateTime.plus(Duration.ofHours(1))
            assertEquals(13, newHijrahDateTime.hour)

            newHijrahDateTime = hijrahDateTime.plus(Duration.ofDays(1))
            assertEquals(6, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = hijrahDateTime.plusWeeks(1)
            assertEquals(12, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = hijrahDateTime.plus(1, ChronoUnit.MONTHS)
            assertEquals(3, newHijrahDateTime.monthValue)

            newHijrahDateTime = hijrahDateTime.plus(1, ChronoUnit.YEARS)
            assertEquals(1447, newHijrahDateTime.year)

            val combinedNewDateTime = hijrahDateTime
                .plusNanos(5)
                .plusSeconds(5)
                .plusMinutes(5)
                .plusHours(1)
                .plusDays(1)
                .plusMonths(1)
                .plusYears(1)

            val expectedDateTime = HijrahDateTime.of(1447, 3, 6, 13, 48, 23, 105)

            assertEquals(expectedDateTime, combinedNewDateTime)


        }

        @Test
        @DisplayName("HijrahDateTime.minus subtracts the specified amount properly")
        fun minus() {

            var newHijrahDateTime = hijrahDateTime.minusNanos(5)
            assertEquals(95, newHijrahDateTime.nanoOfSecond)

            newHijrahDateTime = hijrahDateTime.minusSeconds(5)
            assertEquals(13, newHijrahDateTime.secondOfMinute)

            newHijrahDateTime = hijrahDateTime.minus(Duration.ofMinutes(5))
            assertEquals(38, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = hijrahDateTime.minusHours(1)
            assertEquals(11, newHijrahDateTime.hour)

            newHijrahDateTime = hijrahDateTime.minusDays(1)
            assertEquals(4, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = hijrahDateTime.minusWeeks(1)
            if (newHijrahDateTime.with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth == 30)
                assertEquals(28, newHijrahDateTime.dayOfMonth)
            else
                assertEquals(27, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = hijrahDateTime.minus(1, ChronoUnit.MONTHS)
            assertEquals(1, newHijrahDateTime.monthValue)

            newHijrahDateTime = hijrahDateTime.minus(1, ChronoUnit.YEARS)
            assertEquals(1445, newHijrahDateTime.year)

            val combinedNewDateTime = hijrahDateTime
                .minusNanos(5)
                .minusSeconds(5)
                .minusMinutes(5)
                .minusHours(1)
                .minusDays(1)
                .minusMonths(1)
                .minusYears(1)

            val expectedDateTime = HijrahDateTime.of(1445, 1, 4, 11, 38, 13, 95)

            assertEquals(expectedDateTime, combinedNewDateTime)
        }

        @Test
        @DisplayName("HijrahDateTime.range returns the correct range for the specified field")
        fun range() {
            var range = hijrahDateTime.range(ChronoField.YEAR)
            assertEquals(1300, range.minimum)
            assertEquals(1600, range.maximum)

            range = hijrahDateTime.range(ChronoField.MONTH_OF_YEAR)
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
            var hijrahDateTime2 = HijrahDateTime.of(1446, 2, 5, 12, 43, 23, 100)
            var durationValue = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.SECONDS)
            assertEquals(5, durationValue)

            val duration = hijrahDateTime.until(hijrahDateTime2)
            assertEquals(duration, Duration.ofSeconds(durationValue))

            hijrahDateTime2 = HijrahDateTime.of(1446, 2, 5, 12, 44, 18, 100)
            durationValue = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.MINUTES)
            assertEquals(1, durationValue)

            hijrahDateTime2 = HijrahDateTime.of(1446, 2, 5, 13, 43, 18, 100)
            durationValue = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.HOURS)
            assertEquals(1, durationValue)


            hijrahDateTime2 = HijrahDateTime.of(1446, 2, 6, 12, 43, 18, 100)
            durationValue = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.DAYS)
            assertEquals(1, durationValue)

            hijrahDateTime2 = HijrahDateTime.of(1447, 2, 5, 12, 43, 18, 100)
            durationValue = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.MONTHS)
            assertEquals(12, durationValue)

            hijrahDateTime2 = HijrahDateTime.of(1447, 2, 5, 12, 43, 18, 100)
            durationValue = hijrahDateTime.until(hijrahDateTime2, ChronoUnit.YEARS)
            assertEquals(1, durationValue)

        }


    }


    @Nested
    @DisplayName("Support")
    inner class SupportTest {

        @Test
        @DisplayName("Required fields are supported")
        fun requiredFieldsAreSupported() {
            assertTrue(hijrahDateTime.isSupported(ChronoField.YEAR))
            assertTrue(hijrahDateTime.isSupported(ChronoField.MONTH_OF_YEAR))
            assertTrue(hijrahDateTime.isSupported(ChronoField.DAY_OF_YEAR))
            assertTrue(hijrahDateTime.isSupported(ChronoField.DAY_OF_MONTH))
            assertTrue(hijrahDateTime.isSupported(ChronoField.DAY_OF_WEEK))
            assertTrue(hijrahDateTime.isSupported(ChronoField.HOUR_OF_DAY))
            assertTrue(hijrahDateTime.isSupported(ChronoField.MINUTE_OF_HOUR))
            assertTrue(hijrahDateTime.isSupported(ChronoField.SECOND_OF_MINUTE))
            assertTrue(hijrahDateTime.isSupported(ChronoField.NANO_OF_SECOND))
            assertTrue(hijrahDateTime.isSupported(ChronoField.SECOND_OF_DAY))
            assertTrue(hijrahDateTime.isSupported(ChronoField.NANO_OF_DAY))
        }

        @Test
        @DisplayName("Required units are supported")
        fun requiredUnitsAreSupported() {
            assertTrue(hijrahDateTime.isSupported(ChronoUnit.NANOS))
            assertTrue(hijrahDateTime.isSupported(ChronoUnit.SECONDS))
            assertTrue(hijrahDateTime.isSupported(ChronoUnit.MINUTES))
            assertTrue(hijrahDateTime.isSupported(ChronoUnit.HOURS))
            assertTrue(hijrahDateTime.isSupported(ChronoUnit.DAYS))
            assertTrue(hijrahDateTime.isSupported(ChronoUnit.MONTHS))
            assertTrue(hijrahDateTime.isSupported(ChronoUnit.YEARS))
        }

    }

    @Nested
    @DisplayName("Comparison")
    inner class ComparisonTest {

        @Test
        @DisplayName("earlier HijrahDateTime is before later HijrahDateTime")
        fun earlierHijrahDateTimeIsBeforeLaterHijrahDateTime() {
            val laterHijrahDateTime = hijrahDateTime.plusNanos(1)
            assertTrue(hijrahDateTime.isBefore(laterHijrahDateTime))
            assertEquals(hijrahDateTime.compareTo(laterHijrahDateTime), -1)
        }

        @Test
        @DisplayName("later HijrahDateTime is after earlier HijrahDateTime")
        fun laterHijrahDateTimeIsAfterEarlierHijrahDateTime() {
            val earlierHijrahDateTime = hijrahDateTime.minusNanos(1)
            assertTrue(hijrahDateTime.isAfter(earlierHijrahDateTime))
            assertEquals(hijrahDateTime.compareTo(earlierHijrahDateTime), 1)
        }

        @Test
        @DisplayName("HijrahDateTime is equal to itself")
        fun hijrahDateTimeIsEqualToItself() {
            assertTrue(hijrahDateTime.isEqual(hijrahDateTime))
            assertEquals(hijrahDateTime.compareTo(hijrahDateTime), 0)
        }

        @Test
        @DisplayName("HijrahDateTime.equals returns true for equal HijrahDateTime instances")
        fun equalsReturnsTrueForEqualHijrahDateTimeInstances() {
            val hijrahDateTime2 = HijrahDateTime.of(1446, 2, 5, 12, 43, 18, 100)
            assertTrue(hijrahDateTime == hijrahDateTime2)

            val otherType = hijrahDateTime2.atZone(ZoneOffset.UTC)
            assertFalse(hijrahDateTime.equals(otherType))
        }
    }

    @Test
    @DisplayName("HijrahDateTime is truncated to the specified field properly")
    fun truncate() {

        var truncatedHijrahDateTime = hijrahDateTime.truncatedTo(ChronoUnit.SECONDS)
        assertEquals(HijrahDateTime.of(1446, 2, 5, 12, 43, 18), truncatedHijrahDateTime)

        truncatedHijrahDateTime = hijrahDateTime.truncatedTo(ChronoUnit.MINUTES)
        assertEquals(HijrahDateTime.of(1446, 2, 5, 12, 43, 0), truncatedHijrahDateTime)

        truncatedHijrahDateTime = hijrahDateTime.truncatedTo(ChronoUnit.DAYS)
        assertEquals(HijrahDateTime.of(1446, 2, 5, 0, 0, 0), truncatedHijrahDateTime)

    }

    @Nested
    @DisplayName("Adjustment")
    inner class AdjustmentsTest {

        @Test
        @DisplayName("Adjusting HijrahDateTime to the specified temporal properly")
        fun adjustInto() {
            var target = hijrahDateTime.withMonth(2).withDayOfMonth(1)
            var targetAfterAdjusted = hijrahDateTime.adjustInto(target)
            assertEquals(hijrahDateTime, targetAfterAdjusted)

            target = hijrahDateTime.withMonth(3).withDayOfMonth(1)
            targetAfterAdjusted = hijrahDateTime.adjustInto(target)
            assertEquals(hijrahDateTime, targetAfterAdjusted)

            target = hijrahDateTime.withMonth(2).withDayOfMonth(6)
            targetAfterAdjusted = hijrahDateTime.adjustInto(target)
            assertEquals(hijrahDateTime, targetAfterAdjusted)

            target = hijrahDateTime.withYear(1443).withMonth(8)
            targetAfterAdjusted = hijrahDateTime.adjustInto(target)
            assertEquals(hijrahDateTime, targetAfterAdjusted)

            assertThrows<UnsupportedTemporalTypeException> {
                hijrahDateTime.adjustInto(LocalDateTime.now())
            }
        }

        @Test
        @DisplayName("HijrahDateTime.with adjusts the specified field properly")
        fun with() {
            var newHijrahDateTime = hijrahDateTime.withNano(500)
            assertEquals(500, newHijrahDateTime.nanoOfSecond)

            newHijrahDateTime = hijrahDateTime.withSecond(30)
            assertEquals(30, newHijrahDateTime.secondOfMinute)

            newHijrahDateTime = hijrahDateTime.withMinute(55)
            assertEquals(55, newHijrahDateTime.minuteOfHour)

            newHijrahDateTime = hijrahDateTime.withHour(15)
            assertEquals(15, newHijrahDateTime.hour)

            newHijrahDateTime = hijrahDateTime.withDayOfMonth(10)
            assertEquals(10, newHijrahDateTime.dayOfMonth)

            newHijrahDateTime = hijrahDateTime.withDayOfYear(1)
            assertEquals(1, newHijrahDateTime.dayOfYear)

            newHijrahDateTime = hijrahDateTime.withMonth(3)
            assertEquals(3, newHijrahDateTime.monthValue)

            newHijrahDateTime = hijrahDateTime.withYear(1447)
            assertEquals(1447, newHijrahDateTime.year)
        }

    }


    @Nested
    @DisplayName("Constants")
    inner class ConstantsTest {

        @Test
        @DisplayName("Epoch datetime is 1389-10-22T00:00:00")
        fun epochDayIsCalculatedProperly() {
            val actual = HijrahDateTime.EPOCH
            val expectedEpochDatetime = HijrahDateTime.of(1389, 10, 22, 0, 0, 0)

            assertEquals(expectedEpochDatetime, actual)

            assertEquals(
                LocalDate.EPOCH.atStartOfDay().toInstant(ZoneOffset.UTC),
                actual.toInstant(ZoneOffset.UTC)
            )
        }

        @Test
        @DisplayName("HijrahDateTime.MIN is the minimum supported HijrahDateTime")
        fun minIsMinimumSupportedHijrahDateTime() {
            val min = HijrahDateTime.MIN

            assertEquals(1300, min.year)
        }

        @Test
        @DisplayName("HijrahDateTime.MAX is the maximum supported HijrahDateTime")
        fun maxIsMaximumSupportedHijrahDateTime() {
            val max = HijrahDateTime.MAX

            assertEquals(1600, max.year)
        }


    }

    @Test
    @DisplayName("HijrahDate is extracted properly")
    fun hijrahDateIsExtractedProperly() {
        val hijrahDate = hijrahDateTime.toHijrahDate()
        assertEquals(HijrahDate.of(1446, 2, 5), hijrahDate)
    }

    @Test
    @DisplayName("HijrahDateTime chronology is HijrahChronology")
    fun hijrahDateTimeChronologyIsHijrahChronology() {
        assertEquals(HijrahChronology.INSTANCE, hijrahDateTime.chronology)

        assertNotNull(hijrahDateTime.withYear(1443).chronology)
    }

    @Test
    @DisplayName("HijrahDateTime.toString returns the expected string")
    fun hijrahDateTimeToStringReturnsExpectedString() {
        val expected = "${hijrahDateTime.chronology.id} ${HijrahEra.AH} 1446-02-05T12:43:18.000000100"
        assertEquals(expected, hijrahDateTime.toString())
    }

    @Test
    @DisplayName("Hash code is calculated properly")
    fun hashCodeIsCalculatedProperly() {
        hijrahDateTime.hashCode()
    }

    @Test
    @DisplayName("Temporal queries work properly")
    fun temporalQueriesAreSupported() {
        assertEquals(HijrahChronology.INSTANCE, hijrahDateTime.query(TemporalQueries.chronology()))
        assertNull(hijrahDateTime.query(TemporalQueries.zoneId()))
        assertEquals(ChronoUnit.NANOS, hijrahDateTime.query(TemporalQueries.precision()))
        assertEquals(LocalDate.from(hijrahDateTime),hijrahDateTime.query(TemporalQueries.localDate()))
        assertEquals(hijrahDateTime.toLocalTime(), hijrahDateTime.query(TemporalQueries.localTime()))
        assertNull(hijrahDateTime.query(TemporalQueries.offset()))
        assertNull(hijrahDateTime.query(TemporalQueries.zone()))
    }


}