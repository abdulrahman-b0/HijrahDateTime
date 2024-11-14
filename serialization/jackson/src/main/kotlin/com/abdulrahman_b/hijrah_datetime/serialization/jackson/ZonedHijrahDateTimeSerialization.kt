package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.abdulrahman_b.hijrah_datetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.format.DateTimeFormatter

/**
 * A container for Jackson serializer and deserializer for [ZonedHijrahDateTime] class.
 */
object ZonedHijrahDateTimeSerialization {

    /** A Jackson serializer for [ZonedHijrahDateTime] class. It serializes the [ZonedHijrahDateTime] to a string using the provided [formatter]. */
    class Serializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME
    ) : StdSerializer<ZonedHijrahDateTime>(ZonedHijrahDateTime::class.java) {

        override fun serialize(value: ZonedHijrahDateTime, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeString(value.format(formatter))
        }

    }

    /** A Jackson deserializer for [ZonedHijrahDateTime] class. It deserializes the [ZonedHijrahDateTime] from a string using the provided [formatter]. */
    class Deserializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME
    ) : StdDeserializer<ZonedHijrahDateTime>(ZonedHijrahDateTime::class.java) {

        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): ZonedHijrahDateTime {
            return ZonedHijrahDateTime.parse(parser.text, formatter)
        }
    }
}