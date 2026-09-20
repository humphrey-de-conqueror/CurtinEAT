package com.example.curtineat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VendorDao {

    @Insert
    suspend fun insertVendor(vendor: Vendor)

    @Update
    suspend fun updateVendor(vendor: Vendor)

    @Delete
    suspend fun deleteVendor(vendor: Vendor)

    @Query("SELECT * FROM Vendor")
    suspend fun getAllVendors(): List<Vendor>

    @Query("SELECT * FROM Vendor WHERE vendorId = :vendorId")
    suspend fun getVendorById(vendorId: Int): Vendor?

    @Query("SELECT * FROM Vendor WHERE name = :name")
    suspend fun getVendorByName(name: String): List<Vendor>
}