package com.example.curtineat.data.local.room

import android.content.Context
import androidx.room.Room

object RoomProvider {

	@Volatile
	private var database: AppDatabase? = null

	fun getDatabase(
		context: Context
	): AppDatabase {

		return database ?: synchronized(this) {

			database ?: Room.databaseBuilder(
				context.applicationContext,
				AppDatabase::class.java,
				"curtineat_database"
			)
				.build()
				.also {
					database = it
				}
		}
	}
}