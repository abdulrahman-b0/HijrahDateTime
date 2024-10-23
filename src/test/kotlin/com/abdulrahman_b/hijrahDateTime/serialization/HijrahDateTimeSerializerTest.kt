package com.abdulrahman_b.hijrahDateTime.serialization

import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.serializers.HijrahDateTimeSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

class HijrahDateTimeSerializerTest {

    private val hijrahDateTime = HijrahDateTime.now()

    @Test
    fun deserialize() {
        val jsonElement = JsonObject(mapOf(
            "year" to JsonPrimitive(hijrahDateTime.year),
            "month" to JsonPrimitive(hijrahDateTime.monthValue),
            "dayOfMonth" to JsonPrimitive(hijrahDateTime.dayOfMonth),
            "hour" to JsonPrimitive(hijrahDateTime.hour),
            "minuteOfHour" to JsonPrimitive(hijrahDateTime.minuteOfHour),
            "secondOfMinute" to JsonPrimitive(hijrahDateTime.secondOfMinute),
            "nanoOfSecond" to JsonPrimitive(hijrahDateTime.nanoOfSecond)
        ))

        val deserialized = Json.decodeFromJsonElement(HijrahDateTimeSerializer, jsonElement)

        assertEquals(hijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {
        val jsonElement = Json.encodeToJsonElement(HijrahDateTimeSerializer, hijrahDateTime)

        assertEquals(
            jsonElement,
            JsonObject(mapOf(
                "year" to JsonPrimitive(hijrahDateTime.year),
                "month" to JsonPrimitive(hijrahDateTime.monthValue),
                "dayOfMonth" to JsonPrimitive(hijrahDateTime.dayOfMonth),
                "hour" to JsonPrimitive(hijrahDateTime.hour),
                "minuteOfHour" to JsonPrimitive(hijrahDateTime.minuteOfHour),
                "secondOfMinute" to JsonPrimitive(hijrahDateTime.secondOfMinute),
                "nanoOfSecond" to JsonPrimitive(hijrahDateTime.nanoOfSecond)
            )),
        )
    }


    @Test
    fun serializeAndDeserializeWithJava() {

        val outputByteArray = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(outputByteArray)

        objectOutputStream.writeObject(hijrahDateTime)
        objectOutputStream.close()

        val inputByteArray = outputByteArray.toByteArray()
        val objectInputStream = java.io.ObjectInputStream(inputByteArray.inputStream())

        val deserialized = objectInputStream.readObject() as HijrahDateTime

        assertEquals(hijrahDateTime, deserialized)
    }
}