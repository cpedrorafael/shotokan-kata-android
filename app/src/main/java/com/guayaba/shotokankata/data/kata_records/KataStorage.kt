package com.guayaba.shotokankata.data.kata_records

interface KataStorage {
    suspend fun addOneSession(kataId: Int): Boolean
    fun decreaseOneSession(kataId: Int)
    suspend fun getAllSessionsForKata(kataId: Int): KataCount
    suspend fun getAllSessions(): List<KataRecord>
}