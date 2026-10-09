package com.example.curtineat.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vendors")
data class VendorEntity(
	@PrimaryKey
	val vendorId: String,

	val vendorName: String,
	val vendorEmail: String,
	val rating: Double,
	val category: String,
	val distance: Double,
	val moneyBalance: Double,

	// Image API identifier, not the image binary.
	val vendorImage: String
)