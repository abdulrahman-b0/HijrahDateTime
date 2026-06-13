
package com.abdulrahman_b.hijrahdatetime

import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import java.time.chrono.HijrahChronology
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import java.time.chrono.HijrahDate as JavaHijrahDate

class HijrahDateJvmTest {

    private fun createJavaDate(year: Int, month: Int, day: Int): JavaHijrahDate {
        return HijrahChronology.INSTANCE.date(year, month, day)
    }

    @Test
    fun `test HijrahDate properties match java_time`() {
        // Extensive coverage for years 1300 to 1600
        for (year in 1300..1600) {
            createJavaDate(year, 1, 1)
            if (year < 1600) {
                var total = 0
                for (m in 1..12) total += createJavaDate(year, m, 1).lengthOfMonth()
            }
            
            // To speed up, we can test every 5th day, but still covering all months and years
            for (month in 1..12) {
                val tempJavaDate = createJavaDate(year, month, 1)
                val lengthOfMonth = tempJavaDate.lengthOfMonth()
                for (day in 1..lengthOfMonth) {
                    val myDate = HijrahDate(year, month, day)
                    val javaDate = createJavaDate(year, month, day)

                    myDate.year shouldBe javaDate.get(ChronoField.YEAR)
                    myDate.month.number shouldBe javaDate.get(ChronoField.MONTH_OF_YEAR)
                    myDate.day shouldBe javaDate.get(ChronoField.DAY_OF_MONTH)
                    myDate.dayOfWeek.isoDayNumber shouldBe javaDate.get(ChronoField.DAY_OF_WEEK)
                    myDate.dayOfYear shouldBe javaDate.get(ChronoField.DAY_OF_YEAR)
                    myDate.toEpochDays() shouldBe javaDate.toEpochDay()

                    val fromEpoch = HijrahDate.fromEpochDays(javaDate.toEpochDay())
                    fromEpoch.year shouldBe myDate.year
                    fromEpoch.month.number shouldBe myDate.month.number
                    fromEpoch.day shouldBe myDate.day
                }
            }
        }
    }

    @Test
    fun `test arithmetic plusDays and minusDays match java_time`() {
        val testDates = listOf(
            HijrahDate(1445, 1, 1),
            HijrahDate(1445, 12, 29),
            HijrahDate(1446, 6, 15),
            HijrahDate(1300, 1, 1),
            HijrahDate(1599, 12, 29)
        )
        val increments = listOf(1, 7, 30, 354, 1000, -1, -7, -30, -354, -1000)

        for (myDate in testDates) {
            val javaDate = createJavaDate(myDate.year, myDate.month.number, myDate.day)
            for (inc in increments) {
                val expectedJavaDate = try {
                    javaDate.plus(inc.toLong(), ChronoUnit.DAYS)
                } catch (_: Exception) {
                    null
                }
                
                if (expectedJavaDate != null && 
                    expectedJavaDate.get(ChronoField.YEAR) in 1300..1600) {
                    
                    // plusDays
                    val plusResult = myDate plusDays inc
                    plusResult.toEpochDays() shouldBe expectedJavaDate.toEpochDay()
                    
                    // minusDays
                    val minusResult = myDate minusDays (-inc)
                    minusResult.toEpochDays() shouldBe expectedJavaDate.toEpochDay()
                }
            }
        }
    }

    @Test
    fun `test withAdjusters match java_time`() {
        val testDates = listOf(
            HijrahDate(1445, 1, 1),
            HijrahDate(1445, 2, 30), // Safar 1445 has 30 days
            HijrahDate(1446, 6, 15)
        )
        val daysOfWeek = DayOfWeek.entries

        for (myDate in testDates) {
            val javaDate = createJavaDate(myDate.year, myDate.month.number, myDate.day)
            
            for (dow in daysOfWeek) {
                // Next
                val myNext = myDate.withNextDayOfWeek(dow)
                val javaNext = javaDate.with(java.time.temporal.TemporalAdjusters.next(java.time.DayOfWeek.of(dow.isoDayNumber)))
                myNext.toEpochDays() shouldBe javaNext.toEpochDay()
                
                // Previous
                val myPrev = myDate.withPreviousDayOfWeek(dow)
                val javaPrev = javaDate.with(java.time.temporal.TemporalAdjusters.previous(java.time.DayOfWeek.of(dow.isoDayNumber)))
                myPrev.toEpochDays() shouldBe javaPrev.toEpochDay()
                
                // Same or Next
                val mySameNext = myDate.withSameOrNextDayOfWeek(dow)
                val javaSameNext = javaDate.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.of(dow.isoDayNumber)))
                mySameNext.toEpochDays() shouldBe javaSameNext.toEpochDay()
                
                // Same or Previous
                val mySamePrev = myDate.withSameOrPreviousDayOfWeek(dow)
                val javaSamePrev = javaDate.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.of(dow.isoDayNumber)))
                mySamePrev.toEpochDays() shouldBe javaSamePrev.toEpochDay()
            }
            
            // Last day of month
            val myLast = myDate.withLastDayOfMonth()
            val javaLast = javaDate.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth())
            myLast.toEpochDays() shouldBe javaLast.toEpochDay()
        }
    }

    @Test
    fun `performance test - arithmetic`() {
        val iterations = 50_000
        val myDate = HijrahDate(1445, 1, 1)
        val javaDate = createJavaDate(1445, 1, 1)
        
        val timeMyLib = measureTimeMillis {
            var current = myDate
            repeat(iterations) {
                current = current plusDays 1
                if (current.year > 1590) current = myDate
            }
        }
        
        val timeJava = measureTimeMillis {
            var current = javaDate
            repeat(iterations) {
                current = current.plus(1, ChronoUnit.DAYS)
                if (current.get(ChronoField.YEAR) > 1590) current = javaDate
            }
        }
        
        println("Arithmetic Performance ($iterations iterations):")
        println("My library: $timeMyLib ms")
        println("Java time: $timeJava ms")
        timeMyLib shouldBeGreaterThan 0L
    }

    @Test
    fun `test arithmetic plusMonths match java_time`() {
        val testDates = listOf(
            HijrahDate(1445, 1, 1),
            HijrahDate(1445, 2, 30), // Safar 1445 has 30 days
            HijrahDate(1446, 6, 15)
        )
        val increments = listOf(1, 6, 12, 24, -1, -6, -12, -24)

        for (myDate in testDates) {
            val javaDate = createJavaDate(myDate.year, myDate.month.number, myDate.day)
            for (inc in increments) {
                val expectedJavaDate = try {
                    javaDate.plus(inc.toLong(), ChronoUnit.MONTHS)
                } catch (_: Exception) {
                    null
                }

                if (expectedJavaDate != null && 
                    expectedJavaDate.get(ChronoField.YEAR) in 1300..1600) {
                    val resultDate = myDate plusMonths inc
                    resultDate.toEpochDays() shouldBe expectedJavaDate.toEpochDay()
                }
            }
        }
    }

    @Test
    fun `test units arithmetic match java_time`() {
        val testDates = listOf(
            HijrahDate(1445, 1, 1),
            HijrahDate(1446, 6, 15)
        )
        val units = listOf(
            DateTimeUnit.DAY to ChronoUnit.DAYS,
            DateTimeUnit.WEEK to ChronoUnit.WEEKS,
            DateTimeUnit.MONTH to ChronoUnit.MONTHS,
            DateTimeUnit.YEAR to ChronoUnit.YEARS,
            DateTimeUnit.CENTURY to ChronoUnit.CENTURIES
        )
        val values = listOf(1, 5, -1, -5)

        for (myDate in testDates) {
            val javaDate = createJavaDate(myDate.year, myDate.month.number, myDate.day)
            for ((myUnit, javaUnit) in units) {
                for (v in values) {
                    val expectedJavaDate = try {
                        javaDate.plus(v.toLong(), javaUnit)
                    } catch (_: Exception) {
                        null
                    }

                    if (expectedJavaDate != null && 
                        expectedJavaDate.get(ChronoField.YEAR) in 1300..1600) {
                        val plusResult = myDate.plus(v, myUnit)
                        plusResult.toEpochDays() shouldBe expectedJavaDate.toEpochDay()
                        
                        val minusResult = myDate.minus(v, myUnit)
                        minusResult.toEpochDays() shouldBe javaDate.minus(v.toLong(), javaUnit).toEpochDay()
                    }
                }
            }
        }
    }

    @Test
    fun `performance test - creation and epoch conversion`() {
        val iterations = 100_000
        val timeMyLib = measureTimeMillis {
            var year = 1300
            var month = 1
            var day = 1
            repeat(iterations) {
                val date = HijrahDate(year, month, day)
                date.toEpochDays()
                // Simple increment for next iteration
                day++
                if (day > 29) {
                    day = 1
                    month++
                    if (month > 12) {
                        month = 1
                        year++
                    }
                }
            }
        }
        
        val timeJava = measureTimeMillis {
            var year = 1300
            var month = 1
            var day = 1
            repeat(iterations) {
                val date = createJavaDate(year, month, day)
                date.toEpochDay()
                day++
                if (day > 29) {
                    day = 1
                    month++
                    if (month > 12) {
                        month = 1
                        year++
                    }
                }
            }
        }
        
        println("Performance test ($iterations iterations):")
        println("My library: $timeMyLib ms")
        println("Java time: $timeJava ms")
        
        // We don't necessarily need to be faster than java.time, but we should be reasonable.
        timeMyLib shouldBeGreaterThan 0
    }
}
