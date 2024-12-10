package com.abdulrahman_b.hijrahdatetime.formats

import com.abdulrahman_b.hijrahdatetime.EarlyHijrahDate
import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates.atStartOfDay
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_DATE
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_DATE_TIME
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_OFFSET_DATE
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_ZONED_DATE_TIME
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.LOCAL_TIME_12_HOURS
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.getRecommendedFormatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.HijrahDate
import java.time.temporal.UnsupportedTemporalTypeException
import java.util.Locale

class HijrahFormattersTest {

    @Test
    @DisplayName("Local time with 12 hours format and AM/PM works correctly")
    fun localTimeWith12HoursFormat() {

        val formatter = LOCAL_TIME_12_HOURS.withLocale(Locale.ENGLISH)

        var localTime = LocalTime.of(12, 0)
        var formatted = formatter.format(localTime)
        assertEquals("12:00:00 PM", formatted)

        localTime = LocalTime.of(0, 0)
        formatted = formatter.format(localTime)
        assertEquals("12:00:00 AM", formatted)

        localTime = LocalTime.of(23, 59)
        formatted = formatter.format(localTime)
        assertEquals("11:59:00 PM", formatted)

        localTime = LocalTime.of(11, 59)
        formatted = formatter.format(localTime)
        assertEquals("11:59:00 AM", formatted)

        localTime = LocalTime.of(4, 30, 15)
        formatted = formatter.format(localTime)
        assertEquals("4:30:15 AM", formatted)

        localTime = LocalTime.of(16, 30, 15)
        formatted = formatter.format(localTime)
        assertEquals("4:30:15 PM", formatted)

        localTime = LocalTime.of(16, 30, 15, 500)
        formatted = formatter.format(localTime)
        assertEquals("4:30:15.0000005 PM", formatted)

        localTime = LocalTime.of(16, 30, 15, 500_000_000)
        formatted = formatter.format(localTime)
        assertEquals("4:30:15.5 PM", formatted)
    }

    @Test
    @DisplayName("Recommends the correct Hijrah date format")
    fun hijrahDateFormatter() {
        val hijrahDate = HijrahDate.now()
        val earlyHijrahDate = EarlyHijrahDate.of(785, 10, 1)
        val hijrahDateTime = hijrahDate.atStartOfDay()
        val offsetHijrahDateTime = hijrahDateTime.atOffset(ZoneOffset.of("+03:00"))
        val offsetHijrahDate = offsetHijrahDateTime.toOffsetDate()
        val zonedHijrahDateTime = offsetHijrahDateTime.toZonedHijrahDateTime(ZoneId.of("Asia/Riyadh"))

        assertEquals(HIJRAH_DATE, getRecommendedFormatter(hijrahDate))
        assertEquals(HIJRAH_DATE, getRecommendedFormatter(earlyHijrahDate))
        assertEquals(HIJRAH_DATE_TIME, getRecommendedFormatter(hijrahDateTime))
        assertEquals(HIJRAH_OFFSET_DATE, getRecommendedFormatter(offsetHijrahDate))
        assertEquals(HIJRAH_OFFSET_DATE_TIME, getRecommendedFormatter(offsetHijrahDateTime))
        assertEquals(HIJRAH_ZONED_DATE_TIME, getRecommendedFormatter(zonedHijrahDateTime))
        assertThrows<UnsupportedTemporalTypeException> {
            getRecommendedFormatter(LocalDate.now())
        }
    }

}