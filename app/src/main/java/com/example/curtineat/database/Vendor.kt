package com.example.daodao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Vendor (
    @PrimaryKey(autoGenerate = true)
    val vendorId: Int = 0,
    val vendorName: String,
    val vendorEmail: String,
    val rating: Double,
    val category: String,
    val distance: Double,
    val vendorPassword: String,
    val moneyBalance : Double = 0.0,
)