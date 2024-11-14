package com.abdulrahman_b.hijrahdatetime.jakarta_validation

import com.abdulrahman_b.hijrahdatetime.AbstractHijrahTemporal
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.constraints.FutureOrPresent


internal sealed class AbstractFutureOrPresentValidator<T : AbstractHijrahTemporal<T>> : AbstractHijrahTemporalValidator<FutureOrPresent, T>() {

    override fun isValid(value: T?, context: ConstraintValidatorContext): Boolean {
        val now = getNowReference(context.clockProvider.clock)
        return value == null || value.isAfter(now) || value.isEqual(now)
    }

}