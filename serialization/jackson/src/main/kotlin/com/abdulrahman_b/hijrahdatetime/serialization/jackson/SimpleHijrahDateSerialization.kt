package com.abdulrahman_b.hijrahdatetime.serialization.jackson


import com.abdulrahman_b.hijrahdatetime.SimpleHijrahDate
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_LOCAL_DATE
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.format.DateTimeFormatter

/**
 * A container for Jackson serializer and deserializer for [SimpleHijrahDate] class.
 */
object SimpleHijrahDateSerialization {
    /**
     * A Jackson serializer for [SimpleHijrahDate] class. It serializes the [SimpleHijrahDate] to a string using the provided [formatter].
     */
    class Serializer(
        private val formatter: DateTimeFormatter = HIJRAH_LOCAL_DATE
    ) : StdSerializer<SimpleHijrahDate>(SimpleHijrahDate::class.java) {

        override fun serialize(value: SimpleHijrahDate, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeString(value.format(formatter))
        }

    }

    /**
     * A Jackson deserializer for [SimpleHijrahDate] class. It deserializes the [SimpleHijrahDate] from a string using the provided [formatter].
     */
    class Deserializer(
        private val formatter: DateTimeFormatter = HIJRAH_LOCAL_DATE
    ) : StdDeserializer<SimpleHijrahDate>(SimpleHijrahDate::class.java) {

        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): SimpleHijrahDate? {
            return SimpleHijrahDate.parse(parser.text, formatter)
        }

    }
}