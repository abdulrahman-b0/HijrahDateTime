package com.abdulrahman_b.hijrahdatetime.serialization.jackson

import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

/**
 * A container for Jackson serializer and deserializer for [HijrahDate] class.
 */
object HijrahDateSerialization {
    /**
     * A Jackson serializer for [HijrahDate] class. It serializes the [HijrahDate] to a string using the provided [formatter].
     */
    class Serializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE
    ) : StdSerializer<HijrahDate>(HijrahDate::class.java) {

        override fun serialize(value: HijrahDate, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeString(value.format(formatter))
        }

    }

    /**
     * A Jackson deserializer for [HijrahDate] class. It deserializes the [HijrahDate] from a string using the provided [formatter].
     */
    class Deserializer(
        private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE
    ) : StdDeserializer<HijrahDate>(HijrahDate::class.java) {

        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): HijrahDate {
            return HijrahDates.parse(parser.text, formatter)
        }

    }
}