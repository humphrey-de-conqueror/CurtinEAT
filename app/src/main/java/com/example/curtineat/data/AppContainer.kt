
package com.example.curtineat.data

import android.content.Context
import com.example.curtineat.data.local.room.RoomProvider
import com.example.curtineat.data.remote.api.image.ImageApiProvider
import com.example.curtineat.data.remote.api.image.ImageApiSource
import com.example.curtineat.data.remote.firebase.FirebaseProvider
import com.example.curtineat.data.remote.firebase.source.FirebaseAuthSource
import com.example.curtineat.data.remote.firebase.source.FirebaseCustomerSource
import com.example.curtineat.data.remote.firebase.source.FirebaseNotificationSource
import com.example.curtineat.data.remote.firebase.source.FirebaseOrderSource
import com.example.curtineat.data.remote.firebase.source.FirebaseProductSource
import com.example.curtineat.data.remote.firebase.source.FirebaseVendorSource
import com.example.curtineat.data.repository.api.ImageRepository
import com.example.curtineat.data.repository.api.ImageUploadRepository
import com.example.curtineat.data.repository.auth.AuthenticationRepository
import com.example.curtineat.data.repository.firebase.FirebaseCustomerRepository
import com.example.curtineat.data.repository.firebase.FirebaseNotificationRepository
import com.example.curtineat.data.repository.firebase.FirebaseOrderRepository
import com.example.curtineat.data.repository.firebase.FirebaseProductRepository
import com.example.curtineat.data.repository.firebase.FirebaseVendorRepository
import com.example.curtineat.data.repository.image.ImageCacheRepository
import com.example.curtineat.data.repository.local.CustomerLocalRepository
import com.example.curtineat.data.repository.local.NotificationLocalRepository
import com.example.curtineat.data.repository.local.OrderLocalRepository
import com.example.curtineat.data.repository.local.ProductLocalRepository
import com.example.curtineat.data.repository.local.VendorLocalRepository
import com.example.curtineat.data.repository.sync.CustomerSyncRepository
import com.example.curtineat.data.repository.sync.NotificationSyncRepository
import com.example.curtineat.data.repository.sync.OrderSyncRepository
import com.example.curtineat.data.repository.sync.ProductSyncRepository
import com.example.curtineat.data.repository.sync.VendorSyncRepository
import com.example.curtineat.viewmodel.AppViewModelFactory

class AppContainer(context: Context) {

	// Local database
	private val database = RoomProvider.getDatabase(context)

	// Firebase remote repositories
	private val customerRemote =
		FirebaseCustomerRepository(FirebaseCustomerSource())

	private val vendorRemote =
		FirebaseVendorRepository(FirebaseVendorSource())

	private val productRemote =
		FirebaseProductRepository(FirebaseProductSource())

	private val orderRemote =
		FirebaseOrderRepository(FirebaseOrderSource())

	private val notificationRemote =
		FirebaseNotificationRepository(FirebaseNotificationSource())

	// Image API repository
	private val imageRemote = ImageRepository(
		source = ImageApiSource(
			service = ImageApiProvider.service
		)
	)

	// Image cache: Room first, remote API on cache miss
	private val imageCache = ImageCacheRepository(
		imageDao = database.cachedImageDao(),
		remote = imageRemote
	)

	// Image upload: prepares, uploads, and caches selected images
	private val imageUploadRepository = ImageUploadRepository(
		context = context,
		imageRepository = imageRemote,
		imageCache = imageCache
	)

	// Local repositories
	private val vendorLocal = VendorLocalRepository(
		database = database,
		dao = database.vendorDao()
	)

	private val productLocal = ProductLocalRepository(
		database = database,
		dao = database.productDao()
	)

	private val customerLocal = CustomerLocalRepository(
		database = database,
		dao = database.customerDao()
	)

	private val orderLocal = OrderLocalRepository(
		database = database,
		orderDao = database.orderDao(),
		productDao = database.orderProductDao()
	)

	private val notificationLocal = NotificationLocalRepository(
		database = database,
		dao = database.notificationDao()
	)

	// Synchronization repositories
	private val vendorSync = VendorSyncRepository(
		remote = vendorRemote,
		local = vendorLocal
	)

	private val productSync = ProductSyncRepository(
		remote = productRemote,
		local = productLocal
	)

	private val customerSync = CustomerSyncRepository(
		remote = customerRemote,
		local = customerLocal
	)

	private val orderSync = OrderSyncRepository(
		remote = orderRemote,
		local = orderLocal
	)

	private val notificationSync = NotificationSyncRepository(
		remote = notificationRemote,
		local = notificationLocal
	)

	// Authentication
	private val firebaseAuthSource =
		FirebaseAuthSource(FirebaseProvider.auth)

	private val authenticationRepository = AuthenticationRepository(
		authSource = firebaseAuthSource,
		customerRemote = customerRemote,
		vendorRemote = vendorRemote
	)

	// ViewModel factory
	val appViewModelFactory = AppViewModelFactory(
		vendorSync = vendorSync,
		productSync = productSync,
		customerSync = customerSync,
		orderSync = orderSync,
		notificationSync = notificationSync,
		imageCache = imageCache,
		authenticationRepository = authenticationRepository,
		imageUploadRepository = imageUploadRepository
	)
}