package com.guayaba.shotokankata.ui.kata

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.guayaba.shotokankata.data.kata_records.KataCount
import com.guayaba.shotokankata.data.KataInfo
import com.guayaba.shotokankata.ui.KataApplication.Companion.kataStorage

class KataViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateKata(info: KataInfo): Boolean {
        return kataStorage.addOneSession(info.id)
    }

    suspend fun getSessionsForKata(info: KataInfo): KataCount {
        return kataStorage.getAllSessionsForKata(info.id)
    }

    suspend fun getAllKataRecords() = kataStorage.getAllSessions()

    fun decreaseOneSession(kataInfo: KataInfo) =
        kataStorage.decreaseOneSession(kataInfo.id)

}