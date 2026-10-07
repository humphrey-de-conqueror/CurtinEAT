package com.example.curtineat.data.repository.firebase

import com.example.curtineat.data.remote.firebase.model.FirebaseOrderData
import com.example.curtineat.data.remote.firebase.source.FirebaseOrderSource
import com.google.firebase.firestore.ListenerRegistration

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

	//get real time order notif from vendor
	fun listenToOrdersByVendorId(
		vendorId: String,
		onUpdate: (List<FirebaseOrderData>) -> Unit
	): ListenerRegistration {

		return source.listenToOrdersByVendorId(
			vendorId = vendorId,
			onUpdate = onUpdate
		)
	}


	//get real time order notif from customer
	fun listenToOrdersByCustomerId(
		customerId: String,
		onUpdate: (List<FirebaseOrderData>) -> Unit
	): ListenerRegistration {

		return source.listenToOrdersByCustomerId(
			customerId = customerId,
			onUpdate = onUpdate
		)
	}

	suspend fun addOrder(
		order: FirebaseOrderData
	): String {

		return source.addOrder(order)
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