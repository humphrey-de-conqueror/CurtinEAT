package com.example.curtineat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.curtineat.data.repository.image.ImageCacheRepository
import com.example.curtineat.data.repository.sync.CustomerSyncRepository
import com.example.curtineat.data.repository.sync.NotificationSyncRepository
import com.example.curtineat.data.repository.sync.OrderSyncRepository
import com.example.curtineat.data.repository.sync.ProductSyncRepository
import com.example.curtineat.data.repository.sync.VendorSyncRepository

class AppViewModelFactory(
    private val vendorSync: VendorSyncRepository,
    private val productSync: ProductSyncRepository,
    private val customerSync: CustomerSyncRepository,
    private val orderSync: OrderSyncRepository,
    private val notificationSync: NotificationSyncRepository,
    private val imageCache: ImageCacheRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(
                vendorSync = vendorSync,
                productSync = productSync,
                customerSync = customerSync,
                orderSync = orderSync,
                notificationSync = notificationSync,
                imageCache = imageCache
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}