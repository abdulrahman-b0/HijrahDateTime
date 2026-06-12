package com.abdulrahman_b.hijrahdatetime.yearmonth

private class YearMonthProgressionIterator(private val iterator: IntIterator) : Iterator<HijrahYearMonth> {
    override fun hasNext(): Boolean = iterator.hasNext()
    override fun next(): HijrahYearMonth = HijrahYearMonth.fromProlepticMonth(iterator.next())
}

/**
 * A progression of [HijrahYearMonth] values.
 */
open class HijrahYearMonthProgression internal constructor(internal val progression: IntProgression) : Collection<HijrahYearMonth> {

    override val size: Int = progression.count()

    override fun isEmpty(): Boolean = progression.isEmpty()

    override fun contains(element: HijrahYearMonth): Boolean = progression.contains(element.prolepticMonth)

    override fun iterator(): Iterator<HijrahYearMonth> = YearMonthProgressionIterator(progression.iterator())

    override fun containsAll(elements: Collection<HijrahYearMonth>): Boolean {
        return (elements as Collection<*>).all { it is HijrahYearMonth && contains(it) }
    }

    internal constructor(
        start: HijrahYearMonth,
        endInclusive: HijrahYearMonth,
        step: Int
    ) : this(IntProgression.fromClosedRange(start.prolepticMonth, endInclusive.prolepticMonth, step))

    /**
     * Returns the first [HijrahYearMonth] of the progression
     */
    val first: HijrahYearMonth = HijrahYearMonth.fromProlepticMonth(progression.first)

    /**
     * Returns the last [HijrahYearMonth] of the progression
     */
    val last: HijrahYearMonth = HijrahYearMonth.fromProlepticMonth(progression.last)

}
