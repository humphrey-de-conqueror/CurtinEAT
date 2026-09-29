package com.example.curtineat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.curtineat.database.CustomerDao
import com.example.curtineat.database.ProductDao
import com.example.curtineat.database.VendorDao

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory (
    private val vendorDao: VendorDao,
    private val customerDao: CustomerDao,
    private val productDao: ProductDao
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AppViewModel(vendorDao, customerDao, productDao) as T
    }
}