
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
		recipientType: String
	): Flow<List<NotificationEntity>> =
		local.observeByRecipient(recipientId, recipientType)

	suspend fun refreshAll() {
		val notifications = remote.getAllNotifications()
			.map { it.toEntity() }

		local.replaceAll(notifications)
	}

	suspend fun refreshByRecipient(
		recipientId: String,
		recipientType: RecipientType
	) {
		val remoteNotifications =
			remote.getNotificationsByRecipientId(recipientId)
				.filter { it.recipientType == recipientType }

		val notifications =
			remoteNotifications.map { it.toEntity() }

		local.replaceByRecipient(
			recipientId = recipientId,
			recipientType = recipientType.name,
			notifications = notifications
		)
	}
}