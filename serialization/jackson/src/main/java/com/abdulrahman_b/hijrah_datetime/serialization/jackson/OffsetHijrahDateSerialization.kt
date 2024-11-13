package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDate
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.format.DateTimeFormatter

/**
 * A container for Jackson serializer and deserializer for [OffsetHijrahDate] class.
 */
object OffsetHijrahDateSerialization {

    /** A Jackson serializer for [OffsetHijrahDate] class. It serializes the [OffsetHijrahDate] to a string using the provided [formatter]. */
    class Serializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
    ) : StdSerializer<OffsetHijrahDate>(OffsetHijrahDate::class.java) {
        override fun serialize(value: OffsetHijrahDate, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeString(value.format(formatter))
        }
    }

    /** A Jackson deserializer for [OffsetHijrahDate] class. It deserializes the [OffsetHijrahDate] from a string using the provided [formatter]. */
    class Deserializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
    ) : StdDeserializer<OffsetHijrahDate>(OffsetHijrahDate::class.java) {

        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): OffsetHijrahDate {
            return OffsetHijrahDate.parse(parser.text, formatter)
        }

    }
}