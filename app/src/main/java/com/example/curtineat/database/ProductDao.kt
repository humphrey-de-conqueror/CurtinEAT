package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    suspend fun getAllProduct(): List<Product>

    @Insert
    suspend fun insertProduct(product: Product): Unit
}