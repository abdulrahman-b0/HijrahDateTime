package com.abdulrahman_b.hijrah_datetime.serialization.kotlinx

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.ZonedHijrahDateTime
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters
import kotlinx.serialization.modules.SerializersModule
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

fun buildHijrahSerializersModule(
    hijrahDateFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE,
    hijrahDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_DATE_TIME,
    hijrahOffsetDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_OFFSET_DATE_TIME,
    hijrahZonedDateTimeFormatter : DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME,
): SerializersModule {

    return SerializersModule {
        contextual(HijrahDate::class, HijrahDateSerializerImpl(hijrahDateFormatter))
        contextual(HijrahDateTime::class, HijrahDateTimeSerializerImpl(hijrahDateTimeFormatter))
        contextual(OffsetHijrahDateTime::class, OffsetHijrahDateTimeSerializerImpl(hijrahOffsetDateTimeFormatter))
        contextual(ZonedHijrahDateTime::class, ZonedHijrahDateTimeSerializerImpl(hijrahZonedDateTimeFormatter))
    }

}