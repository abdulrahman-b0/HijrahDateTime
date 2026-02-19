package com.abdulrahman_b.hijrahdatetime


import com.abdulrahman_b.hijrahdatetime.serialization.kotlinx.EarlyHijrahDateSerializer
import com.abdulrahman_b.hijrahdatetime.serialization.kotlinx.SimpleHijrahDateSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SimpleHijrahDateSerializerTest {

    private val hijrahDate = SimpleHijrahDate.of(1200, 10, 1)
    private val jsonElement = "\"1200-10-01\""

    @Test
    @DisplayName("SimpleHijrahDate is deserialized properly")
    fun deserializeWithKotlinx() {

        var deserialized = Json.decodeFromString(SimpleHijrahDateSerializer, jsonElement)
        assertEquals(hijrahDate, deserialized)

        deserialized = JsonWithSerializerModuleApplied.decodeFromString(jsonElement)
        assertEquals(hijrahDate, deserialized)
    }

    @Test
    @DisplayName("SimpleHijrahDate is serialized properly")
    fun serializeWithKotlinx() {
        var serialized = Json.encodeToString(SimpleHijrahDateSerializer, hijrahDate)
        assertEquals(jsonElement, serialized)

        serialized = JsonWithSerializerModuleApplied.encodeToString(hijrahDate)
        assertEquals(jsonElement, serialized)

    }

}