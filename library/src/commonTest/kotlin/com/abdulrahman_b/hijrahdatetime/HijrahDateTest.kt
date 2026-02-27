package com.abdulrahman_b.hijrahdatetime

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.ranges.shouldNotBeIn
import io.kotest.matchers.shouldBe
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.time.Instant

class HijrahDateTest {

    @Test
    fun `test Hijri creation`() {
        val date = shouldNotThrowAny { HijrahDate(1445, 9, 1) }
        date.year shouldBe 1445
        date.month shouldBe 9
        date.dayOfMonth shouldBe 1
        date.dayOfWeek shouldBe DayOfWeek.MONDAY
    }

    @Test
    fun `test arithmetic days`() {
        // 1445-09-29 + 1 day should be 1445-09-30 (Ramadan 1445 has 30 days)
        val date = shouldNotThrowAny { HijrahDate(1445, 9, 29) }
        val nextDay = shouldNotThrowAny { date plusDays 1 }

        nextDay.dayOfMonth shouldBe 30
        nextDay.month shouldBe 9

        val nextMonthFirst = shouldNotThrowAny { nextDay plusDays 1 }
        nextMonthFirst.dayOfMonth shouldBe 1
        nextMonthFirst.month shouldBe 10

        val prevDay = shouldNotThrowAny { nextMonthFirst minusDays 1 }
        prevDay.dayOfMonth shouldBe 30
        prevDay.month shouldBe 9
    }

    @Test
    fun `test arithmetic months`() {
        val date = HijrahDate(1446, 1, 29)
        val nextMonth = shouldNotThrowAny { date plusMonths 1 }
        nextMonth.month shouldBe 2
        nextMonth.dayOfMonth shouldBe 29

        val nextYear = shouldNotThrowAny { date plusYears 1 }
        nextYear.year shouldBe 1447
        nextYear.month shouldBe 1
        nextYear.dayOfMonth shouldBe 29

        val minusMonth = shouldNotThrowAny { nextMonth minusMonths 1 }
        minusMonth.month shouldBe 1
        minusMonth.dayOfMonth shouldBe 29
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
        dateFromEpoch.month shouldBe 1
        dateFromEpoch.dayOfMonth shouldBe 1
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
        date.month shouldBe 9
        date.dayOfMonth shouldBe 1
    }

    @Test
    fun `test withNextDayOfWeek`() {
        // 1445-09-01 is Monday
        val monday = HijrahDate(1445, 9, 1)
        monday.dayOfWeek shouldBe DayOfWeek.MONDAY

        // Next Wednesday should be 1445-09-03
        val nextWednesday = monday.withNextDayOfWeek(DayOfWeek.WEDNESDAY)
        nextWednesday.dayOfWeek shouldBe DayOfWeek.WEDNESDAY
        nextWednesday.dayOfMonth shouldBe 3

        // Next Monday should be 1445-09-08
        val nextMonday = monday.withNextDayOfWeek(DayOfWeek.MONDAY)
        nextMonday.dayOfMonth shouldBe 8
        nextMonday.dayOfWeek shouldBe DayOfWeek.MONDAY
    }

    @Test
    fun `test withPreviousDayOfWeek`() {
        // 1445-09-01 is Monday
        val monday = HijrahDate(1445, 9, 1)

        // Previous Friday should be 1445-08-27
        // 2024-03-08 is 1445-08-27
        val prevFriday = shouldNotThrowAny { monday.withPreviousDayOfWeek(DayOfWeek.FRIDAY) }
        prevFriday.dayOfMonth shouldBe 27
        prevFriday.month shouldBe 8
        prevFriday.dayOfWeek shouldBe DayOfWeek.FRIDAY

        // Previous Monday should be 1445-08-23
        val prevMonday = shouldNotThrowAny { monday.withPreviousDayOfWeek(DayOfWeek.MONDAY) }
        prevMonday.dayOfWeek shouldBe DayOfWeek.MONDAY
        prevMonday.dayOfMonth shouldBe 23
        prevMonday.month shouldBe 8
    }

    @Test
    fun `test withSameOrNextDayOfWeek`() {
        val monday = HijrahDate(1445, 9, 1)

        // Same or next Monday should be Today
        val sameMonday = shouldNotThrowAny { monday.withSameOrNextDayOfWeek(DayOfWeek.MONDAY) }
        sameMonday.dayOfMonth shouldBe 1

        // Same or next Tuesday should be Tomorrow
        val nextTuesday = shouldNotThrowAny { monday.withSameOrNextDayOfWeek(DayOfWeek.TUESDAY) }
        nextTuesday.dayOfMonth shouldBe 2
    }

    @Test
    fun `test withSameOrPreviousDayOfWeek`() {
        val monday = HijrahDate(1445, 9, 1)

        // Same or previous Monday should be Today
        val sameMonday = shouldNotThrowAny { monday.withSameOrPreviousDayOfWeek(DayOfWeek.MONDAY) }
        sameMonday.dayOfMonth shouldBe 1

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
        lastDay.dayOfMonth shouldBe 30
        lastDay.month shouldBe 9

        val date2 = HijrahDate(1445, 10, 1)
        val lastDay2 = shouldNotThrowAny { date2.withLastDayOfMonth() }
        // Shawwal 1445 had 29 days
        lastDay2.dayOfMonth shouldBe 29
    }

    @Test
    fun `test with components`() {
        val date = HijrahDate(1445, 9, 1)
        date.withYear(1446).year shouldBe 1446
        date.withMonth(10).month shouldBe 10
        date.withDayOfMonth(15).dayOfMonth shouldBe 15
    }

    @Test
    fun `test ranges`() {
        val start = HijrahDate(1445, 9, 1)
        val end = HijrahDate(1445, 9, 10)
        val range: ClosedRange<HijrahDate> = start..end

        range.start shouldBeEqual start
        range.endInclusive shouldBeEqual end

        HijrahDate(1445, 9, 5) shouldBeIn range
        HijrahDate(1445, 9, 11) shouldNotBeIn range

        val untilRange: ClosedRange<HijrahDate> = start..<end
        untilRange.start shouldBeEqual start
        untilRange.endInclusive shouldBeEqual HijrahDate(1445, 9, 9)
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

        val startOfDay = date.atStartOfDay()
        startOfDay.hour shouldBe 0
        startOfDay.minute shouldBe 0
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

    }
}
