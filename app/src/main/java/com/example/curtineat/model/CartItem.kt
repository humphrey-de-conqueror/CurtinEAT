
package com.example.curtineat.model

import com.example.curtineat.data.local.room.entity.ProductEntity

data class CartItem(
    val product: ProductEntity,
    val quantity: Int = 1
)