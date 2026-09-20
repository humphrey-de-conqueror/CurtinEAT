package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM Product")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM Product WHERE productId = :productId")
    suspend fun getProductById(productId: Int): Product?

    @Query("SELECT * FROM Product WHERE vendorId = :vendorId")
    suspend fun getProductsByVendor(vendorId: Int): List<Product>

    @Query("SELECT * FROM Product WHERE isAvailable = 1")
    suspend fun getAvailableProducts(): List<Product>

    @Query("SELECT * FROM Product WHERE category = :category")
    suspend fun getProductsByCategory(category: String): List<Product>
}