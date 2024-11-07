@file:Suppress("CanSealedSubClassBeObject")

package com.abdulrahman_b.hijrah_datetime.jakarta_validation

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import java.time.Clock


internal class FutureHijrahDateTimeValidator : AbstractFutureValidator<HijrahDateTime>() {
    override fun getNowReference(clock: Clock) = HijrahDateTime.now(clock)
}

internal class FutureOrPresentHijrahDateTimeValidator : AbstractFutureOrPresentValidator<HijrahDateTime>() {
    override fun getNowReference(clock: Clock) = HijrahDateTime.now(clock)
}

internal class PastHijrahDateTimeValidator : AbstractPastValidator<HijrahDateTime>() {
    override fun getNowReference(clock: Clock) = HijrahDateTime.now(clock)
}

internal class PastOrPresentHijrahDateTimeValidator : AbstractPastOrPresentValidator<HijrahDateTime>() {
    override fun getNowReference(clock: Clock) = HijrahDateTime.now(clock)
}