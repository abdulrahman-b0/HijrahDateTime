package com.abdulrahman_b.hijrahdatetime

actual open class DateTimeException(override val message: String?) : RuntimeException()

actual class DateTimeParseException(message: String?): DateTimeException(message)
