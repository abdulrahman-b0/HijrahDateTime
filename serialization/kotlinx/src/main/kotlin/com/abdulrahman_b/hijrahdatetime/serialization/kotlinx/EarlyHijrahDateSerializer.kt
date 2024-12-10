package com.abdulrahman_b.hijrahdatetime.serialization.kotlinx

import com.abdulrahman_b.hijrahdatetime.EarlyHijrahDate
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

object EarlyHijrahDateSerializer : KSerializer<EarlyHijrahDate> by EarlyHijrahDateSerializerImpl(HijrahFormatters.HIJRAH_DATE) {

    operator fun invoke(formatter: DateTimeFormatter): KSerializer<EarlyHijrahDate> =
        EarlyHijrahDateSerializerImpl(formatter)
}

private class EarlyHijrahDateSerializerImpl(private val formatter: DateTimeFormatter) : KSerializer<EarlyHijrahDate> {

    override val descriptor = PrimitiveSerialDescriptor("EarlyHijrahDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): EarlyHijrahDate {
        return EarlyHijrahDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: EarlyHijrahDate) {
        encoder.encodeString(value.format(formatter))
    }
}