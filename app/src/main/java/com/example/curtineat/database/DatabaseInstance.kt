package com.example.curtineat.database

import android.content.Context
import androidx.room.Room

object DatabaseInstance {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "curtin_eat_database"
            ).build().also {
                INSTANCE = it
            }
        }
    }
}