package com.example.curtineat.data.repository.firebase

import com.example.curtineat.data.remote.firebase.model.FirebaseNotificationData
import com.example.curtineat.data.remote.firebase.source.FirebaseNotificationSource

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

	suspend fun deleteNotification(
		notificationId: String
	) {

		source.deleteNotification(notificationId)
	}
}