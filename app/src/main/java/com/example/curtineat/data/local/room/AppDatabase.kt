package com.example.curtineat.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.curtineat.data.local.room.dao.CachedImageDao
import com.example.curtineat.data.local.room.dao.CustomerDao
import com.example.curtineat.data.local.room.dao.NotificationDao
import com.example.curtineat.data.local.room.dao.OrderDao
import com.example.curtineat.data.local.room.dao.OrderProductDao
import com.example.curtineat.data.local.room.dao.ProductDao
import com.example.curtineat.data.local.room.dao.VendorDao
import com.example.curtineat.data.local.room.entity.CachedImageEntity
import com.example.curtineat.data.local.room.entity.CustomerEntity
import com.example.curtineat.data.local.room.entity.NotificationEntity
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.OrderProductEntity
import com.example.curtineat.data.local.room.entity.ProductEntity
import com.example.curtineat.data.local.room.entity.VendorEntity

@Database(
	entities = [
		VendorEntity::class,
		ProductEntity::class,
		CustomerEntity::class,
		OrderEntity::class,
		OrderProductEntity::class,
		NotificationEntity::class,
		CachedImageEntity::class
	],
	version = 1,
	exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

	abstract fun vendorDao(): VendorDao

	abstract fun productDao(): ProductDao

	abstract fun customerDao(): CustomerDao

	abstract fun orderDao(): OrderDao

	abstract fun orderProductDao(): OrderProductDao

	abstract fun notificationDao(): NotificationDao

	abstract fun cachedImageDao(): CachedImageDao
}