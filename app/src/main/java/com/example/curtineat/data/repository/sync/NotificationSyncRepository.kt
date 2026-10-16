
package com.example.curtineat.data.repository.sync

import android.util.Log
import com.example.curtineat.data.local.room.entity.NotificationEntity
import com.example.curtineat.data.local.room.mapper.toEntity
import com.example.curtineat.data.remote.firebase.model.RecipientType
import com.example.curtineat.data.repository.firebase.FirebaseNotificationRepository
import com.example.curtineat.data.repository.local.NotificationLocalRepository
import kotlinx.coroutines.flow.Flow
import com.example.curtineat.data.remote.firebase.model.FirebaseNotificationData
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NotificationSyncRepository(
	private val remote: FirebaseNotificationRepository,
	private val local: NotificationLocalRepository
) {
	private val _newNotifications =
		MutableSharedFlow<List<NotificationEntity>>(extraBufferCapacity = 8)

	val newNotifications: SharedFlow<List<NotificationEntity>> =
		_newNotifications.asSharedFlow()
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

		val notifications = remoteNotifications.map { it.toEntity() }

		local.replaceByRecipient(
			recipientId = recipientId,
			recipientType = recipientType.name,
			notifications = notifications
		)
	}

	suspend fun markAsRead(notificationId: String) {
		// Update Room first so the UI can reflect the change locally.
		local.markAsRead(notificationId)

		// Synchronize the updated read status with Firebase.
		val notification = remote.getNotificationById(notificationId)

		if (notification != null && !notification.isRead) {
			remote.updateNotification(
				notification.copy(isRead = true)
			)
		}
	}

	suspend fun createNotification(
		notification: FirebaseNotificationData
	): String {
		val notificationId = remote.addNotification(notification)

		refreshByRecipient(
			recipientId = notification.recipientId,
			recipientType = notification.recipientType
		)

		return notificationId
	}

	fun startRealtimeNotifications(
		recipientId: String,
		recipientType: RecipientType,
		scope: CoroutineScope
	): ListenerRegistration {

		// null = first snapshot of this listener, which only sets the baseline
		var knownIds: Set<String>? = null

		return remote.listenToNotificationsByRecipientId(
			recipientId
		) { remoteNotifications ->

			Log.d(
				"NOTIFICATION_REALTIME",
				"$recipientType $recipientId received ${remoteNotifications.size} notifications"
			)

			val notifications = remoteNotifications
				.filter { it.recipientType == recipientType }
				.map { it.toEntity() }

			val previous = knownIds
			knownIds = notifications.map { it.notificationId }.toSet()

			val fresh = if (previous == null) {
				emptyList()
			} else {
				notifications.filter {
					!it.isRead && it.notificationId !in previous
				}
			}

			scope.launch {
				try {
					local.replaceByRecipient(
						recipientId = recipientId,
						recipientType = recipientType.name,
						notifications = notifications
					)

					if (fresh.isNotEmpty()) {
						_newNotifications.emit(fresh)
					}
				} catch (exception: Exception) {
					Log.e(
						"NOTIFICATION_REALTIME",
						"Failed to sync notifications into Room",
						exception
					)
				}
			}
		}
	}
}