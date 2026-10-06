package com.example.curtineat.data.remote.firebase.model
import com.google.firebase.Timestamp
data class FirebaseOrderData(
	val orderId: String = "",
	val vendorId: String = "",
	val customerId: String = "",
	val totalPrice: Double = 0.0,
	val status: String = "",
	val products: List<FirebaseOrderProductData> = emptyList(),
	val timestamp: Timestamp = Timestamp.now()
)