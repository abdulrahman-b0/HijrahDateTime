@file:Suppress("CanSealedSubClassBeObject")
package com.abdulrahman_b.hijrahdatetime.jakarta_validation

import com.abdulrahman_b.hijrahdatetime.ZonedHijrahDateTime
import java.time.Clock

internal class FutureZonedHijrahDateTimeValidator : AbstractFutureValidator<ZonedHijrahDateTime>() {
    override fun getNowReference(clock: Clock) = ZonedHijrahDateTime.now(clock)
}

internal class FutureOrPresentZonedHijrahDateTimeValidator : AbstractFutureOrPresentValidator<ZonedHijrahDateTime>() {
    override fun getNowReference(clock: Clock) = ZonedHijrahDateTime.now(clock)
}

internal class PastZonedHijrahDateTimeValidator : AbstractPastValidator<ZonedHijrahDateTime>() {
    override fun getNowReference(clock: Clock) = ZonedHijrahDateTime.now(clock)
}

internal class PastOrPresentZonedHijrahDateTimeValidator: AbstractPastOrPresentValidator<ZonedHijrahDateTime>() {
    override fun getNowReference(clock: Clock) = ZonedHijrahDateTime.now(clock)
}