package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.FixedOffsetTimeZone
import platform.Foundation.*


internal sealed interface ComponentAccessors {

    interface DateBased {
        val calendarDatePair: Pair<NSCalendar, NSDate>

        val calendar: NSCalendar get() = calendarDatePair.first

        val year get() = calendar.component(NSCalendarUnitYear, calendarDatePair.second).toInt()
        val month get() = calendar.component(NSCalendarUnitMonth, calendarDatePair.second).toInt()
        val dayOfMonth get() = calendar.component(NSCalendarUnitDay, calendarDatePair.second).toInt()
        val dayOfWeek: DayOfWeek get() {
            val sundayIndexed = calendar.component(NSCalendarUnitWeekday, calendarDatePair.second).toInt()
            // Apple: 1=Sun, 2=Mon... 7=Sat.
            // kotlinx.datetime.DayOfWeek: Mon=1, Tue=2... Sun=7
            return when(sundayIndexed) {
                1 -> DayOfWeek.SUNDAY
                2 -> DayOfWeek.MONDAY
                3 -> DayOfWeek.TUESDAY
                4 -> DayOfWeek.WEDNESDAY
                5 -> DayOfWeek.THURSDAY
                6 -> DayOfWeek.FRIDAY
                else -> DayOfWeek.SATURDAY
            }
        }
        val dayOfYear get() = calendar.component(NSCalendarUnitDayOfYear, calendarDatePair.second).toInt()
    }

    interface DateTimeBased : DateBased {
        val hour get() = calendar.component(NSCalendarUnitHour, calendarDatePair.second).toInt()
        val minute get() = calendar.component(NSCalendarUnitMinute, calendarDatePair.second).toInt()
        val second get() = calendar.component(NSCalendarUnitSecond, calendarDatePair.second).toInt()
        val nanosecond get() = calendar.component(NSCalendarUnitNanosecond, calendarDatePair.second).toInt()
    }

    interface OffsetDateTimeBased : DateTimeBased {
        val offset: FixedOffsetTimeZone
    }

}