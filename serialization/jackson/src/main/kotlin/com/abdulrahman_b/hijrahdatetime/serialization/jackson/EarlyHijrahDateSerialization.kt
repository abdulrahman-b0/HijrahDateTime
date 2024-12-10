package com.abdulrahman_b.hijrahdatetime.serialization.jackson


import com.abdulrahman_b.hijrahdatetime.EarlyHijrahDate
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.format.DateTimeFormatter

/**
 * A container for Jackson serializer and deserializer for [EarlyHijrahDate] class.
 */
object EarlyHijrahDateSerialization {
    /**
     * A Jackson serializer for [EarlyHijrahDate] class. It serializes the [EarlyHijrahDate] to a string using the provided [formatter].
     */
    class Serializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE
    ) : StdSerializer<EarlyHijrahDate>(EarlyHijrahDate::class.java) {

        override fun serialize(value: EarlyHijrahDate, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeString(value.format(formatter))
        }

    }

    /**
     * A Jackson deserializer for [EarlyHijrahDate] class. It deserializes the [EarlyHijrahDate] from a string using the provided [formatter].
     */
    class Deserializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE
    ) : StdDeserializer<EarlyHijrahDate>(EarlyHijrahDate::class.java) {

        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): EarlyHijrahDate {
            return EarlyHijrahDate.parse(parser.text, formatter)
        }

    }
}