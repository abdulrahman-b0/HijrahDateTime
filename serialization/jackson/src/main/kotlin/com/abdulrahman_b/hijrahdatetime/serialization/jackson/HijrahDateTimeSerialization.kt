package com.abdulrahman_b.hijrahdatetime.serialization.jackson

import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.format.DateTimeFormatter

/**
 * A container for Jackson serializer and deserializer for [HijrahDateTime] class.
 */
object HijrahDateTimeSerialization {

    /** A Jackson serializer for [HijrahDateTime] class. It serializes the [HijrahDateTime] to a string using the provided [formatter]. */
    class Serializer (
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME
    ): StdSerializer<HijrahDateTime>(HijrahDateTime::class.java) {
        override fun serialize(value: HijrahDateTime, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeString(value.format(formatter))
        }
    }

    /** A Jackson deserializer for [HijrahDateTime] class. It deserializes the [HijrahDateTime] from a string using the provided [formatter]. */
    class Deserializer (
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME
    ) : StdDeserializer<HijrahDateTime>(HijrahDateTime::class.java) {

        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): HijrahDateTime {
            return HijrahDateTime.parse(parser.text, formatter)
        }
    }

}
