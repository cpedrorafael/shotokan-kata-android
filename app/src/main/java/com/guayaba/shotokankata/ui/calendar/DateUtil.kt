package com.guayaba.shotokankata.ui.calendar

import com.guayaba.shotokankata.utils.toEpochMilli
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import java.util.concurrent.TimeUnit

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

fun YearMonth.getDaysOfMonth(): List<LocalDate?> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val firstSundayOfMonth = firstDayOfMonth.with(DayOfWeek.SUNDAY)

    // Offset the list by the amount of days before the first sunday
    val distance = firstSundayOfMonth.toEpochMilli() - firstDayOfMonth.toEpochMilli()
    val offsetDays = 7 - TimeUnit.MILLISECONDS.toDays(distance)
    val firstDayOfNextMonth = firstDayOfMonth.plusMonths(1)

    val list = mutableListOf<LocalDate?>()
    repeat(offsetDays.toInt()) {
        list.add(null)
    }

    val monthDays = generateSequence(firstDayOfMonth) { it.plusDays(1) }
        .takeWhile { it.isBefore(firstDayOfNextMonth) }
        .toList()

    list.addAll(monthDays)

    return list
}

fun YearMonth.getDisplayName(): String {
    return "${month.getDisplayName(TextStyle.FULL, Locale.getDefault())} $year"
}