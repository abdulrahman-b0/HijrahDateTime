package com.abdulrahman_b.hijrahdatetime


import com.abdulrahman_b.hijrahdatetime.serialization.kotlinx.EarlyHijrahDateSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class EarlyHijrahDateSerializerTest {

    private val hijrahDate = EarlyHijrahDate.of(1200, 10, 1)
    private val jsonElement = "\"1200-10-01\""

    @Test
    @DisplayName("EarlyHijrahDate is deserialized properly")
    fun deserializeWithKotlinx() {

        var deserialized = Json.decodeFromString(EarlyHijrahDateSerializer, jsonElement)
        assertEquals(hijrahDate, deserialized)

        deserialized = JsonWithSerializerModuleApplied.decodeFromString(jsonElement)
        assertEquals(hijrahDate, deserialized)
    }

    @Test
    @DisplayName("EarlyHijrahDate is serialized properly")
    fun serializeWithKotlinx() {
        var serialized = Json.encodeToString(EarlyHijrahDateSerializer, hijrahDate)
        assertEquals(jsonElement, serialized)

        serialized = JsonWithSerializerModuleApplied.encodeToString(hijrahDate)
        assertEquals(jsonElement, serialized)

    }

}