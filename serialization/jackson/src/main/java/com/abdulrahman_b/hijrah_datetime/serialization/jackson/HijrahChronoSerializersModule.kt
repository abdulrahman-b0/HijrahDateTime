package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import com.abdulrahman_b.hijrah_datetime.HijrahTemporal
import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.databind.ser.Serializers
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

/**
 * A Jackson module that registers serializers and deserializers for [HijrahDate] and library classes, including, [HijrahDateTime], [OffsetHijrahDateTime], and [ZonedHijrahDateTime].
 */
class HijrahChronoSerializersModule @JvmOverloads constructor(
    private val hijrahDateFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE,
    private val hijrahDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME,
    private val hijrahOffsetDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME,
    private val hijrahZonedDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME
): SimpleModule() {

    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        context.addSerializers(buildSerializers())
        context.addDeserializers(buildDeserializers())
    }

    private fun buildSerializers(): Serializers {
        return SimpleSerializers().apply {
            addSerializer(HijrahDate::class.java, HijrahDateSerialization.Serializer(hijrahDateFormatter))
            addSerializer(HijrahDateTime::class.java, HijrahDateTimeSerialization.Serializer(hijrahDateTimeFormatter))
            addSerializer(OffsetHijrahDateTime::class.java, OffsetHijrahDateTimeSerialization.Serializer(hijrahOffsetDateTimeFormatter))
            addSerializer(ZonedHijrahDateTime::class.java, ZonedHijrahDateTimeSerialization.Serializer(hijrahZonedDateTimeFormatter))
        }
    }

    private fun buildDeserializers(): Deserializers {
        return SimpleDeserializers().apply {
            addDeserializer(HijrahDate::class.java, HijrahDateSerialization.Deserializer(hijrahDateFormatter))
            addDeserializer(HijrahDateTime::class.java, HijrahDateTimeSerialization.Deserializer(hijrahDateTimeFormatter))
            addDeserializer(OffsetHijrahDateTime::class.java, OffsetHijrahDateTimeSerialization.Deserializer(hijrahOffsetDateTimeFormatter))
            addDeserializer(ZonedHijrahDateTime::class.java, ZonedHijrahDateTimeSerialization.Deserializer(hijrahZonedDateTimeFormatter))
        }
    }

}