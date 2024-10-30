package com.abdulrahman_b.hijrahDateTime.formats

import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters.HIJRAH_DATE
import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters.HIJRAH_DATE_TIME
import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters.HIJRAH_ZONED_DATE_TIME
import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters.getRecommendedFormatter
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.atStartOfDay
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.HijrahDate
import java.time.temporal.UnsupportedTemporalTypeException

class HijrahFormattersTest {

    @Test
    @DisplayName("Recommends the correct Hijrah date format")
    fun hijrahDateFormatter() {
        val hijrahDate = HijrahDate.now()
        val hijrahDateTime = hijrahDate.atStartOfDay()
        val offsetHijrahDateTime = hijrahDateTime.atOffset(ZoneOffset.of("+03:00"))
        val zonedHijrahDateTime = offsetHijrahDateTime.toZonedHijrahDateTime(ZoneId.of("Asia/Riyadh"))

        assertEquals(HIJRAH_DATE, getRecommendedFormatter(hijrahDate))
        assertEquals(HIJRAH_DATE_TIME, getRecommendedFormatter(hijrahDateTime))
        assertEquals(HIJRAH_OFFSET_DATE_TIME, getRecommendedFormatter(offsetHijrahDateTime))
        assertEquals(HIJRAH_ZONED_DATE_TIME, getRecommendedFormatter(zonedHijrahDateTime))
        assertThrows<UnsupportedTemporalTypeException> {
            getRecommendedFormatter(LocalDate.now())
        }
    }

}