package com.guayaba.shotokankata.data

import android.content.Context

class KataStorageImpl(val context: Context) : KataStorage {
    override fun addOneSession(kataId: Int) {
        val count = getSessionCount(kataId)
        saveSessionCount(kataId, count + 1)
    }

    override fun decreaseOneSession(kataId: Int) {
        val count = getSessionCount(kataId)
        if(count == 0) return
        saveSessionCount(kataId, count - 1)
    }

    override fun getAllSessionsForKata(kataId: Int): KataRecord =
        KataRecord(kataId, getSessionCount(kataId))

    override fun getAllSessions(): List<KataRecord> {
        val list = mutableListOf<KataRecord>()
        for (i in 1..26) {
            list.add(KataRecord(i, getSessionCount(i)))
        }
        return list
    }

    private fun getSessionCount(kataId: Int): Int {
        val sharedPref = context.getSharedPreferences("sessions", Context.MODE_PRIVATE) ?: return 0
        return sharedPref.getInt(kataId.toString(), 0)
    }

    private fun saveSessionCount(kataId: Int, count: Int) {
        val sharedPref = context.getSharedPreferences("sessions", Context.MODE_PRIVATE) ?: return
        val editor = sharedPref.edit()
        editor.putInt(kataId.toString(), count)
        editor.apply()
    }

}