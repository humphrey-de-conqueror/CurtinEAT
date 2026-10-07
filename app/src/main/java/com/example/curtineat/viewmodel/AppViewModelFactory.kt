package com.example.curtineat.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.curtineat.data.local.room.RoomProvider
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
import com.example.curtineat.data.repository.room.MenuSyncRepository
import com.example.curtineat.data.repository.room.ProductLocalRepository
import com.example.curtineat.data.repository.room.VendorLocalRepository

class AppViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

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

            /*
             * ====================
             * Room
             * ====================
             */

            val database =
                RoomProvider.getDatabase(context)

            val vendorLocalRepository =
                VendorLocalRepository(
                    database.vendorDao()
                )

            val productLocalRepository =
                ProductLocalRepository(
                    database.productDao()
                )

            val menuSyncRepository =
                MenuSyncRepository(
                    firebaseVendorRepository = vendorRepository,
                    firebaseProductRepository = productRepository,
                    vendorLocalRepository = vendorLocalRepository,
                    productLocalRepository = productLocalRepository
                )

            @Suppress("UNCHECKED_CAST")
            return AppViewModel(
                vendorRepository = vendorRepository,
                customerRepository = customerRepository,
                productRepository = productRepository,
                orderRepository = orderRepository,
                notificationRepository = notificationRepository,
                imageRepository = imageRepository,

                /*
                 * Room dependencies
                 */
                vendorLocalRepository = vendorLocalRepository,
                productLocalRepository = productLocalRepository,
                menuSyncRepository = menuSyncRepository

            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}