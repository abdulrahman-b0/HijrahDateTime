package com.abdulrahman_b.hijrahdatetime.jakarta_validation

import com.abdulrahman_b.hijrahdatetime.OffsetHijrahDate
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.slot
import jakarta.validation.Validation
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.PastOrPresent
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock

@ExtendWith(MockKExtension::class)
internal class OffsetHijrahDateValidationTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator


    @BeforeEach
    fun setUp() {
        val capturedClock = slot<Clock>()
        val hijrahDate = OffsetHijrahDate.now()
        mockkObject(OffsetHijrahDate.Companion)

        every { OffsetHijrahDate.now(capture(capturedClock)) } answers { hijrahDate }

    }


    @Test
    @DisplayName("Past date is valid")
    fun testPastDate() {
        val now = OffsetHijrahDate.now(Clock.systemUTC())

        var violations = validator.validate(PastDateHolder(now.minusDays(1)))
        assertTrue(violations.isEmpty())

        violations = validator.validate(PastDateHolder(now))
        assertFalse(violations.isEmpty())

        violations = validator.validate(PastDateHolder(now.plusDays(1)))
        assertFalse(violations.isEmpty())

        violations = validator.validate(PastDateHolder(null))
        assertTrue(violations.isEmpty())

    }

    @Test
    @DisplayName("Future date is valid")
    fun testFutureDate() {
        val now = OffsetHijrahDate.now(Clock.systemUTC())

        var violations = validator.validate(FutureDateHolder(now.plusDays(1)))
        assertTrue(violations.isEmpty())

        violations = validator.validate(FutureDateHolder(now))
        assertFalse(violations.isEmpty())

        violations = validator.validate(FutureDateHolder(now.minusDays(1)))
        assertFalse(violations.isEmpty())

        violations = validator.validate(FutureDateHolder(null))
        assertTrue(violations.isEmpty())

    }

    @Test
    @DisplayName("Past or present date is valid")
    fun testPastOrPresentDate() {
        val now = OffsetHijrahDate.now(Clock.systemUTC())

        var violations = validator.validate(PastOrPresentDateHolder(now.minusDays(1)))
        assertTrue(violations.isEmpty())

        violations = validator.validate(PastOrPresentDateHolder(now))
        assertTrue(violations.isEmpty())

        violations = validator.validate(PastOrPresentDateHolder(now.plusDays(1)))
        assertFalse(violations.isEmpty())

        violations = validator.validate(PastOrPresentDateHolder(null))
        assertTrue(violations.isEmpty())
    }

    @Test
    @DisplayName("Future or present date is valid")
    fun testFutureOrPresentDate() {
        val now = OffsetHijrahDate.now(Clock.systemUTC())

        var violations = validator.validate(FutureOrPresentDateHolder(now.minusDays(1)))
        assertFalse(violations.isEmpty())

        violations = validator.validate(FutureOrPresentDateHolder(now))
        assertTrue(violations.isEmpty())

        violations = validator.validate(FutureOrPresentDateHolder(now.plusDays(1)))
        assertTrue(violations.isEmpty())

        violations = validator.validate(FutureOrPresentDateHolder(null))
        assertTrue(violations.isEmpty())

    }

    private data class PastDateHolder (@field:Past val date: OffsetHijrahDate?)
    private data class FutureDateHolder (@field:Future val date: OffsetHijrahDate?)
    private data class PastOrPresentDateHolder (@field:PastOrPresent val date: OffsetHijrahDate?)
    private data class FutureOrPresentDateHolder (@field:FutureOrPresent val date: OffsetHijrahDate?)


}