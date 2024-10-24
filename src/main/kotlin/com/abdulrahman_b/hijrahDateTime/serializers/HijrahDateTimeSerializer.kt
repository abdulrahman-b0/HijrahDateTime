package com.abdulrahman_b.hijrahDateTime.serializers

import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.utils.InternalUse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@InternalUse
internal object HijrahDateTimeSerializer : KSerializer<HijrahDateTime> {

    @get:JvmSynthetic
    override val descriptor: SerialDescriptor = HijrahDateTimeSurrogate.serializer().descriptor

    @JvmSynthetic
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

    @JvmSynthetic
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
