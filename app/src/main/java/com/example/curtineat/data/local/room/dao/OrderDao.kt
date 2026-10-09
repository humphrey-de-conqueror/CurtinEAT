package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

	@Query("SELECT * FROM orders ORDER BY timestamp DESC")
	fun observeAll(): Flow<List<OrderEntity>>

	@Query("""
        SELECT * FROM orders
        WHERE customerId = :customerId
        ORDER BY timestamp DESC
    """)
	fun observeByCustomerId(customerId: String): Flow<List<OrderEntity>>

	@Query("""
        SELECT * FROM orders
        WHERE vendorId = :vendorId
        ORDER BY timestamp DESC
    """)
	fun observeByVendorId(vendorId: String): Flow<List<OrderEntity>>

	@Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
	fun observeById(orderId: String): Flow<OrderEntity?>

	@Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
	suspend fun getById(orderId: String): OrderEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(order: OrderEntity)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsertAll(orders: List<OrderEntity>)

	@Query("DELETE FROM orders WHERE orderId = :orderId")
	suspend fun deleteById(orderId: String)

	@Query("DELETE FROM orders")
	suspend fun deleteAll()
}