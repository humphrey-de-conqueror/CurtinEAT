package com.example.curtineat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
//data class Order (
//    @PrimaryKey
//    val orderId: Int,
//    val productList: List<Int>, //Product.productId: Int
//    val customerId: Int,
//    val donePayment: Boolean = false,
//    val status: String = "padding"
//)

@Entity
data class Order (
    @PrimaryKey(autoGenerate = true)
    val orderId: Int = 0,
    val totalPrice: Double
)