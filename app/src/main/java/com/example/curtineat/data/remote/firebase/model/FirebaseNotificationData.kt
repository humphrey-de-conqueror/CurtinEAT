package com.example.curtineat.data.remote.firebase.model

data class FirebaseNotificationData(
	val notificationId: String = "",
	val recipientId: String = "",
	val recipientType: String = "",
	val message: String = "",
	val orderId: String = "",
	val timestamp: Long = 0L,
	val isRead: Boolean = false
)