package com.abdulrahman_b.hijrahDateTime.serialization


import com.abdulrahman_b.hijrahDateTime.serializers.HijrahDateSerializer
import com.abdulrahman_b.hijrahDateTime.time.HijrahDates.dayOfMonth
import com.abdulrahman_b.hijrahDateTime.time.HijrahDates.monthValue
import com.abdulrahman_b.hijrahDateTime.time.HijrahDates.year
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.chrono.HijrahDate

class HijrahDateSerializerTest {

    private val hijrahDate = HijrahDate.now()

    @Test
    fun deserializeWithKotlinx() {
        val jsonElement = JsonObject(mapOf(
            "year" to JsonPrimitive(hijrahDate.year),
            "month" to JsonPrimitive(hijrahDate.monthValue),
            "dayOfMonth" to JsonPrimitive(hijrahDate.dayOfMonth)
        ))

        val deserialized = Json.decodeFromJsonElement(HijrahDateSerializer, jsonElement)

        assertEquals(hijrahDate, deserialized)
    }

    @Test
    fun serializeWithKotlinx() {
        val jsonElement = Json.encodeToJsonElement(HijrahDateSerializer, hijrahDate)

        assertEquals(
            jsonElement,
            JsonObject(mapOf(
                "year" to JsonPrimitive(hijrahDate.year),
                "month" to JsonPrimitive(hijrahDate.monthValue),
                "dayOfMonth" to JsonPrimitive(hijrahDate.dayOfMonth)
            )),
        )
    }

}