
package com.example.curtineat.data.repository.sync

import com.example.curtineat.data.local.room.entity.VendorEntity
import com.example.curtineat.data.local.room.mapper.toEntity
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
		val vendors = remote.getAllVendors().map { it.toEntity() }
		local.replaceAll(vendors)
	}

	suspend fun updateProfile(
		vendorId: String,
		vendorName: String,
		category: String,
		vendorImage: String
	) {
		remote.updateVendorProfile(
			vendorId = vendorId,
			vendorName = vendorName,
			category = category,
			vendorImage = vendorImage
		)

		// Refresh Room so all observers receive the updated profile.
		refresh()
	}
}