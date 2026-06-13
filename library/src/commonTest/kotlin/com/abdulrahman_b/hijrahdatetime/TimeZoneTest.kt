package com.abdulrahman_b.hijrahdatetime

import io.kotest.matchers.shouldBe
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class TimeZoneTest {

    @Test
    fun `test to instant UTC`() {
        // Arrange
        val dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        
        // Act
        val instantUtc = dt.toInstant(TimeZone.UTC)
        
        // Assert
        // 1445-09-01 is 2024-03-11
        // 2024-03-11T10:30:00Z
        instantUtc.epochSeconds shouldBe 1710153000L
    }

    @Test
    fun `test to instant Riyadh`() {
        // Arrange
        val dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        val riyadhTz = FixedOffsetTimeZone(UtcOffset(3))
        
        // Act
        val instantRiyadh = dt.toInstant(riyadhTz)
        
        // Assert
        // 2024-03-11T10:30:00+03:00 is 2024-03-11T07:30:00Z
        instantRiyadh.epochSeconds shouldBe 1710142200L
    }

    @Test
    fun `test to instant New York`() {
        // Arrange
        val dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        val nyTz = FixedOffsetTimeZone(UtcOffset(-5))
        
        // Act
        val instantNy = dt.toInstant(nyTz)
        
        // Assert
        // 2024-03-11T10:30:00-05:00 is 2024-03-11T15:30:00Z
        instantNy.epochSeconds shouldBe 1710171000L
    }

    @Test
    fun `test to HijrahDateTime UTC`() {
        // Arrange
        val instant = Instant.fromEpochSeconds(1710153000L) // 2024-03-11T10:30:00Z
        
        // Act
        val dtUtc = instant.toHijrahDateTime(TimeZone.UTC)
        
        // Assert
        dtUtc.year shouldBe 1445
        dtUtc.month.number shouldBe 9
        dtUtc.day shouldBe 1
        dtUtc.hour shouldBe 10
        dtUtc.minute shouldBe 30
    }

    @Test
    fun `test to HijrahDateTime Riyadh`() {
        // Arrange
        val instant = Instant.fromEpochSeconds(1710153000L) // 2024-03-11T10:30:00Z
        val riyadhTz = FixedOffsetTimeZone(UtcOffset(3))
        
        // Act
        val dtRiyadh = instant.toHijrahDateTime(riyadhTz)
        
        // Assert
        // 2024-03-11T10:30:00Z + 3h = 2024-03-11T13:30:00
        dtRiyadh.hour shouldBe 13
        dtRiyadh.minute shouldBe 30
    }

    @Test
    fun `test to HijrahDateTime New York`() {
        // Arrange
        val instant = Instant.fromEpochSeconds(1710153000L) // 2024-03-11T10:30:00Z
        val nyTz = FixedOffsetTimeZone(UtcOffset(-5))
        
        // Act
        val dtNy = instant.toHijrahDateTime(nyTz)
        
        // Assert
        // 2024-03-11T10:30:00Z - 5h = 2024-03-11T05:30:00
        dtNy.hour shouldBe 5
        dtNy.minute shouldBe 30
    }
    @Test
    fun `test timezone offset winter`() {
        // Arrange
        val londonTz = TimeZone.of("Europe/London")
        // London is UTC+0. Expected local time: 23:30
        val winterInstant = Instant.parse("2026-01-15T23:30:00Z")
        val winterEpochDay = 20468L // The exact ISO epoch day for 2026-01-15

        // Act
        val winterHijrah = winterInstant.toHijrahDateTime(londonTz)

        // Assert
        assertEquals(23, winterHijrah.time.hour)
        assertEquals(30, winterHijrah.time.minute)
        assertEquals(HijrahDate.fromEpochDays(winterEpochDay), winterHijrah.date)
    }

    @Test
    fun `test timezone offset summer`() {
        // Arrange
        val londonTz = TimeZone.of("Europe/London")
        // London is UTC+1. Expected local time: 00:30 on June 16th (Next Day!)
        val summerInstant = Instant.parse("2026-06-15T23:30:00Z")
        val summerEpochDay = 20620L // June 15 rolls over to June 16 (20619 + 1)

        // Act
        val summerHijrah = summerInstant.toHijrahDateTime(londonTz)

        // Assert
        assertEquals(0, summerHijrah.time.hour)   // Confirms the midnight rollover
        assertEquals(30, summerHijrah.time.minute)
        assertEquals(HijrahDate.fromEpochDays(summerEpochDay), summerHijrah.date)
    }

    @Test
    fun `test historical negative epoch seconds`() {
        // A fixed historical date: May 15, 1950, at 01:30:15 UTC
        // This results in a negative epochSeconds value
        val historicalInstant = Instant.parse("1950-05-15T01:30:15Z")

        // Test with a positive offset timezone (UTC+3)
        val riyadhTz = TimeZone.of("Asia/Riyadh")
        // Expected Local Time: 01:30:15 + 3 hours = 04:30:15 on the same ISO day

        // The exact ISO epoch day for 1950-05-15
        val expectedEpochDay = -7171L

        // Execute your single-step conversion
        val hijrahDateTime = historicalInstant.toHijrahDateTime(riyadhTz)

        // --- Assertions ---
        // If the old remainder math (%) was used, these numbers would extract as negative
        // and crash the LocalTime constructor, or result in completely corrupt time fields.
        assertEquals(4, hijrahDateTime.time.hour)
        assertEquals(30, hijrahDateTime.time.minute)
        assertEquals(15, hijrahDateTime.time.second)

        // Verify the date portion maps perfectly to the correct negative epoch day
        assertEquals(HijrahDate.fromEpochDays(expectedEpochDay), hijrahDateTime.date)
    }

    @Test
    fun `test atStartOfDay with time zones`() {
        val date = HijrahDate(1445, 9, 1) // 2024-03-11
        
        val riyadhTz = FixedOffsetTimeZone(UtcOffset(3))
        val startOfDayRiyadh = date.atStartOfDay(riyadhTz)
        val datetime = startOfDayRiyadh.toHijrahDateTime(riyadhTz)
        
        datetime.date shouldBe date
        datetime.hour shouldBe 0
        datetime.minute shouldBe 0
        
        // Verify it corresponds to the correct Instant
        val instant = startOfDayRiyadh
        // 2024-03-11T00:00:00+03:00 is 2024-03-10T21:00:00Z
        instant.epochSeconds shouldBe 1710104400L
    }

    @Test
    fun `test timezone rollover at epoch PST`() {
        // Arrange
        // Just before 1970-01-01T00:00:00Z
        val instant = Instant.fromEpochSeconds(-1)
        // UTC-8 (PST) -> 1969-12-31T15:59:59
        val pst = TimeZone.of("America/Los_Angeles")
        
        // Act
        val dtPst = instant.toHijrahDateTime(pst)
        
        // Assert
        // 1969-12-31 is Hijri 1389-10-21
        dtPst.year shouldBe 1389
        dtPst.month.number shouldBe 10
        dtPst.day shouldBe 21
        dtPst.hour shouldBe 15
        dtPst.minute shouldBe 59
        dtPst.second shouldBe 59
    }

    @Test
    fun `test timezone rollover at epoch China`() {
        // Arrange
        // Just before 1970-01-01T00:00:00Z
        val instant = Instant.fromEpochSeconds(-1)
        // UTC+8 -> 1970-01-01T07:59:59
        val china = TimeZone.of("Asia/Shanghai")
        
        // Act
        val dtChina = instant.toHijrahDateTime(china)
        
        // Assert
        // 1970-01-01 is Hijri 1389-10-22
        dtChina.year shouldBe 1389
        dtChina.month.number shouldBe 10
        dtChina.day shouldBe 22
        dtChina.hour shouldBe 7
        dtChina.minute shouldBe 59
    }
}
