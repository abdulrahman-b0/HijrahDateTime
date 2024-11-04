@file:JvmName("-Utils")
package com.abdulrahman_b.hijrah_datetime.utils

import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.time.temporal.TemporalQueries
import java.time.temporal.UnsupportedTemporalTypeException
/**
 * require that the formatter chronology is HijrahChronology
 *
 * @param formatter the formatter to check
 * @return the formatter if it's HijrahChronology
 * @throws IllegalArgumentException if the formatter chronology is not HijrahChronology
 */
internal fun requireHijrahChronology(formatter: DateTimeFormatter): DateTimeFormatter {
    require(formatter.chronology is HijrahChronology) {
        "Formatter's chronology must be a HijrahChronology, but it was ${formatter.chronology}. Did you forget to call 'DateTimeFormatter.withChronology(HijrahChronology.INSTANCE)' ?"
    }
    return formatter
}

/**
 * require that the temporal chronology is HijrahChronology
 *
 * @param temporal the temporal to check
 * @return the temporal if it's HijrahChronology
 * @throws UnsupportedTemporalTypeException if the temporal chronology is not HijrahChronology
 */
internal fun requireHijrahChronology(temporal: Temporal): Temporal {
    val chronology = temporal.query(TemporalQueries.chronology())
    if (chronology !is HijrahChronology)
        throw UnsupportedTemporalTypeException(
            "adjustment of non HijrahChronology temporal is not supported. Temporal was $temporal with chronology was $chronology"
        )
    return temporal
}

