
package com.example.curtineat.data.repository.local

import androidx.room.withTransaction
import com.example.curtineat.data.local.room.AppDatabase
import com.example.curtineat.data.local.room.dao.NotificationDao
import com.example.curtineat.data.local.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationLocalRepository(
	private val database: AppDatabase,
	private val dao: NotificationDao
) {
	fun observeByRecipient(
		recipientId: String,
		recipientType: String
	): Flow<List<NotificationEntity>> =
		dao.observeByRecipient(recipientId, recipientType)

	suspend fun getByRecipient(
		recipientId: String,
		recipientType: String
	): List<NotificationEntity> =
		dao.getByRecipient(recipientId, recipientType)

	suspend fun upsert(notification: NotificationEntity) =
		dao.upsert(notification)

	suspend fun upsertAll(notifications: List<NotificationEntity>) =
		dao.upsertAll(notifications)

	suspend fun replaceAll(notifications: List<NotificationEntity>) {
		database.withTransaction {
			dao.deleteAll()
			dao.upsertAll(notifications)
		}
	}

	suspend fun replaceByRecipient(
		recipientId: String,
		recipientType: String,
		notifications: List<NotificationEntity>
	) {
		database.withTransaction {
			val previousNotifications =
				dao.getByRecipient(recipientId, recipientType)

			previousNotifications.forEach { notification ->
				dao.deleteById(notification.notificationId)
			}

			dao.upsertAll(notifications)
		}
	}

	suspend fun markAsRead(notificationId: String) =
		dao.markAsRead(notificationId)

	suspend fun markAllAsRead(
		recipientId: String,
		recipientType: String
	) = dao.markAllAsRead(recipientId, recipientType)

	suspend fun deleteById(notificationId: String) =
		dao.deleteById(notificationId)

	suspend fun deleteAll() = dao.deleteAll()
}