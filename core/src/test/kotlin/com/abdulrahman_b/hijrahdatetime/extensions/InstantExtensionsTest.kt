package com.abdulrahman_b.hijrahdatetime.extensions

import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.extensions.Instants.atHijrahOffset
import com.abdulrahman_b.hijrahdatetime.extensions.Instants.atHijrahZone
import com.abdulrahman_b.hijrahdatetime.extensions.Instants.toHijrahString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneOffset

class InstantExtensionsTest {

    private lateinit var instant: Instant

    @BeforeEach
    fun setUp() {
        val hdt = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.UTC)
        instant = hdt.toInstant()
    }

    @Test
    fun atZoneHijrah() {

        val zonedHijrahDateTime = instant.atHijrahZone(ZoneOffset.UTC)
        val expected = ZonedHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.UTC)

        assertEquals(expected, zonedHijrahDateTime)

        val localDateTime = instant.atZone(ZoneOffset.UTC)
        val asHijrah = ZonedHijrahDateTime.from(localDateTime)

        assertEquals(expected, asHijrah) //Extra validation
    }

    @Test
    fun atOffsetHijrah() {

        val offsetHijrahDateTime = instant.atHijrahOffset(ZoneOffset.UTC)
        val expected = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.UTC)

        assertEquals(expected, offsetHijrahDateTime)

        val localDateTime = instant.atOffset(ZoneOffset.UTC)
        val asHijrah = OffsetHijrahDateTime.from(localDateTime)

        assertEquals(expected, asHijrah) //Extra validation
    }

    @Test
    fun toHijrahString() {

        val hijrahString = instant.toHijrahString()
        val expected = "1446-02-05T12:43:18Z"

        assertEquals(expected, hijrahString)
    }
}