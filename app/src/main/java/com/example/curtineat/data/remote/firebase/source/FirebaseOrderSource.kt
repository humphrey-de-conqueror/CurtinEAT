package com.example.curtineat.data.remote.firebase.source

import com.example.curtineat.data.remote.firebase.FirebaseProvider
import com.example.curtineat.data.remote.firebase.model.FirebaseOrderData
import com.example.curtineat.data.remote.firebase.model.FirebaseOrderProductData
import kotlinx.coroutines.tasks.await

class FirebaseOrderSource {

	private val firestore = FirebaseProvider.firestore

	private val orderCollection =
		firestore.collection("orders")

	suspend fun getAllOrders(): List<FirebaseOrderData> {

		val snapshot = orderCollection
			.get()
			.await()

		return snapshot.documents.map { document ->
			document.toFirebaseOrder()
		}
	}

	suspend fun getOrderById(
		orderId: String
	): FirebaseOrderData? {

		val document = orderCollection
			.document(orderId)
			.get()
			.await()

		if (!document.exists()) {
			return null
		}

		return document.toFirebaseOrder()
	}

	suspend fun getOrdersByCustomerId(
		customerId: String
	): List<FirebaseOrderData> {

		val snapshot = orderCollection
			.whereEqualTo("customerId", customerId)
			.get()
			.await()

		return snapshot.documents.map { document ->
			document.toFirebaseOrder()
		}
	}

	suspend fun getOrdersByVendorId(
		vendorId: String
	): List<FirebaseOrderData> {

		val snapshot = orderCollection
			.whereEqualTo("vendorId", vendorId)
			.get()
			.await()

		return snapshot.documents.map { document ->
			document.toFirebaseOrder()
		}
	}

	suspend fun addOrder(
		order: FirebaseOrderData
	) {
		val document = orderCollection.document()

		document
			.set(
				mapOf(
					"customerId" to order.customerId,
					"vendorId" to order.vendorId,
					"totalPrice" to order.totalPrice,
					"status" to order.status,
					"products" to order.products.map { product ->
						mapOf(
							"productId" to product.productId,
							"productName" to product.productName,
							"productPrice" to product.productPrice,
							"quantity" to product.quantity
						)
					},
					"timestamp" to order.timestamp
				)
			)
			.await()
	}

	suspend fun updateOrder(
		order: FirebaseOrderData
	) {

		orderCollection
			.document(order.orderId)
			.set(
				mapOf(
					"customerId" to order.customerId,
					"vendorId" to order.vendorId,
					"totalPrice" to order.totalPrice,
					"status" to order.status,
					"products" to order.products.map { product ->
						mapOf(
							"productId" to product.productId,
							"productName" to product.productName,
							"productPrice" to product.productPrice,
							"quantity" to product.quantity
						)
					},
					"timestamp" to order.timestamp
				)
			)
			.await()
	}

	suspend fun deleteOrder(
		orderId: String
	) {

		orderCollection
			.document(orderId)
			.delete()
			.await()
	}

	private fun com.google.firebase.firestore.DocumentSnapshot
		.toFirebaseOrder(): FirebaseOrderData {

		val products = get("products")
			?.let { value ->

				@Suppress("UNCHECKED_CAST")
				(value as? List<Map<String, Any?>>)
					?.map { product ->

						FirebaseOrderProductData(
							productId =
								product["productId"] as? String ?: "",

							productName =
								product["productName"] as? String ?: "",

							productPrice =
								(product["productPrice"] as? Number)
									?.toDouble() ?: 0.0,

							quantity =
								(product["quantity"] as? Number)
									?.toInt() ?: 0
						)
					}
			}
			?: emptyList()

		return FirebaseOrderData(
			orderId = id,
			customerId = getString("customerId") ?: "",
			vendorId = getString("vendorId") ?: "",
			totalPrice = getDouble("totalPrice") ?: 0.0,
			status = getString("status") ?: "",
			products = products,
			timestamp = getTimestamp("timestamp")
				?: com.google.firebase.Timestamp.now()
		)
	}
}