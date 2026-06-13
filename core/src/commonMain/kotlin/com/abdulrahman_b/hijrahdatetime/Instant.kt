package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import com.abdulrahman_b.hijrahdatetime.internal.SECONDS_OF_DAY
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.offsetAt
import kotlin.time.Instant

/**
 * Parses an [Instant] from a Hijri formatted string, or returns null if parsing fails.
 *
 * The string must represent a valid Hijri date and time in the ISO-8601 format with an offset,
 * for example: `1445-09-01T00:00:00Z` or `1445-09-01T03:00:00+03:00`.
 *
 * Supported formats:
 * - `yyyy-MM-dd'T'HH:mm:ssX` (e.g., `1445-09-01T00:00:00Z`)
 * - `yyyy-MM-dd'T'HH:mm:ssXXX` (e.g., `1445-09-01T00:00:00+00:00`)
 */
expect fun Instant.Companion.parseHijriOrNull(value: String): Instant?

/**
 * Parses an [Instant] from a Hijri formatted string.
 *
 * The string must represent a valid Hijri date and time in the ISO-8601 format with an offset,
 * for example: `1445-09-01T00:00:00Z` or `1445-09-01T03:00:00+03:00`.
 *
 * Supported formats:
 * - `yyyy-MM-dd'T'HH:mm:ssX` (e.g., `1445-09-01T00:00:00Z`)
 * - `yyyy-MM-dd'T'HH:mm:ssXXX` (e.g., `1445-09-01T00:00:00+00:00`)
 *
 * @throws IllegalArgumentException if the string cannot be parsed or the date is out of range.
 */
expect fun Instant.Companion.parseHijri(value: String): Instant

/**
 * Formats this [Instant] using the specified [format] and [offset].
 */
expect fun Instant.format(format: HijrahDateTimeFormat, offset: UtcOffset = UtcOffset.ZERO): String


/**
 * Converts this [Instant] directly to a [HijrahDateTime] in the specified [timeZone].
 * * This bypasses intermediate Gregorian object allocations (like [LocalDateTime]),
 * streaming the epoch timeline directly into Hijri components via primitive CPU math.
 */
fun Instant.toHijrahDateTime(timeZone: TimeZone): HijrahDateTime {
    // 1. TIMEZONE COMPLIANCE
    // We fetch the dynamic offset for THIS specific point in time (this), not the system clock.
    // This makes the function pure, deterministic, and safe against Daylight Saving Time (DST) shifts.
    val offsetSeconds = timeZone.offsetAt(this).totalSeconds
    val relativeEpochSeconds = this.epochSeconds + offsetSeconds

    // 2. TRUNCATION VS. FLOORING DIVISION (HISTORICAL DATE SAFETY)
    // For timestamps before Jan 1st, 1970, 'relativeEpochSeconds' becomes negative.
    // Standard integer division truncates towards zero (e.g., -3600 / 86400 = 0).
    // To ensure dates before 1970 roll BACKWARD to Day -1 (Dec 31, 1969), we manually floor the math.
    val epochDay = if (relativeEpochSeconds >= 0) {
        relativeEpochSeconds / SECONDS_OF_DAY
    } else {
        // Artificially pads the negative value down across the daily threshold
        // before dividing, forcing an accurate backward step into negative territory.
        (relativeEpochSeconds - SECONDS_OF_DAY + 1) / SECONDS_OF_DAY
    }

    // 3. MATHEMATICAL MODULO FOR SECONDS
    // Kotlin's '%' operator computes the remainder, preserving the negative sign.
    // To extract positive clock values from a negative timestamp, we execute a double-modulo.
    // Formula: ((rem % mod) + mod) % mod -> guarantees a positive range [0..86399]
    var remainingSeconds = ((relativeEpochSeconds % SECONDS_OF_DAY) + SECONDS_OF_DAY) % SECONDS_OF_DAY

    // 4. POSITIVE PRIMITIVE CLOCK SLICING
    // Since 'remainingSeconds' is guaranteed positive, we can safely slice out the clock fields.
    val hours = (remainingSeconds / 3600).toInt()
    remainingSeconds %= 3600
    val minutes = (remainingSeconds / 60).toInt()
    val seconds = (remainingSeconds % 60).toInt()

    // 5. DIRECT COMPONENT INITIALIZATION
    // We plug the raw calculated primitives straight into the engine factories.
    val date = HijrahDate.fromEpochDays(epochDay)
    val time = LocalTime(hours, minutes, seconds, this.nanosecondsOfSecond)

    return date.atTime(time)
}
