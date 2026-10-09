
package com.example.curtineat.data.repository.sync

import com.example.curtineat.data.local.room.entity.CustomerEntity
import com.example.curtineat.data.local.room.mapper.toEntity
import com.example.curtineat.data.repository.firebase.FirebaseCustomerRepository
import com.example.curtineat.data.repository.local.CustomerLocalRepository
import kotlinx.coroutines.flow.Flow

class CustomerSyncRepository(
	private val remote: FirebaseCustomerRepository,
	private val local: CustomerLocalRepository
) {
	fun observeCustomer(customerId: String): Flow<CustomerEntity?> =
		local.observeById(customerId)

	suspend fun refresh(customerId: String) {
		val customer = remote.getCustomerById(customerId) ?: return
		local.upsert(customer.toEntity())
	}

	suspend fun updateProfile(
		customerId: String,
		customerName: String,
		customerImage: String
	) {
		remote.updateCustomerProfile(
			customerId = customerId,
			customerName = customerName,
			customerImage = customerImage
		)

		refresh(customerId)
	}

	suspend fun removeCachedCustomer(customerId: String) {
		local.deleteById(customerId)
	}
}