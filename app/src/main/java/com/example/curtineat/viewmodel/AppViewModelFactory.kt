package com.example.curtineat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.curtineat.data.remote.api.image.ImageApiProvider
import com.example.curtineat.data.remote.api.image.ImageApiSource
import com.example.curtineat.data.remote.firebase.source.FirebaseCustomerSource
import com.example.curtineat.data.remote.firebase.source.FirebaseNotificationSource
import com.example.curtineat.data.remote.firebase.source.FirebaseOrderSource
import com.example.curtineat.data.remote.firebase.source.FirebaseProductSource
import com.example.curtineat.data.remote.firebase.source.FirebaseVendorSource
import com.example.curtineat.data.repository.api.ImageRepository
import com.example.curtineat.data.repository.firebase.FirebaseCustomerRepository
import com.example.curtineat.data.repository.firebase.FirebaseNotificationRepository
import com.example.curtineat.data.repository.firebase.FirebaseOrderRepository
import com.example.curtineat.data.repository.firebase.FirebaseProductRepository
import com.example.curtineat.data.repository.firebase.FirebaseVendorRepository

class AppViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {

            val vendorRepository =
                FirebaseVendorRepository(
                    FirebaseVendorSource()
                )

            val customerRepository =
                FirebaseCustomerRepository(
                    FirebaseCustomerSource()
                )

            val productRepository =
                FirebaseProductRepository(
                    FirebaseProductSource()
                )

            val orderRepository =
                FirebaseOrderRepository(
                    FirebaseOrderSource()
                )

            val notificationRepository =
                FirebaseNotificationRepository(
                    FirebaseNotificationSource()
                )

            val imageRepository =
                ImageRepository(
                    ImageApiSource(
                        ImageApiProvider.service
                    )
                )

            @Suppress("UNCHECKED_CAST")
            return AppViewModel(
                vendorRepository = vendorRepository,
                customerRepository = customerRepository,
                productRepository = productRepository,
                orderRepository = orderRepository,
                notificationRepository = notificationRepository,
                imageRepository = imageRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}