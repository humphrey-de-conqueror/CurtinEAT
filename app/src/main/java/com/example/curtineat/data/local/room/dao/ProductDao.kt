package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

	@Query("SELECT * FROM products")
	fun getAllProducts(): Flow<List<ProductEntity>>

	@Query(
		"SELECT * FROM products WHERE vendorId = :vendorId"
	)
	fun getProductsByVendorId(
		vendorId: String
	): Flow<List<ProductEntity>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertProducts(
		products: List<ProductEntity>
	)

	@Query("DELETE FROM products")
	suspend fun deleteAllProducts()
}