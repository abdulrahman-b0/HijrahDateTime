package com.abdulrahman_b.hijrahdatetime.jakarta.validation

import com.abdulrahman_b.hijrahdatetime.HijrahDateTime
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
internal class HijrahDateTimeValidationTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator


    @BeforeEach
    fun setUp() {
        val capturedClock = slot<Clock>()
        val hijrahDateTime = HijrahDateTime.now()
        mockkObject(HijrahDateTime.Companion)

        every { HijrahDateTime.now(capture(capturedClock)) } answers { hijrahDateTime }
        every { HijrahDateTime.now() } returns hijrahDateTime

    }


    @Test
    @DisplayName("Past date is valid")
    fun testPastDate() {
        val now = HijrahDateTime.now()

        var violations = validator.validate(PastDateHolder(now.minusMinutes(1)))
        assertTrue(violations.isEmpty())

        violations = validator.validate(PastDateHolder(now))
        assertFalse(violations.isEmpty())

        violations = validator.validate(PastDateHolder(now.plusMinutes(1)))
        assertFalse(violations.isEmpty())

        violations = validator.validate(PastDateHolder(null))
        assertTrue(violations.isEmpty())

    }

    @Test
    @DisplayName("Future date is valid")
    fun testFutureDate() {
        val now = HijrahDateTime.now()

        var violations = validator.validate(FutureDateHolder(now.plusMinutes(1)))
        assertTrue(violations.isEmpty())

        violations = validator.validate(FutureDateHolder(now))
        assertFalse(violations.isEmpty())

        violations = validator.validate(FutureDateHolder(now.minusMinutes(1)))
        assertFalse(violations.isEmpty())

        violations = validator.validate(FutureDateHolder(null))
        assertTrue(violations.isEmpty())

    }

    @Test
    @DisplayName("Past or present date is valid")
    fun testPastOrPresentDate() {
        val now = HijrahDateTime.now()

        var violations = validator.validate(PastOrPresentDateHolder(now.minusMinutes(1)))
        assertTrue(violations.isEmpty())

        violations = validator.validate(PastOrPresentDateHolder(now))
        assertTrue(violations.isEmpty())

        violations = validator.validate(PastOrPresentDateHolder(now.plusMinutes(1)))
        assertFalse(violations.isEmpty())

        violations = validator.validate(PastOrPresentDateHolder(null))
        assertTrue(violations.isEmpty())
    }

    @Test
    @DisplayName("Future or present date is valid")
    fun testFutureOrPresentDate() {
        val now = HijrahDateTime.now()

        var violations = validator.validate(FutureOrPresentDateHolder(now.minusMinutes(1)))
        assertFalse(violations.isEmpty())

        violations = validator.validate(FutureOrPresentDateHolder(now))
        assertTrue(violations.isEmpty())

        violations = validator.validate(FutureOrPresentDateHolder(now.plusMinutes(1)))
        assertTrue(violations.isEmpty())

        violations = validator.validate(FutureOrPresentDateHolder(null))
        assertTrue(violations.isEmpty())
    }

    private data class PastDateHolder (@field:Past val date: HijrahDateTime?)
    private data class FutureDateHolder (@field:Future val date: HijrahDateTime?)
    private data class PastOrPresentDateHolder (@field:PastOrPresent val date: HijrahDateTime?)
    private data class FutureOrPresentDateHolder (@field:FutureOrPresent val date: HijrahDateTime?)


}