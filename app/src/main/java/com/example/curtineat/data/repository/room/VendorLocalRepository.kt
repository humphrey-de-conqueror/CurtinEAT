package com.example.curtineat.data.repository.room

import com.example.curtineat.data.local.room.dao.VendorDao
import com.example.curtineat.data.local.room.entity.VendorEntity
import kotlinx.coroutines.flow.Flow

class VendorLocalRepository(
	private val dao: VendorDao
) {

	fun getAllVendors(): Flow<List<VendorEntity>> {

		return dao.getAllVendors()
	}

	suspend fun saveVendors(
		vendors: List<VendorEntity>
	) {

		dao.insertVendors(vendors)
	}

	suspend fun clearVendors() {

		dao.deleteAllVendors()
	}
}