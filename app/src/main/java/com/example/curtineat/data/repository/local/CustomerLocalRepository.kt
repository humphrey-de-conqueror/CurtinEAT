package com.example.curtineat.data.repository.local

import androidx.room.withTransaction
import com.example.curtineat.data.local.room.AppDatabase
import com.example.curtineat.data.local.room.dao.CustomerDao
import com.example.curtineat.data.local.room.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

class CustomerLocalRepository(
	private val database: AppDatabase,
	private val dao: CustomerDao
) {
	fun observeById(customerId: String): Flow<CustomerEntity?> =
		dao.observeById(customerId)

	suspend fun getById(customerId: String): CustomerEntity? =
		dao.getById(customerId)

	suspend fun upsert(customer: CustomerEntity) = dao.upsert(customer)

	suspend fun upsertAll(customers: List<CustomerEntity>) =
		dao.upsertAll(customers)

	suspend fun replaceAll(customers: List<CustomerEntity>) {
		database.withTransaction {
			dao.deleteAll()
			dao.upsertAll(customers)
		}
	}

	suspend fun deleteById(customerId: String) = dao.deleteById(customerId)

	suspend fun deleteAll() = dao.deleteAll()
}