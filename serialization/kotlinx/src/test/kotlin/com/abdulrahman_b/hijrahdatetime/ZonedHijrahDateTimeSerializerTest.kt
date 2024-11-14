package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serialization.kotlinx.ZonedHijrahDateTimeSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.ZoneId

class ZonedHijrahDateTimeSerializerTest {

    private val zonedHijrahDateTime = ZonedHijrahDateTime.of(
        1446, 2, 5, 12, 43, 18, 0, ZoneId.of("Asia/Riyadh")
    )
    private val jsonString = "\"1446-02-05T12:43:18+03:00[Asia/Riyadh]\""

    @Test
    fun deserialize() {

        var deserialized = Json.decodeFromString(ZonedHijrahDateTimeSerializer, jsonString)
        assertEquals(zonedHijrahDateTime, deserialized)

        deserialized = JsonWithSerializerModuleApplied.decodeFromString(jsonString)
        assertEquals(zonedHijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {

        var serialized = Json.encodeToString(
            ZonedHijrahDateTimeSerializer,
            zonedHijrahDateTime
        )
        assertEquals(jsonString, serialized)

        serialized = JsonWithSerializerModuleApplied.encodeToString(zonedHijrahDateTime)
        assertEquals(jsonString, serialized)
    }

}