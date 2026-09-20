package com.example.curtineat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
    @PrimaryKey(autoGenerate = true)
    val orderId: Int = 0,

    val studentId: String,
    val vendorId: Int,

    val orderDateTime: String,
    val totalAmount: Double,

    val orderStatus: String = "Pending",
    val paymentMethod: String = "QR",
    val paymentStatus: String = "Pending",

    val pickupCode: String = ""
)