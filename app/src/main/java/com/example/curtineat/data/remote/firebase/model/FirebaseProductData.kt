package com.example.curtineat.data.remote.firebase.model


data class FirebaseProductData(
	val productId: String           = "",
	val vendorId: String            = "",
	val productName: String         = "",
	val productPrice: Double        = 0.0,
	val productImage: String        = "",
//	val isAvailable: Boolean = true
)