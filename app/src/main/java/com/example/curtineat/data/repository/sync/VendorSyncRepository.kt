package com.example.curtineat.data.repository.sync

import com.example.curtineat.data.local.room.mapper.toEntity
import com.example.curtineat.data.local.room.entity.VendorEntity
import com.example.curtineat.data.repository.firebase.FirebaseVendorRepository
import com.example.curtineat.data.repository.local.VendorLocalRepository
import kotlinx.coroutines.flow.Flow

class VendorSyncRepository(
	private val remote: FirebaseVendorRepository,
	private val local: VendorLocalRepository
) {
	fun observeVendors(): Flow<List<VendorEntity>> =
		local.observeAll()

	fun observeVendor(vendorId: String): Flow<VendorEntity?> =
		local.observeById(vendorId)

	suspend fun refresh() {
		// Fetch first. If Firebase fails, preserve the existing cache.
		val vendors = remote.getAllVendors()

		local.deleteAll()
		local.upsertAll(vendors.map { it.toEntity() })
	}
}