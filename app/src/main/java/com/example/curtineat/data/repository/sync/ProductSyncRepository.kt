package com.example.curtineat.data.repository.sync

import com.example.curtineat.data.local.room.entity.ProductEntity
import com.example.curtineat.data.local.room.mapper.toEntity
import com.example.curtineat.data.repository.firebase.FirebaseProductRepository
import com.example.curtineat.data.repository.local.ProductLocalRepository
import kotlinx.coroutines.flow.Flow

class ProductSyncRepository(
	private val remote: FirebaseProductRepository,
	private val local: ProductLocalRepository
) {
	fun observeProducts(): Flow<List<ProductEntity>> =
		local.observeAll()

	fun observeProductsByVendor(
		vendorId: String
	): Flow<List<ProductEntity>> =
		local.observeByVendorId(vendorId)

	suspend fun refreshAll() {
		val products = remote.getAllProducts()

		local.deleteAll()
		local.upsertAll(products.map { it.toEntity() })
	}

	suspend fun refreshByVendor(vendorId: String) {
		val products = remote.getProductsByVendorId(vendorId)

		local.deleteByVendorId(vendorId)
		local.upsertAll(products.map { it.toEntity() })
	}
}