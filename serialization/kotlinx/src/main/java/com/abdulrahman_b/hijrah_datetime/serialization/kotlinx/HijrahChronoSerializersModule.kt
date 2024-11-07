package com.abdulrahman_b.hijrah_datetime.serialization.kotlinx

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import kotlinx.serialization.modules.SerializersModule
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

class HijrahChronoSerializersModule (
    private val hijrahDateFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE,
    private val hijrahDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME,
    private val hijrahOffsetDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME,
    private val hijrahZonedDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME,
) {

    fun get() = SerializersModule {
        contextual(HijrahDate::class, HijrahDateSerializer(hijrahDateFormatter))
        contextual(HijrahDateTime::class, HijrahDateTimeSerializer(hijrahDateTimeFormatter))
        contextual(OffsetHijrahDateTime::class, OffsetHijrahDateTimeSerializer(hijrahOffsetDateTimeFormatter))
        contextual(ZonedHijrahDateTime::class, ZonedHijrahDateTimeSerializer(hijrahZonedDateTimeFormatter))
    }
}