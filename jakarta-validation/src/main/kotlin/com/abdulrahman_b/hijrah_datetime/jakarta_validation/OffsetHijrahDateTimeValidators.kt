@file:Suppress("CanSealedSubClassBeObject")
package com.abdulrahman_b.hijrah_datetime.jakarta_validation

import com.abdulrahman_b.hijrah_datetime.OffsetHijrahDateTime
import java.time.Clock

internal class FutureOffsetHijrahDateTimeValidator : AbstractFutureValidator<OffsetHijrahDateTime>() {
    override fun getNowReference(clock: Clock) = OffsetHijrahDateTime.now(clock)
}

internal class FutureOrPresentOffsetHijrahDateTimeValidator : AbstractFutureOrPresentValidator<OffsetHijrahDateTime>() {
    override fun getNowReference(clock: Clock) = OffsetHijrahDateTime.now(clock)
}

internal class PastOffsetHijrahDateTimeValidator : AbstractPastValidator<OffsetHijrahDateTime>() {
    override fun getNowReference(clock: Clock) = OffsetHijrahDateTime.now(clock)
}

internal class PastOrPresentOffsetHijrahDateTimeValidator : AbstractPastOrPresentValidator<OffsetHijrahDateTime>() {
    override fun getNowReference(clock: Clock) = OffsetHijrahDateTime.now(clock)
}