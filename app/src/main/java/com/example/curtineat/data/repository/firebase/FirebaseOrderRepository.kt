package com.example.curtineat.data.repository.firebase

import com.example.curtineat.data.remote.firebase.model.FirebaseOrderData
import com.example.curtineat.data.remote.firebase.source.FirebaseOrderSource

class FirebaseOrderRepository(
	private val source: FirebaseOrderSource
) {

	suspend fun getAllOrders(): List<FirebaseOrderData> {

		return source.getAllOrders()
	}

	suspend fun getOrderById(
		orderId: String
	): FirebaseOrderData? {

		return source.getOrderById(orderId)
	}

	suspend fun getOrdersByCustomerId(
		customerId: String
	): List<FirebaseOrderData> {

		return source.getOrdersByCustomerId(customerId)
	}

	suspend fun getOrdersByVendorId(
		vendorId: String
	): List<FirebaseOrderData> {

		return source.getOrdersByVendorId(vendorId)
	}

	suspend fun addOrder(
		order: FirebaseOrderData
	) {

		source.addOrder(order)
	}

	suspend fun updateOrder(
		order: FirebaseOrderData
	) {

		source.updateOrder(order)
	}

	suspend fun deleteOrder(
		orderId: String
	) {

		source.deleteOrder(orderId)
	}
}