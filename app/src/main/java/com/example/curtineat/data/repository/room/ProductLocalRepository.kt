package com.example.curtineat.data.repository.room

import com.example.curtineat.data.local.room.dao.ProductDao
import com.example.curtineat.data.local.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductLocalRepository(
	private val dao: ProductDao
) {

	fun getAllProducts(): Flow<List<ProductEntity>> {

		return dao.getAllProducts()
	}

	fun getProductsByVendorId(
		vendorId: String
	): Flow<List<ProductEntity>> {

		return dao.getProductsByVendorId(
			vendorId
		)
	}

	suspend fun saveProducts(
		products: List<ProductEntity>
	) {

		dao.insertProducts(products)
	}

	suspend fun clearProducts() {

		dao.deleteAllProducts()
	}
}