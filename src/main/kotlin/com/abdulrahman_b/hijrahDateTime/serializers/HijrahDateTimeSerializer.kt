package com.abdulrahman_b.hijrahDateTime.serializers

import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object HijrahDateTimeSerializer : KSerializer<HijrahDateTime> {

    override val descriptor: SerialDescriptor = HijrahDateTimeSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): HijrahDateTime {

        val surrogate = decoder.decodeSerializableValue(HijrahDateTimeSurrogate.serializer())

        val datetime = with(surrogate) {
            HijrahDateTime.of(
                year,
                month,
                dayOfMonth,
                hour,
                minuteOfHour,
                secondOfMinute,
                nanoOfSecond
            )
        }

        return datetime
    }

    override fun serialize(encoder: Encoder, value: HijrahDateTime) {
        val surrogate = HijrahDateTimeSurrogate(
            value.year, value.monthValue, value.dayOfMonth, value.hour,
            value.minuteOfHour, value.secondOfMinute, value.nanoOfSecond
        )

        encoder.encodeSerializableValue(HijrahDateTimeSurrogate.serializer(), surrogate)
    }

    @Serializable
    private class HijrahDateTimeSurrogate(
        val year: Int, val month: Int, val dayOfMonth: Int, val hour: Int,
        val minuteOfHour: Int, val secondOfMinute: Int, val nanoOfSecond: Int
    )
}
