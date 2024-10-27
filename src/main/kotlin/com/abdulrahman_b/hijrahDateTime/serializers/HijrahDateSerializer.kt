package com.abdulrahman_b.hijrahDateTime.serializers

import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.dayOfMonth
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.monthValue
import com.abdulrahman_b.hijrahDateTime.time.extensions.HijrahDates.year
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.chrono.HijrahDate

object HijrahDateSerializer: KSerializer<HijrahDate> {

    private val delegate = HijrahDateSurrogate.serializer()
    @get:JvmSynthetic
    override val descriptor = delegate.descriptor


    @JvmSynthetic
    override fun deserialize(decoder: Decoder): HijrahDate {
        val surrogate = decoder.decodeSerializableValue(delegate)
        return HijrahDate.of(surrogate.year, surrogate.month, surrogate.dayOfMonth)
    }

    @JvmSynthetic
    override fun serialize(encoder: Encoder, value: HijrahDate) {
        val surrogate = HijrahDateSurrogate(value.year, value.monthValue, value.dayOfMonth)
        encoder.encodeSerializableValue(delegate, surrogate)
    }

    @Serializable
    private data class HijrahDateSurrogate(
        val year: Int,
        val month: Int,
        val dayOfMonth: Int
    )
}