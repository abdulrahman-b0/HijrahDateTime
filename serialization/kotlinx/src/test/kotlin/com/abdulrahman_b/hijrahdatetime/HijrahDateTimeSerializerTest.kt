package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serialization.kotlinx.HijrahDateTimeSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HijrahDateTimeSerializerTest {

    private val hijrahDateTime = HijrahDateTime.of(1446, 1, 10, 13, 30, 45)
    private val jsonElement = "\"1446-01-10T13:30:45\""

    @Test
    fun deserialize() {

        var deserialized = Json.decodeFromString(HijrahDateTimeSerializer, jsonElement)
        assertEquals(hijrahDateTime, deserialized)

        deserialized = JsonWithSerializerModuleApplied.decodeFromString(jsonElement)
        assertEquals(hijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {
        var serialized = Json.encodeToString(HijrahDateTimeSerializer, hijrahDateTime)
        assertEquals(jsonElement, serialized)

        serialized = JsonWithSerializerModuleApplied.encodeToString(hijrahDateTime)
        assertEquals(jsonElement, serialized)
    }
}