package com.guayaba.shotokankata.ui.calendar

import java.time.YearMonth

data class CalendarUiState(
    val yearMonth: YearMonth,
    val dates: List<Date>
) {
    companion object {
        val init = CalendarUiState(
            yearMonth = YearMonth.now(),
            dates = emptyList()
        )
    }
    data class Date(
        val dayOfMonth: String = "",
        val isSelected: Boolean = false,
        val trained: Boolean = false
    ) {
        companion object {
            val Empty = Date()
        }
    }
}