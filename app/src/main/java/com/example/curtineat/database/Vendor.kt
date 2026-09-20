package com.example.curtineat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Vendor(
    @PrimaryKey(autoGenerate = true)
    val vendorId: Int = 0,

    val name: String,
    val storePicturePath: String = "",
    val contactNumber: String = "00 0000000",
    val location: String,
    val locationPicturePath: String = ""
)