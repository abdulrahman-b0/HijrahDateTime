package com.abdulrahman_b.hijrahdatetime.serialization.kotlinx

import com.abdulrahman_b.hijrahdatetime.SimpleHijrahDate
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_LOCAL_DATE
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter

object SimpleHijrahDateSerializer : KSerializer<SimpleHijrahDate> by SimpleHijrahDateSerializerImpl(HIJRAH_LOCAL_DATE) {

    operator fun invoke(formatter: DateTimeFormatter): KSerializer<SimpleHijrahDate> =
        SimpleHijrahDateSerializerImpl(formatter)
}

private class SimpleHijrahDateSerializerImpl(private val formatter: DateTimeFormatter) : KSerializer<SimpleHijrahDate> {

    override val descriptor = PrimitiveSerialDescriptor("SimpleHijrahDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): SimpleHijrahDate {
        return SimpleHijrahDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: SimpleHijrahDate) {
        encoder.encodeString(value.format(formatter))
    }
}