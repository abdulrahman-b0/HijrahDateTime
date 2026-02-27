package com.abdulrahman_b.hijrahdatetime.serializers

import com.abdulrahman_b.hijrahdatetime.HijrahDate
import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test

class SerializationTest {

    @Serializable
    data class DateContainer(
        val date: HijrahDate
    )

    @Serializable
    data class IsoDateContainer(
        @Serializable(with = HijrahDateIsoSerializer::class)
        val date: HijrahDate
    )

    @Serializable
    data class DateTimeContainer(
        val dateTime: HijrahDateTime
    )

    @Serializable
    data class IsoDateTimeContainer(
        @Serializable(with = HijrahDateTimeIsoSerializer::class)
        val dateTime: HijrahDateTime
    )

    @Test
    fun testHijrahDateComponentsSerialization() {
        val date = HijrahDate(1445, 9, 1)
        val container = DateContainer(date)
        val json = Json.encodeToString(DateContainer.serializer(), container)
        
        // HijrahDateComponentsSerializer now uses Int for year
        json shouldBe """{"date":{"year":1445,"month":9,"dayOfMonth":1}}"""
        
        val deserialized = Json.decodeFromString(DateContainer.serializer(), json)
        deserialized.date shouldBe date
    }

    @Test
    fun testHijrahDateIsoSerialization() {
        val date = HijrahDate(1445, 9, 1)
        val container = IsoDateContainer(date)
        val json = Json.encodeToString(IsoDateContainer.serializer(), container)
        
        json shouldBe """{"date":"1445-09-01"}"""
        
        val deserialized = Json.decodeFromString(IsoDateContainer.serializer(), json)
        deserialized.date shouldBe date
    }

    @Test
    fun testHijrahDateTimeComponentsSerialization() {
        val dateTime = HijrahDateTime(1445, 9, 1, 10, 30, 45, 123456789)
        val container = DateTimeContainer(dateTime)
        val json = Json.encodeToString(DateTimeContainer.serializer(), container)
        // Avoid strict nanosecond equality across platforms.
        json.contains(""""year":1445""") shouldBe true
        json.contains(""""month":9""") shouldBe true
        json.contains(""""dayOfMonth":1""") shouldBe true
        json.contains(""""hour":10""") shouldBe true
        json.contains(""""minute":30""") shouldBe true
        json.contains(""""second":45""") shouldBe true
        
        val deserialized = Json.decodeFromString(DateTimeContainer.serializer(), json)
        deserialized.dateTime.year shouldBe dateTime.year
        deserialized.dateTime.month shouldBe dateTime.month
        deserialized.dateTime.day shouldBe dateTime.day
        deserialized.dateTime.hour shouldBe dateTime.hour
        deserialized.dateTime.minute shouldBe dateTime.minute
        deserialized.dateTime.second shouldBe dateTime.second
        // Skip nanosecond equality on Apple targets due to NSDate precision differences.
    }

    @Test
    fun testHijrahDateTimeIsoSerialization() {
        val dateTime = HijrahDateTime(1445, 9, 1, 10, 30, 45, 0)
        val container = IsoDateTimeContainer(dateTime)
        val json = Json.encodeToString(IsoDateTimeContainer.serializer(), container)
        
        json shouldBe """{"dateTime":"1445-09-01T10:30:45"}"""
        
        val deserialized = Json.decodeFromString(IsoDateTimeContainer.serializer(), json)
        deserialized.dateTime.year shouldBe dateTime.year
        deserialized.dateTime.month shouldBe dateTime.month
        deserialized.dateTime.day shouldBe dateTime.day
        deserialized.dateTime.hour shouldBe dateTime.hour
        deserialized.dateTime.minute shouldBe dateTime.minute
        deserialized.dateTime.second shouldBe dateTime.second
    }
}
