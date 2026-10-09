package com.example.curtineat.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
	@PrimaryKey
	val customerId: String,

	val customerName: String,
	val customerEmail: String,
	val moneyBalance: Double,

	// Image API identifier.
	val customerImage: String
)