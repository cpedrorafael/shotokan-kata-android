package com.guayaba.shotokankata.data.kata_records

import java.time.YearMonth

interface KataStorage {
    suspend fun addOneSession(kataId: Int): Boolean
    fun decreaseOneSession(kataId: Int)
    suspend fun getAllSessionsForKata(kataId: Int): KataCount
    suspend fun getAllSessions(): List<KataRecord>
    suspend fun getSessionsInMonth(yearMonth: YearMonth): List<KataRecord>
}