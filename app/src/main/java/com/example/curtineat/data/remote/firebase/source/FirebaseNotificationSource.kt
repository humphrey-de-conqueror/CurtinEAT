package com.example.curtineat.data.remote.firebase.source

import com.example.curtineat.data.remote.firebase.FirebaseProvider
import com.example.curtineat.data.remote.firebase.model.FirebaseNotificationData
import kotlinx.coroutines.tasks.await

class FirebaseNotificationSource {

	private val firestore = FirebaseProvider.firestore

	private val notificationCollection =
		firestore.collection("notifications")

	suspend fun getAllNotifications(): List<FirebaseNotificationData> {

		val snapshot = notificationCollection
			.get()
			.await()

		return snapshot.documents.map { document ->
			document.toFirebaseNotification()
		}
	}

	suspend fun getNotificationById(
		notificationId: String
	): FirebaseNotificationData? {

		val document = notificationCollection
			.document(notificationId)
			.get()
			.await()

		if (!document.exists()) {
			return null
		}

		return document.toFirebaseNotification()
	}

	suspend fun getNotificationsByRecipientId(
		recipientId: String
	): List<FirebaseNotificationData> {

		val snapshot = notificationCollection
			.whereEqualTo("recipientId", recipientId)
			.get()
			.await()

		return snapshot.documents.map { document ->
			document.toFirebaseNotification()
		}
	}

	suspend fun addNotification(
		notification: FirebaseNotificationData
	) {

		val document = notificationCollection.document()

		document
			.set(
				mapOf(
					"recipientId" to notification.recipientId,
					"recipientType" to notification.recipientType,
					"message" to notification.message,
					"orderId" to notification.orderId,
					"timestamp" to notification.timestamp,
					"isRead" to notification.isRead
				)
			)
			.await()
	}

	suspend fun updateNotification(
		notification: FirebaseNotificationData
	) {

		notificationCollection
			.document(notification.notificationId)
			.set(
				mapOf(
					"recipientId" to notification.recipientId,
					"recipientType" to notification.recipientType,
					"message" to notification.message,
					"orderId" to notification.orderId,
					"timestamp" to notification.timestamp,
					"isRead" to notification.isRead
				)
			)
			.await()
	}

	suspend fun deleteNotification(
		notificationId: String
	) {

		notificationCollection
			.document(notificationId)
			.delete()
			.await()
	}

	private fun com.google.firebase.firestore.DocumentSnapshot
		.toFirebaseNotification(): FirebaseNotificationData {

		return FirebaseNotificationData(
			notificationId = id,
			recipientId = getString("recipientId") ?: "",
			recipientType = getString("recipientType") ?: "",
			message = getString("message") ?: "",
			orderId = getString("orderId") ?: "",
			timestamp = getLong("timestamp") ?: 0L,
			isRead = getBoolean("isRead") ?: false
		)
	}
}