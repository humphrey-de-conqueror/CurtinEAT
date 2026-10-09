package com.example.curtineat.data.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "order_products",
	indices = [
		Index(value = ["orderId"])
	]
)
data class OrderProductEntity(
	@PrimaryKey(autoGenerate = true)
	val localId: Long = 0,

	val orderId: String,
	val productId: String,
	val productName: String,
	val productPrice: Double,
	val quantity: Int
)