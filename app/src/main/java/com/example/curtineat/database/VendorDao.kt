package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.daodao.Vendor

@Dao
interface VendorDao {
    @Query("SELECT * FROM Vendor")
    suspend fun getAllVendor(): List<Vendor>

    @Insert
    suspend fun insertVendor(vendor: Vendor): Unit

    @Update
    suspend fun updateVendor(vendor: Vendor): Unit

    @Delete
    suspend fun deleteVendor(vendor: Vendor): Unit
}