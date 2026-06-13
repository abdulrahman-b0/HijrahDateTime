
package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.internal.getLengthOfMonth
import com.abdulrahman_b.hijrahdatetime.internal.getLengthOfYear
import io.kotest.matchers.shouldBe
import java.io.File
import java.time.LocalDate
import kotlin.system.measureTimeMillis
import kotlin.test.Test

class HijrahDateLookupValidationTest {

    private val propertiesFile = File("calendar-data/hijrah-config-islamic-umalqura.properties")

    @Test
    fun `validate library data against properties file`() {
        if (!propertiesFile.exists()) {
            println("Properties file not found at ${propertiesFile.absolutePath}, skipping test.")
            return
        }

        val lines = propertiesFile.readLines()
        val config = mutableMapOf<Int, List<Int>>()
        var isoStart = ""

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("#") || trimmed.isEmpty()) continue
            
            if (trimmed.startsWith("iso-start=")) {
                isoStart = trimmed.substringAfter("iso-start=")
                continue
            }

            if (trimmed.contains("=")) {
                val yearPart = trimmed.substringBefore("=").trim()
                val year = yearPart.toIntOrNull() ?: continue
                val months = trimmed.substringAfter("=").trim().split(Regex("\\s+")).map { it.toInt() }
                config[year] = months
            }
        }

        // 1. Verify ISO Start
        isoStart shouldBe "1882-11-12"
        val startEpochDay = LocalDate.parse(isoStart).toEpochDay()
        val firstDate = HijrahDate(1300, 1, 1)
        firstDate.toEpochDays() shouldBe startEpochDay

        // 2. Verify Every Month Length
        var expectedEpochDay = startEpochDay
        for (year in 1300..1600) {
            val months = config[year] ?: throw Exception("Missing year $year in config")
            months.size shouldBe 12
            
            var yearLength = 0
            for (m in 1..12) {
                val expectedLength = months[m - 1]
                val actualLength = getLengthOfMonth(year, m)
                
                actualLength shouldBe expectedLength
                
                // Verify day-by-day epoch increment
                for (d in 1..expectedLength) {
                    val date = HijrahDate(year, m, d)
                    date.toEpochDays() shouldBe expectedEpochDay
                    
                    val fromEpoch = HijrahDate.fromEpochDays(expectedEpochDay)
                    fromEpoch.year shouldBe year
                    fromEpoch.month.number shouldBe m
                    fromEpoch.day shouldBe d
                    
                    expectedEpochDay++
                }
                yearLength += expectedLength
            }
            
            // Verify year length
            val actualYearLength = getLengthOfYear(year)
            actualYearLength shouldBe yearLength
        }
        
        println("Successfully validated 301 years of Hijrah data against property file.")
    }

    @Test
    fun `test arithmetic boundary cases`() {
        val firstDate = HijrahDate(1300, 1, 1)
        val lastDate = HijrahDate(1600, 12, 30)

        // Test plus/minus 1 at boundaries
        (firstDate plusDays 1).toEpochDays() shouldBe firstDate.toEpochDays() + 1
        (lastDate minusDays 1).toEpochDays() shouldBe lastDate.toEpochDays() - 1
        
        // Test large arithmetic
        val middleDate = HijrahDate(1450, 6, 15)
        val daysToStart = (middleDate.toEpochDays() - firstDate.toEpochDays()).toInt()
        val daysToEnd = (lastDate.toEpochDays() - middleDate.toEpochDays()).toInt()
        
        (middleDate minusDays daysToStart) shouldBe firstDate
        (middleDate plusDays daysToEnd) shouldBe lastDate
    }

    @Test
    fun `extensive performance test through entire range`() {
        val firstDate = HijrahDate(1300, 1, 1)
        val lastDate = HijrahDate(1600, 12, 30)
        val totalDays = (lastDate.toEpochDays() - firstDate.toEpochDays()).toInt()
        val iterations = 5
        
        val time = measureTimeMillis {
            repeat(iterations) {
                var current = firstDate
                repeat(totalDays) {
                    current = current plusDays 1
                }
                current shouldBe lastDate
            }
        }
        
        println("Performance: Iterated through $totalDays days $iterations times in $time ms")
    }
}
