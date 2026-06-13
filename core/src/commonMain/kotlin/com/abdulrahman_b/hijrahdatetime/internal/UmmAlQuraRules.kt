package com.abdulrahman_b.hijrahdatetime.internal


internal fun getLengthOfMonth(year: Int, month: Int): Int {

    val maskIndex = year - UmmAlQuraData.BASE_HIJRI_YEAR
    val monthMask = UmmAlQuraData.MONTH_MASKS[maskIndex].toInt()

    val shiftAmount = 12 - month
    val bit = (monthMask shr shiftAmount) and 1

    return MIN_DAYS_OF_MONTH + bit
}


internal fun getLengthOfYear(year: Int): Int {

    val index = year - UmmAlQuraData.BASE_HIJRI_YEAR

    return UmmAlQuraData.DAYS_OF_YEARS.getOrNull(index)?.toInt() ?:
        throw IllegalArgumentException("Invalid year: $year")
}



