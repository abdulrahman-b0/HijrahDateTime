package com.abdulrahman_b.hijrahdatetime

import com.abdulrahman_b.hijrahdatetime.format.HijrahDateTimeFormat
import platform.Foundation.NSLocale
import kotlin.test.Test
import kotlin.test.assertEquals

class HijrahDateTimeBestFormatTest {

    @Test
    fun `test ofBestPattern`() {
        // We need to use a pattern that works for Hijri.
        // Note: java.time.format.DateTimeFormatter.ofPattern(pattern).withChronology(HijrahChronology.INSTANCE)
        // behaves differently than just ofPattern. But CalendarDateTimeFormat.ofPattern is just a wrapper.
        // To get Hijri formatting we should ideally use the builder.

        val format = HijrahDateTimeFormat.ofBestPattern("dd MMMM, yyyy", NSLocale("en"))


        val dt = HijrahDate(1445, 9, 1)
        val formatted = dt.format(format)
        assertEquals("01 Ramadan, 1445", formatted)

        val parsed = HijrahDate.parse(formatted, format)
        assertEquals(1445, parsed.year)
        assertEquals(9, parsed.month.number)
        assertEquals(1, parsed.day)
    }

    @Test
    fun `test ofBestPattern with Arabic Locale`() {
        // We need to use a pattern that works for Hijri.
        // Note: java.time.format.DateTimeFormatter.ofPattern(pattern).withChronology(HijrahChronology.INSTANCE)
        // behaves differently than just ofPattern. But CalendarDateTimeFormat.ofPattern is just a wrapper.
        // To get Hijri formatting we should ideally use the builder.

        val format = HijrahDateTimeFormat.ofBestPattern("dd MMMM, yyyy", NSLocale("ar"))


        val dt = HijrahDate(1445, 9, 1)
        val formatted = dt.format(format)
        assertEquals("01 رمضان، 1445", formatted)

        val parsed = HijrahDate.parse(formatted, format)
        assertEquals(1445, parsed.year)
        assertEquals(9, parsed.month.number)
        assertEquals(1, parsed.day)
    }

    @Test
    fun `test ofBestPattern with Arabic of Saudi Arabia Locale`() {
        // We need to use a pattern that works for Hijri.
        // Note: java.time.format.DateTimeFormatter.ofPattern(pattern).withChronology(HijrahChronology.INSTANCE)
        // behaves differently than just ofPattern. But CalendarDateTimeFormat.ofPattern is just a wrapper.
        // To get Hijri formatting we should ideally use the builder.

        val format = HijrahDateTimeFormat.ofBestPattern("dd MMMM, yyyy", NSLocale("ar-SA"))


        val dt = HijrahDate(1445, 9, 1)
        val formatted = dt.format(format)
        assertEquals("٠١ رمضان، ١٤٤٥", formatted)

        val parsed = HijrahDate.parse(formatted, format)
        assertEquals(1445, parsed.year)
        assertEquals(9, parsed.month.number)
        assertEquals(1, parsed.day)
    }




}