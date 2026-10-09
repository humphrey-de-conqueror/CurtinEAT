package com.example.curtineat.data.repository.sync

import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.OrderProductEntity
import com.example.curtineat.data.local.room.mapper.toEntity
import com.example.curtineat.data.local.room.mapper.toProductEntities
import com.example.curtineat.data.repository.firebase.FirebaseOrderRepository
import com.example.curtineat.data.repository.local.OrderLocalRepository
import kotlinx.coroutines.flow.Flow
import com.example.curtineat.data.remote.firebase.model.FirebaseOrderData

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

	fun observeOrder(orderId: String): Flow<OrderEntity?> =
		local.observeById(orderId)

	suspend fun refreshAll() {
		val remoteOrders = remote.getAllOrders()
		val orders: List<OrderEntity> =
			remoteOrders.map { it.toEntity() }
		val products: List<OrderProductEntity> =
			remoteOrders.flatMap { it.toProductEntities() }

		local.replaceAll(orders, products)
	}

	suspend fun refreshByCustomer(customerId: String) {
		val remoteOrders = remote.getOrdersByCustomerId(customerId)
		val orders: List<OrderEntity> =
			remoteOrders.map { it.toEntity() }
		val products: List<OrderProductEntity> =
			remoteOrders.flatMap { it.toProductEntities() }

		local.replaceByCustomer(customerId, orders, products)
	}

	suspend fun refreshByVendor(vendorId: String) {
		val remoteOrders = remote.getOrdersByVendorId(vendorId)
		val orders: List<OrderEntity> =
			remoteOrders.map { it.toEntity() }
		val products: List<OrderProductEntity> =
			remoteOrders.flatMap { it.toProductEntities() }

		local.replaceByVendor(vendorId, orders, products)
	}

	suspend fun createOrder(order: FirebaseOrderData): String {
		val orderId = remote.addOrder(order)

		// Refresh Room after Firebase confirms the order.
		refreshByCustomer(order.customerId)
		refreshByVendor(order.vendorId)

		return orderId
	}

	suspend fun updateOrderStatus(
		orderId: String,
		status: String
	) {
		val order = remote.getOrderById(orderId)
			?: throw IllegalArgumentException("Order not found: $orderId")

		remote.updateOrder(
			order.copy(status = status)
		)

		refreshByVendor(order.vendorId)
		refreshByCustomer(order.customerId)
	}

	suspend fun getProductsByOrderId(
		orderId: String
	): List<OrderProductEntity> {
		return local.getProductsByOrderId(orderId)
	}
}