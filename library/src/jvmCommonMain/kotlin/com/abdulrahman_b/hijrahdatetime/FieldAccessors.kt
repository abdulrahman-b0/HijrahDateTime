package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.FixedOffsetTimeZone
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.ChronoZonedDateTime
import java.time.temporal.ChronoField

sealed interface FieldAccessors {

    interface DateBased {
        val date: ChronoLocalDate

    }

    interface DateTimeBased : DateBased {
        val datetime: ChronoLocalDateTime<*>
        override val date: ChronoLocalDate get() = datetime.toLocalDate()

        val hour get() = datetime.get(ChronoField.HOUR_OF_DAY)
        val minute get() = datetime.get(ChronoField.MINUTE_OF_HOUR)
        val second get() = datetime.get(ChronoField.SECOND_OF_MINUTE)
        val nanosecond get() = datetime.get(ChronoField.NANO_OF_SECOND)
    }

    interface OffsetDateTimeBased : DateTimeBased {
        val offsetDateTime: ChronoZonedDateTime<*>
        override val datetime: ChronoLocalDateTime<*> get() = offsetDateTime.toLocalDateTime()
        val offset: FixedOffsetTimeZone
    }

}