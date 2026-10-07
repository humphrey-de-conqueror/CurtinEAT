package com.example.curtineat.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "products"
)
data class ProductEntity(
	@PrimaryKey
	val productId: String = "",

	val vendorId: String = "",
	val productName: String = "",
	val productPrice: Double = 0.0,
	val productImage: String = "",
	val isAvailable: Boolean = true
)