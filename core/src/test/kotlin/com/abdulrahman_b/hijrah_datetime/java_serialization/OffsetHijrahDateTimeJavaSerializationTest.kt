package com.abdulrahman_b.hijrah_datetime.java_serialization

import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

class OffsetHijrahDateTimeJavaSerializationTest {


    companion object {
        private val hijrahDateTime = OffsetHijrahDateTime.now()
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

        val deserialized = objectInputStream.readObject() as OffsetHijrahDateTime

        assertEquals(hijrahDateTime, deserialized)
    }
}