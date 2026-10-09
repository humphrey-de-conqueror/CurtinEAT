package com.example.curtineat.data.repository.local

import androidx.room.withTransaction
import com.example.curtineat.data.local.room.AppDatabase
import com.example.curtineat.data.local.room.dao.VendorDao
import com.example.curtineat.data.local.room.entity.VendorEntity
import kotlinx.coroutines.flow.Flow

class VendorLocalRepository(
	private val database: AppDatabase,
	private val dao: VendorDao
) {
	fun observeAll(): Flow<List<VendorEntity>> = dao.observeAll()

	fun observeById(vendorId: String): Flow<VendorEntity?> =
		dao.observeById(vendorId)

	suspend fun getAll(): List<VendorEntity> = dao.getAll()

	suspend fun getById(vendorId: String): VendorEntity? =
		dao.getById(vendorId)

	suspend fun upsert(vendor: VendorEntity) = dao.upsert(vendor)

	suspend fun upsertAll(vendors: List<VendorEntity>) =
		dao.upsertAll(vendors)

	suspend fun replaceAll(vendors: List<VendorEntity>) {
		database.withTransaction {
			dao.deleteAll()
			dao.upsertAll(vendors)
		}
	}

	suspend fun deleteById(vendorId: String) = dao.deleteById(vendorId)

	suspend fun deleteAll() = dao.deleteAll()
}