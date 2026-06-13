package com.abdulrahman_b.hijrahdatetime

/**
 * Represents a range of values for a date-time field.
 *
 * @property minimum the minimum value.
 * @property maximum the maximum value.
 */
data class ValueRange(
    val minimum: Long,
    val maximum: Long
)