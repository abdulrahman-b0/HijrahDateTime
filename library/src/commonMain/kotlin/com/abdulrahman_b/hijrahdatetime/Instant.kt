package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import kotlinx.datetime.UtcOffset
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