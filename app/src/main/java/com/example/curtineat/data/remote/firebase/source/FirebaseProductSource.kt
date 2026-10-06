package com.example.curtineat.data.remote.firebase.source

import com.example.curtineat.data.remote.firebase.FirebaseProvider
import com.example.curtineat.data.remote.firebase.model.FirebaseProductData
import kotlinx.coroutines.tasks.await

class FirebaseProductSource {

	private val firestore = FirebaseProvider.firestore

	private val productCollection =
		firestore.collection("products")

	suspend fun getAllProducts(): List<FirebaseProductData> {

		val snapshot = productCollection
			.get()
			.await()

		return snapshot.documents.map { document ->
			FirebaseProductData(
				productId = document.id,
				vendorId = document.getString("vendorId") ?: "",
				productName = document.getString("productName") ?: "",
				productPrice = document.getDouble("productPrice") ?: 0.0,
				productImage = document.getString("productImage") ?: "",
//				isAvailable = document.getBoolean("isAvailable") ?: true
			)
		}
	}

	suspend fun getProductById(
		productId: String
	): FirebaseProductData? {

		val document = productCollection
			.document(productId)
			.get()
			.await()

		if (!document.exists()) {
			return null
		}

		return FirebaseProductData(
			productId = document.id,
			vendorId = document.getString("vendorId") ?: "",
			productName = document.getString("productName") ?: "",
			productPrice = document.getDouble("productPrice") ?: 0.0,
			productImage = document.getString("productImage") ?: "",
//			isAvailable = document.getBoolean("isAvailable") ?: true
		)
	}

	suspend fun getProductsByVendorId(
		vendorId: String
	): List<FirebaseProductData> {

		val snapshot = productCollection
			.whereEqualTo("vendorId", vendorId)
			.get()
			.await()

		return snapshot.documents.map { document ->
			FirebaseProductData(
				productId = document.id,
				vendorId = document.getString("vendorId") ?: "",
				productName = document.getString("productName") ?: "",
				productPrice = document.getDouble("productPrice") ?: 0.0,
				productImage = document.getString("productImage") ?: "",
//				isAvailable = document.getBoolean("isAvailable") ?: true
			)
		}
	}

	suspend fun addProduct(
		product: FirebaseProductData
	): String {

		val document = productCollection.document()

		document
			.set(
				mapOf(
					"vendorId" to product.vendorId,
					"productName" to product.productName,
					"productPrice" to product.productPrice,
					"productImage" to product.productImage,
//					"isAvailable" to product.isAvailable
				)
			)
			.await()

		return document.id
	}

	suspend fun updateProduct(
		product: FirebaseProductData
	) {

		productCollection
			.document(product.productId)
			.set(
				mapOf(
					"vendorId" to product.vendorId,
					"productName" to product.productName,
					"productPrice" to product.productPrice,
					"productImage" to product.productImage,
//					"isAvailable" to product.isAvailable
				)
			)
			.await()
	}

	suspend fun deleteProduct(
		productId: String
	) {

		productCollection
			.document(productId)
			.delete()
			.await()
	}
}