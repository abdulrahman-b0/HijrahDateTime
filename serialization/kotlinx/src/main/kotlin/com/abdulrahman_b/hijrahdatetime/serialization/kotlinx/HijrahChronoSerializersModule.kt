package com.abdulrahman_b.hijrahdatetime.serialization.kotlinx

import com.abdulrahman_b.hijrahdatetime.EarlyHijrahDate
import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDate
import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import kotlinx.serialization.modules.SerializersModule
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

/**
 * A module class for registering custom serializers for various Hijrah-based date and time types.
 *
 * This class provides a mechanism to serialize and deserialize different kinds of Hijrah Chrono-related
 * date and time instances, using specified formatters for each type. These formatters allow
 * customization of the formatting and parsing of Hijrah dates and times.
 *
 * The module supports the following types:
 * - [HijrahDate]
 * - [EarlyHijrahDate]
 * - [HijrahDateTime]
 * - [OffsetHijrahDate]
 * - [OffsetHijrahDateTime]
 * - [ZonedHijrahDateTime]
 *
 * The provided formatters are customizable and default to pre-defined Hijrah date and time formatters
 *
 * @property hijrahDateFormatter Formatter for serializing and deserializing `HijrahDate`.
 * @property hijrahDateTimeFormatter Formatter for serializing and deserializing `HijrahDateTime`.
 * @property offsetHijrahDateFormatter Formatter for serializing and deserializing `OffsetHijrahDate`.
 * @property offsetHijrahDateTimeFormatter Formatter for serializing and deserializing `OffsetHijrahDateTime`.
 * @property zonedHijrahDateTimeFormatter Formatter for serializing and deserializing `ZonedHijrahDateTime`.
 */
class HijrahChronoSerializersModule (
    private val hijrahDateFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE,
    private val hijrahDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME,
    private val offsetHijrahDateFormatter: DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE,
    private val offsetHijrahDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME,
    private val zonedHijrahDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME,
) {

    /**
     * Configures and returns a [SerializersModule] with custom serializers.
     *
     * Each serializer is configured with a corresponding formatter provided by the containing class.
     *
     * @return A [SerializersModule] with the specified contextual serializers.
     */
    fun get() = SerializersModule {
        contextual(HijrahDate::class, HijrahDateSerializer(hijrahDateFormatter))
        contextual(EarlyHijrahDate::class, EarlyHijrahDateSerializer(hijrahDateFormatter))
        contextual(HijrahDateTime::class, HijrahDateTimeSerializer(hijrahDateTimeFormatter))
        contextual(OffsetHijrahDate::class, OffsetHijrahDateSerializer(offsetHijrahDateFormatter))
        contextual(OffsetHijrahDateTime::class, OffsetHijrahDateTimeSerializer(offsetHijrahDateTimeFormatter))
        contextual(ZonedHijrahDateTime::class, ZonedHijrahDateTimeSerializer(zonedHijrahDateTimeFormatter))
    }
}