package com.guayaba.shotokankata.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guayaba.shotokankata.data.kata_records.KataRecord
import com.guayaba.shotokankata.data.kata_records.KataRecordDAO

@Database(entities = [KataRecord::class], version = 1)
@TypeConverters(Converters::class)
abstract class Database: RoomDatabase() {
    abstract fun dao(): KataRecordDAO
}