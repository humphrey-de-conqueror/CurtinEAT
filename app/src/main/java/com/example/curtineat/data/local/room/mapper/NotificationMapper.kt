package com.example.curtineat.data.local.room.mapper

import com.example.curtineat.data.local.room.entity.NotificationEntity
import com.example.curtineat.data.remote.firebase.model.FirebaseNotificationData
import com.example.curtineat.data.remote.firebase.model.RecipientType
import com.google.firebase.Timestamp
import java.util.Date

fun FirebaseNotificationData.toEntity(): NotificationEntity {
	return NotificationEntity(
		notificationId = notificationId,
		recipientId = recipientId,
		recipientType = recipientType.name,
		message = message,
		orderId = orderId,
		timestamp = timestamp.toDate().time,
		isRead = isRead
	)
}

fun NotificationEntity.toFirebaseModel(): FirebaseNotificationData {
	val parsedRecipientType = runCatching {
		RecipientType.valueOf(recipientType)
	}.getOrDefault(RecipientType.UNKNOWN)

	return FirebaseNotificationData(
		notificationId = notificationId,
		recipientId = recipientId,
		recipientType = parsedRecipientType,
		message = message,
		orderId = orderId,
		timestamp = Timestamp(Date(timestamp)),
		isRead = isRead
	)
}