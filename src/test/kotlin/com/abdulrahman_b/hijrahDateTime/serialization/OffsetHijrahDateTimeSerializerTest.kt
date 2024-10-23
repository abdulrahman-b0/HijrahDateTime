package com.abdulrahman_b.hijrahDateTime.serialization

import com.abdulrahman_b.hijrahDateTime.serializers.OffsetHijrahDateTimeSerializer
import com.abdulrahman_b.hijrahDateTime.time.OffsetHijrahDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.time.ZoneOffset

class OffsetHijrahDateTimeSerializerTest {

    private val offsetHijrahDateTime = OffsetHijrahDateTime.of(1446, 2, 5, 12, 43, 18, 0, ZoneOffset.ofHours(3))
    private val jsonElement = JsonObject(
        mapOf(
            "hijrahDateTime" to JsonObject(
                mapOf(
                    "year" to  JsonPrimitive(offsetHijrahDateTime.dateTime.year),
                    "month" to  JsonPrimitive(offsetHijrahDateTime.dateTime.monthValue),
                    "dayOfMonth" to  JsonPrimitive(offsetHijrahDateTime.dateTime.dayOfMonth),
                    "hour" to  JsonPrimitive(offsetHijrahDateTime.dateTime.hour),
                    "minuteOfHour" to  JsonPrimitive(offsetHijrahDateTime.dateTime.minuteOfHour),
                    "secondOfMinute" to  JsonPrimitive(offsetHijrahDateTime.dateTime.secondOfMinute),
                    "nanoOfSecond" to  JsonPrimitive(offsetHijrahDateTime.dateTime.nanoOfSecond)
                )
            ),
            "offset" to JsonPrimitive(offsetHijrahDateTime.offset.toString())
        )
    )

    @Test
    fun deserialize() {
        val deserialized = Json.decodeFromString(OffsetHijrahDateTimeSerializer, jsonElement.toString())
        assertEquals(offsetHijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {
        val jsonElement = Json.encodeToJsonElement(OffsetHijrahDateTimeSerializer, offsetHijrahDateTime)
        val expectedElement = this.jsonElement
        assertEquals(expectedElement, jsonElement)
    }

    @Test
    fun serializeAndDeserializeWithJava() {

        val outputByteArray = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(outputByteArray)

        objectOutputStream.writeObject(offsetHijrahDateTime)
        objectOutputStream.close()

        val inputByteArray = outputByteArray.toByteArray()
        val objectInputStream = java.io.ObjectInputStream(inputByteArray.inputStream())

        val deserialized = objectInputStream.readObject() as OffsetHijrahDateTime

        assertEquals(offsetHijrahDateTime, deserialized)
    }
}