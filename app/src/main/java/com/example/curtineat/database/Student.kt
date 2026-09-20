package com.example.curtineat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey
    val studentId: String,
    val name: String,
    val schoolEmail: String,
    val contactNo: String = "00 0000000",
    val defaultPaymentMethod: String = "QR"
)