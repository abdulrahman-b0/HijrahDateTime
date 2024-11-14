package com.abdulrahman_b.hijrahdatetime.serialization.jackson

import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.format.DateTimeFormatter

/**
 * A container for Jackson serializer and deserializer for [OffsetHijrahDateTime] class.
 */
object OffsetHijrahDateTimeSerialization {

    /** A Jackson serializer for [OffsetHijrahDateTime] class. It serializes the [OffsetHijrahDateTime] to a string using the provided [formatter]. */
    class Serializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
    ) : StdSerializer<OffsetHijrahDateTime>(OffsetHijrahDateTime::class.java) {
        override fun serialize(value: OffsetHijrahDateTime, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeString(value.format(formatter))
        }
    }

    /** A Jackson deserializer for [OffsetHijrahDateTime] class. It deserializes the [OffsetHijrahDateTime] from a string using the provided [formatter]. */
    class Deserializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
    ) : StdDeserializer<OffsetHijrahDateTime>(OffsetHijrahDateTime::class.java) {

        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): OffsetHijrahDateTime {
            return OffsetHijrahDateTime.parse(parser.text, formatter)
        }

    }
}