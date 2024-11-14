package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.serialization.kotlinx.HijrahChronoSerializersModule
import kotlinx.serialization.json.Json

internal val JsonWithSerializerModuleApplied = Json {
    serializersModule = HijrahChronoSerializersModule().get()
}