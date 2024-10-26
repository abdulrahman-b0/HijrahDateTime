@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters
import com.abdulrahman_b.hijrahDateTime.serializers.ZonedHijrahDateTimeSerializer
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronologyFormatter
import kotlinx.serialization.Serializable
import java.io.Serial
import java.time.Clock
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.ChronoZonedDateTime
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalField
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange
import java.time.zone.ZoneRules

/**
 * A date-time with a time-zone in the Hijrah calendar system,
 * such as `1446-04-18T17:31:30+03:00 Asia/Riyadh`.
 *
 * [ZonedHijrahDateTime] is an immutable representation of a date-time with a time-zone.
 * This class stores all date and time fields, to a precision of nanoseconds,
 * and a time-zone, with a zone offset used to handle ambiguous local date-times.
 * For example, the value
 * "2nd Shawwal 1445 at 13:45.30.123456789 +03:00 in the Asia/Riyadh time-zone"
 * can be stored in a [ZonedHijrahDateTime].
 *
 * This class handles conversion from the local time-line of [HijrahDateTime]
 * to the instant time-line of [Instant].
 * The difference between the two time-lines is the offset from UTC/Greenwich,
 * represented by a [ZoneOffset].
 *
 * Converting between the two time-lines involves calculating the offset using the
 * [ZoneRules] rules accessed from the [ZoneId].
 * Obtaining the offset for an instant is simple, as there is exactly one valid
 * offset for each instant.
 * For Gaps, the general strategy is that if the local date-time falls in the
 * middle of a Gap, then the resulting zoned date-time will have a local date-time
 * shifted forwards by the length of the Gap, resulting in a date-time in the later
 * offset, typically "summer" time.
 *
 * For Overlaps, the general strategy is that if the local date-time falls in the
 * middle of an Overlap, then the previous offset will be retained. If there is no
 * previous offset, or the previous offset is invalid, then the earlier offset is
 * used, typically "summer" time.. Two additional methods,
 * [withEarlierOffsetAtOverlap] and [withLaterOffsetAtOverlap],
 * help manage the case of an overlap.
 *
 * In terms of design, this class should be viewed primarily as the combination
 * of a [HijrahDateTime] and a [ZoneId]. The [ZoneOffset] is
 * a vital, but secondary, piece of information, used to ensure that the class
 * represents an instant, especially during a daylight savings overlap.

 * This class is immutable and thread-safe.
 *
 */

@Serializable(with = ZonedHijrahDateTimeSerializer::class)
open class ZonedHijrahDateTime internal constructor(
    private val dateTime: ChronoZonedDateTime<HijrahDate>
): DateTimeTemporal<ChronoZonedDateTime<HijrahDate>, ZonedHijrahDateTime>(dateTime), Comparable<ZonedHijrahDateTime>, java.io.Serializable {

    val zone: ZoneId get() = dateTime.zone
    val offset: ZoneOffset get() = dateTime.offset
    val chronology: HijrahChronology get() = dateTime.chronology as HijrahChronology

    @Serial
    private val serialVersionUid = 1L

    override fun truncatedTo(unit: TemporalUnit) = of(toHijrahDateTime().truncatedTo(unit), zone)
    override fun toHijrahDate(): HijrahDate = dateTime.toLocalDate()
    override fun toLocalTime(): LocalTime = dateTime.toLocalTime()
    override fun isEqual(other: ZonedHijrahDateTime) = dateTime.isEqual(other.dateTime)
    override fun isBefore(other: ZonedHijrahDateTime) = dateTime.isBefore(other.dateTime)
    override fun isAfter(other: ZonedHijrahDateTime) = dateTime.isAfter(other.dateTime)
    override fun range(field: TemporalField): ValueRange = dateTime.range(field)
    override fun compareTo(other: ZonedHijrahDateTime) = dateTime.compareTo(other.dateTime)
    fun toHijrahDateTime() = HijrahDateTime(dateTime.toLocalDateTime())

    /**
     * Returns a copy of `this` date-time changing the zone offset to the earlier of the two valid offsets at a local time-line overlap.
     *
     * This method only has any effect when the local time-line overlaps, such as at an autumn daylight savings cutover.
     * In this scenario, there are two valid offsets for the local date-time. Calling this method will return a zoned date-time with the earlier of the two selected.
     * If this method is called when it is not an overlap, this is returned.
     * This instance is immutable and unaffected by this method call.
     * @return a [ZonedHijrahDateTime] based on this date-time with the earlier offset, not null
     * @throws DateTimeException – if no rules are valid for this date-time
     */
    fun withEarlierOffsetAtOverlap() = ZonedHijrahDateTime(dateTime.withEarlierOffsetAtOverlap())

    /**
     * Returns a copy of this date-time changing the zone offset to theclater of the two valid offsets at a local time-line overlap.
     *
     * This method only has any effect when the local time-line overlaps, such as
     * at an autumn daylight savings cutover. In this scenario, there are two
     * valid offsets for the local date-time. Calling this method will return
     * a zoned date-time with the later of the two selected.
     *
     * If this method is called when it is not an overlap, `this` is returned.
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @return a [ChronoZonedDateTime] based on this date-time with the later offset, not null
     * @throws DateTimeException if no rules can be found for the zone
     * @throws DateTimeException if no rules are valid for this date-time
     */
    fun withLaterOffsetAtOverlap() = ZonedHijrahDateTime(dateTime.withLaterOffsetAtOverlap())

    /**
     * Returns a copy of this date-time with a different time-zone,
     * retaining the instant.
     *
     * This method changes the time-zone and retains the instant.
     * This normally results in a change to the local date-time.
     *
     * This method is based on retaining the same instant, thus gaps and overlaps
     * in the local time-line have no effect on the result.
     *
     * To change the offset while keeping the local time,
     * use [withZoneSameLocal].
     *
     * @param zone  the time-zone to change to, not null
     * @return a [ChronoZonedDateTime] based on this date-time with the requested zone, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun withZoneSameInstant(zone: ZoneId) = ZonedHijrahDateTime(dateTime.withZoneSameInstant(zone))

    fun toInstant(): Instant = dateTime.toInstant()

    fun toEpochSecond(): Long = dateTime.toEpochSecond()

    /**
     * Returns a copy of this date-time with a different time-zone,
     * retaining the local date-time if possible.
     *
     * This method changes the time-zone and retains the local date-time.
     * The local date-time is only changed if it is invalid for the new zone.
     *
     * To change the zone and adjust the local date-time,
     * use [withZoneSameInstant].
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param zone  the time-zone to change to, not null
     * @return a {@code ChronoZonedDateTime} based on this date-time with the requested zone, not null
     */
    fun withZoneSameLocal(zone: ZoneId) = ZonedHijrahDateTime(dateTime.withZoneSameLocal(zone))

    /**
     * Obtains an instance of [OffsetHijrahDateTime] from this date-time.
     * This creates an [OffsetHijrahDateTime] with the same local date-time and offset.
     *
     * @return an OffsetHijrahDateTime representing the same local date-time and offset
     */
    fun toOffsetDateTime() = OffsetHijrahDateTime.of(toHijrahDateTime(), offset)

    override fun toString() = dateTime.toString()

    override fun factory(temporal: ChronoZonedDateTime<HijrahDate>) = ZonedHijrahDateTime(temporal)

    override fun equals(other: Any?): Boolean {
        if (other !is ZonedHijrahDateTime) return false
        return dateTime == other.dateTime
    }

    override fun hashCode() = dateTime.hashCode()

    companion object {

        @JvmStatic
        fun now() = ofInstant(Instant.now(), ZoneId.systemDefault())
        @JvmStatic
        fun now(zoneId: ZoneId) = ofInstant(Instant.now(), zoneId)
        @JvmStatic
        fun now(clock: Clock) = now(clock.zone)

        @JvmStatic
        fun of(hijrahDateTime: HijrahDateTime, zoneId: ZoneId): ZonedHijrahDateTime {
            return hijrahDateTime.atZone(zoneId)
        }

        @JvmStatic
        fun of(date: HijrahDate, time: LocalTime, zoneId: ZoneId): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(date.atTime(time).atZone(zoneId))
        }

        @JvmStatic
        @JvmOverloads
        fun of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zoneId: ZoneId = ZoneId.systemDefault()) : ZonedHijrahDateTime {
            return of(HijrahDate.of(year, month, dayOfMonth), LocalTime.of(hour, minute, second, nanoOfSecond), zoneId)
        }

        @JvmStatic
        fun from(temporalAccessor: TemporalAccessor): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(HijrahChronology.INSTANCE.zonedDateTime(temporalAccessor))
        }

        @JvmStatic
        fun ofInstant(instant: Instant, zoneId: ZoneId): ZonedHijrahDateTime {
            return ZonedHijrahDateTime(HijrahChronology.INSTANCE.zonedDateTime(instant, zoneId))
        }

        @JvmStatic
        @JvmOverloads
        fun parse(text: CharSequence, formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_ZONED_DATE_TIME): ZonedHijrahDateTime {
            requireHijrahChronologyFormatter(formatter)
            return formatter.parse(text, Companion::from)
        }
    }

}