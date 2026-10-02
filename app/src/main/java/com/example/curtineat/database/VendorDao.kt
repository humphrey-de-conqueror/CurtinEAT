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

    @Query("SELECT * FROM Vendor WHERE vendorId = :vendorId LIMIT 1")
    suspend fun getVendorById(vendorId: Int): Vendor?

    @Query("DELETE FROM Vendor")
    suspend fun deleteAllVendor(): Unit

    @Query(
        """
        SELECT EXISTS(
            SELECT 1
            FROM Vendor
            WHERE vendorEmail = :email
        )"""
    )
    suspend fun vendorEmailExists(email: String): Boolean

    @Query(
        """
        SELECT * FROM Vendor
        WHERE vendorEmail = :email
        AND vendorPassword = :password
        LIMIT 1
        """
    )
    suspend fun login(
        email: String,
        password: String
    ): Vendor?

    @Insert
    suspend fun insertVendor(vendor: Vendor): Long

    @Update
    suspend fun updateVendor(vendor: Vendor): Unit

    @Delete
    suspend fun deleteVendor(vendor: Vendor): Unit
}