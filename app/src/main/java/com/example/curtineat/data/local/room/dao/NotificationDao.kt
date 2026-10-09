package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

	@Query("""
        SELECT * FROM notifications
        WHERE recipientId = :recipientId
          AND recipientType = :recipientType
        ORDER BY timestamp DESC
    """)
	fun observeByRecipient(
		recipientId: String,
		recipientType: String
	): Flow<List<NotificationEntity>>

	@Query("""
        SELECT * FROM notifications
        WHERE recipientId = :recipientId
          AND recipientType = :recipientType
        ORDER BY timestamp DESC
    """)
	suspend fun getByRecipient(
		recipientId: String,
		recipientType: String
	): List<NotificationEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(notification: NotificationEntity)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsertAll(notifications: List<NotificationEntity>)

	@Query("UPDATE notifications SET isRead = 1 WHERE notificationId = :notificationId")
	suspend fun markAsRead(notificationId: String)

	@Query("""
        UPDATE notifications
        SET isRead = 1
        WHERE recipientId = :recipientId
          AND recipientType = :recipientType
    """)
	suspend fun markAllAsRead(
		recipientId: String,
		recipientType: String
	)

	@Query("DELETE FROM notifications WHERE notificationId = :notificationId")
	suspend fun deleteById(notificationId: String)

	@Query("DELETE FROM notifications")
	suspend fun deleteAll()
}