package com.abdulrahman_b.hijrah_datetime.jakarta_validation

import com.abdulrahman_b.hijrah_datetime.AbstractHijrahTemporal
import jakarta.validation.ConstraintValidator
import java.time.Clock

sealed class AbstractHijrahTemporalValidator <A: Annotation, T: AbstractHijrahTemporal<T>> : ConstraintValidator<A, T> {

    protected abstract fun getNowReference(clock: Clock): T

}