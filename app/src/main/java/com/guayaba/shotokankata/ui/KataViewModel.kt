package com.guayaba.shotokankata.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.guayaba.shotokankata.data.KataStorageImpl

class KataViewModel(val context: Context) : ViewModel() {
    private val storage = KataStorageImpl(context)

    fun updateKata(info: KataInfo) {
        storage.addOneSession(info.id)
    }

    fun getSessionsForKata(info: KataInfo): Int {
        return storage.getAllSessionsForKata(info.id).sessionCount
    }

    fun getAllKataRecords() = storage.getAllSessions().sortedByDescending { it.sessionCount }

    fun decreaseOneSession(kataInfo: KataInfo) =
        storage.decreaseOneSession(kataInfo.id)
}