@file:JvmName("-Utils")
package com.abdulrahman_b.hijrahDateTime.utils

import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.time.temporal.TemporalQueries
import java.time.temporal.UnsupportedTemporalTypeException

internal fun requireHijrahChronologyFormatter(formatter: DateTimeFormatter) {
    require(formatter.chronology is HijrahChronology) {
        "Formatter's chronology must be a HijrahChronology, but it was ${formatter.chronology}. Did you forget to call 'DateTimeFormatter.withChronology(HijrahChronology.INSTANCE)' ?"
    }
}

internal fun requireHijrahChronology(temporal: Temporal) {
    val chronology = temporal.query(TemporalQueries.chronology())
    if (chronology !is HijrahChronology)
        throw UnsupportedTemporalTypeException(
            "adjustment of non HijrahChronology temporal is not supported. Temporal was $temporal with chronology was $chronology"
        )
}

