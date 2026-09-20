package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderItemDao {

    @Insert
    suspend fun insertOrderItem(orderItem: OrderItem)

    @Update
    suspend fun updateOrderItem(orderItem: OrderItem)

    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItem)

    @Query("SELECT * FROM OrderItem")
    suspend fun getAllOrderItems(): List<OrderItem>

    @Query("SELECT * FROM OrderItem WHERE orderItemId = :orderItemId")
    suspend fun getOrderItemById(orderItemId: Int): OrderItem?

    @Query("SELECT * FROM OrderItem WHERE orderId = :orderId")
    suspend fun getOrderItemsByOrder(orderId: Int): List<OrderItem>

    @Query("SELECT * FROM OrderItem WHERE productId = :productId")
    suspend fun getOrderItemsByProduct(productId: Int): List<OrderItem>
}