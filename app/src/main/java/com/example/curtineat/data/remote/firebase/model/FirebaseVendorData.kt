package com.example.curtineat.data.remote.firebase.model

data class FirebaseVendorData(
	val vendorId: String = "",
	val vendorName: String = "",
	val vendorEmail: String = "",
	val rating: Double = 0.0,
	val category: String = "",
	val location: String = "",
	val moneyBalance: Double = 0.0,
	val vendorImage: String = ""
	// val isOpen:Boolean = false
)