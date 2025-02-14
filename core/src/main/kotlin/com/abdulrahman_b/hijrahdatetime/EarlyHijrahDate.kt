package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import java.io.Serializable
import java.time.DateTimeException
import java.time.chrono.HijrahDate
import java.time.chrono.HijrahEra
import java.time.format.DateTimeFormatter
import java.time.temporal.*

/**
 * Represents a date in the early Hijrah calendar system. This class is a simple representation
 * not intended for use with modern Hijrah dates, but rather for historical or educational purposes
 * focusing on the early period of the Islamic calendar.
 *
 * @property year The Hijrah year, which must be between 1 and 1299 inclusively. If you need a higher value, use [HijrahDate] class.
 * @property month The Hijrah month represented by the [HijrahMonth] enum.
 * @property dayOfMonth The day of the month, which must be between 1 and 30.
 *
 * Implements the [TemporalAccessor] interface providing support for accessing temporal fields.
 *
 * Note: Arithmetic operations are not supported in this class.
 *
 * This class is immutable and thread safe.
 *
 * @constructor Initializes a new instance of `EarlyHijrahDate` with the specified year, month, and dayOfMonth.
 * Ensures that the given year is within the allowable range and that the given day of the month is valid.
 *
 * @throws DateTimeException if the year or dayOfMonth are out of their respective ranges.
 *
 */
class EarlyHijrahDate private constructor(
    val year: Int,
    val month: HijrahMonth,
    val dayOfMonth: Int
) : TemporalAccessor, Comparable<EarlyHijrahDate>, Serializable {


    private val serialVersionUID = 1L

    init {
        if (year !in MIN_YEAR..MAX_YEAR ) {
            throw DateTimeException("Out of range year value: $year. Valid range is from 1 to 1299.")
        }
        if (dayOfMonth !in 1..30) {
            throw DateTimeException("Out of range dayOfMonth value: $dayOfMonth. Valid range is from 1 to 30.")
        }
    }

    @JvmOverloads
    fun format(formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE): String {
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
     * of this EarlyHijrahDate implementation.
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
     * Returns a new instance of EarlyHijrahDate with the specified field set to a new value.
     *
     * @param field the field to set, not null
     * @param newValue the new value of the field
     * @throws UnsupportedTemporalTypeException if the field is not supported
     */
    fun with(field: TemporalField, newValue: Int): EarlyHijrahDate {
        return when (field) {
            ChronoField.YEAR -> EarlyHijrahDate(newValue, month, dayOfMonth)
            ChronoField.MONTH_OF_YEAR -> EarlyHijrahDate(year, HijrahMonth.of(newValue), dayOfMonth)
            ChronoField.DAY_OF_MONTH -> EarlyHijrahDate(year, month, newValue)
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    /**
     * Returns a new instance of EarlyHijrahDate with the year set to the specified value.
     *
     * @param year the new year to set
     */
    fun withYear(year: Int) = with(ChronoField.YEAR, year)

    /**
     * Returns a new instance of EarlyHijrahDate with the month set to the specified HijrahMonth.
     *
     * @param month the new month to set, not null
     */
    fun withMonth(month: HijrahMonth) = with(ChronoField.MONTH_OF_YEAR, month.value)

    /**
     * Returns a new instance of EarlyHijrahDate with the day of the month set to the specified value.
     *
     * @param dayOfMonth the new day of the month to set
     * @throws UnsupportedTemporalTypeException if the field ChronoField.DAY_OF_MONTH is not supported
     */
    fun withDayOfMonth(dayOfMonth: Int) = with(ChronoField.DAY_OF_MONTH, dayOfMonth)

    override fun toString(): String {
        return "${HijrahEra.AH} ${format()}"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is EarlyHijrahDate) return false

        return year == other.year && month == other.month && dayOfMonth == other.dayOfMonth
    }

    /**
     * Compares this EarlyHijrahDate object with the specified EarlyHijrahDate object for order.
     *
     * @param other the EarlyHijrahDate object to be compared with this object
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
     */
    override fun compareTo(other: EarlyHijrahDate): Int {
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
         * Creates an instance of `EarlyHijrahDate` based on the specified year, month, and day of the month.
         *
         * @param year the year to represent
         * @param month the month to represent, where 1 is Muharram and 12 is Dhu al-Hijjah
         * @param dayOfMonth the day of the month to represent
         * @return the [EarlyHijrahDate] instance representing the specified year, month, and day of the month.
         *
         * @throws DateTimeException if values are out of range.
         */
        @JvmStatic
        fun of(year: Int, month: Int, dayOfMonth: Int): EarlyHijrahDate {
            return EarlyHijrahDate(year, HijrahMonth.of(month), dayOfMonth)
        }

        /**
         * Parses the given text representation of a date using the provided formatter
         * and converts it to an instance of [EarlyHijrahDate].
         *
         * @param text the text containing the date to be parsed, not null
         * @param formatter the formatter to use for parsing; defaults to `HijrahFormatters.HIJRAH_DATE`
         * @return an instance of `EarlyHijrahDate` representing the parsed date
         * @throws DateTimeException if the text cannot be parsed to an `EarlyHijrahDate`
         */
        @JvmStatic
        @JvmOverloads
        fun parse(
            text: CharSequence,
            formatter: DateTimeFormatter = HijrahFormatters.HIJRAH_DATE
        ): EarlyHijrahDate {
            /* Having `HijrahChronology.INSTANCE` requires year range from 1300 to 1600, which makes the parse fails. */
            return formatter.withChronology(null).parse(text) { accessor: TemporalAccessor ->
                try {
                    accessor as? EarlyHijrahDate
                        ?: of(
                            accessor.get(ChronoField.YEAR),
                            accessor.get(ChronoField.MONTH_OF_YEAR),
                            accessor.get(ChronoField.DAY_OF_MONTH)
                        )
                } catch (ex: UnsupportedTemporalTypeException) {
                    throw DateTimeException(
                        "Unable to obtain EarlyHijrahDare from TemporalAccessor: $accessor of type ${accessor.javaClass.name}", ex
                    )
                }
            }
        }

        const val MAX_YEAR = 1299
        const val MIN_YEAR = 1
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + dayOfMonth
        result = 31 * result + month.hashCode()
        return result
    }

}
