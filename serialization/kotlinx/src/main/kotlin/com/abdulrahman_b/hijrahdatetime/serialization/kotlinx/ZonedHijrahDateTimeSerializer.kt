package com.abdulrahman_b.hijrahdatetime.serialization.kotlinx

import com.abdulrahman_b.hijrahdatetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter

object ZonedHijrahDateTimeSerializer : KSerializer<ZonedHijrahDateTime> by ZonedHijrahDateTimeSerializerImpl(
    HijrahFormatters.HIJRAH_ZONED_DATE_TIME
) {

    operator fun invoke(formatter: DateTimeFormatter): KSerializer<ZonedHijrahDateTime> =
        ZonedHijrahDateTimeSerializerImpl(formatter)
}

private class ZonedHijrahDateTimeSerializerImpl(
    private val formatter: DateTimeFormatter
) : KSerializer<ZonedHijrahDateTime> {


    override val descriptor = PrimitiveSerialDescriptor("ZonedHijrahDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ZonedHijrahDateTime {
        return ZonedHijrahDateTime.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: ZonedHijrahDateTime) {
        encoder.encodeString(value.format(formatter))
    }

}