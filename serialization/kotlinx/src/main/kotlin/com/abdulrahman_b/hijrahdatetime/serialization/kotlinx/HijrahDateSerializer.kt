package com.abdulrahman_b.hijrahdatetime.serialization.kotlinx


import com.abdulrahman_b.hijrahdatetime.extensions.HijrahDates
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

object HijrahDateSerializer : KSerializer<HijrahDate> by HijrahDateSerializerImpl(HijrahFormatters.HIJRAH_DATE) {

    operator fun invoke(formatter: DateTimeFormatter): KSerializer<HijrahDate> =
        HijrahDateSerializerImpl(formatter)
}

private class HijrahDateSerializerImpl(private val formatter: DateTimeFormatter) : KSerializer<HijrahDate> {

    override val descriptor = PrimitiveSerialDescriptor("HijrahDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): HijrahDate {
        return HijrahDates.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: HijrahDate) {
        encoder.encodeString(value.format(formatter))
    }
}