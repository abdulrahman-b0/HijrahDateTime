@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.getRecommendedFormatter
import com.abdulrahman_b.hijrahdatetime.utils.requireHijrahChronology
import java.io.Serial
import java.io.Serializable
import java.time.DateTimeException
import java.time.Duration
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalField
import java.time.temporal.TemporalQueries
import java.time.temporal.TemporalQuery
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange

/**
 * A class that provides a set of functions to manipulate date-time objects.
 *
 * This class is a base class for all date-time classes in this package.
 *
 * @param T the implementation type of the date-time object, which is a subclass of [AbstractHijrahTemporal], all operations in this class return this type.
 */
sealed class AbstractHijrahTemporal<T : AbstractHijrahTemporal<T>>(
    private val temporal: Temporal,
) : Temporal by temporal, Comparable<T>, Serializable {

    @Serial
    private val serialVersionUid: Long = 1L

    val chronology: HijrahChronology =
        temporal.query(TemporalQueries.chronology()) as HijrahChronology

    override fun isSupported(field: TemporalField) = temporal.isSupported(field)

    override fun isSupported(unit: TemporalUnit) = temporal.isSupported(unit)

    override operator fun plus(amount: TemporalAmount): T = factory(temporal.plus(amount))

    /**
     * Returns an object of the same type as this object with the specified period added.
     *
     * This method returns a new object based on this one with the specified period added.
     * For example, on a [HijrahDateTime], this could be used to add a number of years, months or days.
     * The returned object will have the same observable type as this object.

     * @param amountToAdd  the amount of the specified unit to add, may be negative
     * @param unit  the unit of the amount to add, not null
     * @return an object of the same type with the specified period added, not null
     * @throws DateTimeException if the unit cannot be added
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun plus(amountToAdd: Long, unit: TemporalUnit): T =
        factory(temporal.plus(amountToAdd, unit))

    override fun minus(amount: TemporalAmount): T = factory(temporal.minus(amount))

    /**
     * Returns an object of the same type as this object with the specified period subtracted.
     *
     * This method returns a new object based on this one with the specified period subtracted.
     * For example, on a [HijrahDateTime], this could be used to add a number of years, months or days.
     * The returned object will have the same observable type as this object.

     * @param amountToSubtract  the amount of the specified unit to add, may be negative
     * @param unit  the unit of the amount to add, not null
     * @return an object of the same type with the specified period added, not null
     * @throws DateTimeException if the unit cannot be added
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun minus(amountToSubtract: Long, unit: TemporalUnit): T =
        factory(temporal.minus(amountToSubtract, unit))

    /**
     * Returns an adjusted object of the same type as this object with the adjustment made.
     *
     * This adjusts this date-time according to the rules of the specified adjuster.
     * A simple adjuster might simply set the one of the fields, such as the year field.
     * A more complex adjuster might set the date to the last day of the month.
     * A selection of common adjustments is provided in [java.time.temporal.TemporalAdjusters].
     * These include finding the "last day of the month" and "next Wednesday".
     * The adjuster is responsible for handling special cases, such as the varying
     * lengths of month and leap years.
     *
     * Some example code indicating how and why this method is used:
     * ```
     *      date = date.with(HijrahMonth.JUMADA_AL_THANI);        // most key classes implement TemporalAdjuster
     *      date = date.with(lastDayOfMonth());  // static import from Adjusters
     *      date = date.with(next(WEDNESDAY));   // static import from Adjusters and DayOfWeek
     *```

     * @param adjuster  the adjuster to use, not null
     * @return an object of the same type with the specified adjustment made, not null
     * @throws DateTimeException if unable to make the adjustment
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun with(adjuster: TemporalAdjuster): T = factory(temporal.with(adjuster))

    /**
     * Returns an object of the same type as this object with the specified field altered.
     *
     * This returns a new object based on this one with the value for the specified field changed.
     * For example, on a [HijrahDateTime], this could be used to set the year, month or day-of-month.
     * The returned object will have the same observable type as this object.

     * If the field is not a [ChronoField], then the result of this method
     * is obtained by invoking {@code TemporalField.adjustInto(Temporal, long)}
     * passing `this` as the first argument.

     * @param field  the field to set in the result, not null
     * @param newValue  the new value of the field in the result
     * @return an object of the same type with the specified field set, not null
     * @throws DateTimeException if the field cannot be set
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun with(field: TemporalField, newValue: Long): T =
        factory(temporal.with(field, newValue))

    override fun range(field: TemporalField): ValueRange {
        return temporal.range(field)
    }




    /**
     * Formats this date-time using the specified formatter.
     * By default, the formatter is the recommended formatter for the Hijrah calendar, which is obtained from [HijrahFormatters.getRecommendedFormatter].
     *
     * If you want to use a custom formatter, you can pass it as an argument. But, note that the formatter chronology must be a [HijrahChronology],
     * you can set the chronology using the [DateTimeFormatter.withChronology] method. Otherwise, an exception is thrown.
     * @param formatter the formatter to use, by default it is the recommended formatter for the Hijrah calendar
     * @return the formatted date-time string, not null
     * @throws IllegalArgumentException if the formatter chronology is not a [HijrahChronology]
     * @see HijrahFormatters.getRecommendedFormatter
     */
    @JvmOverloads
    open fun format(formatter: DateTimeFormatter = getRecommendedFormatter(this)): String =
        requireHijrahChronology(formatter).format(temporal)

    @Suppress("UNCHECKED_CAST")
    override fun <R : Any?> query(query: TemporalQuery<R>): R {

        return when (query) {
            TemporalQueries.chronology() -> chronology as R
            else -> temporal.query(query)
        }

    }


    /**
     * Returns the duration between this date-time and the specified date-time.
     *
     * The duration can be negative if the specified date-time is before this date-time.
     *
     * @param other  the other date-time, not null
     * @return the duration between this date-time and the specified date-time, not null
     */
    infix fun until(other: Temporal): Duration = Duration.between(temporal, other)


    /**
     * Checks if this date-time is after the specified date-time ignoring the chronology.
     *
     * This method differs from the comparison in [compareTo] in that it
     * only compares the underlying date-time and not the chronology.
     * This allows dates in different calendar systems to be compared based
     * on the time-line position.
     * @param other  the other date-time to compare to, not null
     * @return true if this is after the specified date-time
     */
    abstract infix fun isAfter(other: T): Boolean

    /**
     * Checks if this date-time is before the specified date-time ignoring the chronology.
     *
     * This method differs from the comparison in [compareTo] in that it
     * only compares the underlying date-time and not the chronology.
     * This allows dates in different calendar systems to be compared based
     * on the time-line position.
     * @param other  the other date-time to compare to, not null
     * @return true if this is before the specified date-time
     */
    abstract infix fun isBefore(other: T): Boolean

    /**
     * Checks if this date-time is equal to the specified date-time ignoring the chronology.
     *
     * This method differs from the comparison in [compareTo] in that it
     * only compares the underlying date-time and not the chronology.
     * This allows dates in different calendar systems to be compared based
     * on the time-line position.
     * @param other  the other date-time to compare to, not null
     * @return true if the underlying date-time is equal to the specified date-time
     */
    abstract infix fun isEqual(other: T): Boolean

    /**
     * This method is used to create a new instance of the implementation class.
     * Since this [AbstractHijrahTemporal] has no way to create a new instance, this method is abstract.
     * The implementation of this method should return a new instance of `this` implementation, which is then used to return the result of the functions in this class.
     *
     * The [temporal] type is the same as the provided [Temporal] in the constructor of [AbstractHijrahTemporal], you can safely cast it to the required type.
     *
     * @param temporal the temporal to create a new instance of the implementation class, not null
     */
    protected abstract fun factory(temporal: Temporal): T
}