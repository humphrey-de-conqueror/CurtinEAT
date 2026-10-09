package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.VendorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VendorDao {

	@Query("SELECT * FROM vendors ORDER BY vendorName ASC")
	fun observeAll(): Flow<List<VendorEntity>>

	@Query("SELECT * FROM vendors ORDER BY vendorName ASC")
	suspend fun getAll(): List<VendorEntity>

	@Query("SELECT * FROM vendors WHERE vendorId = :vendorId LIMIT 1")
	fun observeById(vendorId: String): Flow<VendorEntity?>

	@Query("SELECT * FROM vendors WHERE vendorId = :vendorId LIMIT 1")
	suspend fun getById(vendorId: String): VendorEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsertAll(vendors: List<VendorEntity>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(vendor: VendorEntity)

	@Query("DELETE FROM vendors WHERE vendorId = :vendorId")
	suspend fun deleteById(vendorId: String)

	@Query("DELETE FROM vendors")
	suspend fun deleteAll()
}