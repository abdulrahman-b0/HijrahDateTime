package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serialization.kotlinx.OffsetHijrahDateSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.ZoneOffset

class OffsetHijrahDateSerializerTest {

    private val offsetHijrahDate = OffsetHijrahDate.of(1446, 2, 5, ZoneOffset.ofHours(3))
    private val jsonString = "\"1446-02-05+03:00\""

    @Test
    fun deserialize() {
        var deserialized = Json.decodeFromString(OffsetHijrahDateSerializer, jsonString)
        assertEquals(offsetHijrahDate, deserialized)

        deserialized = JsonWithSerializerModuleApplied.decodeFromString(jsonString)
        assertEquals(offsetHijrahDate, deserialized)
    }

    @Test
    fun serialize() {
        var serialized = Json.encodeToString(OffsetHijrahDateSerializer, offsetHijrahDate)
        assertEquals(jsonString, serialized)

        serialized = JsonWithSerializerModuleApplied.encodeToString(offsetHijrahDate)
        assertEquals(jsonString, serialized)
    }

}