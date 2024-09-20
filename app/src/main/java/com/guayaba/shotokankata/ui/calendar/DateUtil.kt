package com.guayaba.shotokankata.ui.calendar

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

object DateUtil {
    val daysOfWeek: Array<String>
        get() {
            val days = listOf(
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
            )

            val daysOfWeek = Array(7) { "" }

            for ((index, dayOfWeek) in days.withIndex()) {
                val localizedDayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                daysOfWeek[index] = localizedDayName
            }

            return daysOfWeek
        }
}

fun YearMonth.getDayOfMonthStartingFromSunday(): List<LocalDate> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val firstMondayOfMonth = firstDayOfMonth.with(DayOfWeek.SUNDAY)
    val firstDayOfNextMonth = firstDayOfMonth.plusMonths(1)

    return generateSequence(firstMondayOfMonth) { it.plusDays(1) }
        .takeWhile { it.isBefore(firstDayOfNextMonth) }
        .toList()
}

fun YearMonth.getDisplayName(): String {
    return "${month.getDisplayName(TextStyle.FULL, Locale.getDefault())} $year"
}