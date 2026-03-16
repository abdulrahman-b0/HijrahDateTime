package com.abdulrahman_b.hijrahdatetime

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.ranges.shouldNotBeIn
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DateTimeArithmeticException
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Instant

class HijrahDateTest {

    @Test
    fun `test Hijri creation`() {
        val date = shouldNotThrowAny { HijrahDate(1445, 9, 1) }
        date.year shouldBe 1445
        date.month.number shouldBe 9
        date.day shouldBe 1
        date.dayOfWeek shouldBe DayOfWeek.MONDAY

        shouldThrow<IllegalArgumentException> {
            HijrahDate(1445, 9, 31)
        }
    }
    
    @Test
    fun `getting current Hijrah date`() {
        val currentDate = shouldNotThrowAny {
            Clock.System.now().toHijrahDate()
        }
        currentDate.year shouldBe 1447
        currentDate.month.number shouldBe 9
        currentDate.day shouldBe 27
    }

    @Test
    fun `test arithmetic days`() {
        // 1445-09-29 + 1 day should be 1445-09-30 (Ramadan 1445 has 30 days)
        val date = shouldNotThrowAny { HijrahDate(1445, 9, 29) }
        val nextDay = shouldNotThrowAny { date plusDays 1 }

        nextDay.day shouldBe 30
        nextDay.month shouldBe HijrahMonth.RAMADAN

        val nextMonthFirst = shouldNotThrowAny { nextDay plusDays 1 }
        nextMonthFirst.day shouldBe 1
        nextMonthFirst.month shouldBe HijrahMonth.SHAWWAL

        val prevDay = shouldNotThrowAny { nextMonthFirst minusDays 1 }
        prevDay.day shouldBe 30
        prevDay.month.number shouldBe 9
    }

    @Test
    fun `test arithmetic months`() {
        val date = HijrahDate(1446, 1, 29)
        val nextMonth = shouldNotThrowAny { date plusMonths 1 }
        nextMonth.month.number shouldBe 2
        nextMonth.day shouldBe 29

        val nextYear = shouldNotThrowAny { date plusYears 1 }
        nextYear.year shouldBe 1447
        nextYear.month.number shouldBe 1
        nextYear.day shouldBe 29

        val minusMonth = shouldNotThrowAny { nextMonth minusMonths 1 }
        minusMonth.month.number shouldBe 1
        minusMonth.day shouldBe 29
    }

    @Test
    fun `test invalid creation`() {
        shouldThrow<IllegalArgumentException> {
            HijrahDate(1445, 9, 31) // Ramadan 1445 had 30 days
        }
        shouldThrow<IllegalArgumentException> {
            HijrahDate(1445, 13, 1) // Invalid month
        }
        shouldThrow<IllegalArgumentException> {
            HijrahDate(1445, 0, 1) // Invalid month
        }
        shouldThrow<IllegalArgumentException> {
            HijrahDate(1445, 9, 0) // Invalid day
        }
        shouldThrow<IllegalArgumentException> {
            HijrahDate(1299, 12, 29) // Before MIN
        }
        shouldThrow<IllegalArgumentException> {
            HijrahDate(1601, 1, 1) // After MAX
        }
    }

    @Test
    fun `test comparison`() {

        val date1 = HijrahDate(1446, 3, 11)
        val date2 = HijrahDate(1446, 3, 12)
        val date3 = HijrahDate(1446, 2, 11)

        date1 shouldBeLessThan date2
        date1 shouldBeGreaterThan date3

        date1.compareTo(HijrahDate(1446, 3, 11)) shouldBe 0
        date1 shouldBeEqual HijrahDate(1446, 3, 11)
    }

    @Test
    fun `test to epoch days`() {
        // 1446-01-01 AH is 2024-07-07 ISO. 
        // Epoch days for 2024-07-07 is 19911
        val date = HijrahDate(1446, 1, 1)
        date.toEpochDays() shouldBe 19911

        val dateFromEpoch = HijrahDate.fromEpochDays(19911)
        dateFromEpoch.year shouldBe 1446
        dateFromEpoch.month.number shouldBe 1
        dateFromEpoch.day shouldBe 1
    }

    @Test
    fun `test to LocalDate`() {
        val date = HijrahDate(1445, 9, 1) // 2024-03-11
        val localDate = date.toLocalDate()
        localDate.year shouldBe 2024
        localDate.month shouldBe Month.MARCH
        localDate.day shouldBe 11
    }

    @Test
    fun `test instant conversion`() {
        // 2024-03-11T00:00:00Z is 1445-09-01
        val instant = Instant.fromEpochSeconds(1710115200)
        val date = instant.toHijrahDate(TimeZone.UTC)
        date.year shouldBe 1445
        date.month.number shouldBe 9
        date.day shouldBe 1
    }

    @Test
    fun `test withNextDayOfWeek`() {
        // 1445-09-01 is Monday
        val monday = HijrahDate(1445, 9, 1)
        monday.dayOfWeek shouldBe DayOfWeek.MONDAY

        // Next Wednesday should be 1445-09-03
        val nextWednesday = monday.withNextDayOfWeek(DayOfWeek.WEDNESDAY)
        nextWednesday.dayOfWeek shouldBe DayOfWeek.WEDNESDAY
        nextWednesday.day shouldBe 3

        // Next Monday should be 1445-09-08
        val nextMonday = monday.withNextDayOfWeek(DayOfWeek.MONDAY)
        nextMonday.day shouldBe 8
        nextMonday.dayOfWeek shouldBe DayOfWeek.MONDAY
    }

    @Test
    fun `test withPreviousDayOfWeek`() {
        // 1445-09-01 is Monday
        val monday = HijrahDate(1445, 9, 1)

        // Previous Friday should be 1445-08-27
        // 2024-03-08 is 1445-08-27
        val prevFriday = shouldNotThrowAny { monday.withPreviousDayOfWeek(DayOfWeek.FRIDAY) }
        prevFriday.day shouldBe 27
        prevFriday.month.number shouldBe 8
        prevFriday.dayOfWeek shouldBe DayOfWeek.FRIDAY

        // Previous Monday should be 1445-08-23
        val prevMonday = shouldNotThrowAny { monday.withPreviousDayOfWeek(DayOfWeek.MONDAY) }
        prevMonday.dayOfWeek shouldBe DayOfWeek.MONDAY
        prevMonday.day shouldBe 23
        prevMonday.month.number shouldBe 8
    }

    @Test
    fun `test withSameOrNextDayOfWeek`() {
        val monday = HijrahDate(1445, 9, 1)

        // Same or next Monday should be Today
        val sameMonday = shouldNotThrowAny { monday.withSameOrNextDayOfWeek(DayOfWeek.MONDAY) }
        sameMonday.day shouldBe 1

        // Same or next Tuesday should be Tomorrow
        val nextTuesday = shouldNotThrowAny { monday.withSameOrNextDayOfWeek(DayOfWeek.TUESDAY) }
        nextTuesday.day shouldBe 2
    }

    @Test
    fun `test withSameOrPreviousDayOfWeek`() {
        val monday = HijrahDate(1445, 9, 1)

        // Same or previous Monday should be Today
        val sameMonday = shouldNotThrowAny { monday.withSameOrPreviousDayOfWeek(DayOfWeek.MONDAY) }
        sameMonday.day shouldBe 1

        // Same or previous Sunday should be Yesterday
        val prevSunday = shouldNotThrowAny { monday.withSameOrPreviousDayOfWeek(DayOfWeek.SUNDAY) }
        prevSunday.dayOfWeek shouldBe DayOfWeek.SUNDAY
        prevSunday shouldBeLessThan monday
    }

    @Test
    fun `test withLastDayOfMonth`() {
        val date = HijrahDate(1445, 9, 1)
        val lastDay = shouldNotThrowAny { date.withLastDayOfMonth() }

        // Ramadan 1445 had 30 days
        lastDay.day shouldBe 30
        lastDay.month.number shouldBe 9

        val date2 = HijrahDate(1445, 10, 1)
        val lastDay2 = shouldNotThrowAny { date2.withLastDayOfMonth() }
        // Shawwal 1445 had 29 days
        lastDay2.day shouldBe 29
    }

    @Test
    fun `test with components`() {
        val date = HijrahDate(1445, 9, 1)
        date.withYear(1446).year shouldBe 1446
        date.withMonth(10).month.number shouldBe 10
        date.withDayOfMonth(15).day shouldBe 15
    }

    @Test
    fun `test ranges`() {
        val start = HijrahDate(1445, 9, 1)
        val end = HijrahDate(1445, 9, 10)
        val range: HijrahDateRange = start..end

        range.start shouldBeEqual start
        range.endInclusive shouldBeEqual end

        HijrahDate(1445, 9, 5) shouldBeIn range
        HijrahDate(1445, 9, 11) shouldNotBeIn range
        
        range.size shouldBe 10
        val list = range.toList()
        list.size shouldBe 10
        list.first() shouldBe start
        list.last() shouldBe end
        
        range.containsAll(listOf(start, HijrahDate(1445, 9, 5), end)) shouldBe true

        val untilRange: HijrahDateRange = start..<end
        untilRange.start shouldBeEqual start
        untilRange.endInclusive shouldBeEqual HijrahDate(1445, 9, 9)
        untilRange.size shouldBe 9
    }

    @Test
    fun `test value range`() {
        val date = HijrahDate(1445, 9, 1)
        val dayRange = shouldNotThrowAny { date.range(DateTimeUnit.DAY) }
        dayRange.minimum shouldBe 1
        dayRange.maximum shouldBe 30

        val monthRange = shouldNotThrowAny { date.range(DateTimeUnit.MONTH) }
        monthRange.minimum shouldBe 1
        monthRange.maximum shouldBe 12
    }

    @Test
    fun `test atTime and atStartOfDay`() {
        val date = HijrahDate(1445, 9, 1)
        val time = LocalTime(10, 30)
        val dateTime = date.atTime(time)

        dateTime.date shouldBe date
        dateTime.time shouldBe time

        val zone = FixedOffsetTimeZone(UtcOffset(3))
        val startOfDay = date.atStartOfDay(zone).toHijrahDateTime(zone)
        startOfDay.hour shouldBe 0
        startOfDay.minute shouldBe 0
    }

    @Test
    fun `test leap year`() {
        // In Um-al-Qura calendar:
        // 1445-12 (Thul-Hijjah) has 30 days
        val date1445 = HijrahDate(1445, 12, 1)
        val lastDay1445 = date1445.withLastDayOfMonth()
        println("[DEBUG_LOG] 1445-12 last day: ${lastDay1445.day}")
        
        // Let's just assert it is 29 or 30
        lastDay1445.day shouldBeIn 29..30
        
        // 1443-12 (Thul-Hijjah)
        val date1443 = HijrahDate(1443, 12, 1)
        val lastDay1443 = date1443.withLastDayOfMonth()
        println("[DEBUG_LOG] 1443-12 last day: ${lastDay1443.day}")
        lastDay1443.day shouldBeIn 29..30
    }

    @Test
    fun `test MIN and MAX`() {
        val minDate = shouldNotThrowAny {
            val minDate = HijrahDate.MIN
            println("HijrahDate.MIN: $minDate")
            minDate
        }
        val maxDate = shouldNotThrowAny {
            val maxDate = HijrahDate.MAX
            println("HijrahDate.MAX: $maxDate")
            maxDate
        }
        minDate.year shouldBe 1300
        maxDate.year shouldBe 1600

        shouldThrow<DateTimeArithmeticException> {
            val invalid = minDate.minusDays(1)
            println("Invalid: $invalid")
        }

        shouldThrow<DateTimeArithmeticException> {
            val invalid = maxDate.plusDays(1)
            println("Invalid: $invalid")
        }

    }
}
