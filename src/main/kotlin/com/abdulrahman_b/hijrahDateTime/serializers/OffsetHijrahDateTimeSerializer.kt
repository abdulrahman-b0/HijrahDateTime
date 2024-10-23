package com.abdulrahman_b.hijrahDateTime.serializers

import com.abdulrahman_b.hijrahDateTime.time.HijrahDateTime
import com.abdulrahman_b.hijrahDateTime.time.OffsetHijrahDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZoneOffset

object OffsetHijrahDateTimeSerializer: KSerializer<OffsetHijrahDateTime> {

    override val descriptor = OffsetHijrahDateTimeSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): OffsetHijrahDateTime {
        val surrogate = decoder.decodeSerializableValue(OffsetHijrahDateTimeSurrogate.serializer())
        val hijrahDateTime = surrogate.hijrahDateTime
        val offset = ZoneOffset.of(surrogate.offset)
        return OffsetHijrahDateTime(hijrahDateTime, offset)
    }

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