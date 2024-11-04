package com.abdulrahman_b.hijrah_datetime.jakarta_validators

import com.abdulrahman_b.hijrah_datetime.HijrahTemporal
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.constraints.FutureOrPresent


internal sealed class AbstractFutureOrPresentValidator<T : HijrahTemporal<T>> : AbstractHijrahTemporalValidator<FutureOrPresent, T>() {

    override fun isValid(value: T?, context: ConstraintValidatorContext): Boolean {
        val now = getNowReference(context.clockProvider.clock)
        return value == null || value.isAfter(now) || value.isEqual(now)
    }

}