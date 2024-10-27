package com.abdulrahman_b.hijrahDateTime.time.extensions

import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.extensions.DateTimeConversions.toHijrahDate
import com.abdulrahman_b.hijrahDateTime.time.extensions.DateTimeConversions.toHijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.extensions.DateTimeConversions.toOffsetHijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.extensions.DateTimeConversions.toZonedHijrahDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.HijrahDate

class DateTimesConversionsTest {


    @Test
    @DisplayName("LocalDate to HijrahDate is converted properly")
    fun localDateToHijrahDate() {

        val expected = HijrahDate.of(1446, 4, 25)

        val localDate = LocalDate.of(2024, 10, 28)
        val hijrahDate = localDate.toHijrahDate()

        assertEquals(expected, hijrahDate)

    }

    @Test
    @DisplayName("LocalDateTime to HijrahDateTime is converted properly")
    fun localDateTimeToHijrahDateTime() {

        val expected = HijrahDateTime.of(1446, 4, 25, 5, 12)

        val localDateTime = LocalDateTime.of(2024, 10, 28, 5, 12)
        val hijrahDateTime = localDateTime.toHijrahDateTime()

        assertEquals(expected, hijrahDateTime)

    }

    @Test
    @DisplayName("ZonedDateTime to ZonedHijrahDateTime is converted properly")
    fun zonedDateTimeToZonedHijrahDateTime() {

        val expected = HijrahDateTime.of(1446, 4, 25, 5, 12).atZone(ZoneId.of("Asia/Riyadh"))

        val zonedDateTime = LocalDateTime.of(2024, 10, 28, 5, 12).atZone(ZoneId.of("Asia/Riyadh"))
        val zonedHijrahDateTime = zonedDateTime.toZonedHijrahDateTime()

        assertEquals(expected, zonedHijrahDateTime)
    }

    @Test
    @DisplayName("OffsetDateTime to OffsetHijrahDateTime is converted properly")
    fun offsetDateTimeToOffsetHijrahDateTime() {

        val expected = HijrahDateTime.of(1446, 4, 25, 5, 12).atOffset(ZoneOffset.of("+03:00"))

        val offsetDateTime = LocalDateTime.of(2024, 10, 28, 5, 12).atOffset(ZoneOffset.of("+03:00"))
        val offsetHijrahDateTime = offsetDateTime.toOffsetHijrahDateTime()

        assertEquals(expected, offsetHijrahDateTime)
    }



}