package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CustomerDao {
    @Query("SELECT * FROM Customer")
    suspend fun getAllCustomer(): List<Customer>

    @Insert
    suspend fun insertCustomer(customer: Customer): Unit

    @Update
    suspend fun updateCustomer(customer: Customer): Unit

    @Delete
    suspend fun deleteCustomer(customer: Customer): Unit
}