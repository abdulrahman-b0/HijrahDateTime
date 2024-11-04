package com.abdulrahman_b.hijrah_datetime

import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.DateTimeException
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.chrono.HijrahEra
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalQueries

class ZonedHijrahDateTimeTest {

    private lateinit var zonedHijrahDateTime: ZonedHijrahDateTime

    @BeforeEach
    fun setUp() {
        zonedHijrahDateTime = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, zoneId =  ZoneId.of("Asia/Riyadh"))
    }

    @Nested
    @DisplayName("Factory Methods")
    inner class FactoryTests {

        @Test
        @DisplayName("ZonedHijrahDateTime.now() returns the current instance")
        fun nowReturnsCurrentInstance() {
            var now = ZonedHijrahDateTime.now()
            assertEquals(now.zone, ZoneId.systemDefault())
            assertEquals(now.offset, ZoneOffset.systemDefault().rules.getOffset(now.toInstant()))

            now = ZonedHijrahDateTime.now(ZoneId.of("Asia/Riyadh"))
            assertEquals(now.zone, ZoneId.of("Asia/Riyadh"))
            assertEquals(now.offset, ZoneOffset.ofHours(3))

            now = ZonedHijrahDateTime.now(ZoneOffset.ofHours(3))
            assertEquals(now.zone, ZoneOffset.ofHours(3))
            assertEquals(now.offset, ZoneOffset.ofHours(3))

            now = ZonedHijrahDateTime.now(Clock.system(ZoneId.of("Asia/Riyadh")))
            assertEquals(now.zone, ZoneId.of("Asia/Riyadh"))
            assertEquals(now.offset, ZoneOffset.ofHours(3))
        }

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

        @Test
        @DisplayName("Invalid zone ID throws DateTimeException")
        fun invalidZoneIdThrowsDateTimeException() {
            assertThrows<DateTimeException> {
                ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, zoneId =  ZoneId.of("Invalid/Zone"))
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
    @DisplayName("Support")
    inner class SupportTest {

        @Test
        @DisplayName("Required fields are supported")
        fun requiredFieldsAreSupported() {
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.YEAR))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.MONTH_OF_YEAR))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.DAY_OF_YEAR))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.DAY_OF_MONTH))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.DAY_OF_WEEK))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.HOUR_OF_DAY))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.MINUTE_OF_HOUR))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.SECOND_OF_MINUTE))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.NANO_OF_SECOND))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.SECOND_OF_DAY))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.NANO_OF_DAY))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.OFFSET_SECONDS))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoField.INSTANT_SECONDS))
        }

        @Test
        @DisplayName("Required units are supported")
        fun requiredUnitsAreSupported() {
            assertTrue(zonedHijrahDateTime.isSupported(ChronoUnit.NANOS))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoUnit.SECONDS))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoUnit.MINUTES))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoUnit.HOURS))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoUnit.DAYS))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoUnit.MONTHS))
            assertTrue(zonedHijrahDateTime.isSupported(ChronoUnit.YEARS))
        }

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
    }

    @Nested
    @DisplayName("Comparison")
    inner class ComparisonTest {

        @Test
        @DisplayName("Earlier instance is before the later one")
        fun earlierInstanceIsBeforeTheLaterOne() {
            val hijrahDateTime2 = zonedHijrahDateTime.plusNanos(1)

            assertTrue(zonedHijrahDateTime.isBefore(hijrahDateTime2))
            assertEquals(zonedHijrahDateTime.compareTo(hijrahDateTime2), -1)
        }

        @Test
        @DisplayName("Later instance is after the earlier one")
        fun laterInstanceIsAfterTheEarlierOne() {
            val hijrahDateTime2 = zonedHijrahDateTime.minusNanos(1)

            assertTrue(zonedHijrahDateTime.isAfter(hijrahDateTime2))
            assertEquals(zonedHijrahDateTime.compareTo(hijrahDateTime2), 1)
        }

        @Test
        @DisplayName("Same instances are actually equal")
        fun instancesAreEqual() {
            val hijrahDateTime2 = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh"))

            assertTrue(zonedHijrahDateTime.isEqual(hijrahDateTime2))
            assertEquals(zonedHijrahDateTime.compareTo(hijrahDateTime2), 0)
            assertTrue(zonedHijrahDateTime == hijrahDateTime2)
            assertFalse(zonedHijrahDateTime.equals(null))
        }

        @Test
        @DisplayName("Earlier instance with different zone is before the later one")
        fun earlierInstanceWithDifferentZoneIsBeforeTheLaterOne() {
            val hijrahDateTime2 = zonedHijrahDateTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai")).plusNanos(1)
            assertTrue(zonedHijrahDateTime.isBefore(hijrahDateTime2))
            assertEquals(zonedHijrahDateTime.compareTo(hijrahDateTime2), -1)
        }

        @Test
        @DisplayName("Later instance with different zone is after the earlier one")
        fun laterInstanceWithDifferentZoneIsAfterTheEarlierOne() {
            val hijrahDateTime2 = zonedHijrahDateTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai")).minusNanos(1)

            assertTrue(zonedHijrahDateTime.isAfter(hijrahDateTime2))
            assertEquals(zonedHijrahDateTime.compareTo(hijrahDateTime2), 1)
        }

        @Test
        @DisplayName("Same instances with different zones are actually equal")
        fun equalInstancesWithDifferentZoneAreActuallyEqual() {
            val hijrahDateTime2 = zonedHijrahDateTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai"))

            assertTrue(zonedHijrahDateTime.isEqual(hijrahDateTime2))
            assertEquals(-1, zonedHijrahDateTime.compareTo(hijrahDateTime2))
        }

    }

    @Test
    @DisplayName("truncate to the specified field properly")
    fun truncate() {

        var truncatedHijrahDateTime = zonedHijrahDateTime.truncatedTo(ChronoUnit.SECONDS)
        var expected = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh"))
        assertEquals(expected, truncatedHijrahDateTime)

        truncatedHijrahDateTime = zonedHijrahDateTime.truncatedTo(ChronoUnit.MINUTES)
        expected = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 0, 0, ZoneId.of("Asia/Riyadh"))
        assertEquals(expected, truncatedHijrahDateTime)

        truncatedHijrahDateTime = zonedHijrahDateTime.truncatedTo(ChronoUnit.DAYS)
        expected = ZonedHijrahDateTime.of(1446, 2, 5, 0, 0, 0, 0, ZoneId.of("Asia/Riyadh"))
        assertEquals(expected, truncatedHijrahDateTime)

    }

    @Test
    @DisplayName("HijrahDateTime is extracted properly")
    fun toHijrahDateTime() {
        val hijrahDateTime = zonedHijrahDateTime.toHijrahDateTime()
        val expected = HijrahDateTime.of(1446, 2, 5, 12, 43, 18)
        assertEquals(expected, hijrahDateTime)
    }

    @Test
    @DisplayName("HijrahDate is extracted properly")
    fun toHijrahDate() {
        val hijrahDate = zonedHijrahDateTime.toHijrahDate()
        val expected = HijrahDate.of(1446, 2, 5)
        assertEquals(expected, hijrahDate)
    }

    @Test
    @DisplayName("LocalTime is extracted properly")
    fun toLocalTime() {
        val localTime = zonedHijrahDateTime.toLocalTime()
        val expected = LocalTime.of(12, 43, 18)
        assertEquals(expected, localTime)
    }

    @Test
    @DisplayName("Converted to OffsetHijrahDateTime properly")
    fun toOffsetHijrahDateTime() {
        val offsetHijrahDateTime = zonedHijrahDateTime.toOffsetHijrahDateTime()
        val expected = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.ofHours(3))
        assertEquals(expected, offsetHijrahDateTime)
        assertEquals(expected.offset, offsetHijrahDateTime.offset)
    }


    @Test
    @DisplayName("ZonedHijrahDateTime.toEpochSeconds returns the correct value")
    fun toEpochSeconds() {
        val expected = ZonedDateTime.from(zonedHijrahDateTime).toEpochSecond()
        val epochSeconds = zonedHijrahDateTime.toEpochSecond()

        assertEquals(expected, epochSeconds)
    }

    @Test
    @DisplayName("toString returns the correct string representation")
    fun toStringReturnsCorrectStringRepresentation() {
        val expected = "${zonedHijrahDateTime.chronology.id} ${HijrahEra.AH} 1446-02-05T12:43:18+03:00[Asia/Riyadh]"
        assertEquals(expected, zonedHijrahDateTime.toString())
    }

    @Test
    @DisplayName("Hash code is calculated properly")
    fun hashCodeIsCalculatedProperly() {
        zonedHijrahDateTime.hashCode()
    }

    @Test
    @DisplayName("Temporal queries work as expected")
    fun temporalQueriesAreSupported() {
        assertEquals(HijrahChronology.INSTANCE, zonedHijrahDateTime.query(TemporalQueries.chronology()))
        assertEquals(zonedHijrahDateTime.zone, zonedHijrahDateTime.query(TemporalQueries.zoneId()))
        assertEquals(ChronoUnit.NANOS, zonedHijrahDateTime.query(TemporalQueries.precision()))
        assertEquals(LocalDate.from(zonedHijrahDateTime),zonedHijrahDateTime.query(TemporalQueries.localDate()))
        assertEquals(zonedHijrahDateTime.toLocalTime(), zonedHijrahDateTime.query(TemporalQueries.localTime()))
        assertEquals(zonedHijrahDateTime.offset ,zonedHijrahDateTime.query(TemporalQueries.offset()))
        assertEquals(zonedHijrahDateTime.zone, zonedHijrahDateTime.query(TemporalQueries.zone()))
    }


}