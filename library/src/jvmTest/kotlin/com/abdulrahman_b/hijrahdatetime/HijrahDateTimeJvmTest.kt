
package com.abdulrahman_b.hijrahdatetime

import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinMonth
import java.time.ZoneOffset
import java.time.chrono.ChronoLocalDateTime
import kotlin.test.Test
import java.time.LocalTime as JavaLocalTime
import java.time.chrono.HijrahDate as JavaHijrahDate

class HijrahDateTimeJvmTest {

    @Test
    fun `compare HijrahDateTime with java time ChronoLocalDateTime`() {
        // Test a range of years and various times of day
        val years = listOf(1300, 1400, 1445, 1500, 1600)
        val months = listOf(1, 6, 12)
        val days = listOf(1, 15, 29, 30)
        val times = listOf(
            JavaLocalTime.MIDNIGHT,
            JavaLocalTime.NOON,
            JavaLocalTime.of(23, 59, 59, 999_999_999),
            JavaLocalTime.of(10, 30, 45, 123_456_789)
        )

        for (y in years) {
            for (m in months) {
                for (d in days) {
                    val javaDate = try {
                        JavaHijrahDate.of(y, m, d)
                    } catch (_: Exception) {
                        continue // Skip invalid dates like 30th of a 29-day month
                    }

                    for (javaTime in times) {
                        val javaLocalDateTime: ChronoLocalDateTime<JavaHijrahDate> = javaDate.atTime(javaTime)
                        
                        val hijrahDateTime = HijrahDateTime(
                            y, m, d, 
                            javaTime.hour, javaTime.minute, javaTime.second, javaTime.nano
                        )

                        // 1. Property Matching
                        hijrahDateTime.year shouldBe javaLocalDateTime.get(java.time.temporal.ChronoField.YEAR)
                        hijrahDateTime.month.number shouldBe javaLocalDateTime.get(java.time.temporal.ChronoField.MONTH_OF_YEAR)
                        hijrahDateTime.day shouldBe javaLocalDateTime.get(java.time.temporal.ChronoField.DAY_OF_MONTH)
                        hijrahDateTime.hour shouldBe javaTime.hour
                        hijrahDateTime.minute shouldBe javaTime.minute
                        hijrahDateTime.second shouldBe javaTime.second
                        hijrahDateTime.nanosecond shouldBe javaTime.nano
                        
                        hijrahDateTime.dayOfWeek.name shouldBe DayOfWeek.entries[javaLocalDateTime.get(java.time.temporal.ChronoField.DAY_OF_WEEK) - 1].name
                        hijrahDateTime.dayOfYear shouldBe javaLocalDateTime.get(java.time.temporal.ChronoField.DAY_OF_YEAR)

                        // 2. Conversion to Instant (Fixed Offset)
                        val offset = ZoneOffset.ofHours(3)
                        val kotlinxTz = TimeZone.of("UTC+03:00") as FixedOffsetTimeZone
                        
                        val javaInstant = javaLocalDateTime.toInstant(offset)
                        val kotlinxInstant = hijrahDateTime.toInstant(kotlinxTz)
                        
                        kotlinxInstant.epochSeconds shouldBe javaInstant.epochSecond
                        kotlinxInstant.nanosecondsOfSecond shouldBe javaInstant.nano

                        // 3. Conversion to LocalDateTime (Gregorian)
                        val javaLdt = java.time.LocalDateTime.from(javaLocalDateTime)
                        val kotlinxLdt = hijrahDateTime.toLocalDateTime()
                        
                        kotlinxLdt.year shouldBe javaLdt.year
                        kotlinxLdt.month.ordinal shouldBe javaLdt.monthValue - 1
                        kotlinxLdt.day shouldBe javaLdt.dayOfMonth
                        kotlinxLdt.hour shouldBe javaLdt.hour
                        kotlinxLdt.minute shouldBe javaLdt.minute
                        kotlinxLdt.second shouldBe javaLdt.second
                        kotlinxLdt.nanosecond shouldBe javaLdt.nano
                    }
                }
            }
        }
    }

    @Test
    fun `test start of range boundary`() {
        // Arrange
        val startHdt = HijrahDateTime(1300, 1, 1, 0, 0, 0, 0)
        
        // Act
        val ldt = startHdt.toLocalDateTime()
        
        // Assert
        ldt.year shouldBe 1882
        ldt.month shouldBe java.time.Month.NOVEMBER.toKotlinMonth()
        ldt.day shouldBe 12
    }

    @Test
    fun `test end of range boundary`() {
        // Arrange
        val endHdt = HijrahDateTime(1600, 12, 30, 23, 59, 59, 999_999_999)
        val javaEndDate = JavaHijrahDate.of(1600, 12, 30)
        val javaEndLdt = javaEndDate.atTime(JavaLocalTime.of(23, 59, 59, 999_999_999))
        val javaLdt = java.time.LocalDateTime.from(javaEndLdt)
        
        // Act
        val kotlinxLdt = endHdt.toLocalDateTime()
        
        // Assert
        kotlinxLdt.year shouldBe javaLdt.year
        kotlinxLdt.month.ordinal shouldBe javaLdt.monthValue - 1
        kotlinxLdt.day shouldBe javaLdt.dayOfMonth
    }
}
