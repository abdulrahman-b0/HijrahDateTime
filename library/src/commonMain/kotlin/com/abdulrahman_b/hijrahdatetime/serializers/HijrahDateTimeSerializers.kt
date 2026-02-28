package com.abdulrahman_b.hijrahdatetime.serializers

import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
import com.abdulrahman_b.hijrahdatetime.HijrahDateTimeFormatBuilder
import com.abdulrahman_b.hijrahdatetime.serializers.HijrahDateTimeComponentsSerializer.descriptor
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
 * A custom general purpose serializer for [com.abdulrahman_b.hijrahdatetime.HijrahDateTime], enabling serialization and deserialization
 * of [com.abdulrahman_b.hijrahdatetime.HijrahDateTime] objects through a surrogate representation using Kotlin serialization.

 *
 * @property descriptor Represents the structure of serialized data as derived from the surrogate's descriptor.
 * @return A newly reconstructed [com.abdulrahman_b.hijrahdatetime.HijrahDateTime] object based on the decoded surrogate data.
 */
object HijrahDateTimeComponentsSerializer: KSerializer<HijrahDateTime> {

    override val descriptor = buildClassSerialDescriptor("HijrahDateTime") {
        element<Int>("year")
        element<Int>("month")
        element<Int>("dayOfMonth")
        element<Int>("hour")
        element<Int>("minute")
        element<Int>("second")
        element<Int>("nanosecond")
    }

    override fun serialize(encoder: Encoder, value: HijrahDateTime) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.year)
            encodeIntElement(descriptor, 1, value.month.number)
            encodeIntElement(descriptor, 2, value.day)
            encodeIntElement(descriptor, 3, value.hour)
            encodeIntElement(descriptor, 4, value.minute)
            encodeIntElement(descriptor, 5, value.second)
            encodeIntElement(descriptor, 6, value.nanosecond)
        }
    }

    override fun deserialize(decoder: Decoder): HijrahDateTime {
        return decoder.decodeStructure(descriptor) {
            var year = 0
            var month = 0
            var dayOfMonth = 0
            var hour = 0
            var minute = 0
            var second = 0
            var nanosecond = 0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> year = decodeIntElement(descriptor, 0)
                    1 -> month = decodeIntElement(descriptor, 1)
                    2 -> dayOfMonth = decodeIntElement(descriptor, 2)
                    3 -> hour = decodeIntElement(descriptor, 3)
                    4 -> minute = decodeIntElement(descriptor, 4)
                    5 -> second = decodeIntElement(descriptor, 5)
                    6 -> nanosecond = decodeIntElement(descriptor, 6)
                    -1 -> break
                    else -> throw kotlinx.serialization.SerializationException("Unexpected index: ${index}")
                }
            }
            HijrahDateTime(
                year = year,
                month = month,
                dayOfMonth = dayOfMonth,
                hour = hour,
                minute = minute,
                second = second,
                nanosecond = nanosecond
            )
        }
    }

}


class HijrahDateTimeIsoSerializer: KSerializer<HijrahDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("HijrahDateTime", PrimitiveKind.STRING)

    private val formatter by lazy {
        HijrahDateTimeFormatBuilder().apply {
            year()
            char('-')
            monthNumber()
            char('-')
            dayOfMonth()
            char('T')
            hour()
            char(':')
            minute()
            char(':')
            second()
        }.build()
    }

    override fun serialize(encoder: Encoder, value: HijrahDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): HijrahDateTime {
        return HijrahDateTime.parse(decoder.decodeString(), formatter)
    }
}