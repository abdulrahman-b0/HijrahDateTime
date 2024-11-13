package com.abdulrahman_b.hijrah_datetime

import com.abdulrahman_b.hijrah_datetime.extensions.HijrahDates.dayOfMonth
import com.abdulrahman_b.hijrah_datetime.extensions.HijrahDates.monthValue
import com.abdulrahman_b.hijrah_datetime.extensions.HijrahDates.year
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.threeten.extra.Days
import org.threeten.extra.OffsetDate
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
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

class OffsetHijrahDateTest {

    private lateinit var offsetHijrahDate: OffsetHijrahDate

    @BeforeEach
    fun setUp() {
        offsetHijrahDate = OffsetHijrahDate.of(1446, 2, 5, offset =  ZoneOffset.of("+03:00"))
    }

    @Nested
    @DisplayName("Factory Methods")
    inner class FactoryTests {

        @Test
        @DisplayName("Current time is obtained properly")
        fun currentTimeIsObtainedProperly() {
            var actual = OffsetHijrahDate.now()
            assertEquals(ZoneOffset.systemDefault().rules.getOffset(actual.toInstant()), actual.offset)

            actual = OffsetHijrahDate.now(ZoneOffset.ofHours(3))
            assertEquals(ZoneOffset.of("+03:00"), actual.offset)
        }

        @Test
        @DisplayName("OffsetHijrahDate is obtained from TemporalAccessor properly")
        fun hijrahDateTimeIsObtainedFromTemporalAccessorProperly() {
            val temporalAccessor = OffsetDate.from(offsetHijrahDate)
            val obtainedOffsetHijrahDateTime = OffsetHijrahDate.from(temporalAccessor)

            assertEquals(offsetHijrahDate, obtainedOffsetHijrahDateTime)
        }


        @Test
        @DisplayName("OffsetHijrahDate.of HijrahDate is obtained properly")
        fun ofHijrahDateAndLocalTime() {
            val hijrahDate = HijrahDate.of(1446, 2, 5)
            val actual = OffsetHijrahDate.of(hijrahDate, ZoneOffset.of("+03:00"))

            assertEquals(offsetHijrahDate, actual)
        }

        @Test
        @DisplayName("OffsetHijrahDate.of individual fields is obtained properly")
        fun ofIndividualFields() {
            val actual = OffsetHijrahDate.of(1446, 2, 5, ZoneOffset.of("+03:00"))

            assertEquals(offsetHijrahDate, actual)
        }


        @Test
        @DisplayName("OffsetHijrahDate.ofInstant is obtained properly")
        fun ofInstant() {
            val instant = offsetHijrahDate.toInstant()

            val actual = OffsetHijrahDate.ofInstant(instant, ZoneOffset.of("+03:00"))

            assertEquals(offsetHijrahDate, actual)
        }


        @Test
        @DisplayName("Invalid zone offset throws exception")
        fun invalidZoneOffsetThrowsException() {
            assertThrows<DateTimeException> {
                OffsetHijrahDate.of(1446, 2, 5, ZoneOffset.of("03:00"))
            }
        }

        @Test
        @DisplayName("OffsetHijrahDate from TemporalAccessor is obtained properly")
        fun fromTemporalAccessor() {
            var actual = OffsetHijrahDate.from(offsetHijrahDate)
            assertEquals(offsetHijrahDate, actual)

            assertThrows<DateTimeException> {
                OffsetHijrahDate.from(HijrahDate.now())
            }
            assertThrows<DateTimeException> {
                OffsetHijrahDate.from(Instant.now())
            }

            actual = OffsetHijrahDate.from(offsetHijrahDate.atStartOfDay())
            assertEquals(offsetHijrahDate, actual)

            actual = OffsetHijrahDate.from(OffsetDate.from(offsetHijrahDate))
            assertEquals(offsetHijrahDate, actual)


        }


    }

    @Nested
    @DisplayName("Formatting and Parsing")
    inner class FormattingAndParsingTest {

        @Test
        @DisplayName("OffsetHijrahDate is formatted properly")
        fun hijrahDateTimeIsFormattedProperly() {
            var expected = "1446-02-05+03:00"
            var actual = offsetHijrahDate.format(HijrahFormatters.HIJRAH_OFFSET_DATE)
            assertEquals(expected, actual)

            expected =  "1446-02-05+03:00"
            actual = offsetHijrahDate.format(HijrahFormatters.HIJRAH_OFFSET_DATE)
            assertEquals(expected, actual)
        }

        @Test
        @DisplayName("OffsetHijrahDate is parsed properly")
        fun hijrahDateTimeIsParsedProperly() {
            val text = "1446-02-05T12:43:18+03:00"
            val parsedHijrahDateTime = OffsetHijrahDate.parse(text, HijrahFormatters.HIJRAH_OFFSET_DATE_TIME)

            assertEquals(offsetHijrahDate, parsedHijrahDateTime)
        }


    }

    @Nested
    @DisplayName("Fields Retrieval")
    inner class FieldsRetrievalTest {

        @Test
        @DisplayName("fields are retrieved properly")
        fun get() {
            assertEquals(1446, offsetHijrahDate.get(ChronoField.YEAR))
            assertEquals(2, offsetHijrahDate.get(ChronoField.MONTH_OF_YEAR))
            assertEquals(5, offsetHijrahDate.get(ChronoField.DAY_OF_MONTH))

            val expected1 = offsetHijrahDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth + offsetHijrahDate.dayOfMonth
            assertEquals(expected1, offsetHijrahDate.get(ChronoField.DAY_OF_YEAR))

            assertEquals(5, offsetHijrahDate.get(ChronoField.DAY_OF_WEEK))
            assertEquals(DayOfWeek.FRIDAY, DayOfWeek.of(offsetHijrahDate.dayOfWeekValue))
            assertEquals(offsetHijrahDate.offset.totalSeconds, offsetHijrahDate.get(ChronoField.OFFSET_SECONDS))
            assertThrows<UnsupportedTemporalTypeException> {
                offsetHijrahDate.get(ChronoField.INSTANT_SECONDS)
            }
        }

        @Test
        @DisplayName("fields are retrieved properly as Long")
        fun getLong() {
            assertEquals(1446L, offsetHijrahDate.getLong(ChronoField.YEAR))
            assertEquals(2L, offsetHijrahDate.getLong(ChronoField.MONTH_OF_YEAR))
            assertEquals(5L, offsetHijrahDate.getLong(ChronoField.DAY_OF_MONTH))
            assertEquals(5L, offsetHijrahDate.getLong(ChronoField.DAY_OF_WEEK))
            assertEquals(Duration.ofHours(3).seconds, offsetHijrahDate.getLong(ChronoField.OFFSET_SECONDS))
            assertThrows<UnsupportedTemporalTypeException> {
                offsetHijrahDate.getLong(ChronoField.INSTANT_SECONDS)
            }
        }
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    inner class ArithmeticTest {

        @Test
        @DisplayName("OffsetHijrahDate.plus adds the specified amount properly")
        fun plus() {

            var newOffsetHijrahDate = offsetHijrahDate.plus(Days.of(2))
            assertEquals(7, newOffsetHijrahDate.dayOfMonth)

            newOffsetHijrahDate = offsetHijrahDate.plus(1, ChronoUnit.MONTHS)
            assertEquals(3, newOffsetHijrahDate.monthValue)

            newOffsetHijrahDate = offsetHijrahDate.plus(1, ChronoUnit.YEARS)
            assertEquals(1447, newOffsetHijrahDate.year)

            val combinedNewDateTime = offsetHijrahDate
                .plus(1, ChronoUnit.DAYS)
                .plus(1, ChronoUnit.MONTHS)
                .plus(1, ChronoUnit.YEARS)

            val expectedDateTime = OffsetHijrahDate.of(1447, 3, 6, ZoneOffset.of("+03:00"))

            assertEquals(expectedDateTime, combinedNewDateTime)


        }

        @Test
        @DisplayName("OffsetHijrahDate.minus subtracts the specified amount properly")
        fun minus() {

            var newOffsetHijrahDate = offsetHijrahDate.minus(Days.of(2))
            assertEquals(3, newOffsetHijrahDate.dayOfMonth)

            newOffsetHijrahDate = offsetHijrahDate.minus(1, ChronoUnit.MONTHS)
            assertEquals(1, newOffsetHijrahDate.monthValue)

            newOffsetHijrahDate = offsetHijrahDate.minus(1, ChronoUnit.YEARS)
            assertEquals(1445, newOffsetHijrahDate.year)

            val combinedNewDateTime = offsetHijrahDate
                .minusDays(1)
                .minusMonths(1)
                .minusYears(1)

            val expectedDateTime = OffsetHijrahDate.of(1445, 1, 4, ZoneOffset.of("+03:00"))

            assertEquals(expectedDateTime, combinedNewDateTime)
        }

        @Test
        @DisplayName("OffsetHijrahDate.range returns the correct range for the specified field")
        fun range() {
            var range = offsetHijrahDate.range(ChronoField.MONTH_OF_YEAR)
            assertEquals(1, range.minimum)
            assertEquals(12, range.maximum)

            range = offsetHijrahDate.range(ChronoField.DAY_OF_MONTH)
            assertEquals(1, range.minimum)
            assertEquals(30, range.maximum)

            range = offsetHijrahDate.range(ChronoField.DAY_OF_WEEK)
            assertEquals(1, range.minimum)
            assertEquals(7, range.maximum)

            assertThrows<UnsupportedTemporalTypeException> {
                offsetHijrahDate.range(IsoFields.QUARTER_OF_YEAR)
            }
        }

        @Test
        @DisplayName("OffsetHijrahDate.until returns the correct duration between two OffsetHijrahDate instances")
        fun until() {

            var offsetDate2 = OffsetHijrahDate.of(1446, 2, 6, ZoneOffset.of("+03:00"))
            var duration = offsetHijrahDate.until(offsetDate2, ChronoUnit.DAYS)
            assertEquals(1, duration)

            offsetDate2 = OffsetHijrahDate.of(1447, 2, 5, ZoneOffset.of("+03:00"))
            duration = offsetHijrahDate.until(offsetDate2, ChronoUnit.MONTHS)
            assertEquals(12, duration)

            offsetDate2 = OffsetHijrahDate.of(1447, 2, 5, ZoneOffset.of("+03:00"))
            duration = offsetHijrahDate.until(offsetDate2, ChronoUnit.YEARS)
            assertEquals(1, duration)

        }


    }




    @Nested
    @DisplayName("Support")
    inner class SupportTest {

        @Test
        @DisplayName("Required fields are supported")
        fun requiredFieldsAreSupported() {
            assertTrue(offsetHijrahDate.isSupported(ChronoField.YEAR))
            assertTrue(offsetHijrahDate.isSupported(ChronoField.MONTH_OF_YEAR))
            assertTrue(offsetHijrahDate.isSupported(ChronoField.DAY_OF_YEAR))
            assertTrue(offsetHijrahDate.isSupported(ChronoField.DAY_OF_MONTH))
            assertTrue(offsetHijrahDate.isSupported(ChronoField.DAY_OF_WEEK))
            assertTrue(offsetHijrahDate.isSupported(ChronoField.OFFSET_SECONDS))
            assertFalse(offsetHijrahDate.isSupported(ChronoField.HOUR_OF_DAY))
            assertFalse(offsetHijrahDate.isSupported(ChronoField.MINUTE_OF_HOUR))
            assertFalse(offsetHijrahDate.isSupported(ChronoField.SECOND_OF_MINUTE))
            assertFalse(offsetHijrahDate.isSupported(ChronoField.NANO_OF_SECOND))
            assertFalse(offsetHijrahDate.isSupported(ChronoField.SECOND_OF_DAY))
            assertFalse(offsetHijrahDate.isSupported(ChronoField.NANO_OF_DAY))
            assertFalse(offsetHijrahDate.isSupported(ChronoField.INSTANT_SECONDS))
        }

        @Test
        @DisplayName("Required units are supported")
        fun requiredUnitsAreSupported() {
            assertFalse(offsetHijrahDate.isSupported(ChronoUnit.NANOS))
            assertFalse(offsetHijrahDate.isSupported(ChronoUnit.SECONDS))
            assertFalse(offsetHijrahDate.isSupported(ChronoUnit.MINUTES))
            assertFalse(offsetHijrahDate.isSupported(ChronoUnit.HOURS))
            assertTrue(offsetHijrahDate.isSupported(ChronoUnit.DAYS))
            assertTrue(offsetHijrahDate.isSupported(ChronoUnit.MONTHS))
            assertTrue(offsetHijrahDate.isSupported(ChronoUnit.YEARS))
        }

    }


    @Nested
    @DisplayName("Offset Conversion")
    inner class OffsetConversionTest {

        @Test
        @DisplayName("withOffsetSameLocal converts the offset properly")
        fun withOffsetSameLocal() {
            val newZone = ZoneOffset.of("+08:00")
            val newZonedHijrahDateTime = offsetHijrahDate.withOffsetSameLocal(newZone)

            val expected = OffsetHijrahDate.of(1446, 2, 5, newZone)

            assertEquals(expected, newZonedHijrahDateTime)
        }

        @Test
        @DisplayName("OffsetHijrahDate.toHijrahDateTime returns the correct HijrahDateTime instance")
        fun toHijrahDateTime() {
            val offsetHijrahDate = offsetHijrahDate.toHijrahDate()

            val expected = HijrahDate.of(1446, 2, 5)

            assertTrue(offsetHijrahDate.isEqual(expected)) {
                "Expected: $expected\nActual: $offsetHijrahDate"
            }
        }
    }

    @Nested
    @DisplayName("Comparison")
    inner class ComparisonTest {

        @Test
        @DisplayName("Later instance is after the earlier instance")
        fun isAfter() {
            val offsetDate2 = OffsetHijrahDate.of(1446, 2, 5, ZoneOffset.of("+03:00")).minusDays(1)
            assertTrue(offsetHijrahDate.isAfter(offsetDate2))
            assertEquals(1, offsetHijrahDate.compareTo(offsetDate2))
        }

        @Test
        @DisplayName("Earlier instance is before the later instance")
        fun isBefore() {
            val offsetDate2 = OffsetHijrahDate.of(1446, 2, 5, ZoneOffset.of("+03:00")).plusDays(1)
            assertTrue(offsetHijrahDate.isBefore(offsetDate2))
            assertEquals(-1, offsetHijrahDate.compareTo(offsetDate2))
        }

        @Test
        @DisplayName("Equal instances are equal")
        fun isEqual() {
            val offsetDate2 = OffsetHijrahDate.of(1446, 2, 5, ZoneOffset.of("+03:00"))
            assertTrue(offsetHijrahDate.isEqual(offsetDate2))
            assertEquals(0, offsetHijrahDate.compareTo(offsetDate2))
        }

        @Test
        @DisplayName("Earlier instance with different offset is before the later instance")
        fun isBeforeWithDifferentOffset() {
            val offsetDate2 = offsetHijrahDate.withOffsetSameLocal(ZoneOffset.of("+02:00"))
            assertTrue(offsetHijrahDate.isBefore(offsetDate2))
            assertEquals(-1, offsetHijrahDate.compareTo(offsetDate2))
        }

        @Test
        @DisplayName("Later instance with different offset is after the earlier instance")
        fun isAfterWithDifferentOffset() {
            val offsetDate2 = offsetHijrahDate.withOffsetSameLocal(ZoneOffset.of("+04:00"))
            assertTrue(offsetHijrahDate.isAfter(offsetDate2))
            assertEquals(1, offsetHijrahDate.compareTo(offsetDate2))
        }

        @Test
        @DisplayName("Equal instances with different offset are equal")
        fun isEqualWithDifferentOffset() {
            val offsetDate2 = offsetHijrahDate.withOffsetSameLocal(ZoneOffset.of("+03:00"))
            assertTrue(offsetHijrahDate.isEqual(offsetDate2))
            assertEquals(0, offsetHijrahDate.compareTo(offsetDate2))
        }
    }

    @Nested
    @DisplayName("Adjustment")
    inner class AdjustmentTest {

        @Test
        @DisplayName("OffsetHijrahDate.with adjusts the specified field properly")
        fun with() {

            var newOffsetHijrahDate = offsetHijrahDate.with(ChronoField.DAY_OF_MONTH, 10)
            assertEquals(10, newOffsetHijrahDate.dayOfMonth)

            newOffsetHijrahDate = offsetHijrahDate.with(ChronoField.MONTH_OF_YEAR, 3)
            assertEquals(3, newOffsetHijrahDate.monthValue)

            newOffsetHijrahDate = offsetHijrahDate.with(ChronoField.YEAR, 1447)
            assertEquals(1447, newOffsetHijrahDate.year)
        }

        @Test
        @DisplayName("Adjusting HijrahDateTime to the specified temporal properly")
        fun adjustInto() {
            var target = offsetHijrahDate.withMonth(2).withDayOfMonth(1)
            var targetAfterAdjusted = offsetHijrahDate.adjustInto(target)
            assertEquals(offsetHijrahDate, targetAfterAdjusted)

            target = offsetHijrahDate.withMonth(3).withDayOfMonth(1)
            targetAfterAdjusted = offsetHijrahDate.adjustInto(target)
            assertEquals(offsetHijrahDate, targetAfterAdjusted)

            target = offsetHijrahDate.withMonth(2).withDayOfMonth(6)
            targetAfterAdjusted = offsetHijrahDate.adjustInto(target)
            assertEquals(offsetHijrahDate, targetAfterAdjusted)

            target = offsetHijrahDate.withYear(1443).withMonth(8)
            targetAfterAdjusted = offsetHijrahDate.adjustInto(target)
            assertEquals(offsetHijrahDate, targetAfterAdjusted)

            target = offsetHijrahDate.withOffsetSameLocal(ZoneOffset.of("+02:00"))
            targetAfterAdjusted = offsetHijrahDate.adjustInto(target)
            assertEquals(offsetHijrahDate, targetAfterAdjusted)
        }

    }


    @Test
    @DisplayName("Temporal queries work properly")
    fun temporalQueriesAreSupported() {
        assertEquals(HijrahChronology.INSTANCE, offsetHijrahDate.query(TemporalQueries.chronology()))
        assertNull(offsetHijrahDate.query(TemporalQueries.zoneId()))
        assertEquals(ChronoUnit.DAYS, offsetHijrahDate.query(TemporalQueries.precision()))
        assertEquals(LocalDate.from(offsetHijrahDate),offsetHijrahDate.query(TemporalQueries.localDate()))
        assertNull(offsetHijrahDate.query(TemporalQueries.localTime()))
        assertEquals(offsetHijrahDate.offset ,offsetHijrahDate.query(TemporalQueries.offset()))
        assertEquals(offsetHijrahDate.offset, offsetHijrahDate.query(TemporalQueries.zone()))
    }

    @Test
    @DisplayName("HijrahDateTime is extracted properly")
    fun hijrahDateTimeIsExtractedProperly() {
        val offsetHijrahDate = offsetHijrahDate.toHijrahDate()
        assertEquals(1446, offsetHijrahDate.year)
        assertEquals(2, offsetHijrahDate.monthValue)
        assertEquals(5, offsetHijrahDate.dayOfMonth)
    }

    @Test
    @DisplayName("HijrahDate is extracted properly")
    fun hijrahDateIsExtractedProperly() {
        val hijrahDate = offsetHijrahDate.toHijrahDate()
        assertEquals(HijrahDate.of(1446, 2, 5), hijrahDate)
    }


    @Test
    @DisplayName("toString returns the correct string representation")
    fun toStringReturnsCorrectStringRepresentation() {
        val expected = "${offsetHijrahDate.chronology.id} ${HijrahEra.AH} 1446-02-05+03:00"
        assertEquals(expected, offsetHijrahDate.toString())
    }

    @Test
    @DisplayName("atTime returns the correct OffsetHijrahDateTime instance")
    fun atTime() {
        val localTime = LocalTime.of(12, 30)
        val offsetDateTime = offsetHijrahDate.atTime(localTime)

        val expected = OffsetHijrahDateTime.of(offsetHijrahDate.toHijrahDate(), localTime, ZoneOffset.of("+03:00"))

        assertEquals(expected, offsetDateTime)
    }

    @Test
    @DisplayName(".equals() returns the correct result")
    fun equalsReturnsCorrectResult() {
        val offsetHijrahDate2 = OffsetHijrahDate.of(1446, 2, 5, ZoneOffset.of("+03:00"))
        assertTrue(offsetHijrahDate == offsetHijrahDate2)
        assertFalse(offsetHijrahDate == OffsetHijrahDate.of(1446, 2, 6, ZoneOffset.of("+03:00")))
        assertFalse(offsetHijrahDate == OffsetHijrahDate.of(1446, 2, 5, ZoneOffset.of("+02:00")))
        assertFalse(offsetHijrahDate.equals(null))
    }


    @Test
    @DisplayName("Hash code is calculated properly")
    fun hashCodeIsCalculatedProperly() {
        offsetHijrahDate.hashCode()
    }
}