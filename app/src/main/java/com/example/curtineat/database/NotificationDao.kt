package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotificationDao {
    @Query("SELECT * FROM Notification")
    suspend fun getAllNotification(): List<Notification>

    @Query("DELETE FROM Notification")
    suspend fun deleteAllNotification(): Unit

    @Insert
    suspend fun insertNotification(notification: Notification): Unit

    @Update
    suspend fun updateNotification(notification: Notification): Unit

    @Delete
    suspend fun deleteNotification(notification: Notification): Unit
}