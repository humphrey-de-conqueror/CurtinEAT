package com.example.curtineat.data.remote.firebase.model
import com.google.firebase.Timestamp

enum class RecipientType {
	CUSTOMER,
	VENDOR,
	UNKNOWN
}
data class FirebaseNotificationData(
	val notificationId: String = "",
	val recipientId: String = "", //customerid or vendor id
	val recipientType: RecipientType = RecipientType.UNKNOWN,
	val message: String = "",
	val orderId: String = "",
	val timestamp: Timestamp = Timestamp.now(),
	val isRead: Boolean = false,
)