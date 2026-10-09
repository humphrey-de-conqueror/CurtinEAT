package com.example.curtineat.data

import android.content.Context
import com.example.curtineat.data.local.room.RoomProvider
import com.example.curtineat.data.repository.api.ImageRepository
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

class AppContainer(
	context: Context,
	private val vendorRemote: FirebaseVendorRepository,
	private val productRemote: FirebaseProductRepository,
	private val customerRemote: FirebaseCustomerRepository,
	private val orderRemote: FirebaseOrderRepository,
	private val notificationRemote: FirebaseNotificationRepository,
	private val imageRemote: ImageRepository
) {
	private val database = RoomProvider.getDatabase(context)

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

	private val imageCache = ImageCacheRepository(
		imageDao = database.cachedImageDao(),
		remote = imageRemote
	)

	val appViewModelFactory = AppViewModelFactory(
		vendorSync = vendorSync,
		productSync = productSync,
		customerSync = customerSync,
		orderSync = orderSync,
		notificationSync = notificationSync,
		imageCache = imageCache
	)
}