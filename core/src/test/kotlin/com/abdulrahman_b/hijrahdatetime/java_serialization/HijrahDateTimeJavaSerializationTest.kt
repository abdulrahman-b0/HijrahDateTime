package com.abdulrahman_b.hijrahdatetime.java_serialization

import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

class HijrahDateTimeJavaSerializationTest {


    companion object {
        private val hijrahDateTime = HijrahDateTime.now()
        private lateinit var serializedStream: ByteArrayOutputStream
    }

    @Test
    @Order(1)
    fun serialize() {
        serializedStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(serializedStream)

        objectOutputStream.writeObject(hijrahDateTime)
        objectOutputStream.close()
    }

    @Test
    @Order(2)
    fun deserialize() {

        val inputByteArray = serializedStream.toByteArray()
        val objectInputStream = java.io.ObjectInputStream(inputByteArray.inputStream())

        val deserialized = objectInputStream.readObject() as HijrahDateTime

        assertEquals(hijrahDateTime, deserialized)
    }
}