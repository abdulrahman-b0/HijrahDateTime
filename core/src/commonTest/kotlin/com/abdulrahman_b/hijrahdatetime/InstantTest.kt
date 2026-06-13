package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormats
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class InstantTest {

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test parse Hijri`() {
        // 1445-09-01T00:00:00Z is 1710115200 epoch seconds
        val instant = Instant.parseHijri("1445-09-01T00:00:00Z")
        instant.epochSeconds shouldBe 1710115200

        val instantWithZeroOffset = Instant.parseHijri("1445-09-01T00:00:00+00:00")
        instantWithZeroOffset.epochSeconds shouldBe 1710115200

        val instantWithOffset = Instant.parseHijri("1445-09-01T03:00:00+03:00")
        instantWithOffset.epochSeconds shouldBe 1710115200

        val instantNegativeOffset = Instant.parseHijri("1445-09-01T00:00:00-05:00")
        instantNegativeOffset.epochSeconds shouldBe 1710115200 + 5 * 3600

        // 1447-10-06T00:00:00Z is 1774396800 epoch seconds
        val instant2 = Instant.parseHijri("1447-10-06T00:00:00Z")
        instant2.epochSeconds shouldBe 1774396800
    }

    @Test
    fun `test parse Hijri fail`() {
        shouldThrow<IllegalArgumentException> {
            Instant.parseHijri("invalid")
        }
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test parse Hijri or null`() {
        val instant = Instant.parseHijriOrNull("1445-09-01T00:00:00Z")
        instant?.epochSeconds shouldBe 1710115200

        Instant.parseHijriOrNull("invalid") shouldBe null
        // It might actually parse if it's a valid Hijri date, but 2024 is out of range for Hijri Umm Al-Qura usually
        // Umm Al-Qura in java.time supports roughly 1300 to 1600 Hijri years. The same applied for apple targets to maintain consistency.
        Instant.parseHijriOrNull("2024-03-11T00:00:00Z") shouldBe null
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test format`() {
        val instant = Instant.fromEpochSeconds(1710115200)
        
        val formattedUtc = instant.format(HijrahDateTimeFormats.OFFSET_DATE_TIME_ISO, UtcOffset.ZERO)
        formattedUtc shouldBe "1445-09-01T00:00:00Z"

        var formattedOffset = instant.format(HijrahDateTimeFormats.OFFSET_DATE_TIME_ISO, UtcOffset(hours = 3))
        formattedOffset shouldBe "1445-09-01T03:00:00+03:00"

        formattedOffset = instant.format(HijrahDateTimeFormats.OFFSET_DATE_TIME_ISO, UtcOffset(hours = -5))
        formattedOffset shouldBe "1445-08-29T19:00:00-05:00"
        
        val formattedDate = instant.format(HijrahDateTimeFormats.DATE_ISO)
        formattedDate shouldBe "1445-09-01"
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test format default`() {
        val instant = Instant.fromEpochSeconds(1710115200)
        val formatted = instant.format(HijrahDateTimeFormats.OFFSET_DATE_TIME_ISO)
        formatted shouldBe "1445-09-01T00:00:00Z"
    }

    @Test
    fun `test to HijrahDateTime before 1970`() {
        // Arrange
        val before1970 = Instant.fromEpochSeconds(-1000000000)
        
        // Act
        val dtBefore = before1970.toHijrahDateTime(TimeZone.UTC)
        
        // Assert
        // -1,000,000,000 seconds is 1357-02-23 22:13:20
        dtBefore.year shouldBe 1357
        dtBefore.month.number shouldBe 2
        dtBefore.day shouldBe 23
        dtBefore.hour shouldBe 22
        dtBefore.minute shouldBe 13
        dtBefore.second shouldBe 20
    }

    @Test
    fun `test to HijrahDateTime exact epoch`() {
        // Arrange
        val epoch = Instant.fromEpochSeconds(0)
        
        // Act
        val dtEpoch = epoch.toHijrahDateTime(TimeZone.UTC)
        
        // Assert
        dtEpoch.year shouldBe 1389
        dtEpoch.month.number shouldBe 10
        dtEpoch.day shouldBe 22
        dtEpoch.hour shouldBe 0
        dtEpoch.minute shouldBe 0
        dtEpoch.second shouldBe 0
    }

    @Test
    fun `test to HijrahDateTime just before epoch`() {
        // Arrange
        val justBeforeEpoch = Instant.fromEpochSeconds(-1)
        
        // Act
        val dtJustBefore = justBeforeEpoch.toHijrahDateTime(TimeZone.UTC)
        
        // Assert
        dtJustBefore.year shouldBe 1389
        dtJustBefore.month.number shouldBe 10
        dtJustBefore.day shouldBe 21
        dtJustBefore.hour shouldBe 23
        dtJustBefore.minute shouldBe 59
        dtJustBefore.second shouldBe 59
    }

    @Test
    fun `test to HijrahDateTime start of Umm Al-Qura`() {
        // Arrange
        val startInstant = Instant.parse("1882-11-12T00:00:00Z")
        
        // Act
        val dtStart = startInstant.toHijrahDateTime(TimeZone.UTC)
        
        // Assert
        dtStart.year shouldBe 1300
        dtStart.month.number shouldBe 1
        dtStart.day shouldBe 1
    }

    @Test
    fun `test to HijrahDateTime end of Umm Al-Qura`() {
        // Arrange
        val endInstant = Instant.parse("2174-11-25T23:59:59Z")
        
        // Act
        val dtEnd = endInstant.toHijrahDateTime(TimeZone.UTC)
        
        // Assert
        dtEnd.year shouldBe 1600
        dtEnd.month.number shouldBe 12
        dtEnd.day shouldBe 30
    }

    @Test
    fun `test to HijrahDateTime with nanoseconds`() {
        val instant = Instant.fromEpochSeconds(1710115200, 123456789)
        val dt = instant.toHijrahDateTime(TimeZone.UTC)
        // Allow for a small drift, e.g., 1000 nanoseconds (1 microsecond), because of apple targets not having nanosecond precision
        val draft = 1000
        dt.nanosecond shouldBeInRange (123456789 - draft) .. (123456789 + draft)
    }
}
