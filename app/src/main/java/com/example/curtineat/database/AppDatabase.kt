package com.example.curtineat.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Student::class,
        Vendor::class,
        Product::class,
        Order::class,
        OrderItem::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    abstract fun vendorDao(): VendorDao

    abstract fun productDao(): ProductDao

    abstract fun orderDao(): OrderDao

    abstract fun orderItemDao(): OrderItemDao
}