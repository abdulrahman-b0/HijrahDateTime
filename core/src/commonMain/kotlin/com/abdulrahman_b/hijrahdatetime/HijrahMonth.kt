@file:Suppress("MemberVisibilityCanBePrivate")

package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.yearmonth.HijrahYearMonth

/**
 * Represents a month-of-year in the Hijrah calendar system.
 */
enum class HijrahMonth {
    /** The 1st month of the Hijrah year. */
    MUHARRAM,
    /** The 2nd month of the Hijrah year. */
    SAFAR,
    /** The 3rd month of the Hijrah year. */
    RABI_AL_AWWAL,
    /** The 4th month of the Hijrah year. */
    RABI_AL_AKHIR,
    /** The 5th month of the Hijrah year. */
    JUMADA_AL_ULA,
    /** The 6th month of the Hijrah year. */
    JUMADA_AL_AKHIRAH,
    /** The 7th month of the Hijrah year. */
    RAJAB,
    /** The 8th month of the Hijrah year. */
    SHAABAN,
    /** The 9th month of the Hijrah year. */
    RAMADAN,
    /** The 10th month of the Hijrah year. */
    SHAWWAL,
    /** The 11th month of the Hijrah year. */
    THUL_QIDAH,
    /** The 12th month of the Hijrah year. */
    THUL_HIJJAH;

    /** The 1-based month number. */
    val number get() = ordinal + 1

    companion object {

        /** Returns the [HijrahMonth] for the given 1-based [number]. */
        fun of(number: Int): HijrahMonth {
            require(number in 1..12) { "Month number must be between 1 and 12" }
            return entries[number - 1]
        }

    }
}

/** Returns the [HijrahYearMonth] for this [HijrahDate]. */
val HijrahDate.yearMonth get() = HijrahYearMonth(year, month)