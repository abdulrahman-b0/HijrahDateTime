package com.abdulrahman_b.hijrahdatetime

class HijrahDateRange(start: HijrahDate, endInclusive: HijrahDate) : HijrahDateProgression(start, endInclusive, 1L),
    ClosedRange<HijrahDate> {


    override fun contains(value: HijrahDate) =
        longProgression.contains(value.toEpochDays())

    override fun isEmpty() = longProgression.isEmpty()

    override val start get() = first

    override val endInclusive get() = last

}

open class HijrahDateProgression internal constructor(internal val longProgression: LongProgression) :
    Collection<HijrahDate> {

    internal constructor(
        start: HijrahDate,
        endInclusive: HijrahDate,
        step: Long
    ) : this(LongProgression.fromClosedRange(start.toEpochDays(), endInclusive.toEpochDays(), step))

    override val size = longProgression.count()

    /**
     * Returns the first [HijrahDate] of the progression
     */
    val first: HijrahDate = HijrahDate.fromEpochDays(longProgression.first)

    /**
     * Returns the last [HijrahDate] of the progression
     */
    val last: HijrahDate = HijrahDate.fromEpochDays(longProgression.last)

    override fun isEmpty() = longProgression.isEmpty()

    override fun contains(element: HijrahDate): Boolean = longProgression.contains(element.toEpochDays())

    override fun iterator(): Iterator<HijrahDate> = HijrahDateProgressionIterator(longProgression.iterator())
    override fun containsAll(elements: Collection<HijrahDate>): Boolean {
        return (elements as Collection<*>).all { it is HijrahDate && contains(it) }
    }


    private class HijrahDateProgressionIterator(private val iterator: LongIterator) : Iterator<HijrahDate> {
        override fun hasNext(): Boolean = iterator.hasNext()
        override fun next(): HijrahDate = HijrahDate.fromEpochDays(iterator.next())
    }
}