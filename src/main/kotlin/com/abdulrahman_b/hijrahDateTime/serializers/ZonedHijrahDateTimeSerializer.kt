package com.abdulrahman_b.hijrahDateTime.serializers

import com.abdulrahman_b.hijrahDateTime.time.ZonedHijrahDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.ZoneId

internal object ZonedHijrahDateTimeSerializer : KSerializer<ZonedHijrahDateTime> {

    private val delegate = ZonedHijrahDateTimeSurrogate.serializer()
    override val descriptor = delegate.descriptor

    override fun deserialize(decoder: Decoder): ZonedHijrahDateTime {
        val surrogate = decoder.decodeSerializableValue(delegate)
        val instant = Instant.ofEpochSecond(surrogate.epochSecond).plusNanos(surrogate.nanoOfSecond.toLong())
        val zone = ZoneId.of(surrogate.zoneId)
        return ZonedHijrahDateTime.ofInstant(instant, zone)
    }

    override fun serialize(encoder: Encoder, value: ZonedHijrahDateTime) {
        val surrogate = ZonedHijrahDateTimeSurrogate(value.toEpochSecond(), value.nanoOfSecond, value.zone.id)
        encoder.encodeSerializableValue(delegate, surrogate)
    }

    @Serializable
    private data class ZonedHijrahDateTimeSurrogate(
        val epochSecond: Long,
        val nanoOfSecond: Int,
        val zoneId: String
    )


}