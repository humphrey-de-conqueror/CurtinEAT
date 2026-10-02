package com.example.curtineat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
//data class Notification (
//    @PrimaryKey(autoGenerate = true)
//    val notificationId: Int,
//    val vendorName: String,
//    val orderId: Int,
//    val time: String,
//    val message: String? = null
//)

@Entity
data class Notification (
    @PrimaryKey(autoGenerate = true)
    val notificationId: Int = 0,
    val message: String? = null,
    val time: String? = null
)