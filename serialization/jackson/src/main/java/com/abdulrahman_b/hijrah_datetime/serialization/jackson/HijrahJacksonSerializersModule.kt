package com.abdulrahman_b.hijrah_datetime.serialization.jackson

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
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

class HijrahJacksonSerializersModule @JvmOverloads constructor(
    private val hijrahDateFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE,
    private val hijrahDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME,
    private val hijrahOffsetDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME,
    private val hijrahZonedDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME,
): SimpleModule() {

    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        context.addSerializers(buildSerializers())
        context.addDeserializers(buildDeserializers())
    }

    private fun buildSerializers(): Serializers {
        return SimpleSerializers().apply {
            addSerializer(HijrahDate::class.java, HijrahDateSerializer(hijrahDateFormatter))
            addSerializer(HijrahDateTime::class.java, HijrahDateTimeSerializer(hijrahDateTimeFormatter))
            addSerializer(OffsetHijrahDateTime::class.java, OffsetHijrahDateTimeSerializer(hijrahOffsetDateTimeFormatter))
            addSerializer(ZonedHijrahDateTime::class.java, ZonedHijrahDateTimeSerializer(hijrahZonedDateTimeFormatter))
        }
    }

    private fun buildDeserializers(): Deserializers {
        return SimpleDeserializers().apply {
            addDeserializer(HijrahDate::class.java, HijrahDateDeserializer(hijrahDateFormatter))
            addDeserializer(HijrahDateTime::class.java, HijrahDateTimeDeserializer(hijrahDateTimeFormatter))
            addDeserializer(OffsetHijrahDateTime::class.java, OffsetHijrahDateTimeDeserializer(hijrahOffsetDateTimeFormatter))
            addDeserializer(ZonedHijrahDateTime::class.java, ZonedHijrahDateTimeDeserializer(hijrahZonedDateTimeFormatter))
        }
    }
}