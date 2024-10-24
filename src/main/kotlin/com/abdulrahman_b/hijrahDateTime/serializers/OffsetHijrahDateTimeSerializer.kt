package com.abdulrahman_b.hijrahDateTime.serializers

import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahDateTime.utils.InternalUse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZoneOffset

@InternalUse
internal object OffsetHijrahDateTimeSerializer: KSerializer<OffsetHijrahDateTime> {

    @get:JvmSynthetic
    override val descriptor = OffsetHijrahDateTimeSurrogate.serializer().descriptor

    @JvmSynthetic
    override fun deserialize(decoder: Decoder): OffsetHijrahDateTime {
        val surrogate = decoder.decodeSerializableValue(OffsetHijrahDateTimeSurrogate.serializer())
        val hijrahDateTime = surrogate.hijrahDateTime
        val offset = ZoneOffset.of(surrogate.offset)
        return OffsetHijrahDateTime.of(hijrahDateTime, offset)
    }

    @JvmSynthetic
    override fun serialize(encoder: Encoder, value: OffsetHijrahDateTime) {
        val surrogate = OffsetHijrahDateTimeSurrogate(value.dateTime, value.offset.toString())
        encoder.encodeSerializableValue(OffsetHijrahDateTimeSurrogate.serializer(), surrogate)
    }

    @Serializable
    private data class OffsetHijrahDateTimeSurrogate(
        val hijrahDateTime: HijrahDateTime,
        val offset: String
    )
}