package com.guayaba.shotokankata.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guayaba.shotokankata.data.kata_records.KataRecord
import com.guayaba.shotokankata.data.kata_records.KataRecordDAO
import com.guayaba.shotokankata.data.quizzes.QuizDAO
import com.guayaba.shotokankata.data.quizzes.QuizResult

@Database(
    entities = [KataRecord::class, QuizResult::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun kataRecordDAO(): KataRecordDAO
    abstract fun quizDAO(): QuizDAO
}