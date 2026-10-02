package com.example.curtineat.model

import com.example.curtineat.database.Product

data class CartItem(
    val product: Product,
    val quantity: Int = 1
)