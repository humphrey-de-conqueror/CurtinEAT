package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.VendorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VendorDao {

	@Query("SELECT * FROM vendors")
	fun getAllVendors(): Flow<List<VendorEntity>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertVendors(
		vendors: List<VendorEntity>
	)

	@Query("DELETE FROM vendors")
	suspend fun deleteAllVendors()
}