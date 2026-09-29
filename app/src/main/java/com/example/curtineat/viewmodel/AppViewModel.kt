package com.example.curtineat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.curtineat.database.Customer
import com.example.curtineat.database.CustomerDao
import com.example.curtineat.database.Notification
import com.example.curtineat.database.NotificationDao
import com.example.curtineat.database.Product
import com.example.curtineat.database.ProductDao
import com.example.curtineat.database.VendorDao
import com.example.daodao.Vendor
import kotlinx.coroutines.launch

class AppViewModel(
    private var vendorDao: VendorDao,
    private var customerDao: CustomerDao,
    private var productDao: ProductDao,
    private var notificationDao: NotificationDao
): ViewModel() {
    var search by mutableStateOf("")

    var notification by mutableStateOf(listOf<Notification>())
        private set
    var vendor by mutableStateOf(listOf<Vendor>())
        private set

    var customer by mutableStateOf(listOf<Customer>())
        private set

    var product by mutableStateOf(listOf<Product>())
        private set

    init {
        viewModelScope.launch {

            if (
                vendorDao.getAllVendor().isEmpty() &&
                customerDao.getAllCustomer().isEmpty() &&
                productDao.getAllProduct().isEmpty()
            ) {
                seedData()
            }

            refresh()
        }
    }

    fun refresh() = viewModelScope.launch {
        vendor = vendorDao.getAllVendor()
        customer = customerDao.getAllCustomer()
        product = productDao.getAllProduct()
        notification = notificationDao.getAllNotification()
    }

    suspend fun clearAll() {
        vendorDao.deleteAllVendor()
        customerDao.deleteAllCustomer()
        productDao.deleteAllProduct()
        notificationDao.deleteAllNotification()
    }

    // vendor dao
    fun insertVendor(vendor: Vendor) = viewModelScope.launch {
        vendorDao.insertVendor(vendor)
        refresh()
    }

    fun updateVendor(vendor: Vendor) = viewModelScope.launch {
        vendorDao.updateVendor(vendor)
        refresh()
    }

    fun deleteVendor(vendor: Vendor) = viewModelScope.launch {
        vendorDao.deleteVendor(vendor)
        refresh()
    }

    // customer dao
    fun insertCustomer(customer: Customer) = viewModelScope.launch {
        customerDao.insertCustomer(customer)
        refresh()
    }

    fun updateCustomer(customer: Customer) = viewModelScope.launch {
        customerDao.updateCustomer(customer)
        refresh()
    }

    fun deleteCustomer(customer: Customer) = viewModelScope.launch {
        customerDao.deleteCustomer(customer)
        refresh()
    }

    // product dao
    fun insertProduct(product: Product) = viewModelScope.launch {
        productDao.insertProduct(product)
        refresh()
    }

    fun updateProduct(product: Product) = viewModelScope.launch {
        productDao.updateProduct(product)
        refresh()
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        productDao.deleteProduct(product)
        refresh()
    }

    // notification dao
    fun insertNotification(notification: Notification) = viewModelScope.launch {
        notificationDao.insertNotification(notification)
        refresh()
    }

    fun updateNotification(notification: Notification) = viewModelScope.launch {
        notificationDao.updateNotification(notification)
        refresh()
    }

    fun deleteNotification(notification: Notification) = viewModelScope.launch {
        notificationDao.deleteNotification(notification)
        refresh()
    }


    // please remove this seed data in production
    suspend fun seedData() {
        vendorDao.insertVendor(Vendor(vendorName = "Mama's Kitchen", rating = 4.5, category = "Local Food", distance = 0.3, vendorPassword = "a"))
        vendorDao.insertVendor(Vendor(vendorName = "Burger Bros", rating = 4.2, category = "Western", distance = 0.8, vendorPassword = "b"))
        vendorDao.insertVendor(Vendor(vendorName = "Sushi Zen", rating = 4.8, category = "Japanese", distance = 1.2, vendorPassword = "c"))
        vendorDao.insertVendor(Vendor(vendorName = "Taco Fiesta", rating = 3.9, category = "Mexican", distance = 2.0, vendorPassword = "d"))
        vendorDao.insertVendor(Vendor(vendorName = "Pizza Palace", rating = 4.1, category = "Western", distance = 1.5, vendorPassword = "e"))

        customerDao.insertCustomer(Customer(customerName = "a", customerEmail = "b", customerPassword = "c"))

        productDao.insertProduct(Product(vendorID = 1, productName = "Nasi Lemak", productPrice = 5.50, productImage = "nasi_lemak"))
        productDao.insertProduct(Product(vendorID = 1, productName = "Mee Goreng", productPrice = 6.00, productImage = "mee_goreng"))
        productDao.insertProduct(Product(vendorID = 2, productName = "Cheeseburger", productPrice = 12.90, productImage = "cheeseburger"))
        productDao.insertProduct(Product(vendorID = 2, productName = "Chicken Wings", productPrice = 9.90, productImage = "chicken_wings"))
        productDao.insertProduct(Product(vendorID = 3, productName = "Salmon Sushi", productPrice = 18.00, productImage = "salmon_sushi"))
        productDao.insertProduct(Product(vendorID = 3, productName = "Miso Soup", productPrice = 4.50, productImage = "miso_soup"))
        productDao.insertProduct(Product(vendorID = 4, productName = "Beef Taco", productPrice = 8.90, productImage = "beef_taco"))
        productDao.insertProduct(Product(vendorID = 5, productName = "Margherita Pizza", productPrice = 22.00, productImage = "margherita"))
        productDao.insertProduct(Product(vendorID = 5, productName = "Garlic Bread", productPrice = 5.00, productImage = "garlic_bread"))

        notificationDao.insertNotification(Notification(message = "order submitted, pending for vendor verification", time = "12 p.m."))
        notificationDao.insertNotification(Notification(message = "vendor accept your order, please wait", time = "11 p.m."))
        notificationDao.insertNotification(Notification(message = "food prepared, please pick up or i buang your food", time = "10 p.m."))
        notificationDao.insertNotification(Notification(message = "vendor blocklist you ", time = "14 p.m."))
    }
}