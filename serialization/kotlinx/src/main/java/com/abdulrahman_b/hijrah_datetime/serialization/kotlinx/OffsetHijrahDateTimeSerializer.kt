package com.abdulrahman_b.hijrah_datetime.serialization.kotlinx

import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter

object OffsetHijrahDateTimeSerializer: KSerializer<OffsetHijrahDateTime> by OffsetHijrahDateTimeSerializerImpl(HijrahFormatters.HIJRAH_OFFSET_DATE_TIME)

internal class OffsetHijrahDateTimeSerializerImpl (
    private val formatter: DateTimeFormatter
): KSerializer<OffsetHijrahDateTime> {

    override val descriptor = PrimitiveSerialDescriptor("OffsetHijrahDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): OffsetHijrahDateTime {
        return OffsetHijrahDateTime.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: OffsetHijrahDateTime) {
        encoder.encodeString(value.format(formatter))
    }
}