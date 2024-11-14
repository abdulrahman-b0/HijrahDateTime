package com.abdulrahman_b.hijrahdatetime.serialization.jackson

import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDateTime
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZoneOffset

class OffsetHijrahDateTimeSerializationTest {

    private val offsetHijrahDateTime =
        OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.ofHours(3))
    private val jsonString = "\"1446-02-05T12:43:18+03:00\""
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper().registerModules(HijrahChronoSerializersModule())
    }

    @Test
    fun deserialize() {

        val deserialized = objectMapper.readValue(jsonString, OffsetHijrahDateTime::class.java)
        assertEquals(offsetHijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {
        val serialized = objectMapper.writeValueAsString(offsetHijrahDateTime)
        assertEquals(jsonString, serialized)
    }
}