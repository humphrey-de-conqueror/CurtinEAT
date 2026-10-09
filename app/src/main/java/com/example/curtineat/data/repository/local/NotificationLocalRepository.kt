package com.example.curtineat.data.repository.local

import com.example.curtineat.data.local.room.dao.NotificationDao
import com.example.curtineat.data.local.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationLocalRepository(
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

	suspend fun markAsRead(notificationId: String) =
		dao.markAsRead(notificationId)

	suspend fun markAllAsRead(
		recipientId: String,
		recipientType: String
	) = dao.markAllAsRead(recipientId, recipientType)

	suspend fun deleteById(notificationId: String) =
		dao.deleteById(notificationId)

	suspend fun deleteAll() =
		dao.deleteAll()
}