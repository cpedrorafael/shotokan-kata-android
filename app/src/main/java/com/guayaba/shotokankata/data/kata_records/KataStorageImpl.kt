package com.guayaba.shotokankata.data.kata_records

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.guayaba.shotokankata.ui.KataApplication.Companion.database
import com.guayaba.shotokankata.utils.AsyncUtil.Companion.ioScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId

class KataStorageImpl : KataStorage {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addOneSession(kataId: Int): Boolean {
        val now = LocalDateTime.now()
        val record = KataRecord(kataId = kataId, dateTime = now)

        // Using a try-catch block to handle potential exceptions during the database operations
        return try {
            // Ensure all database operations are performed on a proper scope
            withContext(ioScope.coroutineContext) {
                val latest = database.kataRecordDAO().getLatestRecordForKata(kataId)
                if (latest != null && latest.dateTime.toLocalDate() == now.toLocalDate()) {
                    throw Exception("Practiced today already")
                }
                database.kataRecordDAO().insert(record)
                true // Return true if the operation is successful
            }
        } catch (e: Exception) {
            false // Return false if any exception is thrown
        }
    }


    override fun decreaseOneSession(kataId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSessionsForKata(kataId: Int): KataCount {
        val deferred = ioScope.async {
            return@async database.kataRecordDAO().getCountByKataId(kataId) ?: KataCount(kataId, 0)
        }
        return deferred.await()
    }

    override suspend fun getAllSessions(): List<KataRecord> {
        val deferred = ioScope.async {
            database.kataRecordDAO().getAllSortedByDateDesc()
        }
        return deferred.await()
    }

    override suspend fun getSessionsInMonth(yearMonth: YearMonth): List<KataRecord> {
        // Start of the month
        val startDateTime = yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault())
        val startDate = startDateTime.toInstant().toEpochMilli()

        // End of the month
        val endDateTime =
            yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault())
        val endDate = endDateTime.toInstant().toEpochMilli()

        val deferred = ioScope.async {
            return@async database.kataRecordDAO().getRecordsInYearMonth(startDate, endDate)
        }

        return deferred.await()
    }

}