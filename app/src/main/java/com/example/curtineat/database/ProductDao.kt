package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    suspend fun getAllProduct(): List<Product>

    @Query("DELETE FROM Product")
    suspend fun deleteAllProduct(): Unit

    @Insert
    suspend fun insertProduct(product: Product): Unit

    @Update
    suspend fun updateProduct(product: Product): Unit

    @Delete
    suspend fun deleteProduct(product: Product): Unit
}