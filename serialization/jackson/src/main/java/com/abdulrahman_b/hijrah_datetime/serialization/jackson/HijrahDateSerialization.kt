package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.abdulrahman_b.hijrah_datetime.extensions.HijrahDates
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter


class HijrahDateSerializer (
    private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE
): StdSerializer<HijrahDate>(HijrahDate::class.java) {

    override fun serialize(value: HijrahDate, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.format(formatter))
    }

}

class HijrahDateDeserializer (
    private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE
): StdDeserializer<HijrahDate>(HijrahDate::class.java) {

    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): HijrahDate {
        return HijrahDates.parse(parser.text, formatter)
    }

}