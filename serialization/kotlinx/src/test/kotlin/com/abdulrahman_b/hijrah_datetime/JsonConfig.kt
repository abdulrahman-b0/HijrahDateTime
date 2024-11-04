package com.abdulrahman_b.hijrah_datetime

import com.abdulrahman_b.hijrah_datetime.serialization.kotlinx.buildHijrahSerializersModule
import kotlinx.serialization.json.Json

internal val JsonWithSerializerModuleApplied = Json {
    serializersModule = buildHijrahSerializersModule()
}