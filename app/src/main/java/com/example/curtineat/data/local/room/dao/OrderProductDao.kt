package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.OrderProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderProductDao {

	@Query("""
        SELECT * FROM order_products
        WHERE orderId = :orderId
        ORDER BY localId ASC
    """)
	fun observeByOrderId(orderId: String): Flow<List<OrderProductEntity>>

	@Query("""
        SELECT * FROM order_products
        WHERE orderId = :orderId
        ORDER BY localId ASC
    """)
	suspend fun getByOrderId(orderId: String): List<OrderProductEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAll(products: List<OrderProductEntity>)

	@Query("DELETE FROM order_products WHERE orderId = :orderId")
	suspend fun deleteByOrderId(orderId: String)

	@Query("DELETE FROM order_products")
	suspend fun deleteAll()
}