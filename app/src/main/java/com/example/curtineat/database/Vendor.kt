package com.example.daodao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Vendor (
    @PrimaryKey(autoGenerate = true)
    val vendorId: Int = 0,
    val vendorName: String = "",
    val vendorEmail: String,
    val rating: Double = 0.0,
    val category: String = "",
    val distance: Double = 0.0,
    val vendorPassword: String,
    val moneyBalance : Double = 0.0,
)