package com.example.curtineat.database

import androidx.compose.ui.node.InternalCoreApi
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderItemDao {
    @Query("SELECT * FROM OrderItem")
    suspend fun getAllOrderItem(): List<OrderItem>

    @Query("DELETE FROM ORDERITEM")
    suspend fun deleteAllOrderItem(): Unit

    @Insert
    suspend fun insertOrderItem(orderItem: OrderItem): Unit

    @Update
    suspend fun updateOrderItem(orderItem: OrderItem): Unit

    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItem): Unit
}