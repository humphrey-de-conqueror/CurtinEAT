package com.example.curtineat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val orderItemId: Int = 0,

    val orderId: Int,
    val productId: Int,

    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)