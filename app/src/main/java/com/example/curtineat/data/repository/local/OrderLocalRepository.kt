package com.example.curtineat.data.repository.local

import androidx.room.withTransaction
import com.example.curtineat.data.local.room.AppDatabase
import com.example.curtineat.data.local.room.dao.OrderDao
import com.example.curtineat.data.local.room.dao.OrderProductDao
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.OrderProductEntity
import kotlinx.coroutines.flow.Flow

class OrderLocalRepository(
	private val database: AppDatabase,
	private val orderDao: OrderDao,
	private val productDao: OrderProductDao
) {
	fun observeAll(): Flow<List<OrderEntity>> =
		orderDao.observeAll()

	fun observeByCustomerId(customerId: String): Flow<List<OrderEntity>> =
		orderDao.observeByCustomerId(customerId)

	fun observeByVendorId(vendorId: String): Flow<List<OrderEntity>> =
		orderDao.observeByVendorId(vendorId)

	fun observeById(orderId: String): Flow<OrderEntity?> =
		orderDao.observeById(orderId)

	suspend fun getById(orderId: String): OrderEntity? =
		orderDao.getById(orderId)

	suspend fun getProductsByOrderId(
		orderId: String
	): List<OrderProductEntity> =
		productDao.getByOrderId(orderId)

	suspend fun upsertOrder(
		order: OrderEntity,
		products: List<OrderProductEntity>
	) {
		require(products.all { it.orderId == order.orderId }) {
			"Every order product must belong to ${order.orderId}"
		}

		database.withTransaction {
			orderDao.upsert(order)
			productDao.deleteByOrderId(order.orderId)
			productDao.insertAll(products)
		}
	}

	suspend fun upsertOrders(
		orders: List<OrderEntity>,
		products: List<OrderProductEntity>
	) {
		val orderIds = orders.map { it.orderId }.toSet()

		require(products.all { it.orderId in orderIds }) {
			"Every product must belong to an order in this batch"
		}

		database.withTransaction {
			orderDao.upsertAll(orders)

			orders.forEach { order ->
				productDao.deleteByOrderId(order.orderId)
			}

			productDao.insertAll(products)
		}
	}

	suspend fun deleteById(orderId: String) {
		database.withTransaction {
			productDao.deleteByOrderId(orderId)
			orderDao.deleteById(orderId)
		}
	}

	suspend fun deleteAll() {
		database.withTransaction {
			productDao.deleteAll()
			orderDao.deleteAll()
		}
	}
}