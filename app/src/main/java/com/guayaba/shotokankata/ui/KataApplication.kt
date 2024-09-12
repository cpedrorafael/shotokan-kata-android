package com.guayaba.shotokankata.ui

import android.app.Application
import androidx.room.Room
import com.guayaba.shotokankata.data.Database
import com.guayaba.shotokankata.data.kata_records.KataStorage
import com.guayaba.shotokankata.data.kata_records.KataStorageImpl
import com.guayaba.shotokankata.data.quizzes.QuizStorage
import com.guayaba.shotokankata.data.quizzes.QuizStorageImpl

class KataApplication: Application() {
    override fun onCreate() {
        super.onCreate()
       initDependencies()
    }

    private fun initDependencies(){
        database = Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            "app_database"
        ).build()

        kataStorage = KataStorageImpl()
        quizStorage = QuizStorageImpl()
    }

    companion object {
        lateinit var database: Database
        lateinit var kataStorage: KataStorage
        lateinit var quizStorage: QuizStorage
    }
}
