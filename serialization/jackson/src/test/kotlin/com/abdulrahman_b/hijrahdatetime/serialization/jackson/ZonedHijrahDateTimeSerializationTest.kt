package com.abdulrahman_b.hijrahdatetime.serialization.jackson

import com.abdulrahman_b.hijrahdatetime.ZonedHijrahDateTime
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZoneId

class ZonedHijrahDateTimeSerializationTest {

    private val zonedHijrahDateTime = ZonedHijrahDateTime.of(
        1446, 2, 5, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh")
    )
    private val jsonString = "\"1446-02-05T12:43:18+03:00[Asia/Riyadh]\""
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper().registerModules(HijrahChronoSerializersModule())
    }

    @Test
    fun deserialize() {

        val deserialized = objectMapper.readValue(jsonString, ZonedHijrahDateTime::class.java)
        assertEquals(zonedHijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {
        val serialized = objectMapper.writeValueAsString(zonedHijrahDateTime)
        assertEquals(jsonString, serialized)
    }
}