package com.example.curtineat.database

class VendorRepository(
    private val vendorDao: VendorDao
) {

    suspend fun insertVendor(vendor: Vendor) {
        vendorDao.insertVendor(vendor)
    }

    suspend fun updateVendor(vendor: Vendor) {
        vendorDao.updateVendor(vendor)
    }

    suspend fun deleteVendor(vendor: Vendor) {
        vendorDao.deleteVendor(vendor)
    }

    suspend fun getAllVendors(): List<Vendor> {
        return vendorDao.getAllVendors()
    }

    suspend fun getVendorById(vendorId: Int): Vendor? {
        return vendorDao.getVendorById(vendorId)
    }

    suspend fun getVendorByName(name: String): List<Vendor> {
        return vendorDao.getVendorByName(name)
    }
}