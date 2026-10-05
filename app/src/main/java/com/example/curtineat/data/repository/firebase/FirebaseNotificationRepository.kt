package com.example.curtineat.data.repository.firebase

import com.example.curtineat.data.remote.firebase.model.FirebaseNotificationData
import com.example.curtineat.data.remote.firebase.source.FirebaseNotificationSource
import com.google.firebase.firestore.ListenerRegistration

class FirebaseNotificationRepository(
	private val source: FirebaseNotificationSource
) {

	suspend fun getAllNotifications(): List<FirebaseNotificationData> {

		return source.getAllNotifications()
	}

	suspend fun getNotificationById(
		notificationId: String
	): FirebaseNotificationData? {

		return source.getNotificationById(notificationId)
	}

	suspend fun getNotificationsByRecipientId(
		recipientId: String
	): List<FirebaseNotificationData> {

		return source.getNotificationsByRecipientId(recipientId)
	}

	suspend fun addNotification(
		notification: FirebaseNotificationData
	) {

		source.addNotification(notification)
	}

	suspend fun updateNotification(
		notification: FirebaseNotificationData
	) {

		source.updateNotification(notification)
	}

	fun listenToNotificationsByRecipientId(
		recipientId: String,
		onUpdate: (List<FirebaseNotificationData>) -> Unit
	): ListenerRegistration {

		return source.listenToNotificationsByRecipientId(
			recipientId = recipientId,
			onUpdate = onUpdate
		)
	}

	suspend fun deleteNotification(
		notificationId: String
	) {

		source.deleteNotification(notificationId)
	}
}