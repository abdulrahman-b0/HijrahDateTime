package com.abdulrahman_b.hijrahdatetime.serializers

import com.abdulrahman_b.hijrahdatetime.HijrahDate
import com.abdulrahman_b.hijrahdatetime.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateComponentsSerializer.descriptor
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
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
        element<String>("year")
        element<Int>("month")
        element<Int>("dayOfMonth")
    }

    override fun serialize(encoder: Encoder, value: HijrahDate) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.year.toString())
            encodeIntElement(descriptor, 1, value.month.number)
            encodeIntElement(descriptor, 2, value.day)
        }
    }

    override fun deserialize(decoder: Decoder): HijrahDate {
        return decoder.decodeStructure(descriptor) {
            HijrahDate(
                year = decodeStringElement(descriptor, 0).toInt(),
                month = decodeIntElement(descriptor, 1),
                dayOfMonth = decodeIntElement(descriptor, 2)
            )
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
