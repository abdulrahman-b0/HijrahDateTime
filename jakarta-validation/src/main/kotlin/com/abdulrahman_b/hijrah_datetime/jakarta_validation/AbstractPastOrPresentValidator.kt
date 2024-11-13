package com.abdulrahman_b.hijrah_datetime.jakarta_validation

import com.abdulrahman_b.hijrah_datetime.AbstractHijrahTemporal
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.constraints.PastOrPresent

abstract class AbstractPastOrPresentValidator<T : AbstractHijrahTemporal<T>> :
    AbstractHijrahTemporalValidator<PastOrPresent, T>() {

    override fun isValid(value: T?, context: ConstraintValidatorContext): Boolean {
        val now = getNowReference(context.clockProvider.clock)
        return value == null || value.isBefore(now) || value.isEqual(now)
    }

}