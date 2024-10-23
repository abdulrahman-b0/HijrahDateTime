package com.github.abdulrahman_b.serializers

import com.abdulrahman_b.hijrahDateTime.extensions.dayOfMonth
import com.abdulrahman_b.hijrahDateTime.extensions.monthValue
import com.abdulrahman_b.hijrahDateTime.extensions.year
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.chrono.HijrahDate

object HijrahDateSerializer: KSerializer<HijrahDate> {

    private val delegate = HijrahDateSurrogate.serializer()
    override val descriptor = delegate.descriptor

    override fun deserialize(decoder: Decoder): HijrahDate {
        val surrogate = decoder.decodeSerializableValue(delegate)
        return HijrahDate.of(surrogate.year, surrogate.month, surrogate.dayOfMonth)
    }

    override fun serialize(encoder: Encoder, value: HijrahDate) {
        val surrogate = HijrahDateSurrogate(value.year, value.monthValue, value.dayOfMonth)
        encoder.encodeSerializableValue(delegate, surrogate)
    }

    @Serializable
    internal data class HijrahDateSurrogate(
        val year: Int,
        val month: Int,
        val dayOfMonth: Int
    )
}