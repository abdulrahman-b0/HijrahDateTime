package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.dayOfMonth
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.monthValue
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.year
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.chrono.HijrahEra
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.IsoFields
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalQueries
import java.time.temporal.UnsupportedTemporalTypeException

class OffsetHijrahDateTimeTest {

    private lateinit var offsetHijrahDateTime: OffsetHijrahDateTime

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


        @Test
        @DisplayName("Invalid zone offset throws exception")
        fun invalidZoneOffsetThrowsException() {
            assertThrows<DateTimeException> {
                OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.of("03:00"))
            }
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
        @DisplayName("fields are retrieved properly")
        fun get() {
            assertEquals(1446, offsetHijrahDateTime.get(ChronoField.YEAR))
            assertEquals(2, offsetHijrahDateTime.get(ChronoField.MONTH_OF_YEAR))
            assertEquals(5, offsetHijrahDateTime.get(ChronoField.DAY_OF_MONTH))

            val expected1 = offsetHijrahDateTime.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth + offsetHijrahDateTime.dayOfMonth
            assertEquals(expected1, offsetHijrahDateTime.get(ChronoField.DAY_OF_YEAR))

            assertEquals(5, offsetHijrahDateTime.get(ChronoField.DAY_OF_WEEK))
            assertEquals(DayOfWeek.FRIDAY, DayOfWeek.of(offsetHijrahDateTime.dayOfWeekValue))
            assertEquals(12, offsetHijrahDateTime.get(ChronoField.HOUR_OF_DAY))
            assertEquals(43, offsetHijrahDateTime.get(ChronoField.MINUTE_OF_HOUR))
            assertEquals(18, offsetHijrahDateTime.get(ChronoField.SECOND_OF_MINUTE))
            assertEquals(0, offsetHijrahDateTime.get(ChronoField.NANO_OF_SECOND))
            assertEquals(offsetHijrahDateTime.offset.totalSeconds, offsetHijrahDateTime.get(ChronoField.OFFSET_SECONDS))
            assertThrows<UnsupportedTemporalTypeException> {
                offsetHijrahDateTime.get(ChronoField.INSTANT_SECONDS)
            }
        }

        @Test
        @DisplayName("fields are retrieved properly as Long")
        fun getLong() {
            assertEquals(1446L, offsetHijrahDateTime.getLong(ChronoField.YEAR))
            assertEquals(2L, offsetHijrahDateTime.getLong(ChronoField.MONTH_OF_YEAR))
            assertEquals(5L, offsetHijrahDateTime.getLong(ChronoField.DAY_OF_MONTH))
            assertEquals(5L, offsetHijrahDateTime.getLong(ChronoField.DAY_OF_WEEK))
            assertEquals(12L, offsetHijrahDateTime.getLong(ChronoField.HOUR_OF_DAY))
            assertEquals(43L, offsetHijrahDateTime.getLong(ChronoField.MINUTE_OF_HOUR))
            assertEquals(18L, offsetHijrahDateTime.getLong(ChronoField.SECOND_OF_MINUTE))
            assertEquals(0L, offsetHijrahDateTime.getLong(ChronoField.NANO_OF_SECOND))
            assertEquals(offsetHijrahDateTime.offset.totalSeconds.toLong(), offsetHijrahDateTime.getLong(ChronoField.OFFSET_SECONDS))
            assertThrows<UnsupportedTemporalTypeException> {
                offsetHijrahDateTime.getLong(ChronoField.INSTANT_SECONDS)
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

            val combinedNewDateTime = offsetHijrahDateTime.minusMinutes(5)
                .minusHours(1)
                .minusDays(1)
                .minusMonths(1)
                .minusYears(1)

            val expectedDateTime = OffsetHijrahDateTime.of(1445, 1, 4, 11, 38, 18, 0, ZoneOffset.of("+03:00"))

            assertEquals(expectedDateTime, combinedNewDateTime)
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

            range = offsetHijrahDateTime.range(ChronoField.OFFSET_SECONDS)
            assertEquals(-64800, range.minimum)
            assertEquals(64800, range.maximum)

            assertThrows<UnsupportedTemporalTypeException> {
                offsetHijrahDateTime.range(IsoFields.QUARTER_OF_YEAR)
            }
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
    @DisplayName("Support")
    inner class SupportTest {

        @Test
        @DisplayName("Required fields are supported")
        fun requiredFieldsAreSupported() {
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.YEAR))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.MONTH_OF_YEAR))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.DAY_OF_YEAR))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.DAY_OF_MONTH))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.DAY_OF_WEEK))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.HOUR_OF_DAY))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.MINUTE_OF_HOUR))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.SECOND_OF_MINUTE))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.NANO_OF_SECOND))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.SECOND_OF_DAY))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.NANO_OF_DAY))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoField.OFFSET_SECONDS))
            assertFalse(offsetHijrahDateTime.isSupported(ChronoField.INSTANT_SECONDS))
        }

        @Test
        @DisplayName("Required units are supported")
        fun requiredUnitsAreSupported() {
            assertTrue(offsetHijrahDateTime.isSupported(ChronoUnit.NANOS))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoUnit.SECONDS))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoUnit.MINUTES))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoUnit.HOURS))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoUnit.DAYS))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoUnit.MONTHS))
            assertTrue(offsetHijrahDateTime.isSupported(ChronoUnit.YEARS))
        }

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
            assertEquals(1, offsetHijrahDateTime.compareTo(hijrahDateTime2))
        }

        @Test
        @DisplayName("Earlier instance is before the later instance")
        fun isBefore() {
            val hijrahDateTime2 = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 19, 0, ZoneOffset.of("+03:00"))
            assertTrue(offsetHijrahDateTime.isBefore(hijrahDateTime2))
            assertEquals(-1, offsetHijrahDateTime.compareTo(hijrahDateTime2))
        }

        @Test
        @DisplayName("Equal instances are equal")
        fun isEqual() {
            val hijrahDateTime2 = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.of("+03:00"))
            assertTrue(offsetHijrahDateTime.isEqual(hijrahDateTime2))
            assertEquals(0, offsetHijrahDateTime.compareTo(hijrahDateTime2))
        }

        @Test
        @DisplayName("Earlier instance with different offset is before the later instance")
        fun isBeforeWithDifferentOffset() {
            val hijrahDateTime2 = offsetHijrahDateTime.withOffsetSameInstant(ZoneOffset.of("+04:00")).plusNanos(1)
            assertTrue(offsetHijrahDateTime.isBefore(hijrahDateTime2))
            assertEquals(-1, offsetHijrahDateTime.compareTo(hijrahDateTime2))
        }

        @Test
        @DisplayName("Later instance with different offset is after the earlier instance")
        fun isAfterWithDifferentOffset() {
            val hijrahDateTime2 = offsetHijrahDateTime.withOffsetSameInstant(ZoneOffset.of("+02:00")).minusNanos(1)
            assertTrue(offsetHijrahDateTime.isAfter(hijrahDateTime2))
            assertEquals(1, offsetHijrahDateTime.compareTo(hijrahDateTime2))
        }

        @Test
        @DisplayName("Equal instances with different offset are equal")
        fun isEqualWithDifferentOffset() {
            val hijrahDateTime2 = offsetHijrahDateTime.withOffsetSameInstant(ZoneOffset.of("+02:00"))
            assertTrue(offsetHijrahDateTime.isEqual(hijrahDateTime2))
            assertEquals(1, offsetHijrahDateTime.compareTo(hijrahDateTime2))
        }
    }

    @Nested
    @DisplayName("Adjustment")
    inner class AdjustmentTest {

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
        @DisplayName("Adjusting HijrahDateTime to the specified temporal properly")
        fun adjustInto() {
            var target = offsetHijrahDateTime.withMonth(2).withDayOfMonth(1)
            var targetAfterAdjusted = offsetHijrahDateTime.adjustInto(target)
            assertEquals(offsetHijrahDateTime, targetAfterAdjusted)

            target = offsetHijrahDateTime.withMonth(3).withDayOfMonth(1)
            targetAfterAdjusted = offsetHijrahDateTime.adjustInto(target)
            assertEquals(offsetHijrahDateTime, targetAfterAdjusted)

            target = offsetHijrahDateTime.withMonth(2).withDayOfMonth(6)
            targetAfterAdjusted = offsetHijrahDateTime.adjustInto(target)
            assertEquals(offsetHijrahDateTime, targetAfterAdjusted)

            target = offsetHijrahDateTime.withYear(1443).withMonth(8)
            targetAfterAdjusted = offsetHijrahDateTime.adjustInto(target)
            assertEquals(offsetHijrahDateTime, targetAfterAdjusted)

            target = offsetHijrahDateTime.withOffsetSameLocal(ZoneOffset.of("+02:00"))
            targetAfterAdjusted = offsetHijrahDateTime.adjustInto(target)
            assertEquals(offsetHijrahDateTime, targetAfterAdjusted)

            target = offsetHijrahDateTime.withOffsetSameInstant(ZoneOffset.of("+02:00"))
            targetAfterAdjusted = offsetHijrahDateTime.adjustInto(target)
            assertEquals(offsetHijrahDateTime, targetAfterAdjusted)
        }

    }

    @Test
    @DisplayName("truncate to the specified field properly")
    fun truncate() {

        var truncatedHijrahDateTime = offsetHijrahDateTime.truncatedTo(ChronoUnit.SECONDS)
        var expected = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.ofHours(3))
        assertEquals(expected, truncatedHijrahDateTime)

        truncatedHijrahDateTime = offsetHijrahDateTime.truncatedTo(ChronoUnit.MINUTES)
        expected = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 0, 0, ZoneOffset.ofHours(3))
        assertEquals(expected, truncatedHijrahDateTime)

        truncatedHijrahDateTime = offsetHijrahDateTime.truncatedTo(ChronoUnit.DAYS)
        expected = OffsetHijrahDateTime.of(1446, 2, 5, 0, 0, 0, 0, ZoneOffset.ofHours(3))
        assertEquals(expected, truncatedHijrahDateTime)

    }

    @Test
    @DisplayName("Temporal queries work properly")
    fun temporalQueriesAreSupported() {
        assertEquals(HijrahChronology.INSTANCE, offsetHijrahDateTime.query(TemporalQueries.chronology()))
        assertNull(offsetHijrahDateTime.query(TemporalQueries.zoneId()))
        assertEquals(ChronoUnit.NANOS, offsetHijrahDateTime.query(TemporalQueries.precision()))
        assertEquals(LocalDate.from(offsetHijrahDateTime),offsetHijrahDateTime.query(TemporalQueries.localDate()))
        assertEquals(offsetHijrahDateTime.toLocalTime(), offsetHijrahDateTime.query(TemporalQueries.localTime()))
        assertEquals(offsetHijrahDateTime.offset ,offsetHijrahDateTime.query(TemporalQueries.offset()))
        assertEquals(offsetHijrahDateTime.offset, offsetHijrahDateTime.query(TemporalQueries.zone()))
    }

    @Test
    @DisplayName("HijrahDateTime is extracted properly")
    fun hijrahDateTimeIsExtractedProperly() {
        val hijrahDateTime = offsetHijrahDateTime.dateTime
        assertEquals(1446, hijrahDateTime.year)
        assertEquals(2, hijrahDateTime.monthValue)
        assertEquals(5, hijrahDateTime.dayOfMonth)
        assertEquals(12, hijrahDateTime.hour)
        assertEquals(43, hijrahDateTime.minuteOfHour)
        assertEquals(18, hijrahDateTime.secondOfMinute)
        assertEquals(0, hijrahDateTime.nanoOfSecond)
    }

    @Test
    @DisplayName("HijrahDate is extracted properly")
    fun hijrahDateIsExtractedProperly() {
        val hijrahDate = offsetHijrahDateTime.toHijrahDate()
        assertEquals(HijrahDate.of(1446, 2, 5), hijrahDate)
    }

    @Test
    @DisplayName("LocalTime is extracted properly")
    fun localTimeIsExtractedProperly() {
        val localTime = offsetHijrahDateTime.toLocalTime()
        assertEquals(LocalTime.of(12, 43, 18, 0), localTime)
    }

    @Test
    @DisplayName("OffsetTime is extracted properly")
    fun offsetTimeIsExtractedProperly() {
        val offsetDateTime = offsetHijrahDateTime.toOffsetTime()
        assertEquals(OffsetTime.of(12, 43, 18, 0, ZoneOffset.of("+03:00")), offsetDateTime)
    }

    @Test
    @DisplayName("toString returns the correct string representation")
    fun toStringReturnsCorrectStringRepresentation() {
        val expected = "${offsetHijrahDateTime.chronology.id} ${HijrahEra.AH} 1446-02-05T12:43:18+03:00"
        assertEquals(expected, offsetHijrahDateTime.toString())
    }


    @Test
    @DisplayName("Hash code is calculated properly")
    fun hashCodeIsCalculatedProperly() {
        offsetHijrahDateTime.hashCode()
    }
}