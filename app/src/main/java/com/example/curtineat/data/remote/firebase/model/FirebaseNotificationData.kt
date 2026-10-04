package com.example.curtineat.data.remote.firebase.model

data class FirebaseNotificationData(
	val notificationId: String = "",
	val recipientId: String = "", //customerid or vendor id
	val recipientType: String = "", // vendor or customer , maybe can use enum
	val message: String = "",
	val orderId: String = "",
	val timestamp: Long = 0L,
	val isRead: Boolean = false
)