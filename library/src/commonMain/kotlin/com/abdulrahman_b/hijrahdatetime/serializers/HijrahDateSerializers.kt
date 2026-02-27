package com.abdulrahman_b.hijrahdatetime.serializers

import com.abdulrahman_b.hijrahdatetime.HijrahDate
import com.abdulrahman_b.hijrahdatetime.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateComponentsSerializer.descriptor
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * A custom general purpose serializer for [HijrahDate], enabling serialization and deserialization
 * of [HijrahDate] objects through a surrogate representation using Kotlin serialization.

 *
 * @property descriptor Represents the structure of serialized data as derived from the surrogate's descriptor.
 * @return A newly reconstructed [HijrahDate] object based on the decoded surrogate data.
 */
object HijrahDateComponentsSerializer : KSerializer<HijrahDate> {

    override val descriptor = buildClassSerialDescriptor("HijrahDate") {
        element<Int>("year")
        element<Int>("month")
        element<Int>("dayOfMonth")
    }

    override fun serialize(encoder: Encoder, value: HijrahDate) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.year)
            encodeIntElement(descriptor, 1, value.month.number)
            encodeIntElement(descriptor, 2, value.day)
        }
    }

    override fun deserialize(decoder: Decoder): HijrahDate {
        return decoder.decodeStructure(descriptor) {
            var year = 0
            var month = 0
            var dayOfMonth = 0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> year = decodeIntElement(descriptor, 0)
                    1 -> month = decodeIntElement(descriptor, 1)
                    2 -> dayOfMonth = decodeIntElement(descriptor, 2)
                    DECODE_DONE -> break
                    else -> throw kotlinx.serialization.SerializationException("Unexpected index: ${index}")
                }
            }
            HijrahDate(year, month, dayOfMonth)
        }
    }

}

object HijrahDateIsoSerializer : KSerializer<HijrahDate> {

    override val descriptor = PrimitiveSerialDescriptor("HijrahDateIso", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: HijrahDate) {
        val formatted = value.format(HijrahDateTimeFormat.ofPattern("yyyy-MM-dd"))
        encoder.encodeString(formatted)
    }

    override fun deserialize(decoder: Decoder): HijrahDate {
        return HijrahDate.parse(
            string = decoder.decodeString(),
            format = HijrahDateTimeFormat.ofPattern("yyyy-MM-dd")
        )
    }

}
