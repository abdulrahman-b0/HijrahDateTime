package jakarta_validators

import com.abdulrahman_b.hijrah_datetime.HijrahDateTime
import com.abdulrahman_b.hijrah_datetime.ZonedHijrahDateTime
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.slot
import jakarta.validation.Validation
import jakarta.validation.Validator
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
import java.time.ZoneId

@ExtendWith(MockKExtension::class)
internal class ZonedHijrahDateTimeValidationTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator


    @BeforeEach
    fun setUp() {
        val capturedClock = slot<Clock>()
        val capturedZoneId = slot<ZoneId>()
        val hijrahDateTime = ZonedHijrahDateTime.now()
        mockkObject(ZonedHijrahDateTime.Companion)

        every { ZonedHijrahDateTime.now(capture(capturedClock)) } answers { hijrahDateTime }
        every { ZonedHijrahDateTime.now(capture(capturedZoneId)) } answers { hijrahDateTime.withZoneSameInstant(ZoneId.of("Asia/Riyadh")) }

    }

    @Test
    @DisplayName("Past date is valid")
    fun testPastDate() {
        val now = ZonedHijrahDateTime.now(ZoneId.of("Asia/Dubai"))

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
        val now = ZonedHijrahDateTime.now(ZoneId.of("Asia/Dubai"))

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
        val now = ZonedHijrahDateTime.now(ZoneId.of("Asia/Dubai"))

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
        val now = ZonedHijrahDateTime.now(ZoneId.of("Asia/Dubai"))

        var violations = validator.validate(FutureOrPresentDateHolder(now.minusMinutes(1)))
        assertFalse(violations.isEmpty())

        violations = validator.validate(FutureOrPresentDateHolder(now))
        assertTrue(violations.isEmpty())

        violations = validator.validate(FutureOrPresentDateHolder(now.plusMinutes(1)))
        assertTrue(violations.isEmpty())

        violations = validator.validate(FutureOrPresentDateHolder(null))
        assertTrue(violations.isEmpty())

    }

    private data class PastDateHolder (@field:Past val date: ZonedHijrahDateTime?)
    private data class FutureDateHolder (@field:Future val date: ZonedHijrahDateTime?)
    private data class PastOrPresentDateHolder (@field:PastOrPresent val date: ZonedHijrahDateTime?)
    private data class FutureOrPresentDateHolder (@field:FutureOrPresent val date: ZonedHijrahDateTime?)


}