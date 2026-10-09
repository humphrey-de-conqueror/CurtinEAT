package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

	@Query("SELECT * FROM products ORDER BY productName ASC")
	fun observeAll(): Flow<List<ProductEntity>>

	@Query("""
        SELECT * FROM products
        WHERE vendorId = :vendorId
        ORDER BY productName ASC
    """)
	fun observeByVendorId(vendorId: String): Flow<List<ProductEntity>>

	@Query("""
        SELECT * FROM products
        WHERE vendorId = :vendorId
        ORDER BY productName ASC
    """)
	suspend fun getByVendorId(vendorId: String): List<ProductEntity>

	@Query("SELECT * FROM products WHERE productId = :productId LIMIT 1")
	suspend fun getById(productId: String): ProductEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsertAll(products: List<ProductEntity>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(product: ProductEntity)

	@Query("DELETE FROM products WHERE productId = :productId")
	suspend fun deleteById(productId: String)

	@Query("DELETE FROM products WHERE vendorId = :vendorId")
	suspend fun deleteByVendorId(vendorId: String)

	@Query("DELETE FROM products")
	suspend fun deleteAll()
}