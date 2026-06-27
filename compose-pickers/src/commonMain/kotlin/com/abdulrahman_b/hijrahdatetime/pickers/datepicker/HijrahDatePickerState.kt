package com.abdulrahman_b.hijrahdatetime.pickers.datepicker

/*
* Copyright 2023 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.abdulrahman_b.hijrahdatetime.HijrahDate
import com.abdulrahman_b.hijrahdatetime.fromEpochDays
import com.abdulrahman_b.hijrahdatetime.pickers.HijrahSelectableDates
import com.abdulrahman_b.hijrahdatetime.pickers.datepicker.HijrahMultiDatePickerState.Companion.Saver
import com.abdulrahman_b.hijrahdatetime.pickers.valueOf
import com.abdulrahman_b.hijrahdatetime.toEpochDays
import com.abdulrahman_b.hijrahdatetime.toHijrahDateTime
import com.abdulrahman_b.hijrahdatetime.yearMonth
import com.abdulrahman_b.hijrahdatetime.yearmonth.HijrahYearMonth
import kotlinx.datetime.TimeZone
import kotlin.time.Clock


/**
 * Represents the state of a Hijri date picker, which can be observed and controlled.
 * Use [rememberHijrahDatePickerState] to create and remember an instance of this state.
 */
@ExperimentalMaterial3Api
@Stable
interface HijrahDatePickerState {

    /**
     * The currently selected date, represented as a [HijrahDate].
     * Throws [IllegalArgumentException] if the date is outside the [yearRange].
     */
    var selectedDate: HijrahDate?

    /**
     * The currently displayed month, represented as a [HijrahDate].
     * Throws [IllegalArgumentException] if the date is outside the [yearRange].
     */
    var displayedMonth: HijrahYearMonth

    /**
     * The current display mode of the date picker, either picker or input.
     */
    var displayMode: DisplayMode

    /**
     * The range of years that the date picker is limited to.
     */
    val yearRange: IntRange

    /**
     * Defines which dates are selectable. Disabled dates will appear grayed out in the UI.
     */
    val selectableDates: HijrahSelectableDates

}

@OptIn(ExperimentalMaterial3Api::class)
internal class HijrahDatePickerStateImpl(
    initialSelectedDate: HijrahDate?,
    initialDisplayedMonth: HijrahYearMonth,
    initialDisplayMode: DisplayMode,
    override val yearRange: IntRange,
    override val selectableDates: HijrahSelectableDates,
) : HijrahDatePickerState {

    override var selectedDate by mutableStateOf(initialSelectedDate)


    override var displayedMonth by mutableStateOf(initialDisplayedMonth)


    override var displayMode by mutableStateOf(initialDisplayMode)


    companion object {

        fun Saver(selectableDates: HijrahSelectableDates): Saver<HijrahDatePickerState, *> = listSaver(
            save = {
                listOf(
                    it.selectedDate?.toEpochDays(),
                    it.displayedMonth.onDay(1).toEpochDays(),
                    it.yearRange.first,
                    it.yearRange.last,
                    it.displayMode.toString(),
                )
            },
            restore = { value ->
                HijrahDatePickerStateImpl(
                    initialSelectedDate = (value[0] as? Long)?.let(HijrahDate::fromEpochDays),
                    initialDisplayedMonth = HijrahDate.fromEpochDays(value[1] as Long).yearMonth,
                    initialDisplayMode = DisplayMode.valueOf(value[4] as String),
                    yearRange = IntRange(value[2] as Int, value[3] as Int),
                    selectableDates = selectableDates,
                )
            }
        )

    }

}

/**
 * State holder for a Hijri **multi-date** picker.
 *
 * This state stores:
 * - The set of currently selected Hijri dates [selectedDates].
 * - The month that is currently visible in the calendar [displayedMonth].
 * - The active display mode [displayMode] (for consistency with the single-date picker).
 * - The allowed Hijri year range [yearRange].
 * - Constraints that determine which dates may be selected [selectableDates].
 *
 * Unlike [HijrahDatePickerState], which only tracks a *single* selected date,
 * [HijrahMultiDatePickerState] allows users to select an arbitrary set of dates within
 * the configured year range. Each date tap toggles that date in or out of [selectedDates].
 *
 * This class is intended to be created and remembered via
 * [rememberHijrahMultiDatePickerState], so that its state survives configuration
 * changes and process death when used with [rememberSaveable].
 *
 * Typical usage:
 *
 * @sample
 * val multiState = rememberHijriMultiDatePickerState()
 * HijriMultiDatePicker(state = multiState)
 *
 * @param initialSelectedDates The dates initially marked as selected in the calendar.
 * @param selectableDates Constraints used to determine which dates are enabled for selection.
 * @param initialDisplayedMonth The Hijri month initially displayed when the picker is shown.
 * @param initialDisplayMode The initial display mode of the picker UI.
 * @param yearRange The allowed Hijri year range that users can navigate through.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Stable
class HijrahMultiDatePickerState internal constructor(
    initialSelectedDates: Set<HijrahDate>,
    val selectableDates: HijrahSelectableDates,
    initialDisplayedMonth: HijrahYearMonth,
    initialDisplayMode: DisplayMode,
    val yearRange: IntRange,
) {

    /**
     * The current set of selected Hijri dates.
     *
     * This property is observable by Compose. Any time [selectedDates] is updated,
     * the picker UI will automatically recompose to reflect the new selection.
     */
    var selectedDates by mutableStateOf(initialSelectedDates)
        internal set

    /**
     * The month currently displayed in the calendar grid, represented as a [HijrahDate].
     *
     * The day component of this date is ignored; it is treated as the "month anchor"
     * the UI uses to show the grid. This value is kept in sync with the underlying
     * pager via [onDisplayedMonthChange] callbacks.
     */
    var displayedMonth by mutableStateOf(initialDisplayedMonth)
        internal set

    /**
     * The current display mode of the picker.
     *
     * For multi-select scenarios this will typically remain [DisplayMode.Picker],
     * but it is kept for parity with [HijrahDatePickerState] and possible future
     * input modes.
     */
    var displayMode by mutableStateOf(initialDisplayMode)
        internal set

    /**
     * Returns `true` if the given [date] is currently selected.
     */
    fun isSelected(date: HijrahDate): Boolean =
        selectedDates.contains(date)

    /**
     * Toggles the selection state of the given [date].
     *
     * - If the [date] is currently in [selectedDates], it will be removed.
     * - If it is not present and satisfies [selectableDates], it will be added.
     *
     * Dates that are not selectable according to [selectableDates] are ignored.
     */
    fun toggleDate(date: HijrahDate) {
        val canSelect = with(selectableDates) {
            isSelectableYear(date.year) && isSelectableDate(date)
        }
        if (!canSelect) return

        val current = selectedDates.toMutableSet()
        if (current.contains(date)) {
            current.remove(date)
        } else {
            current.add(date)
        }
        selectedDates = current.toSet()
    }

    /**
     * Clears all selected dates, resulting in an empty [selectedDates] set.
     */
    fun clearSelection() {
        selectedDates = emptySet()
    }

    internal companion object {

        /**
         * A [Saver] implementation that allows [HijrahMultiDatePickerState] to participate
         * in `rememberSaveable`, so that it can be restored across configuration changes
         * and process recreation.
         *
         * The saver serializes:
         * - The selected dates as a list of epoch days.
         * - The displayed month as an epoch day.
         * - The display mode as a [String].
         * - The year range bounds.
         */
        fun Saver(
            selectableDates: HijrahSelectableDates
        ): Saver<HijrahMultiDatePickerState, *> = listSaver(
            save = { state ->
                listOf(
                    state.selectedDates.map { it.toEpochDays() },
                    state.displayedMonth.onDay(1).toEpochDays(),
                    state.displayMode.toString(),
                    state.yearRange.first,
                    state.yearRange.last,
                )
            },
            restore = { value ->
                val selectedEpochDays = value[0] as List<Long>
                val displayedMonthEpochDay = value[1] as Long
                val displayModeString = value[2] as String
                val startYear = value[3] as Int
                val endYear = value[4] as Int

                val restoredSelectedDates = selectedEpochDays
                    .map(HijrahDate::fromEpochDays)
                    .toSet()

                HijrahMultiDatePickerState(
                    initialSelectedDates = restoredSelectedDates,
                    initialDisplayedMonth = HijrahDate.fromEpochDays(displayedMonthEpochDay).yearMonth,
                    initialDisplayMode = DisplayMode.valueOf(displayModeString),
                    yearRange = IntRange(startYear, endYear),
                    selectableDates = selectableDates,
                )
            }
        )
    }
}

/**
 * Creates and remembers a [HijrahMultiDatePickerState] across recompositions.
 *
 * This overload is similar to [rememberHijrahDatePickerState], but is designed for
 * multi-date selection scenarios where users can select an arbitrary set of dates
 * within the allowed [yearRange].
 *
 * The returned state is automatically saved and restored using [rememberSaveable]
 * and [HijrahMultiDatePickerState.Saver], so it will survive configuration changes
 * and process recreation when used in a standard Compose setup.
 *
 * @param initialSelectedDates The initial set of dates that should appear selected.
 *   Defaults to an empty set, meaning no dates are selected initially.
 * @param initialDisplayedMonth The Hijri month to display first when the picker
 *   is shown. Defaults to [HijrahDate.now].
 * @param initialDisplayMode The initial display mode of the picker UI, either
 *   [DisplayMode.Picker] or [DisplayMode.Input]. For multi-select scenarios,
 *   [DisplayMode.Picker] is typically used.
 * @param yearRange The inclusive Hijri year range users are allowed to navigate.
 *   Defaults to [HijrahDatePickerDefaults.YearRange].
 * @param selectableDates The constraints that determine which dates are enabled
 *   for interaction. Defaults to [HijrahDatePickerDefaults.AllDates], which allows
 *   all dates within [yearRange].
 *
 * @return A remembered [HijrahMultiDatePickerState] instance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberHijrahMultiDatePickerState(
    initialSelectedDates: Set<HijrahDate> = emptySet(),
    initialDisplayedMonth: HijrahYearMonth = Clock.System.now().toHijrahDateTime(TimeZone.currentSystemDefault()).date.yearMonth,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    yearRange: IntRange = HijrahDatePickerDefaults.YearRange,
    selectableDates: HijrahSelectableDates = HijrahDatePickerDefaults.AllDates,
): HijrahMultiDatePickerState {
    return rememberSaveable(
        saver = HijrahMultiDatePickerState.Saver(selectableDates)
    ) {
        HijrahMultiDatePickerState(
            initialSelectedDates = initialSelectedDates,
            initialDisplayedMonth = initialDisplayedMonth,
            initialDisplayMode = initialDisplayMode,
            yearRange = yearRange,
            selectableDates = selectableDates,
        )
    }
}

/**
 * Creates a [HijrahDatePickerState] that can be remembered across compositions.
 *
 * @param initialSelectedDate The initially selected date, represented as a [HijrahDate], or null if no date is selected.
 * @param initialDisplayedMonth The initially displayed month, represented as a [HijrahDate]. Defaults to the current month.
 * @param initialDisplayMode The initial display mode of the date picker, either picker or input. Defaults to [DisplayMode.Picker].
 * @param yearRange The range of years that the date picker is limited to, excluded dates doesn't appear in the picker UI. Defaults to [HijrahDatePickerDefaults.YearRange].
 * @param selectableDates Defines which dates are selectable. Disabled dates will appear grayed out in the UI. Defaults to [HijrahDatePickerDefaults.AllDates], which allows all dates.
 * @return A [HijrahDatePickerState] that can be remembered across compositions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberHijrahDatePickerState(
    initialSelectedDate: HijrahDate? = null,
    initialDisplayedMonth: HijrahYearMonth = remember {
        Clock.System.now().toHijrahDateTime(TimeZone.currentSystemDefault()).date.yearMonth
    },
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    yearRange: IntRange = HijrahDatePickerDefaults.YearRange,
    selectableDates: HijrahSelectableDates = HijrahDatePickerDefaults.AllDates,
): HijrahDatePickerState {
    return rememberSaveable(
        saver = HijrahDatePickerStateImpl.Saver(selectableDates)
    ) {
        HijrahDatePickerStateImpl(
            initialSelectedDate,
            initialDisplayedMonth,
            initialDisplayMode,
            yearRange,
            selectableDates,
        )
    }
}
