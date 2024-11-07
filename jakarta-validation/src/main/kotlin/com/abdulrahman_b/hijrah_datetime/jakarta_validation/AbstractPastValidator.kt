package com.abdulrahman_b.hijrah_datetime.jakarta_validation

import com.abdulrahman_b.hijrah_datetime.HijrahTemporal
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.constraints.Past

sealed class AbstractPastValidator<T : HijrahTemporal<T>> :
    AbstractHijrahTemporalValidator<Past, T>() {

    override fun isValid(value: T?, context: ConstraintValidatorContext): Boolean {
        val now = getNowReference(context.clockProvider.clock)
        return value == null || value.isBefore(now)
    }
}