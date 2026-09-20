package com.example.curtineat.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.curtineat.database.DatabaseInstance
import com.example.curtineat.database.OrderItemRepository
import com.example.curtineat.database.OrderRepository
import com.example.curtineat.database.ProductRepository
import com.example.curtineat.database.StudentRepository
import com.example.curtineat.database.VendorRepository

class AppViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {

            val database = DatabaseInstance.getDatabase(context)

            val studentRepository =
                StudentRepository(database.studentDao())

            val vendorRepository =
                VendorRepository(database.vendorDao())

            val productRepository =
                ProductRepository(database.productDao())

            val orderRepository =
                OrderRepository(database.orderDao())

            val orderItemRepository =
                OrderItemRepository(database.orderItemDao())

            return AppViewModel(
                studentRepository = studentRepository,
                vendorRepository = vendorRepository,
                productRepository = productRepository,
                orderRepository = orderRepository,
                orderItemRepository = orderItemRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}