package com.example.curtineat.data.repository.local

import com.example.curtineat.data.local.room.dao.ProductDao
import com.example.curtineat.data.local.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductLocalRepository(
	private val dao: ProductDao
) {
	fun observeAll(): Flow<List<ProductEntity>> =
		dao.observeAll()

	fun observeByVendorId(vendorId: String): Flow<List<ProductEntity>> =
		dao.observeByVendorId(vendorId)

	suspend fun getByVendorId(vendorId: String): List<ProductEntity> =
		dao.getByVendorId(vendorId)

	suspend fun getById(productId: String): ProductEntity? =
		dao.getById(productId)

	suspend fun upsert(product: ProductEntity) =
		dao.upsert(product)

	suspend fun upsertAll(products: List<ProductEntity>) =
		dao.upsertAll(products)

	suspend fun deleteById(productId: String) =
		dao.deleteById(productId)

	suspend fun deleteByVendorId(vendorId: String) =
		dao.deleteByVendorId(vendorId)

	suspend fun deleteAll() =
		dao.deleteAll()
}