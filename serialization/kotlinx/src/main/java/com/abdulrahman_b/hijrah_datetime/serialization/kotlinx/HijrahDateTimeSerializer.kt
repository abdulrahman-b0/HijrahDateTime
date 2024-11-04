package com.abdulrahman_b.hijrah_datetime.serialization.kotlinx


import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter

object HijrahDateTimeSerializer: KSerializer<HijrahDateTime> by HijrahDateTimeSerializerImpl(HijrahFormatters.HIJRAH_DATE_TIME)

internal class HijrahDateTimeSerializerImpl (
    private val formatter: DateTimeFormatter
): KSerializer<HijrahDateTime> {

    override val descriptor = PrimitiveSerialDescriptor("HijrahDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): HijrahDateTime {
        return HijrahDateTime.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: HijrahDateTime) {
        encoder.encodeString(value.format(formatter))
    }

}
