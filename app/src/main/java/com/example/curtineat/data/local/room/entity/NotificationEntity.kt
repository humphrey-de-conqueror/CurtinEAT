package com.example.curtineat.data.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "notifications",
	indices = [
		Index(value = ["recipientId", "recipientType"]),
		Index(value = ["timestamp"])
	]
)
data class NotificationEntity(
	@PrimaryKey
	val notificationId: String,

	val recipientId: String,

	// Store RecipientType.name, e.g. "CUSTOMER" or "VENDOR".
	val recipientType: String,

	val message: String,
	val orderId: String,

	// Unix timestamp in milliseconds.
	val timestamp: Long,

	val isRead: Boolean
)