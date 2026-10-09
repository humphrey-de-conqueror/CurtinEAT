package com.example.curtineat.data.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "orders",
	indices = [
		Index(value = ["customerId"]),
		Index(value = ["vendorId"]),
		Index(value = ["timestamp"])
	]
)
data class OrderEntity(
	@PrimaryKey
	val orderId: String,

	val vendorId: String,
	val customerId: String,
	val totalPrice: Double,
	val status: String,

	// Unix timestamp in milliseconds.
	val timestamp: Long
)