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

internal fun requireHijrahTemporal(temporal: Temporal) {
    if (temporal.query(TemporalQueries.chronology()) !is HijrahChronology)
        unsupported("adjustment of non HijrahChronology temporal is not supported. Temporal was $temporal with chronology was ${temporal.query(TemporalQueries.chronology())}")
}

private fun unsupported(message: String): Nothing = throw UnsupportedTemporalTypeException(message)
