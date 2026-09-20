package com.example.curtineat.database

class ProductRepository(
    private val productDao: ProductDao
) {

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun getAllProducts(): List<Product> {
        return productDao.getAllProducts()
    }

    suspend fun getProductById(productId: Int): Product? {
        return productDao.getProductById(productId)
    }

    suspend fun getProductsByVendor(vendorId: Int): List<Product> {
        return productDao.getProductsByVendor(vendorId)
    }

    suspend fun getAvailableProducts(): List<Product> {
        return productDao.getAvailableProducts()
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        return productDao.getProductsByCategory(category)
    }
}