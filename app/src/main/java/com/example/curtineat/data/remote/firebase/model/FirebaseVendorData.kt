package com.example.curtineat.data.remote.firebase.model

data class FirebaseVendorData(
	val vendorId: String            = "",
	val vendorName: String          = "",
	val vendorEmail: String         = "",
	val rating: Double              = 0.0,
	val category: String            = "",
	val distance: Double            = 0.0,
	val moneyBalance: Double        = 0.0
)