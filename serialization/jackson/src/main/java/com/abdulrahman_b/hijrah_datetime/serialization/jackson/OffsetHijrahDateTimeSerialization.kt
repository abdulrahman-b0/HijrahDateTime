package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.format.DateTimeFormatter

class OffsetHijrahDateTimeSerializer (
    private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
) : StdSerializer<OffsetHijrahDateTime>(OffsetHijrahDateTime::class.java) {
    override fun serialize(value: OffsetHijrahDateTime, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.format(formatter))
    }
}

class OffsetHijrahDateTimeDeserializer (
    private val formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME
): StdDeserializer<OffsetHijrahDateTime>(OffsetHijrahDateTime::class.java) {

    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): OffsetHijrahDateTime {
        return OffsetHijrahDateTime.parse(parser.text, formatter)
    }

}