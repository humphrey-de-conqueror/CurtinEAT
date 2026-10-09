package com.example.curtineat.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.curtineat.data.local.room.dao.CustomerDao
import com.example.curtineat.data.local.room.dao.ProductDao
import com.example.curtineat.data.local.room.dao.VendorDao
import com.example.curtineat.data.local.room.entity.CustomerEntity
import com.example.curtineat.data.local.room.entity.ProductEntity
import com.example.curtineat.data.local.room.entity.VendorEntity

@Database(
	entities = [
		VendorEntity::class,
		ProductEntity::class,
		CustomerEntity::class
	],
	version = 2,
	exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

	abstract fun vendorDao(): VendorDao

	abstract fun productDao(): ProductDao

	abstract fun customerDao(): CustomerDao
}