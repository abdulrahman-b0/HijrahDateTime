package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.FixedOffsetTimeZone
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.ChronoZonedDateTime
import java.time.temporal.ChronoField

/**
 * Internal interfaces for accessing Java 8 date-time fields.
 */
sealed interface FieldAccessors {

    /** Interface for date-based fields. */
    interface DateBased {
        /** The underlying [ChronoLocalDate]. */
        val date: ChronoLocalDate

    }

    /** Interface for date and time based fields. */
    interface DateTimeBased : DateBased {
        /** The underlying [ChronoLocalDateTime]. */
        val datetime: ChronoLocalDateTime<*>
        override val date: ChronoLocalDate get() = datetime.toLocalDate()

        /** The hour of day. */
        val hour get() = datetime.get(ChronoField.HOUR_OF_DAY)
        /** The minute of hour. */
        val minute get() = datetime.get(ChronoField.MINUTE_OF_HOUR)
        /** The second of minute. */
        val second get() = datetime.get(ChronoField.SECOND_OF_MINUTE)
        /** The nanosecond of second. */
        val nanosecond get() = datetime.get(ChronoField.NANO_OF_SECOND)
    }

    /** Interface for offset date and time based fields. */
    interface OffsetDateTimeBased : DateTimeBased {
        /** The underlying [ChronoZonedDateTime]. */
        val offsetDateTime: ChronoZonedDateTime<*>
        override val datetime: ChronoLocalDateTime<*> get() = offsetDateTime.toLocalDateTime()
        /** The [FixedOffsetTimeZone]. */
        val offset: FixedOffsetTimeZone
    }

}