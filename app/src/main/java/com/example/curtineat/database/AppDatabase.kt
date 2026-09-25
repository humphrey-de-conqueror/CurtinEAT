package com.example.curtineat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.daodao.Vendor

@Database(entities = [Vendor::class, Customer::class, Product::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun vendorDao(): VendorDao
    abstract fun customerDao(): CustomerDao
    abstract fun productDao(): ProductDao

    companion object {
        private var db: AppDatabase? = null

        fun buildDatabase(context: Context): AppDatabase {
            if (db == null) {
                db = databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "Curtin_EAT_Database"
                ).build()
            }

            return db!!
        }
    }
}