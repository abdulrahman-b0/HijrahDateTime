package com.abdulrahman_b.hijrahdatetime.jakarta.validation

import com.abdulrahman_b.hijrahdatetime.AbstractHijrahTemporal
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.constraints.Future

internal sealed class AbstractFutureValidator<T : AbstractHijrahTemporal<T>> : AbstractHijrahTemporalValidator<Future, T>() {
    
    override fun isValid(value: T?, context: ConstraintValidatorContext): Boolean {
        val now = getNowReference(context.clockProvider.clock)
        return value == null || value.isAfter(now)
    }
    
}

