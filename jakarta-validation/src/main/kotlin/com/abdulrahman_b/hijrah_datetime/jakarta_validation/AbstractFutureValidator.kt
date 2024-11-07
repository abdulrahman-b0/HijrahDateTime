package com.abdulrahman_b.hijrah_datetime.jakarta_validation

import com.abdulrahman_b.hijrah_datetime.HijrahTemporal
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.constraints.Future

internal sealed class AbstractFutureValidator<T : HijrahTemporal<T>> : AbstractHijrahTemporalValidator<Future, T>() {
    
    override fun isValid(value: T?, context: ConstraintValidatorContext): Boolean {
        val now = getNowReference(context.clockProvider.clock)
        return value == null || value.isAfter(now)
    }
    
}

