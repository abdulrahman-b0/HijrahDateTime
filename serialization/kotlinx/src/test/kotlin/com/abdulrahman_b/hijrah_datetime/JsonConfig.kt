package com.abdulrahman_b.hijrah_datetime

import com.abdulrahman_b.hijrah_datetime.serialization.kotlinx.HijrahChronoSerializersModule
import kotlinx.serialization.json.Json

internal val JsonWithSerializerModuleApplied = Json {
    serializersModule = HijrahChronoSerializersModule().get()
}