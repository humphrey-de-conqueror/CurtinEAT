package com.example.curtineat.data.repository.room

import com.example.curtineat.data.local.room.mapper.toEntity
import com.example.curtineat.data.repository.firebase.FirebaseCustomerRepository
import com.example.curtineat.data.repository.firebase.FirebaseProductRepository
import com.example.curtineat.data.repository.firebase.FirebaseVendorRepository

class MenuSyncRepository(
	private val firebaseVendorRepository: FirebaseVendorRepository,
	private val firebaseProductRepository: FirebaseProductRepository,
	private val firebaseCustomerRepository: FirebaseCustomerRepository,
	private val vendorLocalRepository: VendorLocalRepository,
	private val productLocalRepository: ProductLocalRepository,
	private val customerLocalRepository: CustomerLocalRepository
) {

	suspend fun syncMenu() {

		val vendors =
			firebaseVendorRepository.getAllVendors()

		val products =
			firebaseProductRepository.getAllProducts()

		val customers =
			firebaseCustomerRepository.getAllCustomers()

		val vendorEntities =
			vendors.map { it.toEntity() }

		val productEntities =
			products.map { it.toEntity() }

		val customerEntities =
			customers.map { it.toEntity() }

		vendorLocalRepository.clearVendors()
		vendorLocalRepository.saveVendors(vendorEntities)

		productLocalRepository.clearProducts()
		productLocalRepository.saveProducts(productEntities)

		customerLocalRepository.clearCustomers()
		customerLocalRepository.saveCustomers(customerEntities)
	}
}