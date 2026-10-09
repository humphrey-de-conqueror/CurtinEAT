package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

	@Query("SELECT * FROM customers WHERE customerId = :customerId LIMIT 1")
	fun observeById(customerId: String): Flow<CustomerEntity?>

	@Query("SELECT * FROM customers WHERE customerId = :customerId LIMIT 1")
	suspend fun getById(customerId: String): CustomerEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(customer: CustomerEntity)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsertAll(customers: List<CustomerEntity>)

	@Query("DELETE FROM customers WHERE customerId = :customerId")
	suspend fun deleteById(customerId: String)

	@Query("DELETE FROM customers")
	suspend fun deleteAll()
}