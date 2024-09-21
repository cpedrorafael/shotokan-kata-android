package com.guayaba.shotokankata.data.kata_records

import java.time.LocalDate
import java.time.YearMonth

interface KataStorage {
    suspend fun addOneSession(kataId: Int): Boolean
    fun decreaseOneSession(kataId: Int)
    suspend fun getAllSessionsForKata(kataId: Int): KataCount
    suspend fun getAllSessionsInDate(date: LocalDate): List<KataRecord>
    suspend fun getAllSessions(): List<KataRecord>
    suspend fun getSessionsInMonth(yearMonth: YearMonth): List<KataRecord>
}