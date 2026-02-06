package com.abdulrahman_b.hijrahdatetime.serialization.jackson

import com.abdulrahman_b.hijrahdatetime.EarlyHijrahDate
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter

class EarlyHijrahDateSerializationTest {

    private lateinit var hijrahDate: EarlyHijrahDate
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        hijrahDate = EarlyHijrahDate.of(1165, 10, 1)
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withChronology(HijrahChronology.INSTANCE)
        objectMapper = ObjectMapper()
            .registerModules(HijrahChronoSerializersModule(hijrahDateFormatter = formatter))
            .registerKotlinModule()
    }

    @Test
    fun serialize() {
        var serialized = objectMapper.writeValueAsString(hijrahDate)
        assertEquals("\"1165/10/01\"", serialized)

        serialized = objectMapper.writeValueAsString(TestClass(hijrahDate))
        assertEquals("""{"hijrahDate":"1165-10-01"}""", serialized)
    }

    @Test
    fun deserialize() {
        val value = "\"1165/10/01\""
        val deserialized = objectMapper.readValue(value, EarlyHijrahDate::class.java)
        assertEquals(hijrahDate, deserialized)

        val testClassValue = """{"hijrahDate":"1165-10-01"}"""
        val testClassDeserialized = objectMapper.readValue(testClassValue, TestClass::class.java)
        assertEquals(hijrahDate, testClassDeserialized.hijrahDate)
    }

    private data class TestClass(
        @JsonSerialize(using = EarlyHijrahDateSerialization.Serializer::class)
        @JsonDeserialize(using = EarlyHijrahDateSerialization.Deserializer::class)
        val hijrahDate: EarlyHijrahDate
    )

}