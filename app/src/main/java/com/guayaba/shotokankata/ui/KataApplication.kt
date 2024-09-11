package com.guayaba.shotokankata.ui

import android.app.Application
import androidx.room.Room
import com.guayaba.shotokankata.data.Database

class KataApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            "app_database"
            ).build()
    }

    companion object {
        lateinit var database: Database
    }
}
