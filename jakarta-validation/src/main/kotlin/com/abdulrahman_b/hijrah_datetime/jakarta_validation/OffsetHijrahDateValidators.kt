@file:Suppress("CanSealedSubClassBeObject")
package com.abdulrahman_b.hijrah_datetime.jakarta_validation

import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDate
import java.time.Clock

internal class FutureOffsetHijrahDateValidator : AbstractFutureValidator<OffsetHijrahDate>() {
    override fun getNowReference(clock: Clock) = OffsetHijrahDate.now(clock)
}

internal class FutureOrPresentOffsetHijrahDateValidator : AbstractFutureOrPresentValidator<OffsetHijrahDate>() {
    override fun getNowReference(clock: Clock) = OffsetHijrahDate.now(clock)
}

internal class PastOffsetHijrahDateValidator : AbstractPastValidator<OffsetHijrahDate>() {
    override fun getNowReference(clock: Clock) = OffsetHijrahDate.now(clock)
}

internal class PastOrPresentOffsetHijrahDateValidator : AbstractPastOrPresentValidator<OffsetHijrahDate>() {
    override fun getNowReference(clock: Clock) = OffsetHijrahDate.now(clock)
}