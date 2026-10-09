package com.example.curtineat.data.repository.room

import com.example.curtineat.data.local.room.dao.CustomerDao
import com.example.curtineat.data.local.room.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

class CustomerLocalRepository(
	private val dao: CustomerDao
) {

	fun getAllCustomers(): Flow<List<CustomerEntity>> {
		return dao.getAllCustomers()
	}

	suspend fun getCustomerById(
		customerId: String
	): CustomerEntity? {
		return dao.getCustomerById(customerId)
	}

	suspend fun saveCustomers(
		customers: List<CustomerEntity>
	) {
		dao.insertCustomers(customers)
	}

	suspend fun saveCustomer(
		customer: CustomerEntity
	) {
		dao.insertCustomer(customer)
	}

	suspend fun clearCustomers() {
		dao.deleteAllCustomers()
	}
}