package com.guayaba.shotokankata.ui

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.guayaba.shotokankata.data.kata_records.KataCount
import com.guayaba.shotokankata.data.kata_records.KataRecord
import com.guayaba.shotokankata.data.kata_records.KataStorageImpl
import kotlinx.coroutines.flow.MutableStateFlow

class KataViewModel : ViewModel() {
    private lateinit var storage: KataStorageImpl
    private val allSessionsFlow = MutableStateFlow<List<KataRecord>>(listOf())

    fun initStorage(context: Context) {
        storage = KataStorageImpl(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateKata(info: KataInfo): Boolean {
        return storage.addOneSession(info.id)
    }

    suspend fun getSessionsForKata(info: KataInfo): KataCount {
        return storage.getAllSessionsForKata(info.id)
    }

    //
    suspend fun getAllKataRecords() = storage.getAllSessions()

    fun decreaseOneSession(kataInfo: KataInfo) =
        storage.decreaseOneSession(kataInfo.id)

}