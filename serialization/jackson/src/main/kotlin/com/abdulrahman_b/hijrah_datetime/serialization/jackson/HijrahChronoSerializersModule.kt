package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDate
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
    private val offsetHijrahDateFormatter: DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE,
    private val offsetHijrahDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME,
    private val zonedHijrahDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME
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
            addSerializer(OffsetHijrahDate::class.java, OffsetHijrahDateSerialization.Serializer(offsetHijrahDateFormatter))
            addSerializer(OffsetHijrahDateTime::class.java, OffsetHijrahDateTimeSerialization.Serializer(offsetHijrahDateTimeFormatter))
            addSerializer(ZonedHijrahDateTime::class.java, ZonedHijrahDateTimeSerialization.Serializer(zonedHijrahDateTimeFormatter))
        }
    }

    private fun buildDeserializers(): Deserializers {
        return SimpleDeserializers().apply {
            addDeserializer(HijrahDate::class.java, HijrahDateSerialization.Deserializer(hijrahDateFormatter))
            addDeserializer(HijrahDateTime::class.java, HijrahDateTimeSerialization.Deserializer(hijrahDateTimeFormatter))
            addDeserializer(OffsetHijrahDate::class.java, OffsetHijrahDateSerialization.Deserializer(offsetHijrahDateFormatter))
            addDeserializer(OffsetHijrahDateTime::class.java, OffsetHijrahDateTimeSerialization.Deserializer(offsetHijrahDateTimeFormatter))
            addDeserializer(ZonedHijrahDateTime::class.java, ZonedHijrahDateTimeSerialization.Deserializer(zonedHijrahDateTimeFormatter))
        }
    }

}