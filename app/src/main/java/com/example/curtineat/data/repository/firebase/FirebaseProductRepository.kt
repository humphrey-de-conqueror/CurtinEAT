package com.example.curtineat.data.repository.firebase

import com.example.curtineat.data.remote.firebase.model.FirebaseProductData
import com.example.curtineat.data.remote.firebase.source.FirebaseProductSource

class FirebaseProductRepository(
	private val source: FirebaseProductSource
) {

	suspend fun getAllProducts(): List<FirebaseProductData> {

		return source.getAllProducts()
	}

	suspend fun getProductById(
		productId: String
	): FirebaseProductData? {

		return source.getProductById(productId)
	}

	suspend fun getProductsByVendorId(
		vendorId: String
	): List<FirebaseProductData> {

		return source.getProductsByVendorId(vendorId)
	}

	suspend fun addProduct(
		product: FirebaseProductData
	): String {

		return source.addProduct(product)
	}

	suspend fun updateProduct(
		product: FirebaseProductData
	) {

		source.updateProduct(product)
	}

	suspend fun deleteProduct(
		productId: String
	) {

		source.deleteProduct(productId)
	}
}