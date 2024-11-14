package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serialization.kotlinx.OffsetHijrahDateTimeSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.ZoneOffset

class OffsetHijrahDateTimeSerializerTest {

    private val offsetHijrahDateTime = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.ofHours(3))
    private val jsonString = "\"1446-02-05T12:43:18+03:00\""

    @Test
    fun deserialize() {
        var deserialized = Json.decodeFromString(OffsetHijrahDateTimeSerializer, jsonString)
        assertEquals(offsetHijrahDateTime, deserialized)

        deserialized = JsonWithSerializerModuleApplied.decodeFromString(jsonString)
        assertEquals(offsetHijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {
        var serialized = Json.encodeToString(OffsetHijrahDateTimeSerializer, offsetHijrahDateTime)
        assertEquals(jsonString, serialized)

        serialized = JsonWithSerializerModuleApplied.encodeToString(offsetHijrahDateTime)
        assertEquals(jsonString, serialized)
    }

}