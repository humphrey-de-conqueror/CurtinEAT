package com.example.curtineat.data.remote.firebase.model

data class FirebaseOrderData(
	val orderId: String = "",
	val vendorId: String = "",
	val customerId: String = "",
	val totalPrice: Double = 0.0,
	val status: String = "",
	val products: List<FirebaseOrderProductData> = emptyList()
)
