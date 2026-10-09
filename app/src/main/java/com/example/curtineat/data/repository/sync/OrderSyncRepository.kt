package com.example.curtineat.data.repository.sync

import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.mapper.toEntity
import com.example.curtineat.data.local.room.mapper.toProductEntities
import com.example.curtineat.data.repository.firebase.FirebaseOrderRepository
import com.example.curtineat.data.repository.local.OrderLocalRepository
import kotlinx.coroutines.flow.Flow

class OrderSyncRepository(
	private val remote: FirebaseOrderRepository,
	private val local: OrderLocalRepository
) {
	fun observeOrdersByCustomer(
		customerId: String
	): Flow<List<OrderEntity>> =
		local.observeByCustomerId(customerId)

	fun observeOrdersByVendor(
		vendorId: String
	): Flow<List<OrderEntity>> =
		local.observeByVendorId(vendorId)

	fun observeOrder(
		orderId: String
	): Flow<OrderEntity?> =
		local.observeById(orderId)

	suspend fun refreshAll() {
		val orders = remote.getAllOrders()

		val orderEntities = orders.map { it.toEntity() }
		val productEntities = orders.flatMap { it.toProductEntities() }

		// Replace the local snapshot only after the remote fetch succeeds.
		local.deleteAll()
		local.upsertOrders(orderEntities, productEntities)
	}
}