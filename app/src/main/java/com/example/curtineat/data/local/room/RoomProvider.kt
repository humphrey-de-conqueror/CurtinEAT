package com.example.curtineat.data.local.room

import android.content.Context
import androidx.room.Room

object RoomProvider {

	@Volatile
	private var instance: AppDatabase? = null

	fun getDatabase(context: Context): AppDatabase {
		return instance ?: synchronized(this) {
			instance ?: Room.databaseBuilder(
				context.applicationContext,
				AppDatabase::class.java,
				"curtineat_database"
			)
				.build()
				.also { database ->
					instance = database
				}
		}
	}
}