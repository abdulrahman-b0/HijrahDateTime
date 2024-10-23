package com.abdulrahman_b.hijrahDateTime.serialization

import com.abdulrahman_b.hijrahDateTime.time.ZonedHijrahDateTime
import com.abdulrahman_b.hijrahDateTime.serializers.ZonedHijrahDateTimeSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

class ZonedHijrahDateTimeSerializerTest {

    private val zonedHijrahDateTime = ZonedHijrahDateTime.now()
    private val serializer = ZonedHijrahDateTimeSerializer

    @Test
    fun deserialize() {
        val jsonElement = JsonObject(mapOf(
            "epochSecond" to JsonPrimitive(zonedHijrahDateTime.toEpochSecond()),
            "nanoOfSecond" to JsonPrimitive(zonedHijrahDateTime.nanoOfSecond),
            "zoneId" to JsonPrimitive(zonedHijrahDateTime.zone.id)
        ))

        val deserialized = Json.decodeFromJsonElement(serializer, jsonElement)

        assertEquals(zonedHijrahDateTime, deserialized)
    }

    @Test
    fun serialize() {

        val jsonElement = Json.encodeToJsonElement(serializer, zonedHijrahDateTime)

        assertEquals(
            jsonElement,
            JsonObject(mapOf(
                "epochSecond" to JsonPrimitive(zonedHijrahDateTime.toEpochSecond()),
                "nanoOfSecond" to JsonPrimitive(zonedHijrahDateTime.nanoOfSecond),
                "zoneId" to JsonPrimitive(zonedHijrahDateTime.zone.id)
            )),
        )
    }

    @Test
    fun serializeAndDeserializeWithJava() {

        val outputByteArray = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(outputByteArray)

        objectOutputStream.writeObject(zonedHijrahDateTime)
        objectOutputStream.close()

        val inputByteArray = outputByteArray.toByteArray()
        val objectInputStream = java.io.ObjectInputStream(inputByteArray.inputStream())

        val deserialized = objectInputStream.readObject() as ZonedHijrahDateTime

        assertEquals(zonedHijrahDateTime, deserialized)
    }
}