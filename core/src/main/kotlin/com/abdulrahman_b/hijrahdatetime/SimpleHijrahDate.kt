package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters.HIJRAH_LOCAL_DATE
import java.io.Serializable
import java.time.DateTimeException
import java.time.chrono.HijrahDate
import java.time.chrono.HijrahEra
import java.time.format.DateTimeFormatter
import java.time.temporal.*

/**
 * A flexible representation of dates in the Hijrah (Islamic) calendar system that prioritizes display capabilities
 * over arithmetic operations. This class extends the functionality of [HijrahDate] by offering more flexible date ranges
 * and month lengths.
 *
 * Key features and differences from standard [HijrahDate]:
 * 1. Extended Year Range: Supports dates from year 1 to 1600 (compared to the standard 1300-1600 range)
 * 2. Flexible Month Lengths: Always allows 30-day months, regardless of astronomical calculations
 * 3. Historical Support: Particularly useful for educational and historical purposes
 *
 * Primary use cases:
 * - Display of historical Islamic dates (especially pre-1300 Hijri)
 * - Educational applications requiring representation of early Islamic calendar dates
 * - Scenarios where strict astronomical accuracy is less important than display flexibility
 *
 * Important limitations:
 * - Arithmetic operations (like plus/minus days/months) are NOT supported
 * - Does not follow strict astronomical rules for month lengths
 * - Not suitable for precise date calculations or conversions
 *
 * @property year The Hijrah year, which must be between 1 and 1600 inclusively
 * @property month The Hijrah month represented by the [HijrahMonth] enum
 * @property dayOfMonth The day of the month, which must be between 1 and 30
 *
 * This class is immutable and thread-safe.
 *
 * @constructor Creates a new instance of [SimpleHijrahDate] with the specified year, month, and dayOfMonth
 * @throws DateTimeException if the year is not within 1-1600 or if dayOfMonth is not within 1-30
 *
 * @see HijrahDate For standard Java implementation with strict astronomical rules
 */
class SimpleHijrahDate private constructor(
    val year: Int,
    val month: HijrahMonth,
    val dayOfMonth: Int
) : TemporalAccessor, Comparable<SimpleHijrahDate>, Serializable {


    private val serialVersionUID = 1L

    init {
        if (year !in MIN_YEAR..MAX_YEAR ) {
            throw DateTimeException("Out of range year value: $year. Valid range is from 1 to 1600.")
        }
        if (dayOfMonth !in 1..30) {
            throw DateTimeException("Out of range dayOfMonth value: $dayOfMonth. Valid range is from 1 to 30.")
        }
    }

    @JvmOverloads
    fun format(formatter: DateTimeFormatter = HIJRAH_LOCAL_DATE): String {
        return formatter.withChronology(null).format(this)
    }

    /**
     * Checks if the specified temporal field is supported by this date.
     *
     *
     * @param field the temporal field to check for support, not null
     * @return true if the specified field is supported, false otherwise
     */
    override fun isSupported(field: TemporalField): Boolean {
        return field == ChronoField.YEAR_OF_ERA || field == ChronoField.YEAR || field == ChronoField.MONTH_OF_YEAR || field == ChronoField.DAY_OF_MONTH
    }

    /**
     * Retrieves the value of the specified temporal field from this date.
     *
     * @param field the temporal field to retrieve the value for, not null
     * @return the value of the specified field as an integer
     * @throws UnsupportedTemporalTypeException if the specified field is unsupported
     */
    override fun get(field: TemporalField): Int {
        return when (field) {
            ChronoField.YEAR, ChronoField.YEAR_OF_ERA -> year
            ChronoField.MONTH_OF_YEAR -> month.value
            ChronoField.DAY_OF_MONTH -> dayOfMonth
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    /**
     * Retrieves the valid range of values for the specified temporal field within the context
     * of this SimpleHijrahDate implementation.
     *
     * @param field the temporal field for which the range is requested, not null
     * @return the range of valid values for the specified field
     * @throws UnsupportedTemporalTypeException if the specified field is not supported
     */
    override fun range(field: TemporalField): ValueRange {
        return when (field) {
            ChronoField.YEAR, ChronoField.YEAR_OF_ERA -> ValueRange.of(MIN_YEAR.toLong(), MAX_YEAR.toLong())
            ChronoField.MONTH_OF_YEAR -> ValueRange.of(1, 12)
            ChronoField.DAY_OF_MONTH -> ValueRange.of(1, 30)
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    /**
     * Queries this date for information using the specified [TemporalQuery].
     *
     * @param query the query to invoke, not null
     * @return the result of the query, which can be null if the query is not supported
     */
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> query(query: TemporalQuery<R>): R? {
        return when (query) {
            TemporalQueries.precision() -> ChronoUnit.DAYS as R
            else -> super.query(query)
        }
    }

    /**
     * Retrieves the value of the specified temporal field from this date as a [Long].
     *
     * @param field the temporal field to retrieve the value for, not null
     * @return the value of the specified field as a [Long]
     * @throws UnsupportedTemporalTypeException if the specified field is unsupported
     */
    override fun getLong(field: TemporalField): Long = get(field).toLong()

    /**
     * Returns a new instance of SimpleHijrahDate with the specified field set to a new value.
     *
     * @param field the field to set, not null
     * @param newValue the new value of the field
     * @throws UnsupportedTemporalTypeException if the field is not supported
     */
    fun with(field: TemporalField, newValue: Int): SimpleHijrahDate {
        return when (field) {
            ChronoField.YEAR -> SimpleHijrahDate(newValue, month, dayOfMonth)
            ChronoField.MONTH_OF_YEAR -> SimpleHijrahDate(year, HijrahMonth.of(newValue), dayOfMonth)
            ChronoField.DAY_OF_MONTH -> SimpleHijrahDate(year, month, newValue)
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    /**
     * Returns a new instance of SimpleHijrahDate with the year set to the specified value.
     *
     * @param year the new year to set
     */
    fun withYear(year: Int) = with(ChronoField.YEAR, year)

    /**
     * Returns a new instance of SimpleHijrahDate with the month set to the specified HijrahMonth.
     *
     * @param month the new month to set, not null
     */
    fun withMonth(month: HijrahMonth) = with(ChronoField.MONTH_OF_YEAR, month.value)

    /**
     * Returns a new instance of SimpleHijrahDate with the day of the month set to the specified value.
     *
     * @param dayOfMonth the new day of the month to set
     * @throws UnsupportedTemporalTypeException if the field ChronoField.DAY_OF_MONTH is not supported
     */
    fun withDayOfMonth(dayOfMonth: Int) = with(ChronoField.DAY_OF_MONTH, dayOfMonth)

    override fun toString(): String {
        return "${HijrahEra.AH} ${format()}"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SimpleHijrahDate) return false

        return year == other.year && month == other.month && dayOfMonth == other.dayOfMonth
    }

    /**
     * Compares this [SimpleHijrahDate] object with the specified [SimpleHijrahDate] object for order.
     *
     * @param other the [SimpleHijrahDate] object to be compared with this object
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
     */
    override fun compareTo(other: SimpleHijrahDate): Int {
        return when {
            year > other.year -> 1
            year < other.year -> -1
            month > other.month -> 1
            month < other.month -> -1
            dayOfMonth > other.dayOfMonth -> 1
            dayOfMonth < other.dayOfMonth -> -1
            else -> 0
        }
    }

    companion object {

        /**
         * Creates an instance of [SimpleHijrahDate] based on the specified year, month, and day of the month.
         *
         * @param year the year to represent
         * @param month the month to represent, where 1 is Muharram and 12 is Dhu al-Hijjah
         * @param dayOfMonth the day of the month to represent
         * @return the [SimpleHijrahDate] instance representing the specified year, month, and day of the month.
         *
         * @throws DateTimeException if values are out of range.
         */
        @JvmStatic
        fun of(year: Int, month: Int, dayOfMonth: Int): SimpleHijrahDate {
            return SimpleHijrahDate(year, HijrahMonth.of(month), dayOfMonth)
        }

        /**
         * Parses the given text representation of a date using the provided formatter
         * and converts it to an instance of [SimpleHijrahDate].
         *
         * @param text the text containing the date to be parsed, not null
         * @param formatter the formatter to use for parsing; defaults to `HijrahFormatters.HIJRAH_DATE`
         * @return an instance of `SimpleHijrahDate` representing the parsed date
         * @throws DateTimeException if the text cannot be parsed to an `SimpleHijrahDate`
         */
        @JvmStatic
        @JvmOverloads
        fun parse(
            text: CharSequence,
            formatter: DateTimeFormatter = HIJRAH_LOCAL_DATE
        ): SimpleHijrahDate {
            /* Having `HijrahChronology.INSTANCE` required year range from 1300 to 1600, which makes the parse fails. */
            return formatter.withChronology(null).parse(text) { accessor: TemporalAccessor ->
                try {
                    accessor as? SimpleHijrahDate
                        ?: of(
                            accessor.get(ChronoField.YEAR),
                            accessor.get(ChronoField.MONTH_OF_YEAR),
                            accessor.get(ChronoField.DAY_OF_MONTH)
                        )
                } catch (ex: UnsupportedTemporalTypeException) {
                    throw DateTimeException(
                        "Unable to obtain SimpleHijrahDate from TemporalAccessor: $accessor of type ${accessor.javaClass.name}", ex
                    )
                }
            }
        }

        const val MAX_YEAR = 1600
        const val MIN_YEAR = 1
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + dayOfMonth
        result = 31 * result + month.hashCode()
        return result
    }

}
