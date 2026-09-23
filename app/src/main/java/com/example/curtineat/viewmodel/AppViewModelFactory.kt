package com.example.curtineat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.curtineat.database.ProductDao
import com.example.curtineat.database.VendorDao

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory (
    private val vendorDao: VendorDao,
    private val productDao: ProductDao
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AppViewModel(vendorDao, productDao) as T
    }
}