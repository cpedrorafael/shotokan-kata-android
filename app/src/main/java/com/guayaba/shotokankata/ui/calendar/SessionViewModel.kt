package com.guayaba.shotokankata.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guayaba.shotokankata.data.kata_records.KataRecord
import com.guayaba.shotokankata.ui.KataApplication.Companion.kataStorage
import com.guayaba.shotokankata.utils.AsyncUtil.Companion.backgroundScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

const val TAG = "SessionViewModel"
class SessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState.init)
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _sessionDay = MutableStateFlow(listOf<KataRecord>())
    val sessionDay: StateFlow<List<KataRecord>> = _sessionDay

    private var deletedRecord: KataRecord? = null

    init {
        update()
    }

    fun update() = viewModelScope.launch {
        _uiState.update { currentState ->
            currentState.copy(
                dates = getDates(currentState.yearMonth)
            )
        }
    }

    private fun getSessions(yearMonth: YearMonth) =
        viewModelScope.launch {
            kataStorage.getAllSessions().filter {
                it.dateTime.year == yearMonth.year && it.dateTime.month == yearMonth.month
            }.map {
                CalendarUiState.Date(it.dateTime.toString(), false)
            }
        }

    fun toNextMonth(nextMonth: YearMonth) {
        val now = LocalDateTime.now()
        if (nextMonth.year >= now.year && nextMonth.month > now.month) {
            return
        }
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = nextMonth,
                    dates = getDates(nextMonth)
                )
            }
        }
        getSessions(nextMonth)
    }

    fun toPreviousMonth(prevMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = prevMonth,
                    dates = getDates(prevMonth)
                )
            }
        }
    }

    private suspend fun getRecords(yearMonth: YearMonth): List<KataRecord> {
        val deferred = backgroundScope.async {
            kataStorage.getSessionsInMonth(yearMonth)
        }
        return deferred.await()
    }

    private suspend fun getDates(yearMonth: YearMonth): List<CalendarUiState.Date> {
        val records = getRecords(yearMonth)
        val datesFromRecords = records.map { it.dateTime.toLocalDate() }
        return yearMonth.getDayOfMonthStartingFromSunday()
            .map { date ->
                CalendarUiState.Date(
                    dayOfMonth = if (date.monthValue == yearMonth.monthValue) {
                        "${date.dayOfMonth}"
                    } else {
                        "" // Fill with empty string for days outside the current month
                    },
                    isSelected = date.isEqual(LocalDate.now()) && date.monthValue == yearMonth.monthValue,
                    localDate = date,
                    trained = date in datesFromRecords
                )
            }
    }

    fun deleteKataRecord(record: KataRecord) = viewModelScope.launch {
        deletedRecord = record
        kataStorage.deleteRecord(record)
        val newList = _sessionDay.value.filter { it != record }
        _sessionDay.emit(newList)
    }

    fun undoDeleteLastRecord()= viewModelScope.launch{
        deletedRecord?.let {
            kataStorage.saveRecord(it)
            val list = mutableListOf<KataRecord>()
            list.addAll(_sessionDay.value)
            list.add(it)
            _sessionDay.emit(list)
        }
    }

    fun getSessionsForDay(date: LocalDate) = viewModelScope.launch {
        val records = kataStorage.getAllSessionsInDate(date)
        _sessionDay.value = records.toMutableList()
    }

    fun updateKata(id: Int, date: LocalDate, selected: Boolean) {
        val job = viewModelScope.launch {
            if (selected) {
                kataStorage.addRecordByKataIdAtDate(id, date)
            } else {
                kataStorage.deleteRecordByKataIdAtDate(id, date)
            }
        }
        job.invokeOnCompletion {
            update()
        }
    }

}