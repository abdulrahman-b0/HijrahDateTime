package com.abdulrahman_b.hijrahdatetime.serialization.jackson

import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HijrahDateTimeSerializationTest {

    private val hijrahDateTime = HijrahDateTime.of(1446, 1, 10, 13, 30, 45)
    private val jsonElement = "\"1446-01-10T13:30:45\""
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
            .registerModules(HijrahChronoSerializersModule())
    }

    @Test
    fun deserialize() {

        val deserialized = objectMapper.readValue(jsonElement, HijrahDateTime::class.java)
        assertEquals(hijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {
        val serialized = objectMapper.writeValueAsString(hijrahDateTime)
        assertEquals(jsonElement, serialized)
    }

}