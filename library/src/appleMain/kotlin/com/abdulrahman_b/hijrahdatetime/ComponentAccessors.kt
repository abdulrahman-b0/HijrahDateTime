package com.abdulrahman_b.hijrahdatetime

import kotlinx.datetime.DayOfWeek
import platform.Foundation.*


internal sealed interface ComponentAccessors {

    interface DateBased {

        val nsCalendar: NSCalendar

        val nsDate: NSDate

        val year get() = nsCalendar.component(NSCalendarUnitYear, nsDate).toInt()
        val month get() = HijrahMonth.of(nsCalendar.component(NSCalendarUnitMonth, nsDate).toInt())
        val day get() = nsCalendar.component(NSCalendarUnitDay, nsDate).toInt()
        val dayOfWeek: DayOfWeek get() {
            val sundayIndexed = nsCalendar.component(NSCalendarUnitWeekday, nsDate).toInt()
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
        val dayOfYear get() = nsCalendar.component(NSCalendarUnitDayOfYear, nsDate).toInt()
    }

    interface DateTimeBased : DateBased {
        val hour get() = nsCalendar.component(NSCalendarUnitHour, nsDate).toInt()
        val minute get() = nsCalendar.component(NSCalendarUnitMinute, nsDate).toInt()
        val second get() = nsCalendar.component(NSCalendarUnitSecond, nsDate).toInt()
        val nanosecond get() = nsCalendar.component(NSCalendarUnitNanosecond, nsDate).toInt()
    }

}