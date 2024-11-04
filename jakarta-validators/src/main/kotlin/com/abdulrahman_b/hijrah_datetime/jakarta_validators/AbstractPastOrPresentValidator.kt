package com.abdulrahman_b.hijrah_datetime.jakarta_validators

import com.abdulrahman_b.hijrah_datetime.HijrahTemporal
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.constraints.PastOrPresent

abstract class AbstractPastOrPresentValidator<T : HijrahTemporal<T>> :
    AbstractHijrahTemporalValidator<PastOrPresent, T>() {

    override fun isValid(value: T?, context: ConstraintValidatorContext): Boolean {
        val now = getNowReference(context.clockProvider.clock)
        return value == null || value.isBefore(now) || value.isEqual(now)
    }

}