package com.abdulrahman_b.hijrahdatetime.format

/**
 * Provides access to the underlying `java.time.format.DateTimeFormatter`
 * associated with the `HijrahDateTimeFormat`.
 */
expect val HijrahDateTimeFormat.javaFormatter: java.time.format.DateTimeFormatter
