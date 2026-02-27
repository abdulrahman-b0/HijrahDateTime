package com.abdulrahman_b.hijrahdatetime

import io.kotest.matchers.shouldBe
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.time.Instant

class TimeZoneTest {

    @Test
    fun testToInstantWithDifferentTimeZones() {
        // 1445-09-01 10:30:00
        val dt = HijrahDateTime(1445, 9, 1, 10, 30, 0, 0)
        
        // UTC
        val instantUtc = dt.toInstant(TimeZone.UTC)
        // 1445-09-01 is 2024-03-11
        // 2024-03-11T10:30:00Z
        instantUtc.epochSeconds shouldBe 1710153000L
        
        // UTC+3 (e.g. Riyadh)
        val riyadhTz = FixedOffsetTimeZone(UtcOffset(3))
        val instantRiyadh = dt.toInstant(riyadhTz)
        // 2024-03-11T10:30:00+03:00 is 2024-03-11T07:30:00Z
        instantRiyadh.epochSeconds shouldBe 1710142200L
        
        // UTC-5 (e.g. New York)
        val nyTz = FixedOffsetTimeZone(UtcOffset(-5))
        val instantNy = dt.toInstant(nyTz)
        // 2024-03-11T10:30:00-05:00 is 2024-03-11T15:30:00Z
        instantNy.epochSeconds shouldBe 1710171000L
    }

    @Test
    fun testInstantToHijrahDateTimeWithTimeZones() {
        val instant = Instant.fromEpochSeconds(1710153000L) // 2024-03-11T10:30:00Z
        
        // To UTC
        val dtUtc = instant.toHijrahDateTime(TimeZone.UTC)
        dtUtc.year shouldBe 1445
        dtUtc.month.number shouldBe 9
        dtUtc.day shouldBe 1
        dtUtc.hour shouldBe 10
        dtUtc.minute shouldBe 30
        
        // To UTC+3
        val riyadhTz = FixedOffsetTimeZone(UtcOffset(3))
        val dtRiyadh = instant.toHijrahDateTime(riyadhTz)
        // 2024-03-11T10:30:00Z + 3h = 2024-03-11T13:30:00
        dtRiyadh.hour shouldBe 13
        dtRiyadh.minute shouldBe 30
        
        // To UTC-5
        val nyTz = FixedOffsetTimeZone(UtcOffset(-5))
        val dtNy = instant.toHijrahDateTime(nyTz)
        // 2024-03-11T10:30:00Z - 5h = 2024-03-11T05:30:00
        dtNy.hour shouldBe 5
        dtNy.minute shouldBe 30
    }

    @Test
    fun testAtStartOfDayWithTimeZones() {
        val date = HijrahDate(1445, 9, 1) // 2024-03-11
        
        val riyadhTz = FixedOffsetTimeZone(UtcOffset(3))
        val startOfDayRiyadh = date.atStartOfDay(riyadhTz)
        
        startOfDayRiyadh.date shouldBe date
        startOfDayRiyadh.hour shouldBe 0
        startOfDayRiyadh.minute shouldBe 0
        
        // Verify it corresponds to the correct Instant
        val instant = startOfDayRiyadh.toInstant(riyadhTz)
        // 2024-03-11T00:00:00+03:00 is 2024-03-10T21:00:00Z
        instant.epochSeconds shouldBe 1710104400L
    }
}
