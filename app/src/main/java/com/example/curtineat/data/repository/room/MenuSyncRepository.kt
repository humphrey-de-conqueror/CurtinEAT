package com.example.curtineat.data.repository.room

import com.example.curtineat.data.local.room.mapper.toEntity
import com.example.curtineat.data.repository.firebase.FirebaseProductRepository
import com.example.curtineat.data.repository.firebase.FirebaseVendorRepository

class MenuSyncRepository(
	private val firebaseVendorRepository: FirebaseVendorRepository,
	private val firebaseProductRepository: FirebaseProductRepository,
	private val vendorLocalRepository: VendorLocalRepository,
	private val productLocalRepository: ProductLocalRepository
) {

	suspend fun syncMenu() {

		val vendors =
			firebaseVendorRepository.getAllVendors()

		val products =
			firebaseProductRepository.getAllProducts()

		val vendorEntities =
			vendors.map {
				it.toEntity()
			}

		val productEntities =
			products.map {
				it.toEntity()
			}

		vendorLocalRepository.clearVendors()
		vendorLocalRepository.saveVendors(
			vendorEntities
		)

		productLocalRepository.clearProducts()
		productLocalRepository.saveProducts(
			productEntities
		)
	}
}