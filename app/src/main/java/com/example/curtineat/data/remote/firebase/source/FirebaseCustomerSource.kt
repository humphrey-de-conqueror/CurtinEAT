package com.example.curtineat.data.remote.firebase.source

import com.example.curtineat.data.remote.firebase.FirebaseProvider
import com.example.curtineat.data.remote.firebase.model.FirebaseCustomerData
import kotlinx.coroutines.tasks.await

class FirebaseCustomerSource {

	private val firestore = FirebaseProvider.firestore

	private val customerCollection =
		firestore.collection("customers")

	suspend fun getAllCustomers(): List<FirebaseCustomerData> {

		val snapshot = customerCollection
			.get()
			.await()

		return snapshot.documents.map { document ->
			FirebaseCustomerData(
				customerId = document.id,
				customerName =
					document.getString("customerName") ?: "",
				customerEmail =
					document.getString("customerEmail") ?: "",
				moneyBalance =
					document.getDouble("moneyBalance") ?: 0.0,
				customerImage =
					document.getString("customerImage") ?: ""
			)
		}
	}

	suspend fun getCustomerById(
		customerId: String
	): FirebaseCustomerData? {

		val document = customerCollection
			.document(customerId)
			.get()
			.await()

		if (!document.exists()) {
			return null
		}

		return FirebaseCustomerData(
			customerId = document.id,
			customerName =
				document.getString("customerName") ?: "",
			customerEmail =
				document.getString("customerEmail") ?: "",
			moneyBalance =
				document.getDouble("moneyBalance") ?: 0.0,
			customerImage =
				document.getString("customerImage") ?: ""
		)
	}

	suspend fun addCustomer(
		customer: FirebaseCustomerData
	) {

		customerCollection
			.document(customer.customerId)
			.set(
				mapOf(
					"customerName" to customer.customerName,
					"customerEmail" to customer.customerEmail,
					"moneyBalance" to customer.moneyBalance,
					"customerImage" to customer.customerImage
				)
			)
			.await()
	}

	suspend fun updateCustomer(
		customer: FirebaseCustomerData
	) {

		customerCollection
			.document(customer.customerId)
			.set(
				mapOf(
					"customerName" to customer.customerName,
					"customerEmail" to customer.customerEmail,
					"moneyBalance" to customer.moneyBalance,
					"customerImage" to customer.customerImage
				)
			)
			.await()
	}

	suspend fun updateCustomerProfile(
		customerId: String,
		customerName: String,
		customerImage: String
	) {

		customerCollection
			.document(customerId)
			.update(
				mapOf(
					"customerName" to customerName,
					"customerImage" to customerImage
				)
			)
			.await()
	}

	suspend fun deleteCustomer(
		customerId: String
	) {

		customerCollection
			.document(customerId)
			.delete()
			.await()
	}
}