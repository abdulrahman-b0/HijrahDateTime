package com.abdulrahman_b.hijrah_datetime


import com.abdulrahman_b.hijrah_datetime.serialization.kotlinx.HijrahDateSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.chrono.HijrahDate

class HijrahDateSerializerTest {

    private val hijrahDate = HijrahDate.of(1446, 10, 1)
    private val jsonElement = "\"1446-10-01\""

    @Test
    @DisplayName("HijrahDate is deserialized properly")
    fun deserializeWithKotlinx() {

        var deserialized = Json.decodeFromString(HijrahDateSerializer, jsonElement)
        assertEquals(hijrahDate, deserialized)

        deserialized = JsonWithSerializerModuleApplied.decodeFromString(jsonElement)
        assertEquals(hijrahDate, deserialized)
    }

    @Test
    @DisplayName("HijrahDate is serialized properly")
    fun serializeWithKotlinx() {
        var serialized = Json.encodeToString(HijrahDateSerializer, hijrahDate)
        assertEquals(jsonElement, serialized)

        serialized = JsonWithSerializerModuleApplied.encodeToString(hijrahDate)
        assertEquals(jsonElement, serialized)

    }

}