package com.example.curtineat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.curtineat.database.CustomerDao
import com.example.curtineat.database.NotificationDao
import com.example.curtineat.database.OrderDao
import com.example.curtineat.database.OrderItemDao
import com.example.curtineat.database.ProductDao
import com.example.curtineat.database.VendorDao

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val vendorDao: VendorDao,
    private val customerDao: CustomerDao,
    private val productDao: ProductDao,
    private val notificationDao: NotificationDao,
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return AppViewModel(
            vendorDao,
            customerDao,
            productDao,
            notificationDao,
            orderDao,
            orderItemDao
        ) as T
    }
}