package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

class HijrahDateSerializationTest {

    private lateinit var hijrahDate: HijrahDate
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        hijrahDate = HijrahDate.of(1446, 10, 1)
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withChronology(HijrahChronology.INSTANCE)
        objectMapper = ObjectMapper()
            .registerModules(HijrahJacksonSerializersModule(hijrahDateFormatter = formatter))
            .registerKotlinModule()
    }

    @Test
    fun serialize() {
        var serialized = objectMapper.writeValueAsString(hijrahDate)
        assertEquals("\"1446/10/01\"", serialized)

        serialized = objectMapper.writeValueAsString(TestClass(hijrahDate))
        assertEquals("""{"hijrahDate":"1446-10-01"}""", serialized)
    }

    @Test
    fun deserialize() {
        val value = "\"1446/10/01\""
        val deserialized = objectMapper.readValue(value, HijrahDate::class.java)
        assertEquals(hijrahDate, deserialized)

        val testClassValue = """{"hijrahDate":"1446-10-01"}"""
        val testClassDeserialized = objectMapper.readValue(testClassValue, TestClass::class.java)
        assertEquals(hijrahDate, testClassDeserialized.hijrahDate)
    }

    private data class TestClass(
        @JsonSerialize(using = HijrahDateSerializer::class)
        @JsonDeserialize(using = HijrahDateDeserializer::class)
        val hijrahDate: HijrahDate
    )

}