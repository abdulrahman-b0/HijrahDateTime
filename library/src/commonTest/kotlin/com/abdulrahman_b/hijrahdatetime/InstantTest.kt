package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormats
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class InstantTest {

    @OptIn(ExperimentalTime::class)
    @Test
    fun testParseHijri() {
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
    fun testParseHijriFail() {
        shouldThrow<IllegalArgumentException> {
            Instant.parseHijri("invalid")
        }
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testParseHijriOrNull() {
        val instant = Instant.parseHijriOrNull("1445-09-01T00:00:00Z")
        instant?.epochSeconds shouldBe 1710115200

        Instant.parseHijriOrNull("invalid") shouldBe null
        // It might actually parse if it's a valid Hijri date, but 2024 is out of range for Hijri Umm Al-Qura usually
        // Umm Al-Qura in java.time supports roughly 1300 to 1600 Hijri years. The same applied for apple targets to maintain consistency.
        Instant.parseHijriOrNull("2024-03-11T00:00:00Z") shouldBe null
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun testFormat() {
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
    fun testFormatDefault() {
        val instant = Instant.fromEpochSeconds(1710115200)
        val formatted = instant.format(HijrahDateTimeFormats.OFFSET_DATE_TIME_ISO)
        formatted shouldBe "1445-09-01T00:00:00Z"
    }
}
