package com.guayaba.shotokankata.data

interface KataStorage {
    fun addOneSession(kataId: Int)
    fun decreaseOneSession(kataId: Int)
    fun getAllSessionsForKata(kataId: Int): KataRecord
    fun getAllSessions(): List<KataRecord>
}