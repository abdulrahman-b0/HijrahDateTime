@file:Suppress("MemberVisibilityCanBePrivate")

package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronology
import java.time.DateTimeException
import java.time.chrono.Chronology
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalField
import java.time.temporal.TemporalQueries
import java.time.temporal.TemporalQuery
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange

/**
 * Represents a month-of-year in the Hijrah calendar system.
 */
enum class HijrahMonth(val value: Int) : TemporalAccessor, TemporalAdjuster {

    MUHARRAM(1),
    SAFAR(2),
    RABI_AL_AWWAL(3),
    RABI_AL_THANI(4),
    JUMADA_AL_AWWAL(5),
    JUMADA_AL_THANI(6),
    RAJAB(7),
    SHAABAN(8),
    RAMADAN(9),
    SHAWWAL(10),
    DHU_AL_QIDAH(11),
    DHU_AL_HIJJAH(12);


     /**
     * Checks if the specified field is supported.
     *
     * This checks if this month-of-year can be queried for the specified field.
     * If false, then calling the [TemporalField.range] and
     * [Temporal.get] methods throw an exception.
     *
     * If the field is [ChronoField.MONTH_OF_YEAR] then
     * this method returns true.
     * All other [ChronoField] instances will return false.
     *
     * If the field is not a [ChronoField], then the result of this method
     * is obtained by invoking [TemporalField.isSupportedBy]
     * passing `this` as the argument.
     * Whether the field is supported is dependent on the field.
     *
     * @param field  the field to check, null returns false
     * @return true if the field is supported on this month-of-year, false if not
     */
    override fun isSupported(field: TemporalField): Boolean {
        if (field is ChronoField) {
            return field === ChronoField.MONTH_OF_YEAR
        }
        return field.isSupportedBy(this)
    }

    /**
     * Gets the range of valid values for the specified field.
     *
     * The range object expresses the minimum and maximum valid values for a field.
     * This month is used to enhance the accuracy of the returned range.
     * If it is not possible to return the range, because the field is not supported
     * or for some other reason, an exception is thrown.
     *
     * If the field is [ChronoField.MONTH_OF_YEAR] then the
     * range of the month-of-year, from 1 to 12, is returned.
     * All other [ChronoField] instances throws an [UnsupportedTemporalTypeException].
     *
     * If the field is not a [ChronoField], then the result of this method
     * is obtained by invoking [TemporalField.rangeRefinedBy]
     * passing `this` as the argument.
     * Whether the range can be obtained is determined by the field.
     *
     * @param field  the field to query the range for, not null
     * @return the range of valid values for the field, not null
     * @throws DateTimeException if the range for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the field is not supported
     */
    override fun range(field: TemporalField): ValueRange {
        if (field === ChronoField.MONTH_OF_YEAR) {
            return field.range()
        }
        return super.range(field)
    }

    /**
     * Gets the value of the specified field from this month-of-year as an [Int].
     *
     * This queries this month for the value of the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is [ChronoField.MONTH_OF_YEAR] MONTH_OF_YEAR} then the
     * value of the month-of-year, from 1 to 12, will be returned.
     * All other [ChronoField] instances throws an [UnsupportedTemporalTypeException].
     *
     * If the field is not a [ChronoField], then the result of this method
     * is obtained by invoking [TemporalField.getFrom]
     * passing {@code this} as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field  the field to get, not null
     * @return the value for the field, within the valid range of values
     * @throws DateTimeException if a value for the field cannot be obtained or
     *         the value is outside the range of valid values for the field
     * @throws UnsupportedTemporalTypeException if the field is not supported or
     *         the range of values exceeds an {@code int}
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun get(field: TemporalField): Int {
        if (field === ChronoField.MONTH_OF_YEAR) {
            return value
        }
        return super.get(field)
    }


    /**
     * Gets the value of the specified field from this month-of-year as a `long`.
     *
     *
     * This queries this month for the value of the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     *
     *
     * If the field is [ChronoField.MONTH_OF_YEAR] then the
     * value of the month-of-year, from 1 to 12, will be returned.
     * All other [ChronoField] instances throws an [UnsupportedTemporalTypeException].
     *
     *
     * If the field is not a [ChronoField], then the result of this method
     * is obtained by invoking `TemporalField.getFrom(TemporalAccessor)`
     * passing `this` as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field  the field to get, not null
     * @return the value for the field
     * @throws DateTimeException if a value for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun getLong(field: TemporalField): Long {
        if (field === ChronoField.MONTH_OF_YEAR) {
            return value.toLong()
        } else if (field is ChronoField) {
            throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
        return field.getFrom(this)
    }

    /**
     * Queries this month-of-year using the specified query.
     *
     * This queries this month-of-year using the specified query strategy object.
     * The [TemporalQuery] object defines the logic to be used to
     * obtain the result. Read the documentation of the query to understand
     * what the result of this method will be.
     *
     * The result of this method is obtained by invoking the
     * [TemporalQuery.queryFrom] method on the
     * specified query passing `this` as the argument.
     *
     * @param <R> the type of the result
     * @param query  the query to invoke, not null
     * @return the query result, null may be returned (defined by the query)
     * @throws DateTimeException if unable to query (defined by the query)
     * @throws ArithmeticException if numeric overflow occurs (defined by the query)
     */
    @Suppress("UNCHECKED_CAST")
    override fun <R> query(query: TemporalQuery<R>): R {
        if (query === TemporalQueries.chronology()) {
            return HijrahChronology.INSTANCE as R
        } else if (query === TemporalQueries.precision()) {
            return ChronoUnit.MONTHS as R
        }
        return super.query(query)
    }


    /**
     * Adjusts the specified temporal object to have this month-of-year.
     *
     * This returns a temporal object of the same observable type as the input
     * with the month-of-year changed to be the same as this.
     *
     * The adjustment is equivalent to using [Temporal.with]
     * passing [ChronoField.MONTH_OF_YEAR] as the field.
     * If the specified temporal object does not use the Hijrah calendar system then
     * a [DateTimeException] is thrown.
     *
     * In most cases, it is clearer to reverse the calling pattern by using
     * [Temporal.with][TemporalAdjuster]:
     *
     *      // these two lines are equivalent, but the second approach is recommended
     *      temporal = thisMonth.adjustInto(temporal)
     *      temporal = temporal.with(thisMonth)
     *
     *
     * For example, given a date in Jumada Al-Awwal, the following are output:
     *
     *      dateInJumadaAlAwwal.with(MUHARRAM);    // four months earlier
     *      dateInJumadaAlAwwal.with(RABI_AL_THANI);      // one months earlier
     *      dateInJumadaAlAwwal.with(JUMADA_AL_AWWAL);        // same date
     *      dateInJumadaAlAwwal.with(JUMADA_AL_THANI);       // one month later
     *      dateInJumadaAlAwwal.with(DHU_AL_HIJJAH);   // seven months later
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param temporal  the target object to be adjusted, not null
     * @return the adjusted object, not null
     * @throws DateTimeException if unable to make the adjustment
     * @throws ArithmeticException if numeric overflow occurs
     */

    override fun adjustInto(temporal: Temporal): Temporal {
        requireHijrahChronology(temporal)
        return temporal.with(ChronoField.MONTH_OF_YEAR, value.toLong())
    }

    companion object {
        fun of(value: Int): HijrahMonth {
            return entries.first { it.value == value }
        }

        /**
         * Obtains an instance of [HijrahMonth] from a temporal object.
         *
         *
         * This obtains a month based on the specified temporal.
         * A [TemporalAccessor] represents an arbitrary set of date and time information,
         * which this factory converts to an instance of [HijrahMonth].
         *
         *
         * The conversion extracts the [ChronoField.MONTH_OF_YEAR] field.
         * The extraction is only permitted if the temporal object has a Hijrah
         * chronology, or can be converted to a [HijrahDate].
         *
         * @param temporal  the temporal object to convert, not null
         * @return the month-of-year, not null
         * @throws DateTimeException if unable to convert to a [HijrahMonth]
         */
        fun from(temporal: TemporalAccessor): HijrahMonth {
            var temporalVariable = temporal
            if (temporalVariable is HijrahMonth) {
                return temporalVariable
            }
            try {
                if (HijrahChronology.INSTANCE != Chronology.from(temporalVariable)) {
                    temporalVariable = HijrahDate.from(temporalVariable)
                }
                return of(temporalVariable[ChronoField.MONTH_OF_YEAR])
            } catch (ex: DateTimeException) {
                throw DateTimeException("Unable to obtain HijrahMonth from TemporalAccessor: $temporalVariable of type ${temporalVariable.javaClass.name}, $ex")
            }
        }
    }
}