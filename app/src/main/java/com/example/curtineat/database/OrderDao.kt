package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderDao {

    @Insert
    suspend fun insertOrder(order: Order)

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("SELECT * FROM `Order`")
    suspend fun getAllOrders(): List<Order>

    @Query("SELECT * FROM `Order` WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: Int): Order?

    @Query("SELECT * FROM `Order` WHERE studentId = :studentId")
    suspend fun getOrdersByStudent(studentId: String): List<Order>

    @Query("SELECT * FROM `Order` WHERE vendorId = :vendorId")
    suspend fun getOrdersByVendor(vendorId: Int): List<Order>

    @Query("SELECT * FROM `Order` WHERE orderStatus = :orderStatus")
    suspend fun getOrdersByStatus(orderStatus: String): List<Order>

    @Query("SELECT * FROM `Order` WHERE paymentStatus = :paymentStatus")
    suspend fun getOrdersByPaymentStatus(paymentStatus: String): List<Order>
}