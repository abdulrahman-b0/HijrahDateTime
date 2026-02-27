@file:Suppress("MemberVisibilityCanBePrivate")

package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.yearmonth.HijrahYearMonth

/**
 * Represents a month-of-year in the Hijrah calendar system.
 */
enum class HijrahMonth {
    MUHARRAM,
    SAFAR,
    RABI_AL_AWWAL,
    RABI_AL_AKHIR,
    JUMADA_AL_ULA,
    JUMADA_AL_AKHIRAH,
    RAJAB,
    SHAABAN,
    RAMADAN,
    SHAWWAL,
    THUL_QIDAH,
    THUL_HIJJAH;

    val number get() = ordinal + 1

    companion object {

        fun of(number: Int): HijrahMonth {
            require(number in 1..12) { "Month number must be between 1 and 12" }
            return entries[number - 1]
        }

    }
}

val HijrahDate.yearMonth get() = HijrahYearMonth(year, month)