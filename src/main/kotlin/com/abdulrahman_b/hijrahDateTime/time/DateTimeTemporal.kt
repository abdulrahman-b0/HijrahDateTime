@file:Suppress("unused")
package com.abdulrahman_b.hijrahDateTime.time

import com.abdulrahman_b.hijrahDateTime.formats.HijrahFormatters
import com.abdulrahman_b.hijrahDateTime.utils.requireHijrahChronologyFormatter
import java.io.Serial
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalField
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange

/**
 * A class that provides a set of functions to manipulate date-time objects.
 *
 * This class is a base class for all date-time classes in this package.
 *
 * @param T the input type of the date-time object, which is a subclass of [Temporal], all operations in this class are applied to this temporal object.
 * @param Impl the output type of the date-time object, which is a subclass of [DateTimeTemporal], all operations in this class return this type.
 */
sealed class DateTimeTemporal <in T: Temporal, Impl: DateTimeTemporal<T, Impl>>(
    private val temporal: T,
): Temporal by temporal, java.io.Serializable {

    @Serial
    private val serialVersionUid: Long = 1L

    val year get() = get(ChronoField.YEAR)
    val monthValue get() = get(ChronoField.MONTH_OF_YEAR)
    val month get() = HijrahMonth.of(monthValue)
    val dayOfYear get() = get(ChronoField.DAY_OF_YEAR)
    val dayOfMonth get() = get(ChronoField.DAY_OF_MONTH)
    val dayOfWeekValue get() = get(ChronoField.DAY_OF_WEEK)
    val dayOfWeek: DayOfWeek get() = DayOfWeek.of(dayOfWeekValue)
    val hour get() = get(ChronoField.HOUR_OF_DAY)
    val minuteOfHour get() = get(ChronoField.MINUTE_OF_HOUR)
    val secondOfMinute get() = get(ChronoField.SECOND_OF_MINUTE)
    val nanoOfSecond get() = get(ChronoField.NANO_OF_SECOND)
    val nanoOfDay get() = getLong(ChronoField.NANO_OF_DAY)

    override fun isSupported(field: TemporalField): Boolean {
        return temporal.isSupported(field)
    }

    override fun isSupported(unit: TemporalUnit): Boolean {
        return temporal.isSupported(unit)
    }
    
    override operator fun plus(amount: TemporalAmount): Impl = typedFactory(temporal.plus(amount))

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
    override fun plus(amountToAdd: Long, unit: TemporalUnit): Impl = typedFactory(temporal.plus(amountToAdd, unit))

    override operator fun minus(amount: TemporalAmount): Impl = typedFactory(temporal.plus(amount))

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
    override fun minus(amountToSubtract: Long, unit: TemporalUnit): Impl = typedFactory(temporal.minus(amountToSubtract, unit))

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
    override fun with(adjuster: TemporalAdjuster): Impl  = typedFactory(temporal.with(adjuster))

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
    override fun with(field: TemporalField, newValue: Long): Impl = typedFactory(temporal.with(field, newValue))

    override fun range(field: TemporalField): ValueRange {
        return temporal.range(field)
    }


    /**
     *  Returns a copy of this date-time with the specified nano-of-second added.
     *
     *  This is equivalent to `plus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result
     *  @return the same type of this object based on this date-time with the requested nanosecond, not null
     */
    fun plusNanos(nanos: Long): Impl = plus(nanos, ChronoUnit.NANOS)


    /**
     *  Returns a copy of this date-time with the specified second-of-minute added.
     *
     *  This is equivalent to `plus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result
     *  @return the same type of this object based on this date-time plus the requested second, not null
     */
    fun plusSeconds(seconds: Long): Impl = plus(seconds, ChronoUnit.SECONDS)

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour added.
     *
     *  This is equivalent to `plus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result
     *  @return the same type of this object based on this date-time plus the requested minute, not null
     */
    fun plusMinutes(minutes: Long): Impl = plus(minutes, ChronoUnit.MINUTES)


    /**
     *  Returns a copy of this date-time with the specified hour-of-day added.
     *
     *  This is equivalent to `plus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result
     *  @return the same type of this object based on this date-time plus the requested hour, not null
     */
    fun plusHours(hours: Long): Impl = plus(hours, ChronoUnit.HOURS)

    /**
     *  Returns a copy of this date-time with the specified day-of-month added.
     *
     *  This is equivalent to `plus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result
     *  @return the same type of this object based on this date-time plus the requested day, not null
     */
    fun plusDays(days: Long): Impl = plus(days, ChronoUnit.DAYS)

    /**
     *  Returns a copy of this date-time with the specified week-of-year added.
     *
     *  This is equivalent to `plus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result
     *  @return the same type of this object based on this date-time plus the requested week, not null
     */
    fun plusWeeks(weeks: Long): Impl = plus(weeks, ChronoUnit.WEEKS)

    /**
     *  Returns a copy of this date-time with the specified month-of-year added.
     *
     *  This is equivalent to `plus(months, ChronoUnit.MONTHS)`
     *  @param months  the month-of-year to set in the result
     *  @return the same type of this object based on this date-time plus the requested month, not null
     */
    fun plusMonths(months: Long): Impl = plus(months, ChronoUnit.MONTHS)

    /**
     *  Returns a copy of this date-time with the specified year added.
     *
     *  This is equivalent to `plus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return the same type of this object based on this date-time plus the requested year, not null
     */
    fun plusYears(years: Long): Impl = plus(years, ChronoUnit.YEARS)


    /**
     *  Returns a copy of this date-time with the specified nano-of-second subtracted.
     *
     *  This is equivalent to `minus(nanos, ChronoUnit.NANOS)`
     *  @param nanos  the nano-of-second to set in the result
     *  @return the same type of this object based on this date-time minus the requested nanosecond, not null
     */
    fun minusNanos(nanos: Long): Impl = minus(nanos, ChronoUnit.NANOS)

    /**
     *  Returns a copy of this date-time with the specified second-of-minute subtracted.
     *
     *  This is equivalent to `minus(seconds, ChronoUnit.SECONDS)`
     *  @param seconds  the second-of-minute to set in the result
     *  @return the same type of this object based on this date-time minus the requested second, not null
     */
    fun minusSeconds(seconds: Long): Impl = minus(seconds, ChronoUnit.SECONDS)

    /**
     *  Returns a copy of this date-time with the specified minute-of-hour subtracted.
     *
     *  This is equivalent to `minus(minutes, ChronoUnit.MINUTES)`
     *  @param minutes  the minute-of-hour to set in the result
     *  @return the same type of this object based on this date-time minus the requested minute, not null
     */
    fun minusMinutes(minutes: Long): Impl = minus(minutes, ChronoUnit.MINUTES)

    /**
     *  Returns a copy of this date-time with the specified hour-of-day subtracted.
     *
     *  This is equivalent to `minus(hours, ChronoUnit.HOURS)`
     *  @param hours  the hour-of-day to set in the result
     *  @return the same type of this object based on this date-time minus the requested hour, not null
     */
    fun minusHours(hours: Long): Impl = minus(hours, ChronoUnit.HOURS)

    /**
     *  Returns a copy of this date-time with the specified day-of-month subtracted.
     *
     *  This is equivalent to `minus(days, ChronoUnit.DAYS)`
     *  @param days  the day-of-month to set in the result
     *  @return the same type of this object based on this date-time minus the requested day, not null
     */
    fun minusDays(days: Long): Impl = minus(days, ChronoUnit.DAYS)

    /**
     *  Returns a copy of this date-time with the specified week-of-year subtracted.
     *
     *  This is equivalent to `minus(weeks, ChronoUnit.WEEKS)`
     *  @param weeks  the week-of-year to set in the result.
     *  @return the same type of this object based on this date-time minus the requested week, not null
     */
    fun minusWeeks(weeks: Long): Impl  = minus(weeks, ChronoUnit.WEEKS)


    /**
     * Returns a copy of this date-time with the specified month-of-year subtracted.
     *
     * This is equivalent to `minus(months, ChronoUnit.MONTHS)`
     * @param months the month-of-year to set in the result
     * @return the same type of this object based on this date-time minus the requested month, not null
     */
    fun minusMonths(months: Long): Impl  = minus(months, ChronoUnit.MONTHS)

    /**
     *  Returns a copy of this date-time with the specified year subtracted.
     *
     *  This is equivalent to `minus(years, ChronoUnit.YEARS)`
     *  @param years  the year to set in the result
     *  @return the same type of this object based on this date-time minus the requested year, not null
     */
    fun minusYears(years: Long): Impl = minus(years, ChronoUnit.YEARS)

    /**
     *  Returns a copy of this date-time with the specified nano-of-second set.
     *
     *  This is equivalent to `with(ChronoField.NANO_OF_SECOND, nanoOfSecond)`
     *  @param nanoOfSecond  the nano-of-second to set in the result, from 0 to 999,999,999
     *  @return the same type of this object based on this date-time with the requested nanosecond, not null
     */
    fun withNano(nanoOfSecond: Int): Impl = with(ChronoField.NANO_OF_SECOND, nanoOfSecond.toLong())


    /**
     * Returns a copy of this date-time with the specified second-of-minute set.
     *
     * This is equivalent to `with(ChronoField.SECOND_OF_MINUTE, secondOfMinute)`
     * @param secondOfMinute the second-of-minute to set in the result, from 0 to 59
     * @return the same type of this object based on this date-time with the requested second, not null
     */
    fun withSecond(secondOfMinute: Int): Impl = with(ChronoField.SECOND_OF_MINUTE, secondOfMinute.toLong())

    /**
     * Returns a copy of this date-time with the specified minute-of-hour set.
     *
     * This is equivalent to `with(ChronoField.MINUTE_OF_HOUR, minuteOfHour)`
     * @param minuteOfHour the minute-of-hour to set in the result, from 0 to 59
     * @return the same type of this object based on this date-time with the requested minute, not null
     */
    fun withMinute(minuteOfHour: Int): Impl = with(ChronoField.MINUTE_OF_HOUR, minuteOfHour.toLong())

    /**
     * Returns a copy of this date-time with the specified hour-of-day set.
     *
     * This is equivalent to `with(ChronoField.HOUR_OF_DAY, hour)`
     * @param hour the hour-of-day to set in the result, from 0 to 23
     * @return the same type of this object based on this date-time with the requested hour, not null
     */
    fun withHour(hour: Int): Impl = with(ChronoField.HOUR_OF_DAY, hour.toLong())

    /**
     * Returns a copy of this date-time with the specified day-of-month set.
     *
     * This is equivalent to `with(ChronoField.DAY_OF_MONTH, dayOfMonth)`
     * @param dayOfMonth the day-of-month to set in the result, from 1 to 30 in Hijrah calendar
     */
    fun withDayOfMonth(dayOfMonth: Int): Impl = with(ChronoField.DAY_OF_MONTH, dayOfMonth.toLong())

    /**
     * Returns a copy of this date-time with the specified day-of-year set.
     *
     * This is equivalent to `with(ChronoField.DAY_OF_YEAR, dayOfYear)`
     * @param dayOfYear the day-of-year to set in the result, from 1 to 354 or 355 in Hijrah calendar
     */
    fun withDayOfYear(dayOfYear: Int): Impl = with(ChronoField.DAY_OF_YEAR, dayOfYear.toLong())

    /**
     * Returns a copy of this date-time with the specified day-of-week set.
     *
     * This is equivalent to `with(ChronoField.MONTH, month)`
     * @param month the month-of-year to set in the result, from 1 to 12
     */
    fun withMonth(month: Int): Impl = with(ChronoField.MONTH_OF_YEAR, month.toLong())

    /**
     * Returns a copy of this date-time with the specified month-of-year set.
     *
     * This is equivalent to `with(ChronoField.YEAR, month)`
     * @param year the year to set in the result, ranges from 1300 to 1600
     */
    fun withYear(year: Int): Impl = with(ChronoField.YEAR, year.toLong())

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
    open fun format(formatter: DateTimeFormatter = HijrahFormatters.getRecommendedFormatter(temporal)): String {
        requireHijrahChronologyFormatter(formatter)
        return formatter.format(temporal)
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
     * Returns a copy of this of the implementation class with the time truncated.
     *
     * Truncation returns a copy of the original date-time with fields
     * smaller than the specified unit set to zero.
     * For example, truncating with the [ChronoUnit.MINUTES] unit
     * will set the second-of-minute and nano-of-second field to zero.
     *
     * The unit must have a [TemporalUnit.getDuration] that divides into the length of a standard day without remainder.
     * This includes all supplied time units on [ChronoUnit] and
     * [ChronoUnit.DAYS]. Other units throw an exception.
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param unit  the unit to truncate to, not null
     * @return a {@code LocalDateTime} based on this date-time with the time truncated, not null
     * @throws DateTimeException if unable to truncate
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     */
    abstract infix fun truncatedTo(unit: TemporalUnit): Impl

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
    abstract infix fun isAfter(other: Impl): Boolean

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
    abstract infix fun isBefore(other: Impl): Boolean

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
    abstract infix fun isEqual(other: Impl): Boolean

    /**
     * Gets the hijrah date part of this date-time.
     *
     * This returns a local date with the same year, month and day
     * as this date-time.
     *
     * @return the date part of this date-time, not null
     */
    abstract fun toHijrahDate(): HijrahDate

    /**
     * Gets the local time part of this date-time.
     *
     * This returns a local time with the same hour, minute, second and
     * nanosecond as this date-time.
     *
     * @return the time part of this date-time, not null
     */
    abstract fun toLocalTime(): LocalTime

    @Suppress("UNCHECKED_CAST")
    private fun typedFactory(temporal: Temporal): Impl {
        return factory(temporal as T)
    }

    /**
     * This method is used to create a new instance of the implementation class.
     * Since this [DateTimeTemporal] has no way to create a new instance, this method is abstract.
     * The implementation of this method should return a new instance of `this` implementation, which is then used to return the result of the functions in this class.
     */
    protected abstract fun factory(temporal: T): Impl
}