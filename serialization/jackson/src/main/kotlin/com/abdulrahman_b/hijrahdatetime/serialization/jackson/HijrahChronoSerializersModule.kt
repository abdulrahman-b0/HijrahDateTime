package com.abdulrahman_b.hijrahdatetime.serialization.jackson

import com.abdulrahman_b.hijrahdatetime.EarlyHijrahDate
import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDate
import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.databind.ser.Serializers
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

/**
 * A custom Jackson module that provides serializers and deserializers for Hijrah-based date and time classes.
 *
 * This module is specifically designed to handle the serialization and deserialization of Hijrah-based date and time
 * types such as [HijrahDate], [HijrahDateTime], [OffsetHijrahDate], [OffsetHijrahDateTime], and [ZonedHijrahDateTime].
 * It utilizes custom [DateTimeFormatter] instances to serialize and deserialize these classes in a consistent format.
 *
 * @constructor
 * Constructs the [HijrahChronoSerializersModule] with optional custom [DateTimeFormatter] instances for each date and
 * time type supported. Default formatters are provided via the [HijrahFormatters] utility.
 *
 * @param hijrahDateFormatter The formatter used for serializing and deserializing [HijrahDate] or equivalent objects.
 * @param hijrahDateTimeFormatter Formatter used for [HijrahDateTime] serialization and deserialization.
 * @param offsetHijrahDateFormatter Formatter used for [OffsetHijrahDate] serialization and deserialization.
 * @param offsetHijrahDateTimeFormatter Formatter used for [OffsetHijrahDateTime] serialization and deserialization.
 * @param zonedHijrahDateTimeFormatter Formatter for [ZonedHijrahDateTime] serialization and deserialization.
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
            addSerializer(EarlyHijrahDate::class.java, EarlyHijrahDateSerialization.Serializer(hijrahDateFormatter))
            addSerializer(HijrahDateTime::class.java, HijrahDateTimeSerialization.Serializer(hijrahDateTimeFormatter))
            addSerializer(OffsetHijrahDate::class.java, OffsetHijrahDateSerialization.Serializer(offsetHijrahDateFormatter))
            addSerializer(OffsetHijrahDateTime::class.java, OffsetHijrahDateTimeSerialization.Serializer(offsetHijrahDateTimeFormatter))
            addSerializer(ZonedHijrahDateTime::class.java, ZonedHijrahDateTimeSerialization.Serializer(zonedHijrahDateTimeFormatter))
        }
    }

    private fun buildDeserializers(): Deserializers {
        return SimpleDeserializers().apply {
            addDeserializer(HijrahDate::class.java, HijrahDateSerialization.Deserializer(hijrahDateFormatter))
            addDeserializer(EarlyHijrahDate::class.java, EarlyHijrahDateSerialization.Deserializer(hijrahDateFormatter))
            addDeserializer(HijrahDateTime::class.java, HijrahDateTimeSerialization.Deserializer(hijrahDateTimeFormatter))
            addDeserializer(OffsetHijrahDate::class.java, OffsetHijrahDateSerialization.Deserializer(offsetHijrahDateFormatter))
            addDeserializer(OffsetHijrahDateTime::class.java, OffsetHijrahDateTimeSerialization.Deserializer(offsetHijrahDateTimeFormatter))
            addDeserializer(ZonedHijrahDateTime::class.java, ZonedHijrahDateTimeSerialization.Deserializer(zonedHijrahDateTimeFormatter))
        }
    }

}