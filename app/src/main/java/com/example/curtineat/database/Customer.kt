package com.example.curtineat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Customer (
    @PrimaryKey(autoGenerate = true)
    val customerId: Int = 0,
    val customerName: String = "",
    val customerEmail: String,
    val customerPassword: String,
    //a new one for wallet money
    val moneyBalance: Double = 0.0
)