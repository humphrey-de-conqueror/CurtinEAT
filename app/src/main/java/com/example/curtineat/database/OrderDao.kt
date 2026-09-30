package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderDao {
    @Query("SELECT * FROM `Order`")
    suspend fun getAllOrder(): List<Order>

    @Query("DELETE FROM `Order`")
    suspend fun deleteAllOrder(): Unit

//    @Insert
//    suspend fun insertOrder(order: Order): Unit

    @Insert
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order): Unit

    @Delete
    suspend fun deleteOrder(order: Order): Unit
}