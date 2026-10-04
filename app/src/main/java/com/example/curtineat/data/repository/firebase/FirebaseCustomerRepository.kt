package com.example.curtineat.data.repository.firebase

import com.example.curtineat.data.remote.firebase.model.FirebaseCustomerData
import com.example.curtineat.data.remote.firebase.source.FirebaseCustomerSource

class FirebaseCustomerRepository(
	private val source: FirebaseCustomerSource
) {

	suspend fun getAllCustomers(): List<FirebaseCustomerData> {

		return source.getAllCustomers()
	}

	suspend fun getCustomerById(
		customerId: String
	): FirebaseCustomerData? {

		return source.getCustomerById(customerId)
	}

	suspend fun addCustomer(
		customer: FirebaseCustomerData
	) {

		source.addCustomer(customer)
	}

	suspend fun updateCustomer(
		customer: FirebaseCustomerData
	) {

		source.updateCustomer(customer)
	}

	suspend fun deleteCustomer(
		customerId: String
	) {

		source.deleteCustomer(customerId)
	}
}