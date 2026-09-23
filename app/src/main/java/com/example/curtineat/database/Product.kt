package com.example.curtineat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product (
    @PrimaryKey(autoGenerate = true)
    val productId: Int = 0,
    val vendorID: Int,
    val productName: String,
    val productPrice: Double,
    val productImage: String
)