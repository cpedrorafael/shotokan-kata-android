package com.guayaba.shotokankata.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guayaba.shotokankata.data.kata_records.KataRecord
import com.guayaba.shotokankata.data.kata_records.KataStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth

class SessionViewModel(private val dataSource: KataStorage) : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState.init)
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    dates = getSessions(currentState.yearMonth)
                )
            }
        }
    }

    private suspend fun getSessions(yearMonth: YearMonth): List<CalendarUiState.Date>  {
        val sessions = dataSource.getAllSessions().filter {
            it.dateTime.year == yearMonth.year && it.dateTime.month == yearMonth.month
        }.map {
            CalendarUiState.Date(it.dateTime.toString(), false)
        }

        return sessions
    }

    fun toNextMonth(nextMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = nextMonth,
                    dates = getSessions(nextMonth)
                )
            }
        }
    }

    fun toPreviousMonth(prevMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = prevMonth,
                    dates = getSessions(prevMonth)
                )
            }
        }
    }

}