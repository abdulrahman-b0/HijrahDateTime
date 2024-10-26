package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters.HIJRAH_ZONED_DATE_TIME
import com.abdulrahman_b.hijrahDateTime.time.Instants.atHijrahOffset
import com.abdulrahman_b.hijrahDateTime.time.Instants.atHijrahZone
import com.abdulrahman_b.hijrahDateTime.time.Instants.toHijrahString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneOffset

class InstantExtensionsKtTest {

    private lateinit var instant: Instant

    @BeforeEach
    fun setUp() {
        val hdt = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.UTC)
        instant = hdt.toInstant()
    }

    @Test
    fun atZoneHijrah() {

        val zonedHijrahDateTime = instant.atHijrahZone(ZoneOffset.UTC)
        val expected = "1446-02-05T12:43:18Z"

        assertEquals(expected, zonedHijrahDateTime.format(HIJRAH_ZONED_DATE_TIME))
    }

    @Test
    fun atOffsetHijrah() {

        val offsetHijrahDateTime = instant.atHijrahOffset(ZoneOffset.UTC)
        val expected = "1446-02-05T12:43:18Z"

        assertEquals(expected, offsetHijrahDateTime.format(HIJRAH_OFFSET_DATE_TIME))
    }

    @Test
    fun toHijrahString() {

        val hijrahString = instant.toHijrahString()
        val expected = "1446-02-05T12:43:18Z"

        assertEquals(expected, hijrahString)
    }
}