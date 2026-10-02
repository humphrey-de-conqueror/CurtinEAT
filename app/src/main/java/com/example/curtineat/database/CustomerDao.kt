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

    @Query("DELETE FROM Customer")
    suspend fun deleteAllCustomer(): Unit

    @Query(
        """
        SELECT * FROM Customer
        WHERE customerId = :customerId
        LIMIT 1
        """
    )
    suspend fun getCustomerById(customerId: Int): Customer?

    @Query(
        """
        SELECT EXISTS(
            SELECT 1
            FROM Customer
            WHERE customerEmail = :email
        )
        """
    )
    suspend fun customerEmailExists(email: String): Boolean

    @Query(
        """
        SELECT * FROM Customer
        WHERE customerEmail = :email
        AND customerPassword = :password
        LIMIT 1
        """
    )
    suspend fun login(
        email: String,
        password: String
    ): Customer?

    @Insert
    suspend fun insertCustomer(customer: Customer): Long

    @Update
    suspend fun updateCustomer(customer: Customer): Unit

    @Delete
    suspend fun deleteCustomer(customer: Customer): Unit
}