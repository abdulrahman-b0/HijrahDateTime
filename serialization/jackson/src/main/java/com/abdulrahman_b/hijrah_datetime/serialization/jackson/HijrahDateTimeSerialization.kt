package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.format.DateTimeFormatter

class HijrahDateTimeSerializer (
    private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME
): StdSerializer<HijrahDateTime>(HijrahDateTime::class.java) {
    override fun serialize(value: HijrahDateTime, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.format(formatter))
    }
}

class HijrahDateTimeDeserializer (
    private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME
) : StdDeserializer<HijrahDateTime>(HijrahDateTime::class.java) {

    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): HijrahDateTime {
        return HijrahDateTime.parse(parser.text, formatter)
    }
}