package com.abdulrahman_b.hijrahdatetime

import kotlin.test.*

class HijrahDateTimeFormatTest {

    @Test
    fun `test ofPattern`() {
        // We need to use a pattern that works for Hijri. 
        // Note: java.time.format.DateTimeFormatter.ofPattern(pattern).withChronology(HijrahChronology.INSTANCE)
        // behaves differently than just ofPattern. But CalendarDateTimeFormat.ofPattern is just a wrapper.
        // To get Hijri formatting we should ideally use the builder.
        
        val format = HijrahDateTimeFormatBuilder().apply {
            byUnicodePattern("yyyy-MM-dd")
            locale = FormatLocale.English
        }.build()

        val dt = HijrahDate(1445, 9, 1)
        val formatted = dt.format(format)
        assertEquals("1445-09-01", formatted)

        val parsed = HijrahDate.parse(formatted, format)
        assertEquals(1445, parsed.year)
        assertEquals(9, parsed.month)
        assertEquals(1, parsed.dayOfMonth)
    }

    @Test
    fun `test builder components`() {
        val format = HijrahDateTimeFormatBuilder().apply {
            locale = FormatLocale.English
            year()
            char('-')
            monthNumber()
            char('-')
            dayOfMonth()
            chars(" at ")
            dayOfWeek(NameStyle.FULL)
        }.build()

        val dt = HijrahDate(1447, 7, 22)
        val dayOfWeek = dt.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val formatted = dt.format(format)
        assertEquals("1447-07-22 at $dayOfWeek", formatted)
    }

    @Test
    fun `test month names`() {
        val format = HijrahDateTimeFormatBuilder().apply {
            locale = FormatLocale.English
            monthName(NameStyle.FULL)
            char(' ')
            year()
        }.build()

        val dt = HijrahDate(1447, 7, 22)
        assertEquals("Rajab 1447", dt.format(format))
        
        val shortFormat = HijrahDateTimeFormatBuilder().apply {
            locale = FormatLocale.English
            monthName(NameStyle.ABBREVIATED)
            char(' ')
            year()
        }.build()
        // Abbreviation for Rajab in English Hijri is usually Raj or Raj. depending on platform/provider
        assertTrue(dt.format(shortFormat).startsWith("Raj"))
        assertTrue(dt.format(shortFormat).contains("1447"))
    }


    @Test
    fun `test parse or null`() {
        val format = HijrahDateTimeFormat.ofPattern("yyyy-MM-dd")
        assertNull(HijrahDate.parseOrNull("invalid", format))
        assertNotNull(HijrahDate.parseOrNull("1447-07-29", format))
    }
}
