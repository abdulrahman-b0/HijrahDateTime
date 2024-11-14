package com.abdulrahman_b.hijrah_datetime;

import com.abdulrahman_b.hijrah_datetime.extensions.HijrahDates;
import com.abdulrahman_b.hijrah_datetime.formats.HijrahFormatters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;

/**
 * This class is used to call all static functions in HijrahDateTime, HijrahMonth, OffsetHijrahDateTime, OffsetHijrahDate, and ZonedHijrahDateTime.
 * This is to increase the code coverage of the library.
 * Since the static in companion objects are not counted in the code coverage, although they are tested in the unit tests of the library.
 * But since tests are written in Kotlin, the code coverage tool does not count the generated static functions in the companion objects. Which leads to a lower code coverage falsely.
 */
public class StaticMethodsCoverageTest {

    @Test
    @DisplayName("Call all static functions in HijrahDateTime")
    public void callHijrahDateTimeStaticFunctions() {
        HijrahDateTime.now();
        HijrahDateTime.now(ZoneId.systemDefault());
        HijrahDateTime.now(Clock.systemDefaultZone());
        HijrahDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        HijrahDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        HijrahDateTime.of(1443, 1, 1, 0, 0, 0, 0);
        HijrahDateTime.of(1443, 1, 1, 0, 0, 0);
        HijrahDateTime.of(1443, 1, 1, 0, 0);
        HijrahDateTime.of(HijrahDate.now(), LocalTime.now());
        HijrahDateTime.parse("1443-01-01T00:00:00");
        HijrahDateTime.parse("1443-01-01T00:00:00", HijrahFormatters.HIJRAH_DATE_TIME);
        HijrahDateTime.from(OffsetHijrahDateTime.now());
    }

    @Test
    @DisplayName("Call all static functions in HijrahMonth")
    public void callHijrahMonthStaticFunctions() {
        HijrahMonth.of(1);
        HijrahMonth.from(HijrahDate.now());
    }

    @Test
    @DisplayName("Call all static functions in OffsetHijrahDateTime")
    public void callOffsetHijrahDateTimeStaticFunctions() {
        OffsetHijrahDateTime.now();
        OffsetHijrahDateTime.now(ZoneOffset.UTC);
        OffsetHijrahDateTime.now(Clock.systemDefaultZone());
        OffsetHijrahDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        OffsetHijrahDateTime.of(1443, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetHijrahDateTime.of(1443, 1, 1, 0, 0, 0, ZoneOffset.UTC);
        OffsetHijrahDateTime.of(1443, 1, 1, 0, 0, ZoneOffset.UTC);
        OffsetHijrahDateTime.of(HijrahDate.now(), LocalTime.now(), ZoneOffset.UTC);
        OffsetHijrahDateTime.of(HijrahDateTime.now(), ZoneOffset.UTC);
        OffsetHijrahDateTime.parse("1443-01-01T00:00:00+00:00");
        OffsetHijrahDateTime.parse("1443-01-01T00:00:00+00:00", HijrahFormatters.HIJRAH_OFFSET_DATE_TIME);
        OffsetHijrahDateTime.from(ZonedHijrahDateTime.now());
    }

    @Test
    @DisplayName("Call all static functions in OffsetHijrahDate")
    public void callOffsetHijrahDateStaticFunctions() {
        OffsetHijrahDate.now();
        OffsetHijrahDate.now(ZoneOffset.UTC);
        OffsetHijrahDate.now(Clock.systemDefaultZone());
        OffsetHijrahDate.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        OffsetHijrahDate.of(1443, 1, 1, ZoneOffset.UTC);
        OffsetHijrahDate.of(HijrahDate.now(), ZoneOffset.UTC);
        OffsetHijrahDate.parse("1443-01-01+00:00");
        OffsetHijrahDate.parse("1443-01-01+00:00", HijrahFormatters.HIJRAH_OFFSET_DATE);
        OffsetHijrahDate.from(ZonedHijrahDateTime.now());
    }

    @Test
    @DisplayName("Call all static functions in ZonedHijrahDateTime")
    public void callZonedHijrahDateTimeStaticFunctions() {
        ZonedHijrahDateTime.now();
        ZonedHijrahDateTime.now(ZoneId.systemDefault());
        ZonedHijrahDateTime.now(Clock.systemDefaultZone());
        ZonedHijrahDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
        ZonedHijrahDateTime.of(1443, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
        ZonedHijrahDateTime.of(1443, 1, 1, 0, 0, 0, ZoneId.systemDefault());
        ZonedHijrahDateTime.of(1443, 1, 1, 0, 0, ZoneId.systemDefault());
        ZonedHijrahDateTime.of(HijrahDate.now(), LocalTime.now(), ZoneId.systemDefault());
        ZonedHijrahDateTime.of(HijrahDateTime.now(), ZoneId.systemDefault());
        ZonedHijrahDateTime.parse("1443-01-01T00:00:00+00:00[UTC]");
        ZonedHijrahDateTime.parse("1443-01-01T00:00:00+00:00[UTC]", HijrahFormatters.HIJRAH_ZONED_DATE_TIME);
        ZonedHijrahDateTime.from(ZonedDateTime.now());
    }

    @Test
    @DisplayName("Call all static functions in HijrahFormatters")
    public void callHijrahFormattersStaticFunctions() {
        HijrahFormatters.buildHijrahDateFormatter("");
        HijrahFormatters.buildHijrahDateTimeFormatter(HijrahFormatters.HIJRAH_DATE);
        HijrahFormatters.buildHijrahDateTimeFormatter(HijrahFormatters.HIJRAH_DATE, " ");
        HijrahFormatters.buildHijrahDateTimeFormatter(HijrahFormatters.HIJRAH_DATE, " ", HijrahFormatters.LOCAL_TIME_12_HOURS);
        HijrahFormatters.buildOffsetHijrahDateFormatter();
        HijrahFormatters.buildOffsetHijrahDateTimeFormatter(HijrahFormatters.HIJRAH_DATE_TIME);
        HijrahFormatters.buildOffsetHijrahDateFormatter();
        HijrahFormatters.buildOffsetHijrahDateFormatter(HijrahFormatters.HIJRAH_DATE_TIME);
        HijrahFormatters.buildZonedHijrahDateTimeFormatter();
        HijrahFormatters.buildZonedHijrahDateTimeFormatter(HijrahFormatters.HIJRAH_DATE_TIME);
    }

    @Test
    @DisplayName("Call all static functions in HijrahDates")
    public void callHijrahDatesStaticFunctions() {
        var date = HijrahDate.now();

        HijrahDates.ofEpochDay(0);
        HijrahDates.ofInstant(Instant.now(), ZoneId.systemDefault());
        HijrahDates.ofYearDay(1443, 1);
        HijrahDates.parse("1443-01-01");
        HijrahDates.parse("1443-01-01", HijrahFormatters.HIJRAH_DATE);

        HijrahDates.atLocalTime(date, LocalTime.now());
        HijrahDates.atStartOfDay(date);
        HijrahDates.atStartOfDay(date, ZoneId.systemDefault());
        HijrahDates.atTime(date, OffsetTime.now());
        HijrahDates.datesUntil(date, date);
        HijrahDates.datesUntil(date, date, 2);
        HijrahDates.toInstant(date, ZoneOffset.UTC);
        HijrahDates.plusDays(date, 1);
        HijrahDates.plusWeeks(date, 1);
        HijrahDates.plusMonths(date, 1);
        HijrahDates.plusYears(date, 1);
        HijrahDates.minusDays(date, 1);
        HijrahDates.minusWeeks(date, 1);
        HijrahDates.minusMonths(date, 1);
        HijrahDates.minusYears(date, 1);
        HijrahDates.withDayOfMonth(date, 1);
        HijrahDates.withMonth(date, HijrahMonth.DHU_AL_HIJJAH);
        HijrahDates.withYear(date, 1443);

    }

}
