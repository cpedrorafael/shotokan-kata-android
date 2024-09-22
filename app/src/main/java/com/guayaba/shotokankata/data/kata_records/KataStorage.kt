package com.guayaba.shotokankata.data.kata_records

import java.time.LocalDate
import java.time.YearMonth

interface KataStorage {
    suspend fun addOneSession(kataId: Int): Boolean
    suspend fun saveRecord(record: KataRecord)
    fun deleteRecord(record: KataRecord)
    suspend fun getAllSessionsForKata(kataId: Int): KataCount
    suspend fun getAllSessionsInDate(date: LocalDate): List<KataRecord>
    suspend fun getAllSessions(): List<KataRecord>
    suspend fun getSessionsInMonth(yearMonth: YearMonth): List<KataRecord>
}