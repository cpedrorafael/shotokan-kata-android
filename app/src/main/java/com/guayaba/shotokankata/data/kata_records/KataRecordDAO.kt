package com.guayaba.shotokankata.data.kata_records

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KataRecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: KataRecord)

    @Query("select * from kata_records")
    suspend fun getAll(): List<KataRecord>

    @Query("SELECT kataId, COUNT(kataId) as count FROM kata_records WHERE kataId = :kataId GROUP BY kataId")
    suspend fun getCountByKataId(kataId: Int): KataCount


    @Query("SELECT * FROM kata_records WHERE kataId = :kataId ORDER BY dateTime DESC LIMIT 1")
    suspend fun getLatestRecordForKata(kataId: Int): KataRecord?

    @Query("SELECT * FROM kata_records ORDER BY dateTime DESC")
    suspend fun getAllSortedByDateDesc(): List<KataRecord>

    @Query("SELECT * FROM kata_records WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<KataRecord>

    @Delete
    fun delete(kataRecord: KataRecord)
}