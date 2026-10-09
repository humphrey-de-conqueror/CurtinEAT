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
		database.withTransaction {
			orderDao.upsertAll(orders)

			orders.forEach { order ->
				productDao.deleteByOrderId(order.orderId)
			}

			productDao.insertAll(products)
		}
	}

	suspend fun replaceAll(
		orders: List<OrderEntity>,
		products: List<OrderProductEntity>
	) {
		database.withTransaction {
			productDao.deleteAll()
			orderDao.deleteAll()
			orderDao.upsertAll(orders)
			productDao.insertAll(products)
		}
	}

	suspend fun replaceByCustomer(
		customerId: String,
		orders: List<OrderEntity>,
		products: List<OrderProductEntity>
	) {
		database.withTransaction {
			val previousOrders = orderDao.getByCustomerId(customerId)

			previousOrders.forEach { order ->
				productDao.deleteByOrderId(order.orderId)
			}

			orderDao.deleteByCustomerId(customerId)
			orderDao.upsertAll(orders)
			productDao.insertAll(products)
		}
	}

	suspend fun replaceByVendor(
		vendorId: String,
		orders: List<OrderEntity>,
		products: List<OrderProductEntity>
	) {
		database.withTransaction {
			val previousOrders = orderDao.getByVendorId(vendorId)

			previousOrders.forEach { order ->
				productDao.deleteByOrderId(order.orderId)
			}

			orderDao.deleteByVendorId(vendorId)
			orderDao.upsertAll(orders)
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