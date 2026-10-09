package com.example.curtineat.data.repository.sync

import com.example.curtineat.data.local.room.entity.NotificationEntity
import com.example.curtineat.data.local.room.mapper.toEntity
import com.example.curtineat.data.remote.firebase.model.RecipientType
import com.example.curtineat.data.repository.firebase.FirebaseNotificationRepository
import com.example.curtineat.data.repository.local.NotificationLocalRepository
import kotlinx.coroutines.flow.Flow

class NotificationSyncRepository(
	private val remote: FirebaseNotificationRepository,
	private val local: NotificationLocalRepository
) {
	fun observeNotifications(
		recipientId: String,
		recipientType: RecipientType
	): Flow<List<NotificationEntity>> =
		local.observeByRecipient(
			recipientId = recipientId,
			recipientType = recipientType.name
		)

	suspend fun refreshAll() {
		val notifications = remote.getAllNotifications()

		local.deleteAll()
		local.upsertAll(notifications.map { it.toEntity() })
	}
}