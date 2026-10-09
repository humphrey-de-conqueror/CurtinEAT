package com.example.curtineat.data.local.room.mapper

import com.example.curtineat.data.local.room.entity.VendorEntity
import com.example.curtineat.data.remote.firebase.model.FirebaseVendorData

fun FirebaseVendorData.toEntity(): VendorEntity {
	return VendorEntity(
		vendorId = vendorId,
		vendorName = vendorName,
		vendorEmail = vendorEmail,
		rating = rating,
		category = category,
		distance = distance,
		moneyBalance = moneyBalance,
		vendorImage = vendorImage
	)
}

fun VendorEntity.toFirebaseModel(): FirebaseVendorData {
	return FirebaseVendorData(
		vendorId = vendorId,
		vendorName = vendorName,
		vendorEmail = vendorEmail,
		rating = rating,
		category = category,
		distance = distance,
		moneyBalance = moneyBalance,
		vendorImage = vendorImage
	)
}