package com.abdulrahman_b.hijrahdatetime.yearmonth

class HijrahYearMonthRange(
    override val start: HijrahYearMonth,
    override val endInclusive: HijrahYearMonth
) : HijrahYearMonthProgression(start, endInclusive, 1), ClosedRange<HijrahYearMonth> {

    override fun contains(value: HijrahYearMonth): Boolean =
        value in start..endInclusive

    override fun isEmpty(): Boolean = progression.isEmpty()

    override fun equals(other: Any?): Boolean {
        return other is HijrahYearMonthRange && (isEmpty() && other.isEmpty() ||
                start == other.start && endInclusive == other.endInclusive)
    }

    override fun hashCode(): Int {
        return if (isEmpty()) -1 else 31 * start.hashCode() + endInclusive.hashCode()
    }

    override fun toString(): String = "$start..$endInclusive"
}