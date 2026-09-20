package com.example.curtineat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val productId: Int = 0,

    val vendorId: Int,
    val name: String,
    val description: String = "No description provided",
    val imagePath: String = "",
    val price: Double,
    val category: String,
    val allergyWarning: String = "None",
    val isAvailable: Boolean = true
)