package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDate
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZoneOffset

class OffsetHijrahDateSerializationTest {

    private val offsetHijrahDateTime =
        OffsetHijrahDate.of(1446, 2, 5, ZoneOffset.ofHours(3))
    private val jsonString = "\"1446-02-05+03:00\""
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper().registerModules(HijrahChronoSerializersModule())
    }

    @Test
    fun deserialize() {

        val deserialized = objectMapper.readValue(jsonString, OffsetHijrahDate::class.java)
        assertEquals(offsetHijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {
        val serialized = objectMapper.writeValueAsString(offsetHijrahDateTime)
        assertEquals(jsonString, serialized)
    }
}