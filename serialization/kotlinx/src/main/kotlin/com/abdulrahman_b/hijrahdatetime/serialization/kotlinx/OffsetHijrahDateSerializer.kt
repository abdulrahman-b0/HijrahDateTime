package com.abdulrahman_b.hijrahdatetime.serialization.kotlinx

import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDate
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter

object OffsetHijrahDateSerializer : KSerializer<OffsetHijrahDate> by OffsetHijrahDateSerializerImpl(
    HijrahFormatters.HIJRAH_OFFSET_DATE
) {

    operator fun invoke(formatter: DateTimeFormatter): KSerializer<OffsetHijrahDate> =
        OffsetHijrahDateSerializerImpl(formatter)
}

private class OffsetHijrahDateSerializerImpl(
    private val formatter: DateTimeFormatter
) : KSerializer<OffsetHijrahDate> {

    override val descriptor =
        PrimitiveSerialDescriptor("OffsetHijrahDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): OffsetHijrahDate {
        return OffsetHijrahDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: OffsetHijrahDate) {
        encoder.encodeString(value.format(formatter))
    }
}