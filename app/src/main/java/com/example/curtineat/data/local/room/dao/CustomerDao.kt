package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

	@Query("SELECT * FROM customers")
	fun getAllCustomers(): Flow<List<CustomerEntity>>

	@Query("SELECT * FROM customers WHERE customerId = :customerId")
	suspend fun getCustomerById(
		customerId: String
	): CustomerEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertCustomers(
		customers: List<CustomerEntity>
	)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertCustomer(
		customer: CustomerEntity
	)

	@Query("DELETE FROM customers")
	suspend fun deleteAllCustomers()
}