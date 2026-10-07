package com.example.curtineat.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "vendors"
)
data class VendorEntity(
	@PrimaryKey
	val vendorId: String = "",

	val vendorName: String = "",
	val vendorEmail: String = "",
	val category: String = "",
	val rating: Double = 0.0,
	val distance: Double = 0.0,
	val moneyBalance: Double = 0.0,
	val vendorImage: String = ""
)